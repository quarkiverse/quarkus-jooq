package io.quarkiverse.jooq.it;

import java.math.BigDecimal;
import java.util.Date;

import org.jooq.impl.UpdatableRecordImpl;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 *
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
@SuppressWarnings("serial")
@RegisterForReflection
public class RDemo extends UpdatableRecordImpl<RDemo> {

    private String id;
    private String name;
    private BigDecimal amount;
    private Date createdAt;

    /**
     * Create a detached RCompany
     */
    public RDemo() {
        super(QDemo.$);
    }

    public String getId() {
        return (String) get(0);
    }

    public RDemo setId(String id) {
        set(0, id);
        return this;
    }

    public String getName() {
        return (String) get(1);
    }

    public RDemo setName(String name) {
        set(1, name);
        return this;
    }

    public BigDecimal getAmount() {
        return (BigDecimal) get(2);
    }

    public RDemo setAmount(BigDecimal amount) {
        set(2, amount);
        return this;
    }

    public Date getCreatedAt() {
        return (Date) get(3);
    }

    public RDemo setCreatedAt(Date createdAt) {
        set(3, createdAt);
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + "[id=" + id + ", name=" + name + ", amount=" + amount + ", createdAt=" + createdAt
                + "]";
    }

}
