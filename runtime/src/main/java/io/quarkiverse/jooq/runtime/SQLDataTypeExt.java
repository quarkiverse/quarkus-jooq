package io.quarkiverse.jooq.runtime;

import java.sql.Types;
import java.util.Date;

import org.jooq.DataType;
import org.jooq.impl.DefaultDataType;

/**
 * org.jooq.impl.SQLDataType.TIMESTAMP(XXX) to io.quarkiverse.jooq.UTILDATE(XXX)
 *
 * @see org.jooq.impl.SQLDataType
 * @see java.sql.Types
 */
public final class SQLDataTypeExt {
    /**
     * The {@link Types#TIMESTAMP} type.
     */
    public static final DataType<java.util.Date> UTILDATE = new DefaultDataType<Date>(null, java.util.Date.class, "timestamp");

    /**
     * The {@link Types#TIMESTAMP_WITH_TIMEZONE} type.
     */
    public static final DataType<java.util.Date> UTILDATEWITHTIMEZONE = new DefaultDataType<Date>(null, java.util.Date.class,
            "timestamptz");
}
