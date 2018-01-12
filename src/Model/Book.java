package Model;

public class Book  extends Title{
    private String readerPesel;
    private String isbn;
    private int id;

    public String getReader() { return readerPesel; }

    public void setReader(String readerPesel) { this.readerPesel = readerPesel; }

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
