package com.dws.challenge;


import com.dws.challenge.domain.TransferRequest;
import com.dws.challenge.exception.InsufficientBalanceException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.web.AccountsController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTestMocked {

    @Mock
    private AccountsService accountService;

    @InjectMocks
    private AccountsController accountController;

    @Test
    public void testWithdrawFromAccount_SuccessfulWithdrawal() {
        TransferRequest transferRequest = new TransferRequest("accountFromId","accountToId");
        transferRequest.setAmount(new BigDecimal(String.valueOf(BigDecimal.TEN)));
        doNothing().when(accountService).withdrawFromAccount(anyString(), anyString(), any(BigDecimal.class));
        ResponseEntity<String> response = accountController.withdrawFromAccount(transferRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Withdrawal successful.", response.getBody());
    }

    @Test
    public void testWithdrawFromAccount_InsufficientBalance() {
        TransferRequest transferRequest = new TransferRequest("accountFromId","accountToId");
        transferRequest.setAmount(new BigDecimal(String.valueOf(BigDecimal.TEN)));
        doThrow(new InsufficientBalanceException("Insufficient balance")).when(accountService)
                .withdrawFromAccount(anyString(), anyString(), any(BigDecimal.class));
        ResponseEntity<String> response = accountController.withdrawFromAccount(transferRequest);
        assertEquals("Insufficient balance", response.getBody());
        verify(accountService, times(1)).withdrawFromAccount("accountFromId", "accountToId", BigDecimal.TEN);
    }
}