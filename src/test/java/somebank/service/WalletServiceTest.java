package somebank.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class WalletServiceTest {
    private final BigDecimal amount = BigDecimal.valueOf(10L);
    private final Long userId = 1L;
    private WalletService walletService;

    @Before
    public void before(){
        walletService = new WalletService();
    }

    @Test
    public void addDeposit(){
        walletService.deposit(userId, amount);

        Assert.assertEquals(amount, walletService.userAmount(userId));
        Assert.assertEquals(1, walletService.history(userId).size());
    }

    @Test
    public void addWithdraw(){
        walletService.withdraw(userId, amount);

        Assert.assertEquals(amount.negate(), walletService.userAmount(userId));
        Assert.assertEquals(1, walletService.history(userId).size());
    }
}
