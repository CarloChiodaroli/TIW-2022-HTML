package it.polimi.tiw.tiw2022chioda.bean;

import it.polimi.tiw.tiw2022chioda.enums.UserType;

import java.util.Objects;

public class User {

    private int ID;
    private String name;
    private String surname;
    private String email;
    private String username;
    private UserType userType;

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getUserTypeAsString() {
        return userType.toString();
    }

    public void setUserType(String userType){
        this.userType = UserType.getUserTypeFromString(userType);
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", userType=" + userType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return ID == user.ID && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(email, user.email) && Objects.equals(username, user.username) && userType == user.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name, surname, email, username, userType);
    }
}
