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
        Assert.assertTrue(walletService.deposit(userId, amount));
        Assert.assertEquals(amount, walletService.userAmount(userId));
        Assert.assertEquals(1, walletService.history(userId).size());
    }

    @Test
    public void addWithdraw(){
        Assert.assertFalse(walletService.withdraw(userId, amount));
        Assert.assertEquals(BigDecimal.ZERO, walletService.userAmount(userId));
        Assert.assertTrue(walletService.history(userId).isEmpty());
    }

    @Test
    public void addDepositAndWithdraw(){
        Assert.assertTrue(walletService.deposit(userId, amount));
        Assert.assertTrue(walletService.withdraw(userId, BigDecimal.ONE));
        Assert.assertEquals(BigDecimal.valueOf(9L), walletService.userAmount(userId));
        Assert.assertEquals(2, walletService.history(userId).size());
    }

    @Test
    public void addWithdrawAndDeposite(){
        Assert.assertFalse(walletService.withdraw(userId, amount));
        Assert.assertTrue(walletService.deposit(userId, BigDecimal.ONE));
        Assert.assertEquals(BigDecimal.ONE, walletService.userAmount(userId));
        Assert.assertEquals(1, walletService.history(userId).size());
    }
}
