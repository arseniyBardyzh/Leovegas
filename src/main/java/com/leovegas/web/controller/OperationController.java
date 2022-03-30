package com.leovegas.web.controller;

import com.leovegas.model.dto.RequestOperationDto;
import com.leovegas.model.dto.ResultOperationDto;
import com.leovegas.serivce.api.TransactionService;
import com.leovegas.serivce.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class OperationController {
    private final TransactionService transactionService;
    private static final String CREDIT_OPERATION = "Credit";
    private static final String DEBIT_OPERATION = "Debit";

    @Autowired
    public OperationController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{id}/credit")
    public ResultOperationDto creditOperation(final @PathVariable("id") String playerId,
                                              final @RequestBody RequestOperationDto operationDto) throws TransactionException {
        return transactionService.createOperation(operationDto, playerId, CREDIT_OPERATION);
    }

    @PostMapping("/{id}/debit")
    public ResultOperationDto debitOperation(final @PathVariable("id") String playerId,
                                             final @RequestBody RequestOperationDto operationDto) throws TransactionException {
        return transactionService.createOperation(operationDto, playerId, DEBIT_OPERATION);
    }
}
