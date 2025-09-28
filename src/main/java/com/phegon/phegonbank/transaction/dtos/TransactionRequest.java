package com.phegon.phegonbank.transaction.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.phegon.phegonbank.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

    private TransactionType transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String description;

    private String destinationAccountNumber; // The receiving account number if it's a transfer

}
