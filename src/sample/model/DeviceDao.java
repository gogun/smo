package sample.model;

public class DeviceDao {
    String name;
    String coef;

    public DeviceDao(String name, String coef) {
        this.name = name;
        this.coef = coef;
    }

    public String getName() {
        return name;
    }

    public String getCoef() {
        return coef;
    }
}
