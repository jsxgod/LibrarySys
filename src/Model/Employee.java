package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employee {
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleIntegerProperty salary;
    private SimpleStringProperty bankAccount;

    public Employee(){ this("","",0,""); }

    public Employee(String name, String surname, int salary, String bankAccount) {
        setName(name);
        setSurname(surname);
        setSalary(salary);
        setBankAccount(bankAccount);
    }

    public String getName() { return name.get(); }

    public SimpleStringProperty nameProperty() { return name; }

    public void setName(String name) { this.name.set(name); }

    public String getSurname() { return surname.get(); }

    public SimpleStringProperty surnameProperty() { return surname; }

    public void setSurname(String surname) { this.surname.set(surname); }

    public int getSalary() { return salary.get(); }

    public SimpleIntegerProperty salaryProperty() { return salary; }

    public void setSalary(int salary) { this.salary.set(salary); }

    public String getBankAccount() { return bankAccount.get(); }

    public SimpleStringProperty bankAccountProperty() { return bankAccount; }

    public void setBankAccount(String bankAccount) { this.bankAccount.set(bankAccount); }


}
