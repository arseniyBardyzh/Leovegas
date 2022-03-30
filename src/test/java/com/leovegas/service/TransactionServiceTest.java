package com.leovegas.service;

import com.leovegas.model.domain.Player;
import com.leovegas.model.domain.TransactionHistory;
import com.leovegas.model.dto.RequestOperationDto;
import com.leovegas.model.dto.ResultOperationDto;
import com.leovegas.repository.PlayerRepository;
import com.leovegas.repository.TransactionHistoryRepository;
import com.leovegas.serivce.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    PlayerRepository playerRepository;

    @InjectMocks
    TransactionServiceImpl service;

    @Test
    void createOperation_shouldUpdatePlayerBalance_WhenCreditOperationParameterCorrect() {
        RequestOperationDto requestOperationDto = createRequestOperationDto();
        Player player = createPlayer();
        String operationType = "Credit";
        String playerId = "1";

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(transactionHistoryRepository.findById(requestOperationDto.getTransactionId())).thenReturn(
                Optional.empty());
        ResultOperationDto result = service.createOperation(requestOperationDto, playerId, operationType);
        verify(playerRepository).findById(anyString());
        verify(transactionHistoryRepository).findById(anyString());
        verify(playerRepository).save(any());
        verify(transactionHistoryRepository).save(any());

        assertEquals(BigDecimal.valueOf(70), player.getBalance());
        assertEquals("00", result.getErrorCode());
    }

    @Test
    void createOperation_shouldUpdatePlayerBalance_WhenDebitOperationParameterCorrect() {
        RequestOperationDto requestOperationDto = createRequestOperationDto();
        Player player = createPlayer();
        String operationType = "Debit";
        String playerId = "1";

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(transactionHistoryRepository.findById(requestOperationDto.getTransactionId())).thenReturn(
                Optional.empty());
        ResultOperationDto result = service.createOperation(requestOperationDto, playerId, operationType);
        verify(playerRepository).findById(anyString());
        verify(transactionHistoryRepository).findById(anyString());
        verify(playerRepository).save(any());
        verify(transactionHistoryRepository).save(any());

        assertEquals(BigDecimal.valueOf(30), player.getBalance());
        assertEquals("00", result.getErrorCode());
    }

    @Test
    void createOperation_shouldReturnTransactionException_WhenTransactionIsNotUnique() {
        RequestOperationDto requestOperationDto = createRequestOperationDto();
        TransactionHistory transactionHistory = createTransactionHistory("1", "Debit", createPlayer(), BigDecimal.ONE);
        String operationType = "Debit";
        String playerId = "1";
        when(transactionHistoryRepository.findById(requestOperationDto.getTransactionId())).thenReturn(
                Optional.of(transactionHistory));
        ResultOperationDto result = service.createOperation(requestOperationDto, playerId, operationType);
        verify(playerRepository, times(0)).findById(anyString());
        verify(transactionHistoryRepository).findById(anyString());
        verify(playerRepository, times(0)).save(any());
        verify(transactionHistoryRepository, times(0)).save(any());

        assertEquals("01", result.getErrorCode());
    }

    @Test
    void createOperation_shouldReturnPlayerException_WhenPlayerIsNotDefined() {
        RequestOperationDto requestOperationDto = createRequestOperationDto();
        requestOperationDto.setOperationAmount(BigDecimal.valueOf(1000));
        String operationType = "Debit";
        String playerId = "1";

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());
        when(transactionHistoryRepository.findById(requestOperationDto.getTransactionId())).thenReturn(
                Optional.empty());
        ResultOperationDto result = service.createOperation(requestOperationDto, playerId, operationType);
        verify(playerRepository).findById(anyString());
        verify(transactionHistoryRepository).findById(anyString());
        verify(playerRepository, times(0)).save(any());
        verify(transactionHistoryRepository, times(0)).save(any());

        assertEquals("02", result.getErrorCode());
    }

    @Test
    void createOperation_shouldReturnOperationException_WhenBalanceIsLowerThenOperationSum() {
        RequestOperationDto requestOperationDto = createRequestOperationDto();
        requestOperationDto.setOperationAmount(BigDecimal.valueOf(1000));
        Player player = createPlayer();
        String operationType = "Debit";
        String playerId = "1";

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(transactionHistoryRepository.findById(requestOperationDto.getTransactionId())).thenReturn(
                Optional.empty());
        ResultOperationDto result = service.createOperation(requestOperationDto, playerId, operationType);
        verify(playerRepository).findById(anyString());
        verify(transactionHistoryRepository).findById(anyString());
        verify(playerRepository, times(0)).save(any());
        verify(transactionHistoryRepository, times(0)).save(any());

        assertEquals("03", result.getErrorCode());
    }

    private RequestOperationDto createRequestOperationDto() {
        RequestOperationDto requestOperationDto = new RequestOperationDto();
        requestOperationDto.setOperationAmount(BigDecimal.valueOf(20));
        requestOperationDto.setTransactionId("1");
        return requestOperationDto;
    }

    private Player createPlayer() {
        Player player = new Player();
        player.setId("1");
        player.setBalance(BigDecimal.valueOf(50));
        return player;
    }

    private TransactionHistory createTransactionHistory(String id,
                                                        String operationType,
                                                        Player player,
                                                        BigDecimal operationAmount) {
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setOperationType(operationType);
        transactionHistory.setPlayer(player);
        transactionHistory.setOperationAmount(operationAmount);
        transactionHistory.setId(id);
        return transactionHistory;
    }
}
