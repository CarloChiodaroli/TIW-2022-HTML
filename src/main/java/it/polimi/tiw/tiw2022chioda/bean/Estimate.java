package it.polimi.tiw.tiw2022chioda.bean;

import it.polimi.tiw.tiw2022chioda.enums.UserType;
import it.polimi.tiw.tiw2022chioda.exception.WrongUserTypeException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Estimate {

    private int code;
    private int client;
    private int employee;
    private int product;
    private double price;
    private List<Integer> options;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public List<Integer> getOptionCodes() {
        return options;
    }

    public int getProductCode() {
        return product;
    }

    public int getClientId() {
        return client;
    }

    public int getEmployeeId() {
        return employee;
    }

    public void setClient(User client) throws WrongUserTypeException {
        if(!UserType.CLIENT.equals(client.getUserType()))
            throw new WrongUserTypeException(UserType.CLIENT, client.getUserType());
        this.client = client.getID();
    }

    public void setClientId(int clientId){
        this.client = clientId;
    }

    public void setEmployee(User employee) throws WrongUserTypeException{
        if(UserType.EMPLOYEE.equals(employee.getUserType()))
            throw new WrongUserTypeException(UserType.EMPLOYEE, employee.getUserType());
        this.employee = employee.getID();
    }

    public void setEmployeeId(int employeeId){
        this.employee = employeeId;
    }

    public void setOptions(List<Option> options) {
        this.options = options.stream()
                .map(Option::getCode)
                .collect(Collectors.toList());
    }

    public void setOptionCodes(List<Integer> options) {
        this.options = options;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProduct(Product product) {
        this.product = product.getCode();
    }

    public void setProductCode(int productCode) {
        this.product = productCode;
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
