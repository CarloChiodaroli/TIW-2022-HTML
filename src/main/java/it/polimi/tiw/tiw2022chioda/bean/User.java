package it.polimi.tiw.tiw2022chioda.bean;

import it.polimi.tiw.tiw2022chioda.enums.UserType;

import java.util.Objects;

public class User {

    private int ID;
    private String email;
    private String username;
    private UserType userType;

    public User(){
        ID = 0;
        email = null;
        username = null;
        userType = null;
    }

    public int getID() {
        return ID;
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
        this.userType = UserType.valueOf(userType);
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

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
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
        return ID == user.ID && Objects.equals(email, user.email) && Objects.equals(username, user.username) && userType == user.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, email, username, userType);
    }
}
