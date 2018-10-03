package somebank.service;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;

class UserOperationSummary {
    private final Long userId;
    private AtomicReference<BigDecimal> totalAmount = new AtomicReference<>(BigDecimal.ZERO);
    private ConcurrentLinkedDeque<Operation> operations = new ConcurrentLinkedDeque<>();

    UserOperationSummary(Long userId) {
        Objects.requireNonNull(userId);

        this.userId = userId;
    }

    void add(Operation operation){
        Objects.requireNonNull(operation);
        Preconditions.checkArgument(userId.equals(operation.getUserId()));

        totalAmount.updateAndGet(a -> a.add(operation.getAmount()));
        operations.add(operation);
    }

    BigDecimal getTotalAmount(){
        return totalAmount.get();
    }

    List<Operation> getHistory(){
        return new ArrayList<>(operations);
    }
}
