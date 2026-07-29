package io.quarkiverse.jooq.pro.runtime;

import org.jboss.logging.Logger;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import io.agroal.api.AgroalDataSource;
import io.quarkiverse.jooq.runtime.JooqCustomContext;
import io.r2dbc.spi.ConnectionFactory;

public class DslContextFactory {
    private static final Logger log = Logger.getLogger(DslContextFactory.class);

    static {
        System.setProperty("org.jooq.no-logo", String.valueOf(true));
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
        } else if ("PostgreSQL12".equalsIgnoreCase(sqlDialect) || "Postgres12".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.POSTGRES_12;
        } else if ("PostgreSQL11".equalsIgnoreCase(sqlDialect) || "Postgres11".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.POSTGRES_11;
        } else if ("PostgreSQL10".equalsIgnoreCase(sqlDialect) || "Postgres10".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.POSTGRES_10;
        } else if ("PostgreSQL93".equalsIgnoreCase(sqlDialect) || "Postgres93".equalsIgnoreCase(sqlDialect)
                || "PostgreSQL9.3".equalsIgnoreCase(sqlDialect) || "Postgres9.3".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.POSTGRES_9_3;
        } else if ("PostgreSQL94".equalsIgnoreCase(sqlDialect) || "Postgres94".equalsIgnoreCase(sqlDialect)
                || "PostgreSQL9.4".equalsIgnoreCase(sqlDialect) || "Postgres9.4".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.POSTGRES_9_4;
        } else if ("PostgreSQL95".equalsIgnoreCase(sqlDialect) || "Postgres95".equalsIgnoreCase(sqlDialect)
                || "PostgreSQL9.5".equalsIgnoreCase(sqlDialect) || "Postgres9.5".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.POSTGRES_9_5;
        } else if ("MySQL".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MYSQL;
        } else if ("MySQL57".equalsIgnoreCase(sqlDialect) || "MySQL5.7".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MYSQL_5_7;
        } else if ("MySQL80".equalsIgnoreCase(sqlDialect) || "MySQL8.0".equalsIgnoreCase(sqlDialect)
                || "MySQL8".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MYSQL_8_0;
        } else if ("MySQL8019".equalsIgnoreCase(sqlDialect) || "MySQL8.0.19".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MYSQL_8_0_19;
        } else if ("MARIADB".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MARIADB;
        } else if ("MARIADB10".equalsIgnoreCase(sqlDialect) || "MARIADB10.0".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MARIADB;
        } else if ("MARIADB10.1".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MARIADB_10_1;
        } else if ("MARIADB10.2".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MARIADB_10_2;
        } else if ("MARIADB10.3".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MARIADB_10_3;
        } else if ("MARIADB10.4".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MARIADB_10_4;
        } else if ("MARIADB10.5".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MARIADB_10_5;
        } else if ("Oracle".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.DEFAULT;
        } else if ("ORACLE10G".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ORACLE10G;
        } else if ("ORACLE11G".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ORACLE11G;
        } else if ("ORACLE12C".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ORACLE12C;
        } else if ("ORACLE18C".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ORACLE18C;
        } else if ("ORACLE20C".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ORACLE20C;
        } else if ("SQLServer".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.DEFAULT;
        } else if ("SQLSERVER2008".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SQLSERVER2008;
        } else if ("SQLSERVER2012".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SQLSERVER2012;
        } else if ("SQLSERVER2014".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SQLSERVER2014;
        } else if ("SQLSERVER2016".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SQLSERVER2016;
        } else if ("SQLSERVER2017".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SQLSERVER2017;
        } else if ("Derby".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.DERBY;
        } else if ("HSQLDB".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.HSQLDB;
        } else if ("H2".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.H2;
        } else if ("Firebird".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.FIREBIRD;
        } else if ("Firebird25".equalsIgnoreCase(sqlDialect) || "Firebird2.5".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.FIREBIRD_2_5;
        } else if ("Firebird30".equalsIgnoreCase(sqlDialect) || "Firebird3.0".equalsIgnoreCase(sqlDialect)
                || "Firebird3".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.FIREBIRD_3_0;
        } else if ("SQLite".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SQLITE;
        } else if ("SQLite325".equalsIgnoreCase(sqlDialect) || "SQLite3.25".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SQLITE_3_25;
        } else if ("SQLite328".equalsIgnoreCase(sqlDialect) || "SQLite3.28".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SQLITE_3_28;
        } else if ("SQLite330".equalsIgnoreCase(sqlDialect) || "SQLite3.30".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SQLITE_3_30;
        } else if ("ACCESS".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ACCESS;
        } else if ("ACCESS2013".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ACCESS2013;
        } else if ("ASE".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ASE;
        } else if ("ASE125".equalsIgnoreCase(sqlDialect) || "ASE12.5".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ASE_12_5;
        } else if ("ASE155".equalsIgnoreCase(sqlDialect) || "ASE15.5".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ASE_15_5;
        } else if ("ASE157".equalsIgnoreCase(sqlDialect) || "ASE15.7".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ASE_15_7;
        } else if ("ASE160".equalsIgnoreCase(sqlDialect) || "ASE16.0".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.ASE_16_0;
        } else if ("AURORAMYSQL".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.AURORA_MYSQL;
        } else if ("AURORAPOSTGRES".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.AURORA_POSTGRES;
        } else if ("COCKROACHDB".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.COCKROACHDB;
        } else if ("DB2".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.DB2;
        } else if ("DB29".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.DB2_9;
        } else if ("DB210".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.DB2_10;
        } else if ("DB211".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.DB2_11;
        } else if ("HANA".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.HANA;
        } else if ("INFORMIX".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.INFORMIX;
        } else if ("INGRES".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.INGRES;
        } else if ("MEMSQL".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.MEMSQL;
        } else if ("REDSHIFT".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.REDSHIFT;
        } else if ("SQLDATAWAREHOUSE".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SQLDATAWAREHOUSE;
        } else if ("SYBASE".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.SYBASE;
        } else if ("TERADATA".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.TERADATA;
        } else if ("VERTICA".equalsIgnoreCase(sqlDialect)) {
            return SQLDialect.VERTICA;
        } else {
            log.warnv("Undefined sqlDialect: {0}", sqlDialect);
            return SQLDialect.DEFAULT;
        }
    }
}
