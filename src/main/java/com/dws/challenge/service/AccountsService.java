package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AccountNotFoundException;
import com.dws.challenge.exception.InsufficientBalanceException;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public void withdrawFromAccount(String accountFromId, String accountToId, BigDecimal amount)
  {
    Account accountFrom = accountsRepository.findById(accountFromId).orElseThrow();
    Account accountTo = accountsRepository.findById(accountToId).orElseThrow();

    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be a positive number ");
    }

    synchronized (accountFrom) {
      if (accountFrom.getBalance().compareTo(amount) < 0)
          throw new InsufficientBalanceException("Insufficient balance in account " + accountFromId);
      accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
    }

    synchronized (accountTo) {
      accountTo.setBalance(accountTo.getBalance().add(amount));
    }

    accountsRepository.save(accountFrom);
    accountsRepository.save(accountTo);

    //NotificationService.notifyAboutTransfer(accountFrom, amount+ "has been credited to your account");
    //NotificationService.notifyAboutTransfer(accountTo, amount+ "has been debited to your account");
  }
  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
}
