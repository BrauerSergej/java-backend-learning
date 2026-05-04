package app.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class House {

    // long vs. Long -> null
    private Long id;
    private String color;
    private double area;
    private int rooms;
    private int year;
    private BigDecimal price;

    public House() {
    }

    public House(Long id, String color, double area, int rooms, int year, BigDecimal price) {
        this.id = id;
        this.color = color;
        this.area = area;
        this.rooms = rooms;
        this.year = year;
        this.price = price;
    }

    public House(String color, double area, int rooms, int year, BigDecimal price) {
        this.color = color;
        this.area = area;
        this.rooms = rooms;
        this.year = year;
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof House house)) return false;
        return Double.compare(area, house.area) == 0 && rooms == house.rooms && year == house.year && Objects.equals(id, house.id) && Objects.equals(color, house.color) && Objects.equals(price, house.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color, area, rooms, year, price);
    }

    @Override
    public String toString() {
        return "House{" +
                "id=" + id +
                ", color='" + color + '\'' +
                ", area=" + area +
                ", rooms=" + rooms +
                ", year=" + year +
                ", price=" + price +
                '}';
    }
}
