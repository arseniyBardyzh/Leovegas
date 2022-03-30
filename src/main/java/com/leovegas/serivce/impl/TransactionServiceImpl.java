package com.leovegas.serivce.impl;

import com.leovegas.model.domain.Player;
import com.leovegas.model.domain.TransactionHistory;
import com.leovegas.model.dto.RequestOperationDto;
import com.leovegas.model.dto.ResultOperationDto;
import com.leovegas.repository.PlayerRepository;
import com.leovegas.repository.TransactionHistoryRepository;
import com.leovegas.serivce.api.TransactionService;
import com.leovegas.serivce.exception.OperationException;
import com.leovegas.serivce.exception.PlayerException;
import com.leovegas.serivce.exception.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final PlayerRepository playerRepository;
    private static final String SUCCESS_ERROR_CODE = "00";
    private static final String TRANSACTION_ERROR_CODE = "01";
    private static final String PLAYER_ERROR_CODE = "02";
    private static final String OPERATION_ERROR_CODE = "03";
    private static final String UNHANDLED_EXCEPTION = "100";
    private static final String DEBIT_OPERATION_TYPE = "Debit";

    @Autowired
    public TransactionServiceImpl(final TransactionHistoryRepository transactionHistoryRepository,
                                  final PlayerRepository playerRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public ResultOperationDto createOperation(RequestOperationDto operationDto, String playerId, String operationType) {
        ResultOperationDto resultOperationDto = new ResultOperationDto();
        TransactionHistory transactionHistory = new TransactionHistory();
        try {
            checkTransactionUnique(operationDto.getTransactionId());
            Player player = getPlayer(playerId);

            calculateBalance(player, operationDto.getOperationAmount(), operationType);
            setTransactionHistory(transactionHistory, operationDto, player, operationType);

            transactionHistoryRepository.save(transactionHistory);
            playerRepository.save(player);
            resultOperationDto.setErrorCode(SUCCESS_ERROR_CODE);
            return resultOperationDto;
        } catch (TransactionException e) {
            resultOperationDto.setErrorCode(TRANSACTION_ERROR_CODE);
            resultOperationDto.setErrorDescription(e.getMessage());
            return resultOperationDto;
        } catch (PlayerException e) {
            resultOperationDto.setErrorCode(PLAYER_ERROR_CODE);
            resultOperationDto.setErrorDescription(e.getMessage());
            return resultOperationDto;
        } catch (OperationException e) {
            resultOperationDto.setErrorCode(OPERATION_ERROR_CODE);
            resultOperationDto.setErrorDescription(e.getMessage());
            return resultOperationDto;
        } catch (Exception e) {
            resultOperationDto.setErrorCode(UNHANDLED_EXCEPTION);
            resultOperationDto.setErrorDescription(e.getMessage());
            return resultOperationDto;
        }

    }

    private void checkTransactionUnique(String transactionId) throws TransactionException {
        Optional<TransactionHistory> transactionOpt = transactionHistoryRepository.findById(transactionId);
        if (transactionOpt.isPresent()) {
            throw new TransactionException("This transaction Id has already presented in Database");
        }
    }

    private Player getPlayer(String playerId) throws PlayerException {
        Optional<Player> playerOpt = playerRepository.findById(playerId);

        if (!playerOpt.isPresent()) {
            throw new PlayerException("Player is not exist. Please try to complete operation later");
        }
        return playerOpt.get();
    }

    private void calculateBalance(Player player,
                                  BigDecimal operationAmount,
                                  String operationType) throws OperationException {
        BigDecimal balanceAfterOperation;
        if (operationType.equals(DEBIT_OPERATION_TYPE)) {
            balanceAfterOperation = player.getBalance().subtract(operationAmount);
        } else {
            balanceAfterOperation = player.getBalance().add(operationAmount);
        }
        if (balanceAfterOperation.compareTo(BigDecimal.ZERO) < 0) {
            throw new OperationException("Insufficient funds on your account");
        }
        player.setBalance(balanceAfterOperation);
    }

    private void setTransactionHistory(TransactionHistory transactionHistory,
                                       RequestOperationDto operationDto,
                                       Player player,
                                       String operationType) {
        transactionHistory.setId(operationDto.getTransactionId());
        transactionHistory.setPlayer(player);
        transactionHistory.setOperationAmount(operationDto.getOperationAmount());
        transactionHistory.setOperationType(operationType);
    }
}
