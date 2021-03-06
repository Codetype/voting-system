package com.voting.service.payload;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class JwtAuthenticationResponse {

    @NonNull
    private String accessToken;

    private String tokenType = "Bearer";

}
