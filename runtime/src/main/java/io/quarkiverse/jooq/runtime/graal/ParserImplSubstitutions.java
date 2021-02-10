package io.quarkiverse.jooq.runtime.graal;

import static com.oracle.svm.core.annotate.RecomputeFieldValue.Kind.FromAlias;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import org.jooq.Configuration;
import org.jooq.DDLQuery;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.conf.ParamType;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.DataTypeException;
import org.reactivestreams.Subscriber;

import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.RecomputeFieldValue;
import com.oracle.svm.core.annotate.TargetClass;

@TargetClass(className = "org.jooq.impl.ParserImpl")
public final class ParserImplSubstitutions {
    @Alias
    @RecomputeFieldValue(kind = FromAlias)
    public static DDLQuery IGNORE;
    @Alias
    @RecomputeFieldValue(kind = FromAlias)
    public static Query IGNORE_NO_DELIMITER;

    static {
        IGNORE = new DDLQuery() {

            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {

            }

            @Override
            public void attach(Configuration configuration) {

            }

            @Override
            public void detach() {

            }

            @Override
            public Configuration configuration() {
                return null;
            }

            @Override
            public int execute() throws DataAccessException {
                return 0;
            }

            @Override
            public CompletionStage<Integer> executeAsync() {
                return null;
            }

            @Override
            public CompletionStage<Integer> executeAsync(Executor executor) {
                return null;
            }

            @Override
            public boolean isExecutable() {
                return false;
            }

            @Override
            public String getSQL() {
                return null;
            }

            @Deprecated
            @Override
            public String getSQL(boolean inline) {
                return null;
            }

            @Override
            public String getSQL(ParamType paramType) {
                return null;
            }

            @Override
            public List<Object> getBindValues() {
                return null;
            }

            @Override
            public Map<String, Param<?>> getParams() {
                return null;
            }

            @Override
            public Param<?> getParam(String name) {
                return null;
            }

            @Override
            public Query bind(String param, Object value) throws IllegalArgumentException, DataTypeException {
                return null;
            }

            @Override
            public Query bind(int index, Object value) throws IllegalArgumentException, DataTypeException {
                return null;
            }

            @Override
            public Query poolable(boolean poolable) {
                return null;
            }

            @Override
            public Query queryTimeout(int seconds) {
                return null;
            }

            @Override
            public Query keepStatement(boolean keepStatement) {
                return null;
            }

            @Override
            public void close() throws DataAccessException {

            }

            @Override
            public void cancel() throws DataAccessException {

            }
        };

        IGNORE_NO_DELIMITER = new Query() {

            @Override
            public void attach(Configuration configuration) {

            }

            @Override
            public void detach() {

            }

            @Override
            public Configuration configuration() {
                return null;
            }

            @Override
            public int execute() throws DataAccessException {
                return 0;
            }

            @Override
            public CompletionStage<Integer> executeAsync() {
                return null;
            }

            @Override
            public CompletionStage<Integer> executeAsync(Executor executor) {
                return null;
            }

            @Override
            public boolean isExecutable() {
                return false;
            }

            @Override
            public String getSQL() {
                return null;
            }

            @Deprecated
            @Override
            public String getSQL(boolean inline) {
                return null;
            }

            @Override
            public String getSQL(ParamType paramType) {
                return null;
            }

            @Override
            public List<Object> getBindValues() {
                return null;
            }

            @Override
            public Map<String, Param<?>> getParams() {
                return null;
            }

            @Override
            public Param<?> getParam(String name) {
                return null;
            }

            @Override
            public Query bind(String param, Object value) throws IllegalArgumentException, DataTypeException {
                return null;
            }

            @Override
            public Query bind(int index, Object value) throws IllegalArgumentException, DataTypeException {
                return null;
            }

            @Override
            public Query poolable(boolean poolable) {
                return null;
            }

            @Override
            public Query queryTimeout(int seconds) {
                return null;
            }

            @Override
            public Query keepStatement(boolean keepStatement) {
                return null;
            }

            @Override
            public void close() throws DataAccessException {

            }

            @Override
            public void cancel() throws DataAccessException {

            }
        };
    }
}
