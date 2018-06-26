package a123.vaidya.nihal.foodcrunchserver.Model;

import java.util.List;

/**
 * Created by nnnn on 28/12/2017.
 */

public class Request {
    private String phone;
    private String name;
    private String address;
    private String total;
<<<<<<< HEAD
    private String comment;
    private String status;
    private List<Order>foods;
=======
    private String status;
    private String comment;
    private String email;
    private String latlng;
    private String paymentState;
    private List<Order> foods;

>>>>>>> old1/master

    public Request() {
    }

<<<<<<< HEAD
    public Request(String phone, String name, String address, String total,String comment,String status, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.comment = comment;
        this.total = total;
        this.foods = foods;
        this.status = "0";
        //0 is placed 1 is shiping 2 is shipped
=======
    public Request(String phone, String name, String address, String total, String status, String comment, String email, String latlng, String paymentState, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.comment = comment;
        this.email = email;
        this.latlng = latlng;
        this.paymentState = paymentState;
        this.foods = foods;
>>>>>>> old1/master
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

<<<<<<< HEAD
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

=======
>>>>>>> old1/master
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

<<<<<<< HEAD
    public List<Order> getFoods() {
        return foods;
    }

    public String getStatus() {
        return status;
    }
    public static String convertCodeToStatus(String code)
    {
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "Your food is on the way ";
        else
            return "Shipped!!";
    }
=======
    public String getStatus() {
        return status;
    }

>>>>>>> old1/master
    public void setStatus(String status) {
        this.status = status;
    }

<<<<<<< HEAD
    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
=======
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
>>>>>>> old1/master
