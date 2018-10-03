package somebank.service;


import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class UserOperationSummaryTest {
    private final Long userId = 1L;
    private final Long timestamp = System.currentTimeMillis();
    private UserOperationSummary userOperationSummary;

    @Before
    public void before(){
        userOperationSummary = new UserOperationSummary(userId);
    }

    @Test
    public void positiveAmount(){
        BigDecimal amount = BigDecimal.valueOf(10L);

        Operation op = new Operation(userId, amount, timestamp);
        Assert.assertTrue(userOperationSummary.add(op));

        Assert.assertEquals(amount, userOperationSummary.getTotalAmount());
        Assert.assertEquals(Lists.newArrayList(op), userOperationSummary.getHistory());
    }

    @Test
    public void discardNegativeAmount(){
        Operation op = new Operation(userId, BigDecimal.valueOf(-10L), timestamp);

        Assert.assertFalse(userOperationSummary.add(op));
        Assert.assertEquals(BigDecimal.ZERO, userOperationSummary.getTotalAmount());
        Assert.assertTrue(userOperationSummary.getHistory().isEmpty());
    }

    @Test
    public void zeroAmount(){
        Operation op1 = new Operation(userId, BigDecimal.valueOf(10L), timestamp);
        Operation op2 = new Operation(userId, BigDecimal.valueOf(-10L), timestamp);

        Assert.assertTrue(userOperationSummary.add(op1));
        Assert.assertTrue(userOperationSummary.add(op2));

        Assert.assertEquals(BigDecimal.ZERO, userOperationSummary.getTotalAmount());
        Assert.assertEquals(Lists.newArrayList(op1, op2), userOperationSummary.getHistory());
    }
}
