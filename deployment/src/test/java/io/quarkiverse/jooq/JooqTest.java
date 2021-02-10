package io.quarkiverse.jooq;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.h2.tools.Server;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.test.QuarkusUnitTest;

/**
 *
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
//@Disabled
public class JooqTest {
    private static final Logger LOGGER = Logger.getLogger(JooqTest.class);

    /**
     * http://www.h2database.com/html/main.html
     */
    static private Server server;

    @Order(1)
    @BeforeAll
    static public void startDatabase() {
        LOGGER.debug("Start H2 server..." + server);
        try {
            server = Server.createTcpServer(new String[] { "-trace", "-tcp", "-tcpAllowOthers", "-tcpPort", "19092" })
                    .start();
            Thread.sleep(1000 * 1); // waiting for database to be ready
        } catch (Exception e) {
            LOGGER.error("Start H2 server failed", e);
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static public void stopDatabase() {
        LOGGER.debug("Stop H2 server..." + server);
        if (server != null) {
            server.stop();
            server = null;
        }
        // while (true) { // only for GUI tool to view data
        // try {
        // Thread.sleep(100);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }
    }

    @Order(10)
    @RegisterExtension
    static final QuarkusUnitTest config = new QuarkusUnitTest() //
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class) //
                    .addAsResource("application.properties", "application.properties") //
                    .addClasses(
                            TestBean.class,
                            MyCustomConfigurationFactory.class,
                            MyCustomConfiguration1.class,
                            QDemo.class,
                            RDemo.class,
                            Public.class,
                            Demo.class));

    @Inject
    TestBean testBean;

    // @BeforeEach
    // public void setUp() throws Exception {
    // LOGGER.debug("setUp...");
    // }
    //
    // @AfterEach
    // public void tearDown() throws Exception {
    // LOGGER.debug("tearDown...");
    // }

    @Test
    public void test1() {
        LOGGER.info("BEGIN test1...");
        try {
            testBean.testConnection();
            //
            testBean.testDDL();
            testBean.testAll();

            // testBean.testInsert();
            // testBean.testUpdate();
            // testBean.testQuery();
            // testBean.testDelete(); // get connection failed if max<=2
            // testBean.testCount();

            testBean.testDDL1();
            testBean.testAllService1();
        } catch (Exception e) {
            LOGGER.error("", e);
        } finally {
            LOGGER.info("END test1.");
        }
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
                Assertions.assertTrue(tableExists);
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
                Assertions.assertEquals(1, row);
            }
        }

        void updateData() throws Exception {
            long row = dsl.update(QDemo.$) //
                    .set(QDemo.$.name, prefix + "-" + "5U") //
                    .set(QDemo.$.createdAt, new Date()) //
                    .where(QDemo.$.name.eq(prefix + "-" + 5)) //
                    .execute();
            LOGGER.debugv("updateData prefix: {0}, row: {1}", prefix, row);
            Assertions.assertEquals(1, row);
        }

        void queryData() throws Exception {
            List<Demo> dataList = dsl.select() //
                    .from(QDemo.$) //
                    .fetchInto(Demo.class);

            LOGGER.debugv("queryData prefix: {0}, dataList.size: {1}", prefix, dataList.size());
            Assertions.assertTrue(dataList.size() == loop);

            Demo data = dsl.select() //
                    .from(QDemo.$) //
                    .where(QDemo.$.name.eq(prefix + "-" + "5U")) //
                    .fetchOneInto(Demo.class);
            LOGGER.debugv("queryData prefix: {0}, data: {1}", prefix, data);
            Assertions.assertTrue(data != null);
        }

        void deleteData() throws Exception {
            LOGGER.debug("deleteData...");
            long row = dsl.delete(QDemo.$) //
                    .execute();
            LOGGER.debugv("deleteData prefix: {0}, row: {1}", prefix, row);
            Assertions.assertTrue(row == loop);
        }

        void countData() throws Exception {
            LOGGER.debug("countData...");
            long total = dsl.select(DSL.count()).from(QDemo.$).fetchOneInto(long.class);
            // long total = dsl.selectCount().from(QDemo.$).fetchOne(0, long.class);
            LOGGER.debugv("countData prefix: {0}, total: {1}", prefix, total);
            Assertions.assertTrue(total == 0);
        }
    }
}
