package it.polimi.tiw.tiw2022chioda.bean;

import java.util.Objects;

public class Product {

    private int code;
    private String name;
    private String image;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public String getImage() {
        return image;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return code == product.code && Objects.equals(name, product.name) && Objects.equals(image, product.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, image);
    }
}
