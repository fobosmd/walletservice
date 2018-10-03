package somebank.service;

import java.math.BigDecimal;
import java.util.List;

public interface QueryWalletService {
    BigDecimal userAmount(Long userId);
    List<Operation> history(Long userId);
}
