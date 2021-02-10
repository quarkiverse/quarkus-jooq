package io.quarkiverse.jooq;

import org.jooq.impl.SchemaImpl;

@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    /**
     * The reference instance of <code>PUBLIC</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>PUBLIC.BOOK</code>.
     */
    public final QDemo DEMO = QDemo.$;

    /**
     * No further instances allowed
     */
    private Public() {
        super("PUBLIC", null);
    }

    //    /**
    //     * {@inheritDoc}
    //     */
    //    @Override
    //    public Catalog getCatalog() {
    //        return DefaultCatalog.DEFAULT_CATALOG;
    //    }
    //
    //    @Override
    //    public final List<Table<?>> getTables() {
    //        List result = new ArrayList();
    //        result.addAll(getTables0());
    //        return result;
    //    }
    //
    //    private final List<Table<?>> getTables0() {
    //        return Arrays.<Table<?>>asList(
    //            Author.AUTHOR,
    //            Book.BOOK,
    //            BookStore.BOOK_STORE,
    //            BookToBookStore.BOOK_TO_BOOK_STORE);
    //    }
}
