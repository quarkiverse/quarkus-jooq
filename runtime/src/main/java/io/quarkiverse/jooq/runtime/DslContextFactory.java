package io.quarkiverse.jooq.runtime;

import org.jboss.logging.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import io.agroal.api.AgroalDataSource;
import io.r2dbc.spi.ConnectionFactory;

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

    static public DSLContext create(String sqlDialect, AgroalDataSource ds, JooqCustomContext customContext) {
        DSLContext context = DSL.using(ds, toSQLDialect(sqlDialect));
        customContext.apply(context.configuration());
        return context;
    }

    static public DSLContext create(String sqlDialect, ConnectionFactory connectionFactory, JooqCustomContext customContext) {
        DSLContext context = DSL.using(connectionFactory, toSQLDialect(sqlDialect));
        customContext.apply(context.configuration());
        return context;
    }

    static private SQLDialect toSQLDialect(String sqlDialect) {
        if ("PostgreSQL".equalsIgnoreCase(sqlDialect) || "Postgres".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.POSTGRES;
        } else if ("MySQL".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MYSQL;
        } else if ("MARIADB".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MARIADB;
        } else if ("Oracle".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.DEFAULT;
        } else if ("SQLServer".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.DEFAULT;
        } else if ("DB2".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.DEFAULT;
        } else if ("Derby".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.DERBY;
        } else if ("HSQLDB".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.HSQLDB;
        } else if ("H2".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.H2;
        } else if ("Firebird".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.FIREBIRD;
        } else if ("SQLite".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SQLITE;
        } else {
            log.warnv("Undefined sqlDialect: {0}", sqlDialect);
            return SQLDialect.DEFAULT;
        }
    }
}
