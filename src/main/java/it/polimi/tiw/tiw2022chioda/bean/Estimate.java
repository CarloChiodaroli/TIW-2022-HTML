package it.polimi.tiw.tiw2022chioda.bean;

import it.polimi.tiw.tiw2022chioda.enums.UserType;
import it.polimi.tiw.tiw2022chioda.exception.WrongUserTypeException;

import java.util.List;
import java.util.Objects;

public class Estimate {

    private int code;
    private User client;
    private User employee;
    private Product product;
    private double price;
    private List<Option> options;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public List<Option> getOptions() {
        return options;
    }

    public Product getProduct() {
        return product;
    }

    public User getClient() {
        return client;
    }

    public User getEmployee() {
        return employee;
    }

    public void setClient(User client) throws WrongUserTypeException {
        if(UserType.CLIENT.equals(client.getUserType()))
            throw new WrongUserTypeException(UserType.CLIENT, client.getUserType());
        this.client = client;
    }

    public void setEmployee(User employee) throws WrongUserTypeException{
        if(UserType.EMPLOYEE.equals(client.getUserType()))
            throw new WrongUserTypeException(UserType.EMPLOYEE, client.getUserType());
        this.employee = employee;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estimate estimate = (Estimate) o;
        return code == estimate.code && Double.compare(estimate.price, price) == 0 && Objects.equals(client, estimate.client) && Objects.equals(employee, estimate.employee) && Objects.equals(product, estimate.product) && Objects.equals(options, estimate.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, client, employee, product, price, options);
    }

    @Override
    public String toString() {
        return "Estimate{" +
                "code=" + code +
                ", clientCode=" + client +
                ", employeeCode=" + employee +
                ", productCode=" + product +
                ", price=" + price +
                ", options=" + options +
                '}';
    }
}
