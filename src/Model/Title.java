package Model;

import javafx.beans.property.SimpleStringProperty;

public class Title {

    private final SimpleStringProperty author = new SimpleStringProperty("-");
    private final SimpleStringProperty title = new SimpleStringProperty("-");
    private final SimpleStringProperty ISBN = new SimpleStringProperty("0");

    public Title(){
        this("-","-", "0");
    }

    public Title(String author, String title, String ISBN) {
        setAuthor(author);
        setTitle(title);
        setISBN(ISBN);
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setISBN(String ISBN) {
        if(ISBN.length() == 13) {
            this.ISBN.set(ISBN);
        }
    }


    public String getAuthor() {
        return author.get();
    }

    public String getTitle() { return title.get(); }

    public String getISBN(){ return ISBN.get(); }

}

