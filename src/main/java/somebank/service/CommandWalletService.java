package somebank.service;

import java.math.BigDecimal;

public interface CommandWalletService {
    boolean deposit(Long userId, BigDecimal amount);
    boolean withdraw(Long userId, BigDecimal amount);
}
