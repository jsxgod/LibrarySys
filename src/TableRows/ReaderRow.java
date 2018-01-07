package TableRows;

import javafx.beans.property.SimpleStringProperty;

public class ReaderRow {

    private final SimpleStringProperty pesel = new SimpleStringProperty("");
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty surname = new SimpleStringProperty("");

    public ReaderRow(){
        this("","", "");
    }

    public ReaderRow(String author, String title, String ISBN) {
        setPesel(author);
        setName(title);
        setSurname(ISBN);
    }

    public void setPesel(String pesel) {
        /*
        TODO
        VALIDATE PESEL (PeselValidator class?)
        probably not needed because pesel will be validated on the DB side.
         */

        this.pesel.set(pesel);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }


    public String getPesel() {
        return pesel.get();
    }

    public String getName() { return name.get(); }

    public String getSurname(){ return surname.get(); }

}


