package com.voting.service.utils;

import com.voting.model.Voting;
import com.voting.service.payload.OptionResponse;
import com.voting.service.payload.OptionResultsResponse;
import com.voting.service.payload.UserSummary;
import com.voting.service.payload.VotingResponse;
import com.voting.service.payload.VotingResultsResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VotingMapper {

    public static VotingResponse mapVotingToVotingResponse(Voting voting,
                                                           List<Long> userSelectedOptionIds,
                                                           boolean isAdmin) {
        LocalDateTime now = LocalDateTime.now();
        List<OptionResponse> optionResponses = voting.getOptions().stream()
                .map(option -> OptionResponse.builder()
                        .id(option.getId())
                        .name(option.getName())
                        .build())
                .collect(Collectors.toList());

        VotingResponse.VotingResponseBuilder votingResponse = VotingResponse.builder()
                .id(voting.getId())
                .name(voting.getName())
                .start(voting.getStart())
                .end(voting.getEnd())
                .singleChoice(voting.getSingleChoice())
                .isExpired(voting.getEnd().isBefore(now))
                .selectedOptionIds(userSelectedOptionIds)
                .options(optionResponses);

        if (isAdmin) {
            Set<UserSummary> voterSummaries = voting.getVoters().stream()
                    .map(voter -> UserSummary.builder()
                            .id(voter.getId())
                            .name(voter.getName())
                            .email(voter.getEmail())
                            .build())
                    .collect(Collectors.toSet());
            votingResponse.voters(voterSummaries);
        }

        return votingResponse.build();
    }

    public static VotingResultsResponse mapVotingToVotingResultsResponse(Voting voting,
                                                                         Map<Long, Long> optionIdsToVoteCounts,
                                                                         List<Long> userSelectedOptionIds) {
        LocalDateTime now = LocalDateTime.now();
        List<OptionResultsResponse> optionResults = voting.getOptions().stream()
                .map(option -> OptionResultsResponse.builder()
                        .id(option.getId())
                        .name(option.getName())
                        .voteCount(optionIdsToVoteCounts.getOrDefault(option.getId(), 0L))
                        .build())
                .collect(Collectors.toList());

        return VotingResultsResponse.builder()
                .id(voting.getId())
                .name(voting.getName())
                .start(voting.getStart())
                .end(voting.getEnd())
                .singleChoice(voting.getSingleChoice())
                .isExpired(voting.getEnd().isBefore(now))
                .options(optionResults)
                .selectedOptionIds(userSelectedOptionIds)
                .build();
    }
}
