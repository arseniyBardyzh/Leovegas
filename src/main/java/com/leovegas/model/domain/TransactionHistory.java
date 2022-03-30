package com.leovegas.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "TRANSACTION_HISTORY")
public class TransactionHistory {

    @Id
    @Column(name = "ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    private Player player;

    @Column(name = "OPERATION_TYPE")
    private String operationType;

    @Column(name = "OPERATION_AMOUNT")
    private BigDecimal operationAmount;
}
