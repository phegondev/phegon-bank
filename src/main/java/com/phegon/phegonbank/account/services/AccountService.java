package com.phegon.phegonbank.account.services;

import com.phegon.phegonbank.account.dtos.AccountDTO;
import com.phegon.phegonbank.account.entity.Account;
import com.phegon.phegonbank.auth_users.entity.User;
import com.phegon.phegonbank.enums.AccountType;
import com.phegon.phegonbank.res.Response;

import java.util.List;

public interface AccountService {
    Account createAccount(AccountType accountType, User user);

    Response<List<AccountDTO>> getMyAccounts();

    Response<?> closeAccount(String accountNumber);
}
