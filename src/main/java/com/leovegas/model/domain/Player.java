package com.leovegas.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Data
@Table(name = "PLAYER")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private String Id;

    @Column(name = "BALANCE")
    private BigDecimal balance;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY)
    private Collection<TransactionHistory> transactionHistory;
}
