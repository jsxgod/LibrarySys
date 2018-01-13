package util;

import java.util.List;

public interface SQLStatementBuilder {

    SQLStatementBuilder select();

    SQLStatementBuilder insert(String table, List<String> values);

    SQLStatementBuilder withWhere(String column, String value);

    SQLStatementBuilder clear();

    String build();
}
