package io.quarkiverse.jooq.pro.deployment;

import java.util.List;

import org.jboss.logging.Logger;
import org.jooq.tools.LoggerListener;

import io.quarkiverse.jooq.deployment.JooqProcessor;
import io.quarkiverse.jooq.pro.runtime.AbstractDslContextProducer;
import io.quarkiverse.jooq.runtime.JooqConfig;
import io.quarkiverse.jooq.runtime.JooqRecorder;
import io.quarkus.agroal.spi.JdbcDataSourceBuildItem;
import io.quarkus.arc.deployment.GeneratedBeanBuildItem;
import io.quarkus.arc.deployment.UnremovableBeanBuildItem;
import io.quarkus.datasource.runtime.DataSourcesBuildTimeConfig;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.recording.RecorderContext;

class JooqProProcessor extends JooqProcessor {

    private static final Logger log = Logger.getLogger(JooqProProcessor.class);

    @SuppressWarnings("unchecked")
    @Record(ExecutionTime.STATIC_INIT)
    @BuildStep
    @Override
    protected void build(RecorderContext recorder, JooqRecorder template,
            BuildProducer<ReflectiveClassBuildItem> reflectiveClass,
            BuildProducer<UnremovableBeanBuildItem> unremovableBeans,
            JooqConfig jooqConfig,
            BuildProducer<GeneratedBeanBuildItem> generatedBean,
            DataSourcesBuildTimeConfig dataSourceConfig,
            List<JdbcDataSourceBuildItem> jdbcDataSourcesBuildItem) {
        if (isUnconfigured(jooqConfig)) {
            return;
        }

        reflectiveClass.produce(new ReflectiveClassBuildItem(true, false, AbstractDslContextProducer.class));
        reflectiveClass.produce(new ReflectiveClassBuildItem(false, true, LoggerListener.class));

        if (!isPresentDialect(jooqConfig.defaultConfig)) {
            log.warn("No default sql-dialect been defined");
        }

        createDslContextProducerBean(generatedBean, unremovableBeans, jooqConfig, dataSourceConfig, jdbcDataSourcesBuildItem,
                AbstractDslContextProducer.class);
    }
}
