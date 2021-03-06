package com.voting.service.controller;

import com.voting.model.Voting;
import com.voting.service.VotingService;
import com.voting.service.payload.ApiResponse;
import com.voting.service.payload.VotingRequest;
import com.voting.service.payload.VotingResponse;
import com.voting.service.payload.VotingResultsResponse;
import com.voting.service.security.CurrentUser;
import com.voting.service.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/votings")
public class VotingController {

    @Autowired
    private VotingService votingService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<List<VotingResponse>> getVotings(@CurrentUser UserPrincipal currentUser) {
        return ResponseEntity.ok(votingService.getAllVotings(currentUser));
    }

    @GetMapping("/{votingId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<VotingResponse> getVotingById(@CurrentUser UserPrincipal currentUser, @PathVariable Long votingId) {
        return ResponseEntity.ok(votingService.getVotingResponseById(votingId, currentUser));
    }

    @GetMapping("/{votingId}/results")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<VotingResultsResponse> getVotingResultsById(@CurrentUser UserPrincipal currentUser, @PathVariable Long votingId) {
        return ResponseEntity.ok(votingService.getVotingResultsById(votingId, currentUser));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> createVoting(@Valid @RequestBody VotingRequest votingRequest) {
        Voting voting = votingService.createVoting(votingRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{votingId}")
                .buildAndExpand(voting.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Voting created successfully."));
    }

}
