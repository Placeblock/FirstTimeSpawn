package de.codelix.firsttimespawn;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DBConfig {
    private final String url;
    private final String username;
    private final String password;
}
