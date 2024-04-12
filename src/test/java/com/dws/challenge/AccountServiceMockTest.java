package com.dws.challenge;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import com.dws.challenge.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AccountServiceMockTest {
    @Mock
    private AccountsRepository accountRepository;

    @InjectMocks
    private AccountsService accountService;


    @Test
    public void testWithdrawFromAccount() {

        String accountFromId = "accountFromId";
        String accountToId = "accountToId";
        BigDecimal amount = BigDecimal.TEN;

        Account accountFrom = new Account("1");
        accountFrom.setBalance(BigDecimal.valueOf(100));
        Account accountTo = new Account("2");
        accountTo.setBalance(BigDecimal.ZERO);

        when(accountRepository.findById(eq(accountFromId))).thenReturn(Optional.of(accountFrom));
        when(accountRepository.findById(eq(accountToId))).thenReturn(Optional.of(accountTo));

        accountService.withdrawFromAccount(accountFromId, accountToId, amount);


        assertEquals(BigDecimal.valueOf(90), accountFrom.getBalance()); // AccountFrom balance decreased by amount
        assertEquals(BigDecimal.TEN, accountTo.getBalance()); // AccountTo balance increased by amount

    }

    /*@Test(expected = IllegalArgumentException.class)
    public void testWithdrawFromAccountWithNegativeAmount() {

        String accountFromId = "accountFromId";
        String accountToId = "accountToId";
        BigDecimal amount = BigDecimal.valueOf(-10); // Negative amount

        accountService.withdrawFromAccount(accountFromId, accountToId, amount);
    }*/
}