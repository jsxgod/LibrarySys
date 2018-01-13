package util;

import java.util.List;

public class TitleStatementBuilder implements SQLStatementBuilder {

    private String statement = "";

    private boolean firstWhere = true;

    private static final String EMPTY_STRING = "";

    @Override
    public TitleStatementBuilder select() {
        statement = "SELECT * FROM titles";
        return this;
    }

    @Override
    public TitleStatementBuilder insert(String table, List<String> values) {

        int lastIndex = values.size()-1;
        int year = Integer.parseInt(values.get(lastIndex));
        values.remove(lastIndex);

        values.removeIf(value -> value.equals(EMPTY_STRING));
        for(String value : values){
            if (value.equals(EMPTY_STRING)){
                values.remove(value);
            }
        }

        String valuesPrepared = String.join("', '", values);
        statement += "INSERT INTO "+table+" VALUES('"+ valuesPrepared+"', "+year+");";

        return this;
    }

    @Override
    public TitleStatementBuilder withWhere(String column, String value) {
        if(!value.equals(EMPTY_STRING)){
            if(firstWhere){
                if(column.equals("year")){
                    int year = Integer.parseInt(value);
                    statement += " WHERE "+column+" LIKE "+year;
                    firstWhere = false;
                } else {
                    statement += " WHERE "+column+" LIKE '%"+value+"%'";
                    firstWhere = false;
                }
            } else {
                if(column.equals("year")){
                    statement += " AND "+column+"="+value;
                    firstWhere = false;
                } else { statement += " AND "+column+" LIKE '%"+value+"%'"; }
            }
        }

        return this;
    }

    @Override
    public TitleStatementBuilder clear() {
        statement = "";
        firstWhere = true;

        return this;
    }

    @Override
    public String build() {
        return statement;
    }
}
