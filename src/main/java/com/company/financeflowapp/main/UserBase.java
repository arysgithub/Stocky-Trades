package com.company.financeflowapp.main;
import java.util.Date;
//import java.sql.Date;

public class UserBase  {
    protected String username;
    protected int userId;
    protected String firstName;
    protected String lastName;
    protected Date dateOfBirth; //import
    protected double accountBalance;

    public UserBase(int userId, String firstName, String lastName, String username, Date dateOfBirth, double accountBalance) {
        System.out.println("User details have been saved in User Base ");
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.dateOfBirth = dateOfBirth;
        this.accountBalance = accountBalance;
    }

    public UserBase(String username) {
        this.username  = username;
    }


    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public double getAccountBalance() {
        return accountBalance;
    }


}