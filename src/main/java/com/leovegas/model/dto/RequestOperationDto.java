package com.leovegas.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RequestOperationDto {
    @JsonProperty("transactionId")
    private String transactionId;
    @JsonProperty("operationAmount")
    private BigDecimal operationAmount;
}
