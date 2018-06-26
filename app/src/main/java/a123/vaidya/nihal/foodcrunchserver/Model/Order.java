package a123.vaidya.nihal.foodcrunchserver.Model;

/**
 * Created by nnnn on 27/12/2017.
 */

public class Order {
    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Discount;
<<<<<<< HEAD
<<<<<<< HEAD
=======
    private String Image;
    private String Email;
>>>>>>> old1/master
=======
    private String Image;
    private String Email;
>>>>>>> old2/master

    public Order() {

    }
<<<<<<< HEAD
<<<<<<< HEAD
    public static String convertCodeToStatus(String code)
    {
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "Your food is on the way ";
        else
            return "Shipped!!";
    }
    public Order(String productId, String productName,String quantity, String price, String discount) {
=======
=======
>>>>>>> old2/master

    public Order(int id, String productId, String productName, String quantity, String price, String discount, String image, String email) {
    }

    public static String convertCodeToStatus(String code)
    {
        switch (code) {
            case "0":
                return "Placed";
            case "1":
                return "Your food is on the way ";
            default:
                return "Shipped!!";
        }
    }
    public Order(String productId, String productName,String quantity, String price, String discount,String image,String email) {
<<<<<<< HEAD
>>>>>>> old1/master
=======
>>>>>>> old2/master
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
<<<<<<< HEAD
<<<<<<< HEAD
=======
        Image = image;
        Email = email;
>>>>>>> old1/master
=======
        Image = image;
        Email = email;
>>>>>>> old2/master
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
=======
>>>>>>> old2/master
    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

<<<<<<< HEAD
>>>>>>> old1/master
=======
>>>>>>> old2/master
    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
