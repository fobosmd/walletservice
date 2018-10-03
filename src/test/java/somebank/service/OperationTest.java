package somebank.service;

import org.junit.Test;

import java.math.BigDecimal;

public class OperationTest {

    @Test(expected = IllegalArgumentException.class)
    public void addZeroAmount(){
        new Operation(1L, BigDecimal.ZERO, 10L);
    }
}
