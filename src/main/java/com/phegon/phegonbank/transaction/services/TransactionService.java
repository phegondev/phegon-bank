package com.phegon.phegonbank.transaction.services;

import com.phegon.phegonbank.res.Response;
import com.phegon.phegonbank.transaction.dtos.TransactionDTO;
import com.phegon.phegonbank.transaction.dtos.TransactionRequest;

import java.util.List;

public interface TransactionService {
    Response<?>createTransaction(TransactionRequest transactionRequest);
    Response<List<TransactionDTO>> getTransactionsForMyAccount(String accountNumber, int page, int size);
}
