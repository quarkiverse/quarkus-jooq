package io.quarkiverse.jooq.runtime.graal;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@TargetClass(className = "org.jooq.impl.DefaultRecordMapper", innerClass = "ProxyMapper")
public final class DefaultRecordMapperSubstitutions<E> {
    @Substitute
    private E proxy() {
        return null;
    }
}