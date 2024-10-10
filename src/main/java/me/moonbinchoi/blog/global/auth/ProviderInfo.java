package me.moonbinchoi.blog.global.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ProviderInfo {

    GITHUB("id", "login"),
    GOOGLE("sub", "email");

    private final String providerCode; // Social provider determine code
    private final String identifier; // User identifier key

    private static ProviderInfo from(String providerName) {
        final String upperCaseProviderName = providerName.toUpperCase();

        return Arrays.stream(ProviderInfo.values())
                .filter(item -> item.name().equals(upperCaseProviderName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown provider: " + providerName));
    }
}
