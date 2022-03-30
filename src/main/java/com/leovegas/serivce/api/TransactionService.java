package com.leovegas.serivce.api;

import com.leovegas.model.dto.RequestOperationDto;
import com.leovegas.model.dto.ResultOperationDto;
import com.leovegas.serivce.exception.TransactionException;

public interface TransactionService {
    /**
     * save transaction in db.
     *
     * @param operationDto  - Dto with Operation parameter
     * @param playerId      - Player Identifier
     * @param operationType - type of Operation
     * @return
     */
    ResultOperationDto createOperation(RequestOperationDto operationDto, String playerId, String operationType) throws TransactionException;
}
