package a123.vaidya.nihal.foodcrunchserver.Model;

import java.util.List;

/**
 * Created by nnnn on 28/12/2017.
 */

public class Request {
    private String phone;
    private String name;
    private String address;
    private String status;
    private String comment;
    //private String email;
    private String total;
    private List<Order> foods;
    private String paymentState;

    public Request() {
    }

    public Request(String phone, String name, String address, String status, String comment, String total, List<Order> foods, String paymentState) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.status = status;
        this.comment = comment;
        this.total = total;
        this.foods = foods;
        this.paymentState = paymentState;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }
}
