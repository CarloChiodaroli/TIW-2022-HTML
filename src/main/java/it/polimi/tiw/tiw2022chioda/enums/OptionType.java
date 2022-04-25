package it.polimi.tiw.tiw2022chioda.enums;

public enum OptionType {

    STANDARD, SALE;

    public static OptionType getUserTypeFromString(String type){
        return switch(type){
            case "STANDARD" -> STANDARD;
            case "SALE" -> SALE;
            default -> null;
        };
    }
}
