package it.polimi.tiw.tiw2022chioda.bean;

import it.polimi.tiw.tiw2022chioda.enums.OptionType;

public class Option {

    private int code;
    private OptionType type;
    private String name;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OptionType getType() {
        return type;
    }

    public String getTypeAsString() {
        return type.toString();
    }

    public void setType(String type){
        this.type = OptionType.valueOf(type);
    }

    public void setType(OptionType type){
        this.type = type;
    }
}
