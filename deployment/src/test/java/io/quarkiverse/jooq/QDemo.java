package io.quarkiverse.jooq;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import io.quarkiverse.jooq.runtime.SQLDataTypeExt;

/**
 *
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
@SuppressWarnings("serial")
public class QDemo extends TableImpl<RDemo> {

    public static final QDemo $ = new QDemo();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RDemo> getRecordType() {
        return RDemo.class;
    }

    public final TableField<RDemo, String> id = createField(DSL.name("id"), SQLDataType.CHAR(22).nullable(false).identity(true),
            this,
            "The PK");
    public final TableField<RDemo, String> name = createField(DSL.name("name"), SQLDataType.VARCHAR(128).nullable(false), this,
            "The Name");
    public final TableField<RDemo, BigDecimal> amount = createField(DSL.name("amount"), SQLDataType.NUMERIC(12, 3), this,
            "Amount Value");
    public final TableField<RDemo, Date> createdAt = createField(DSL.name("created_at"),
            SQLDataTypeExt.UTILDATE.nullable(false),
            this, "Created At");

    public QDemo() {
        this(DSL.name("demo"), null);
    }

    public QDemo(String alias) {
        this(DSL.name(alias), $);
    }

    private QDemo(Name alias, Table<RDemo> aliased) {
        this(alias, aliased, null);
    }

    private QDemo(Name alias, Table<RDemo> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("Demo Table"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<RDemo> getPrimaryKey() {
        return Keys.DEMO_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<RDemo>> getKeys() {
        return Arrays.<UniqueKey<RDemo>> asList(Keys.DEMO_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QDemo as(String alias) {
        return new QDemo(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QDemo as(Name alias) {
        return new QDemo(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public QDemo rename(String name) {
        return new QDemo(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public QDemo rename(Name name) {
        return new QDemo(name, null);
    }

}
