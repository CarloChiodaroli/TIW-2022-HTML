package it.polimi.tiw.tiw2022chioda.exception;

import it.polimi.tiw.tiw2022chioda.enums.UserType;

public class WrongUserTypeException extends RuntimeException {

    public WrongUserTypeException(UserType expected, UserType gotten) {
        super("Expected " + expected.toString() + " User type but got " + gotten.toString());
    }

    public WrongUserTypeException(UserType gotten) {
        super("User type " + gotten.toString() + " was not expected");
    }
}
