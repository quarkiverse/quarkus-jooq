package io.quarkiverse.jooq.runtime.graal;

import java.util.function.BooleanSupplier;

public class PostgreSQLNotPresent implements BooleanSupplier {
    @Override
    public boolean getAsBoolean() {
        try {
            Class.forName("org.postgresql.util.PGInterval");
            return false;
        } catch (ClassNotFoundException e) {
            return true;
        }
    }
}
