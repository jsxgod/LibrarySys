package test;

import org.junit.Test;
import util.SQLStatementBuilder;
import util.TitleStatementBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TitleStatementBuilderTest {

    private SQLStatementBuilder builder;
    private static final String EMPTY_STRING = "";

    @org.junit.Before
    public void setUp() throws Exception {
        this.builder = new TitleStatementBuilder();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        this.builder = null;
    }

    @Test
    public void selectTest(){

        assertEquals("SELECT * FROM titles", builder.select().build());
    }

    @Test
    public void singleStringWhereTest(){
        String column = "column";
        String value = "value";

        assertEquals("SELECT * FROM titles WHERE "+column+"='"+value+"'", builder.select().withWhere(column,value).build());
    }

    @Test
    public void multipleStringWhereTest(){
        String column = "column";
        String value = "value";

        assertEquals("SELECT * FROM titles WHERE "+column+"='"+value+"' AND "+column+"='"+value+"'",
                        builder.select().
                        withWhere(column,value).
                        withWhere(column,value).
                        build());
    }

    @Test
    public void singleIntWhereTest(){
        String column = "year";
        String value = "1997";
        Integer year = 1997;

        assertEquals("SELECT * FROM titles WHERE "+column+"="+year, builder.select().withWhere(column,value).build());
    }

    @Test
    public void IntStringWhereTest(){
        String yearColumn = "year";
        String column = "column";
        String value = "1997";
        Integer year = 1997;

        assertEquals("SELECT * FROM titles WHERE "+yearColumn+"="+year+" AND column='"+value+"'", builder.select().withWhere(yearColumn,value).withWhere(column,value).build());
    }

    @Test
    public void insertTest(){
        String table = "table";
        Integer year = 1997;
        List<String> list = new ArrayList<String>();
        list.add("isbn");
        list.add("author");
        list.add("title");
        list.add("publisher");
        list.add("1997");
        assertEquals("INSERT INTO "+table+" VALUES('isbn', 'author', 'title', 'publisher', "+year+");", builder.insert(table,list).build());
    }

    @Test
    public void insertWithEmptyTest(){
        String table = "table";
        Integer year = 1997;

        String unexpected = "INSERT INTO "+table+" VALUES('isbn', 'author', 'title', 'publisher', "+year+");";

        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("author");
        list.add("title");
        list.add("publisher");
        list.add("1997");

        assertNotEquals(unexpected, builder.insert(table,list).build());
    }

    @Test
    public void test(){
        String unexpected = "SELECT * FROM titles WHERE author='autor' AND title='tytuł' AND publisher='pub' AND year=1900";
        String actual = builder.select().withWhere("author",EMPTY_STRING).withWhere("title","tytuł").withWhere("publisher","pub").withWhere("year","1900").build();

        assertNotEquals(unexpected, actual);
    }
}