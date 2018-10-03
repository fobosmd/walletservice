package somebank.service;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.util.Objects;

public final class Operation {
    private final Long userId;
    private final BigDecimal amount;
    private final Long timestamp;

    public Operation(Long userId, BigDecimal amount, Long timestamp) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(timestamp);
        Preconditions.checkArgument(!amount.equals(BigDecimal.ZERO));

        this.userId = userId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public OperationType getOperationType() {
        return amount.compareTo(BigDecimal.ZERO) > 0 ? OperationType.DEPOSIT : OperationType.WITHDRAW;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, amount, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Operation other = (Operation)obj;

        return Objects.equals(this.userId, other.userId)
                && Objects.equals(this.amount, other.amount)
                && Objects.equals(this.timestamp, other.timestamp);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("userId", userId)
                .add("amount", amount)
                .add("timestamp", timestamp)
                .toString();
    }
}
