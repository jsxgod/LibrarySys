package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Title {

    private final SimpleStringProperty ISBN = new SimpleStringProperty("");
    private final SimpleStringProperty author = new SimpleStringProperty("");
    private final SimpleStringProperty title = new SimpleStringProperty("");
    private final SimpleStringProperty publisher = new SimpleStringProperty("");
    private final SimpleIntegerProperty year = new SimpleIntegerProperty(1900);

    public Title(){
        this("-","-", "","",1900);
    }

    public Title(String author, String title, String ISBN, String publisher, Integer year) {
        setAuthor(author);
        setTitle(title);
        setISBN(ISBN);
        setPublisher(publisher);
        setYear(year);
    }

    public void setISBN(String ISBN) { this.ISBN.set(ISBN); }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setPublisher(String publisher) { this.publisher.set(publisher); }

    public void setYear(Integer year) { this.year.set(year); }

    public String getISBN() { return ISBN.get(); }

    public String getAuthor() {
        return author.get();
    }

    public String getTitle() { return title.get(); }

    public String getPublisher() { return publisher.get(); }

    public Integer getYear() { return year.get(); }

    public SimpleStringProperty titleISBNProperty() { return this.ISBN; }

    public SimpleStringProperty titleAuthorProperty() { return this.author; }

    public SimpleStringProperty titleTitleProperty() { return this.title; }

    public SimpleStringProperty titlePublisherProperty() { return this.publisher; }

    public SimpleIntegerProperty titleYearProperty() { return this.year; }

}

