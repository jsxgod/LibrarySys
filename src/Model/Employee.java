package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employee {
    private SimpleStringProperty pesel = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty surname = new SimpleStringProperty();
    private SimpleStringProperty email = new SimpleStringProperty();
    private SimpleIntegerProperty salary = new SimpleIntegerProperty();
    private SimpleStringProperty bankAccount = new SimpleStringProperty();

    public Employee(){ this("","","",0,""); }

    public Employee(String name, String surname, String email, int salary, String bankAccount) {
        setName(name);
        setSurname(surname);
        setEmail(email);
        setSalary(salary);
        setBankAccount(bankAccount);
    }

    public String getPesel() { return pesel.get(); }

    public SimpleStringProperty peselProperty() { return pesel; }

    public void setPesel(String pesel) { this.pesel.set(pesel); }

    public String getName() { return name.get(); }

    public SimpleStringProperty nameProperty() { return name; }

    public void setName(String name) { this.name.set(name); }

    public String getSurname() { return surname.get(); }

    public SimpleStringProperty surnameProperty() { return surname; }

    public void setSurname(String surname) { this.surname.set(surname); }

    public String getEmail() { return email.get(); }

    public SimpleStringProperty emailProperty() { return email; }

    public void setEmail(String email) { this.email.set(email); }

    public int getSalary() { return salary.get(); }

    public SimpleIntegerProperty salaryProperty() { return salary; }

    public void setSalary(int salary) { this.salary.set(salary); }

    public String getBankAccount() { return bankAccount.get(); }

    public SimpleStringProperty bankAccountProperty() { return bankAccount; }

    public void setBankAccount(String bankAccount) { this.bankAccount.set(bankAccount); }


}
