package a123.vaidya.nihal.foodcrunchserver.Model;


public class Food {
    private String Name,Image,Description,Price,Discount,MenuId,Email,Video,Recepixes;
    private Integer Quantity;
    public Food() {
    }

    public Food(String name, String image, String description, String price,
                String discount, String menuId, String email, String video, String recepixes,  Integer quantity) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        Discount = discount;
        MenuId = menuId;
        Email = email;//extra
        Video = video;//extra2
        Recepixes = recepixes;
        Quantity = quantity;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public String getRecepixes() {
        return Recepixes;
    }

    public void setRecepixes(String recepixes) {
        Recepixes = recepixes;
    }
}
