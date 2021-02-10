package io.quarkiverse.jooq.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.jboss.logging.Logger;
import org.jooq.Configuration;
import org.jooq.ExecuteContext;
import org.jooq.ExecuteType;
import org.jooq.Param;
import org.jooq.QueryPart;
import org.jooq.VisitContext;
import org.jooq.VisitListener;
import org.jooq.VisitListenerProvider;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultVisitListener;
import org.jooq.impl.DefaultVisitListenerProvider;
import org.jooq.tools.LoggerListener;
import org.jooq.tools.StringUtils;

/**
 * Logger (package) name is 'io.quarkiverse.jooq.sql'
 */
@SuppressWarnings("serial")
public class SqlLoggerListener extends LoggerListener {
    private static final Logger log = Logger.getLogger(SqlLoggerListener.class);

    static public org.jooq.tools.JooqLogger sqlLog = org.jooq.tools.JooqLogger.getLogger(SqlLoggerListener.class);

    static {
        log.debug("sqlLog.isTraceEnabled: " + sqlLog.isTraceEnabled());
        if (sqlLog.isTraceEnabled()) {
            try {
                setStaticFinalField(LoggerListener.class, "log", sqlLog);
            } catch (Exception e) {
                log.warn(e.toString());
            }
        }
    }

    /**
     * Add a {@link VisitListener} that transforms all bind variables by
     * abbreviating them.
     */
    private final Configuration abbreviateBindVariables(Configuration configuration) {
        VisitListenerProvider[] oldProviders = configuration.visitListenerProviders();
        VisitListenerProvider[] newProviders = new VisitListenerProvider[oldProviders.length + 1];
        System.arraycopy(oldProviders, 0, newProviders, 0, oldProviders.length);
        newProviders[newProviders.length - 1] = new DefaultVisitListenerProvider(new BindValueAbbreviator());

        return configuration.derive(newProviders);
    }

    @Override
    public void renderEnd(ExecuteContext ctx) {
        if (log.isDebugEnabled()) {
            Configuration configuration = ctx.configuration();
            String newline = Boolean.TRUE.equals(configuration.settings().isRenderFormatted()) ? "\n" : "";

            // [#2939] Prevent excessive logging of bind variables only in DEBUG mode, not
            // in TRACE mode.
            if (!log.isTraceEnabled())
                configuration = abbreviateBindVariables(configuration);

            String[] batchSQL = ctx.batchSQL();
            if (ctx.query() != null) {

                // [#1278] DEBUG log also SQL with inlined bind values, if
                // that is not the same as the actual SQL passed to JDBC
                String inlined = DSL.using(configuration).renderInlined(ctx.query());
                if (!ctx.sql().equals(inlined)) {
                    sqlLog.debug("<<SQL>>", newline + inlined);
                } else {
                    // Actual SQL passed to JDBC
                    sqlLog.debug("<<SQL>>", newline + ctx.sql());
                }
            }

            // [#2987] Log routines
            else if (ctx.routine() != null) {

                String inlined = DSL.using(configuration).renderInlined(ctx.routine());

                if (!ctx.sql().equals(inlined)) {
                    sqlLog.debug("<<SQL:routine>>", newline + inlined);
                } else {
                    sqlLog.debug("<<SQL:routine>>", newline + ctx.sql());
                }
            }

            else if (!StringUtils.isBlank(ctx.sql())) {

                // [#1529] Batch queries should be logged specially
                if (ctx.type() == ExecuteType.BATCH)
                    sqlLog.debug("<<SQL:batch>>", newline + ctx.sql());
                else
                    sqlLog.debug("<<SQL>>", newline + ctx.sql());
            }

            // [#2532] Log a complete BatchMultiple query
            else if (batchSQL.length > 0) {
                if (batchSQL[batchSQL.length - 1] != null)
                    for (String sql : batchSQL)
                        sqlLog.debug("<<SQL:batch>>", newline + sql);
            }
        }
    }

    @Override
    public void recordEnd(ExecuteContext ctx) {
        // log.debug("..........recordEnd");
    }

    @Override
    public void resultEnd(ExecuteContext ctx) {
        // log.debug("..........resultEnd");
    }

    @Override
    public void executeEnd(ExecuteContext ctx) {
        // log.debug("Affected row(s)", ctx.rows());
    }

    private static final int maxLength = 2000;

    private static class BindValueAbbreviator extends DefaultVisitListener {

        private boolean anyAbbreviations = false;

        @Override
        public void visitStart(VisitContext context) {
            if (context.renderContext() != null) {
                QueryPart part = context.queryPart();

                if (part instanceof Param<?>) {
                    Param<?> param = (Param<?>) part;
                    Object value = param.getValue();

                    if (value instanceof String && ((String) value).length() > maxLength) {
                        anyAbbreviations = true;
                        context.queryPart(DSL.val(StringUtils.abbreviate((String) value, maxLength)));
                    } else if (value instanceof byte[] && ((byte[]) value).length > maxLength) {
                        anyAbbreviations = true;
                        context.queryPart(DSL.val(Arrays.copyOf((byte[]) value, maxLength)));
                    }
                }
            }
        }

        @Override
        public void visitEnd(VisitContext context) {
            if (anyAbbreviations) {
                if (context.queryPartsLength() == 1) {
                    context.renderContext().sql(
                            " -- Bind values may have been abbreviated for DEBUG logging. Use TRACE logging for very large bind variables.");
                }
            }
        }
    }

    // ===
    static void setStaticFinalField(Class<?> clsObj, String fieldName, Object newValue) {
        setFinalField(null, clsObj, fieldName, newValue);
    }

    static void setFinalField(Object reflectObj, Class<?> clsObj, String fieldName, Object newValue) {
        if (clsObj == null) {
            throw new IllegalArgumentException("(clsObj == null)");
        }
        if (fieldName == null || fieldName.length() == 0) {
            throw new IllegalArgumentException(
                    "(fieldName == null || fieldName.length() == 0), fieldName=[" + fieldName + "]");
        }
        try {
            Field field = clsObj.getDeclaredField(fieldName);
            boolean keepStatus = field.canAccess(reflectObj);
            if (!keepStatus) {
                field.setAccessible(true);
            }
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            try {
                field.set(reflectObj, newValue);
            } finally {
                field.setAccessible(keepStatus);
                modifiersField.setInt(field, field.getModifiers() & Modifier.FINAL);
            }
        } catch (Exception e) {
            String msg = "reflect object class: " + (reflectObj == null ? "<null>" : reflectObj.getClass().getName())
                    + ", declared class: " + clsObj.getName() + ", field name: " + fieldName;
            log.warn(msg + ", error: " + e.toString());
            throw new RuntimeException(msg, e);
        }
    }
}
