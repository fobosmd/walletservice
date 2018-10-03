package somebank.service;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class WalletService implements CommandWalletService, QueryWalletService {
    private ConcurrentMap<Long, UserOperationSummary> userMap = new ConcurrentHashMap<>();

    @Override
    public boolean deposit(Long userId, BigDecimal amount) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(amount);
        Preconditions.checkArgument(amount.compareTo(BigDecimal.ZERO) > 0);

        Operation operation = new Operation(userId, amount, now());
        prepareUserMap(userId);
        userMap.get(userId).add(operation);
        return true;
    }

    @Override
    public boolean withdraw(Long userId, BigDecimal amount) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(amount);
        Preconditions.checkArgument(amount.compareTo(BigDecimal.ZERO) > 0);

        Operation operation = new Operation(userId, amount.negate(), now());
        prepareUserMap(userId);
        userMap.get(userId).add(operation);
        return true;
    }

    @Override
    public BigDecimal userAmount(Long userId) {
        Objects.requireNonNull(userId);

        prepareUserMap(userId);
        return userMap.get(userId).getTotalAmount();
    }

    @Override
    public List<Operation> history(Long userId) {
        Objects.requireNonNull(userId);

        prepareUserMap(userId);
        return userMap.get(userId).getHistory();
    }

    private long now(){
        return Instant.now().toEpochMilli();
    }

    private void prepareUserMap(Long userId){
        if(!userMap.containsKey(userId)){
            userMap.putIfAbsent(userId, new UserOperationSummary(userId));
        }
    }
}
