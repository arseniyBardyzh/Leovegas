package com.leovegas.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResultOperationDto {
    @JsonProperty("errorCode")
    private String errorCode;
    @JsonProperty("errorDescription")
    private String errorDescription;
}
