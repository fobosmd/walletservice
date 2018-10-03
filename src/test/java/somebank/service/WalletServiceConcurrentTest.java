package somebank.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class WalletServiceConcurrentTest {
    private final Long userId = 1L;
    private final long userIdMin = 0L;
    private final long userIdMax = 100L;
    private final long count = 10_000;
    private final BigDecimal expAmount = BigDecimal.valueOf(50005000L);
    private WalletService walletService;

    @Before
    public void before(){
        walletService = new WalletService();
    }

    @Test
    public void addDepositsSameUser(){
        createStream().forEach(n -> walletService.deposit(userId, n));

        Assert.assertEquals(count, walletService.history(userId).size());
        Assert.assertEquals(expAmount, walletService.userAmount(userId));
    }

    @Test
    public void addWithdrawsSameUser(){
        createStream().forEach(n -> walletService.withdraw(userId, n));

        Assert.assertEquals(count, walletService.history(userId).size());
        Assert.assertEquals(expAmount.negate(), walletService.userAmount(userId));
    }

    @Test
    public void addDepositsDifferentUsers(){
        createStream().forEach(n -> walletService.deposit(generateUserId(), n));
        assertDifferentUsers(expAmount);
    }

    @Test
    public void addWithdrawDifferentUsers(){
        createStream().forEach(n -> walletService.withdraw(generateUserId(), n));
        assertDifferentUsers(expAmount.negate());
    }

    private void assertDifferentUsers(BigDecimal expectedAmount){
        BigDecimal totalAmount = BigDecimal.ZERO;
        long historySize = 0;

        for (long i = userIdMin; i < userIdMax; i++) {
            totalAmount = totalAmount.add(walletService.userAmount(i));
            historySize += walletService.history(i).size();
        }

        Assert.assertEquals(expectedAmount, totalAmount);
        Assert.assertEquals(count, historySize);
    }

    private Stream<BigDecimal> createStream(){
        return Stream.iterate(BigDecimal.ONE, n -> n.add(BigDecimal.ONE)).limit(count).parallel();
    }

    private long generateUserId(){
        return ThreadLocalRandom.current().nextLong(userIdMin, userIdMax);
    }
}
