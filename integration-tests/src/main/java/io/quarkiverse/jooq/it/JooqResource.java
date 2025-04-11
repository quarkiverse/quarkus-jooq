/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package io.quarkiverse.jooq.it;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.jboss.logging.Logger;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@Path("/jooq")
@ApplicationScoped
public class JooqResource {

    private static final Logger LOGGER = Logger.getLogger(JooqResource.class);

    @GET
    public String hello() {
        return "Hello jooq";
    }

    @ApplicationScoped
    static class TestBean {

        @Inject
        DSLContext dsl; // default

        @Inject
        @Named("dsl1")
        DSLContext dsl1;

        @Inject
        @Named("dsl2")
        DSLContext dsl2;

        private ServiceAction action;
        private ServiceAction action1;

        @PostConstruct
        void onPostConstruct() {
            LOGGER.debug("onPostConstruct");
        }

        /**
         * Called when the runtime has started
         *
         * @param event
         */
        void onStart(@Observes StartupEvent event) {
            LOGGER.debug("onStart, event=" + event);
            action = new ServiceAction(dsl, "name00", 10);
            action1 = new ServiceAction(dsl1, "name11", 15);
        }

        void onStop(@Observes ShutdownEvent event) {
            LOGGER.debug("onStop, event=" + event);
        }

        // https://github.com/quarkusio/quarkus/issues/2224
        // public void onTxSuccessEvent(@Observes(during =
        // TransactionPhase.AFTER_SUCCESS) String msg) {
        // LOGGER.debug("onTxSuccessEvent, msg=" + msg);
        // }
        //
        // public void onTxFailureEvent(@Observes(during =
        // TransactionPhase.AFTER_FAILURE) String msg) {
        // LOGGER.debug("onTxFailureEvent, msg=" + msg);
        // }
        //
        // public void onTxCompletionEvent(@Observes(during =
        // TransactionPhase.AFTER_COMPLETION) String msg) {
        // LOGGER.debug("onTxCompletionEvent, msg=" + msg);
        // }

        void testConnection() throws Exception {
            // (default)
            try (Connection conn = dsl.parsingConnection()) {
                DatabaseMetaData dmd = conn.getMetaData();
                LOGGER.debugv("dsl, databaseProductName: {0}, databaseProductVersion: {1}",
                        dmd.getDatabaseProductName(), dmd.getDatabaseProductVersion());
            } catch (Exception e) {
                LOGGER.error("dsl.getConnection", e);
                throw e;
            }

            // (1)
            try (Connection conn = dsl1.parsingConnection()) {
                DatabaseMetaData dmd = conn.getMetaData();
                LOGGER.debugv("dsl1, databaseProductName: {0}, databaseProductVersion: {1}",
                        dmd.getDatabaseProductName(), dmd.getDatabaseProductVersion());
            } catch (Exception e) {
                LOGGER.error("dsl1.getConnection", e);
                throw e;
            }

            // (2)
            try (Connection conn = dsl2.parsingConnection()) {
                DatabaseMetaData dmd = conn.getMetaData();
                LOGGER.debugv("dsl2, databaseProductName: {0}, databaseProductVersion: {1}",
                        dmd.getDatabaseProductName(), dmd.getDatabaseProductVersion());
            } catch (Exception e) {
                LOGGER.error("dsl2.getConnection", e);
                throw e;
            }
        }

        // === (default)
        void testDDL() throws Exception {
            action.createDDL();
        }

        @Transactional
        void testInsert() throws Exception {
            action.insertData();
        }

        @Transactional
        void testUpdate() throws Exception {
            action.updateData();
        }

        @Transactional(TxType.SUPPORTS)
        void testQuery() throws Exception {
            action.queryData();
        }

        @Transactional(TxType.REQUIRED)
        void testDelete() throws Exception {
            action.deleteData();
        }

        @Transactional(TxType.SUPPORTS)
        void testCount() throws Exception {
            action.countData();
        }

        @Transactional(TxType.REQUIRED)
        void testAllService() throws Exception {
            action.insertData();
            action.updateData();
            action.queryData();
            action.deleteData();
            action.countData();
        }

        @Transactional(TxType.REQUIRED)
        void testAll() throws Exception {
            testInsert();
            testUpdate();
            testQuery();
            testDelete();
            testCount();
        }

        // === (1)
        void testDDL1() throws Exception {
            action1.createDDL();
        }

        @Transactional
        void testInsert1() throws Exception {
            action1.insertData();
        }

        @Transactional
        void testUpdate1() throws Exception {
            action1.updateData();
        }

        @Transactional(TxType.SUPPORTS)
        void testQuery1() throws Exception {
            action1.queryData();
        }

        @Transactional(TxType.REQUIRED)
        void testDelete1() throws Exception {
            action1.deleteData();
        }

        @Transactional(TxType.SUPPORTS)
        void testCount1() throws Exception {
            action1.countData();
        }

        @Transactional(TxType.REQUIRED)
        void testAllService1() throws Exception {
            action1.insertData();
            action1.updateData();
            action1.queryData();
            action1.deleteData();
            action1.countData();
        }

        @Transactional(TxType.REQUIRED)
        void testAll1() throws Exception {
            testInsert1();
            testUpdate1();
            testQuery1();
            testDelete1();
            testCount1();
        }

    }

    static class ServiceAction {

        final private DSLContext dsl;
        final private int loop;
        final private String prefix;

        public ServiceAction(DSLContext dsl, String prefix, int loop) {
            this.dsl = dsl;
            this.prefix = prefix;
            this.loop = loop;
        }

        public ServiceAction(DSLContext dsl) {
            this.dsl = dsl;
            this.prefix = null;
            this.loop = -1;
        }

        void createDDL() throws Exception {
            String table = QDemo.$.getName();
            String ddl = "" + //
                    "CREATE TABLE " + table + "(" + //
                    " id char(32) NOT NULL," + //
                    " name varchar(128) NOT NULL," + //
                    " amount decimal(12,3) ," + //
                    " created_at timestamp NOT NULL," + //
                    " PRIMARY KEY (id)" + //
                    ")";

            try (Connection conn = dsl.parsingConnection()) {
                Statement stmt = conn.createStatement();
                stmt.execute(ddl);
                stmt.close();
                // listTableTypes(conn);
                ResultSet rs = conn.getMetaData().getTables(null, null, table.toUpperCase(), new String[] { "TABLE" });
                boolean tableExists = false;
                while (rs.next()) {
                    String tableType = rs.getString("TABLE_TYPE");
                    String tableCatalog = rs.getString("TABLE_CAT");
                    String tableSchema = rs.getString("TABLE_SCHEM");
                    String tableName = rs.getString("TABLE_NAME");
                    // tableType:TABLE, tableCatalog:DEFAULT, tableSchema:PUBLIC, table:demo
                    LOGGER.debugv("tableType:{0}, tableCatalog:{1}, tableSchema:{2}, table:{3}", tableType,
                            tableCatalog, tableSchema, table);
                    if (table.equalsIgnoreCase(tableName)) {
                        tableExists = true;
                    }
                }

                LOGGER.debugv("createDDL table: {0} success: {1}", table, tableExists);
                rs.close();

                if (!tableExists) {
                    throw new IllegalStateException("Table does not exist");
                }
            }
        }

        @SuppressWarnings("unused")
        private void listTableTypes(Connection conn) throws Exception {
            DatabaseMetaData dmd = conn.getMetaData();
            ResultSet ttRs = dmd.getTableTypes();
            int columnCount = ttRs.getMetaData().getColumnCount();
            while (ttRs.next()) {
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    String tableType = ttRs.getString(columnIndex);
                    // tableType: "EXTERNAL","SYSTEM TABLE","TABLE","TABLE LINK","VIEW"
                    LOGGER.debugv("tableType: {0}", tableType);
                }
            }
            ttRs.close();
        }

        void insertData() throws Exception {
            for (int i = 0; i < loop; i++) {
                String id = UUID.randomUUID().toString().replaceAll("-", "");
                int randomNum = ThreadLocalRandom.current().nextInt(0, 9);
                long row = dsl.insertInto(QDemo.$) //
                        .set(QDemo.$.id, id) //
                        .set(QDemo.$.name, prefix + "-" + i) //
                        .set(QDemo.$.amount, new BigDecimal(12.1 + randomNum)) //
                        .set(QDemo.$.createdAt, new Date()) //
                        .execute();
                LOGGER.debugv("insertData prefix: {0}, i: {1}, row: {2}", prefix, i, row);
                if (row != 1L) {
                    throw new IllegalStateException("row is not equal to 1");
                }
            }
        }

        void updateData() throws Exception {
            long row = dsl.update(QDemo.$) //
                    .set(QDemo.$.name, prefix + "-" + "5U") //
                    .set(QDemo.$.createdAt, new Date()) //
                    .where(QDemo.$.name.eq(prefix + "-" + 5)) //
                    .execute();
            LOGGER.debugv("updateData prefix: {0}, row: {1}", prefix, row);
            if (row != 1L) {
                throw new IllegalStateException("row is not equal to 1");
            }
        }

        void queryData() throws Exception {
            List<Demo> dataList = dsl.select() //
                    .from(QDemo.$) //
                    .fetchInto(Demo.class);

            LOGGER.debugv("queryData prefix: {0}, dataList.size: {1}", prefix, dataList.size());
            if (dataList.size() != loop) {
                throw new IllegalStateException("dataList.size() != loop");
            }

            Demo data = dsl.select() //
                    .from(QDemo.$) //
                    .where(QDemo.$.name.eq(prefix + "-" + "5U")) //
                    .fetchOneInto(Demo.class);
            LOGGER.debugv("queryData prefix: {0}, data: {1}", prefix, data);
            if (data == null) {
                throw new IllegalStateException("data == null");
            }
        }

        void deleteData() throws Exception {
            LOGGER.debug("deleteData...");
            long row = dsl.delete(QDemo.$) //
                    .execute();
            LOGGER.debugv("deleteData prefix: {0}, row: {1}", prefix, row);
            if (row != loop) {
                throw new IllegalStateException("row != loop");
            }
        }

        void countData() throws Exception {
            LOGGER.debug("countData...");
            long total = dsl.select(DSL.count()).from(QDemo.$).fetchOneInto(long.class);
            // long total = dsl.selectCount().from(QDemo.$).fetchOne(0, long.class);
            LOGGER.debugv("countData prefix: {0}, total: {1}", prefix, total);
            if (total != 0) {
                throw new IllegalStateException("total != 0");
            }
        }
    }
}
