package it.polimi.tiw.tiw2022chioda.enums;

public enum UserType {

    CLIENT, EMPLOYEE;

    public static UserType getUserTypeFromString(String type){
        return switch(type){
            case "CLIENT" -> CLIENT;
            case "EMPLOYEE" -> EMPLOYEE;
        };
    }
}
