package io.quarkiverse.jooq;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 *
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
@SuppressWarnings("serial")
@RegisterForReflection
public class Demo implements Serializable {

    private String id;
    private String name;
    private BigDecimal amount;
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return super.toString() + "[id=" + id + ", name=" + name + ", amount=" + amount + ", createdAt=" + createdAt
                + "]";
    }

}