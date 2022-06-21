package io.quarkiverse.jooq.runtime.graal;

import org.jooq.types.DayToSecond;
import org.jooq.types.YearToMonth;
import org.jooq.types.YearToSecond;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@TargetClass(className = "org.jooq.util.postgres.PostgresUtils", onlyWith = PostgreSQLNotPresent.class)
public final class PostgresUtilsSubstitutions {

    @Substitute
    public static DayToSecond toDayToSecond(Object pgInterval) {
        throw new IllegalArgumentException(
                "Unsupported interval type. Make sure you have the pgjdbc or redshift driver on your classpath: " + pgInterval);
    }

    @Substitute
    public static YearToMonth toYearToMonth(Object pgInterval) {
        throw new IllegalArgumentException(
                "Unsupported interval type. Make sure you have the pgjdbc or redshift driver on your classpath: " + pgInterval);
    }

    @Substitute
    public static Object toPGInterval(DayToSecond interval) {
        throw new IllegalArgumentException(
                "Unsupported interval type. Make sure you have the pgjdbc or redshift driver on your classpath: " + interval);

    }

    @Substitute
    public static Object toPGInterval(YearToSecond interval) {
        throw new IllegalArgumentException(
                "Unsupported interval type. Make sure you have the pgjdbc or redshift driver on your classpath: " + interval);
    }

    @Substitute
    public static Object toPGInterval(YearToMonth interval) {
        throw new IllegalArgumentException(
                "Unsupported interval type. Make sure you have the pgjdbc or redshift driver on your classpath: " + interval);
    }

    @Substitute
    private static final boolean pgIntervalAvailable() {
        return false;
    }
}
