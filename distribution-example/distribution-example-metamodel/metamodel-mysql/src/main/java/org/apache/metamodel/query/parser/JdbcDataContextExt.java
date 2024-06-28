package org.apache.metamodel.query.parser;

import org.apache.metamodel.MetaModelException;
import org.apache.metamodel.jdbc.JdbcDataContext;
import org.apache.metamodel.query.Query;
import org.apache.metamodel.schema.TableType;

import javax.sql.DataSource;
import java.sql.Connection;

public class JdbcDataContextExt extends JdbcDataContext {

    public JdbcDataContextExt(DataSource dataSource, TableType[] tableTypes, String catalogName) {
        super(dataSource, tableTypes, catalogName);
    }

    public JdbcDataContextExt(Connection connection, TableType[] tableTypes, String catalogName) {
        super(connection, tableTypes, catalogName);
    }

    public JdbcDataContextExt(Connection connection) {
        super(connection);
    }

    public JdbcDataContextExt(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Query parseQuery(final String queryString) throws MetaModelException {
        final QueryParserExt parser = new QueryParserExt(this, queryString);
        final Query query = parser.parse();
        return query;
    }

}
