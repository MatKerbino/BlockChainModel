package com.kerbino.bcpredict.dto;

import lombok.NonNull;

public record CoinDTO(
        @NonNull String id,
        @NonNull String Symbol,
        @NonNull String name
){};
