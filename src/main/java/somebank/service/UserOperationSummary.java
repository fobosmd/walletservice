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

    boolean add(Operation operation){
        Objects.requireNonNull(operation);
        Preconditions.checkArgument(userId.equals(operation.getUserId()));

        switch (operation.getOperationType()){
            case WITHDRAW:
                return handleWithdraw(operation);
            case DEPOSIT:
                return handleDeposit(operation);
            default:
                throw new IllegalStateException(String.format("Unknown operationType %s", operation.getOperationType()));
        }
    }

    private boolean handleWithdraw(Operation operation){
        BigDecimal next = operation.getAmount();
        BigDecimal prev = totalAmount.getAndUpdate(a -> a.compareTo(next.abs()) >= 0 ? a.add(next) : a);

        if(prev.compareTo(next.abs()) < 0){
            return false;
        }

        operations.add(operation);
        return true;
    }

    private boolean handleDeposit(Operation operation){
        totalAmount.updateAndGet(a -> a.add(operation.getAmount()));
        operations.add(operation);
        return true;
    }

    BigDecimal getTotalAmount(){
        return totalAmount.get();
    }

    List<Operation> getHistory(){
        return new ArrayList<>(operations);
    }
}
