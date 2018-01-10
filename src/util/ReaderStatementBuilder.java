package util;

import java.util.List;

public class ReaderStatementBuilder implements SQLStatementBuilder {

    private String statement = "";
    private static final String EMPTY_STRING = "";
    private boolean firstWhere = true;

    @Override
    public ReaderStatementBuilder select() {
        statement = "SELECT * FROM readers";

        return this;
    }

    @Override
    public ReaderStatementBuilder insert(String table, List<String> values) {
       statement += "BEGIN\n" +
                "INSERT INTO readers\n" +
                "VALUES('"+values.get(0)+"', '"+values.get(1)+"', '"+values.get(2)+"','"+values.get(3)+"', '"+ "STR_TO_DATE('"+values.get(4)+"', '%d-%m-%Y'));\n" +
                "END;";

        return this;
    }

    @Override
    public ReaderStatementBuilder withWhere(String column, String value) {
        if(!value.equals(EMPTY_STRING)){
            if(firstWhere){
                statement += " WHERE "+column+"='"+value+"'";
                firstWhere = false;
            }
            else {
                statement += " AND "+column+"='"+value+"'";
            }
        }

        return this;
    }

    @Override
    public ReaderStatementBuilder clear() {
        statement = "";
        firstWhere = true;

        return this;
    }

    @Override
    public String build() {
        return statement;
    }
}
