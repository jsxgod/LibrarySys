package Model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import util.ReaderStatus;

import java.sql.Date;

public class Reader {

    private final SimpleStringProperty pesel = new SimpleStringProperty("");
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty surname = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private SimpleObjectProperty<Date> birthday = new SimpleObjectProperty<>();
    private SimpleStringProperty status = new SimpleStringProperty(ReaderStatus.INACTIVE.toString());

    public Reader(){
        this("","", "","", new Date(1900,1,1));
    }

    public Reader(String pesel, String name, String surname, String email, Date birthday) {
        setPesel(pesel);
        setName(name);
        setSurname(surname);
        setEmail(email);
        setBirthday(birthday);
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

    public void setEmail(String email) { this.email.set(email); }

    public void setBirthday(Date birthday) { this.birthday.set(birthday); }

    public void setStatus(ReaderStatus status) { this.status.set(status.toString()); }

    public String getPesel() { return pesel.get(); }

    public String getName() { return name.get(); }

    public String getSurname(){ return surname.get(); }

    public String getEmail() { return email.get(); }

    public Date getBirthday() { return birthday.get(); }

    public String getStatus() { return status.get(); }

    public SimpleStringProperty readerPeselProperty() { return this.pesel; }

    public SimpleStringProperty readerNameProperty() { return this.name; }

    public SimpleStringProperty readerSurnameProperty() { return this.surname; }

    public SimpleStringProperty readerEmailProperty() { return this.email; }

    public SimpleObjectProperty<Date> readerBirthdayProperty() { return this.birthday; }
}


