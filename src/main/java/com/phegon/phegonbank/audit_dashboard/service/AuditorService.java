package com.phegon.phegonbank.audit_dashboard.service;

import com.phegon.phegonbank.account.dtos.AccountDTO;
import com.phegon.phegonbank.auth_users.dtos.UserDTO;
import com.phegon.phegonbank.transaction.dtos.TransactionDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AuditorService {

    Map<String, Long> getSystemTotals();

    Optional<UserDTO> findUserByEmail(String email);

    Optional<AccountDTO> findAccountDetailsByAccountNumber(String accountNumber);

    List<TransactionDTO> findTransactionsByAccountNumber(String accountNumber);

    Optional<TransactionDTO> findTransactionById(Long transactionId);
}
