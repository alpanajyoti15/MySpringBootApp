package com.dws.challenge.domain;

import java.math.BigDecimal;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TransferRequest {

    @NotNull
    @NotEmpty
    private final String accountFromId;

    @NotNull
    @Min(value = 0, message = "Initial balance must be positive.")
    private BigDecimal amount;

    @NotNull
    @NotEmpty
    private final String accountToId;
}
