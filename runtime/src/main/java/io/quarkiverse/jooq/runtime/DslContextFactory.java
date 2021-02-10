package io.quarkiverse.jooq.runtime;

import org.jboss.logging.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import io.agroal.api.AgroalDataSource;

/**
 *
 *
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
public class DslContextFactory {
    private static final Logger log = Logger.getLogger(DslContextFactory.class);

    static {
        System.setProperty("org.jooq.no-logo", String.valueOf(true)); // -Dorg.jooq.no-logo=true
    }

    // TODO: the commented out dialects are only available in a licensed version. How should this be handled?
    static public DSLContext create(String sqlDialect, AgroalDataSource ds, JooqCustomContext customContext) {
        DSLContext context;
        if ("PostgreSQL".equalsIgnoreCase(sqlDialect) || "Postgres".equalsIgnoreCase(sqlDialect)) {
            context = DSL.using(ds, SQLDialect.POSTGRES);
            //        } else if ("PostgreSQL10".equalsIgnoreCase(sqlDialect) || "Postgres10".equalsIgnoreCase(sqlDialect)) {
            //            context = DSL.using(ds, SQLDialect.POSTGRES_10);
            //        } else if ("PostgreSQL93".equalsIgnoreCase(sqlDialect) || "Postgres93".equalsIgnoreCase(sqlDialect)
            //                || "PostgreSQL9.3".equalsIgnoreCase(sqlDialect) || "Postgres9.3".equalsIgnoreCase(sqlDialect)) {
            //            context = DSL.using(ds, SQLDialect.POSTGRES_9_3);
            //        } else if ("PostgreSQL94".equalsIgnoreCase(sqlDialect) || "Postgres94".equalsIgnoreCase(sqlDialect)
            //                || "PostgreSQL9.4".equalsIgnoreCase(sqlDialect) || "Postgres9.4".equalsIgnoreCase(sqlDialect)) {
            //            context = DSL.using(ds, SQLDialect.POSTGRES_9_4);
            //        } else if ("PostgreSQL95".equalsIgnoreCase(sqlDialect) || "Postgres95".equalsIgnoreCase(sqlDialect)
            //                || "PostgreSQL9.5".equalsIgnoreCase(sqlDialect) || "Postgres9.5".equalsIgnoreCase(sqlDialect)) {
            //            context = DSL.using(ds, SQLDialect.POSTGRES_9_5);
        } else if ("MySQL".equalsIgnoreCase(sqlDialect)) {
            context = DSL.using(ds, SQLDialect.MYSQL);
            //        } else if ("MySQL57".equalsIgnoreCase(sqlDialect) || "MySQL5.7".equalsIgnoreCase(sqlDialect)) {
            //            context = DSL.using(ds, SQLDialect.MYSQL_5_7);
            //        } else if ("MySQL80".equalsIgnoreCase(sqlDialect) || "MySQL8.0".equalsIgnoreCase(sqlDialect)
            //                || "MySQL8".equalsIgnoreCase(sqlDialect)) {
            //            context = DSL.using(ds, SQLDialect.MYSQL_8_0);
        } else if ("MARIADB".equalsIgnoreCase(sqlDialect)) {
            context = DSL.using(ds, SQLDialect.MARIADB);
        } else if ("Oracle".equalsIgnoreCase(sqlDialect)) {
            context = DSL.using(ds, SQLDialect.DEFAULT);
        } else if ("SQLServer".equalsIgnoreCase(sqlDialect)) {
            context = DSL.using(ds, SQLDialect.DEFAULT);
        } else if ("DB2".equalsIgnoreCase(sqlDialect)) {
            context = DSL.using(ds, SQLDialect.DEFAULT);
        } else if ("Derby".equalsIgnoreCase(sqlDialect)) {
            context = DSL.using(ds, SQLDialect.DERBY);
        } else if ("HSQLDB".equalsIgnoreCase(sqlDialect)) {
            context = DSL.using(ds, SQLDialect.HSQLDB);
        } else if ("H2".equalsIgnoreCase(sqlDialect)) {
            context = DSL.using(ds, SQLDialect.H2);
        } else if ("Firebird".equalsIgnoreCase(sqlDialect)) {
            context = DSL.using(ds, SQLDialect.FIREBIRD);
            //        } else if ("Firebird25".equalsIgnoreCase(sqlDialect) || "Firebird2.5".equalsIgnoreCase(sqlDialect)) {
            //            context = DSL.using(ds, SQLDialect.FIREBIRD_2_5);
            //        } else if ("Firebird30".equalsIgnoreCase(sqlDialect) || "Firebird3.0".equalsIgnoreCase(sqlDialect)
            //                || "Firebird3".equalsIgnoreCase(sqlDialect)) {
            //            context = DSL.using(ds, SQLDialect.FIREBIRD_3_0);
        } else if ("SQLite".equalsIgnoreCase(sqlDialect)) {
            context = DSL.using(ds, SQLDialect.SQLITE);
            //        } else if ("CUBRID".equalsIgnoreCase(sqlDialect)) {
            //            context = DSL.using(ds, SQLDialect.CUBRID);
        } else {
            log.warnv("Undefined sqlDialect: {0}", sqlDialect);
            context = DSL.using(ds, SQLDialect.DEFAULT);
        }
        customContext.apply(context.configuration());
        return context;
    }

}
