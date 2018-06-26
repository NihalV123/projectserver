package a123.vaidya.nihal.foodcrunchserver.Model;


public class User {
    private String Name;
    private String Password;
    private String Phone;
    private String IsStaff;
    private String secureCode;
<<<<<<< HEAD
=======
    private String HomeAddress;
    private String Email;
    private String Extra;
>>>>>>> old1/master

    public User (){
    }

<<<<<<< HEAD
    public User (String name, String password,String secureCode) {
=======
    public User (String name, String password,String secureCode,String homeAddress,String email
            //,String extra
                ) {
>>>>>>> old1/master
        Name = name;
        IsStaff="true";
        Password = password;
        this.secureCode = secureCode;
<<<<<<< HEAD
=======
        HomeAddress = homeAddress;
        Email = email;
       // Extra= extra;
>>>>>>> old1/master
    }

    public String getSecureCode() {
        return secureCode;
    }

<<<<<<< HEAD
=======
    public String getExtra() {
        return Extra;
    }

    public void setExtra(String extra) {
        Extra = extra;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getHomeAddress() {
        return HomeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        HomeAddress = homeAddress;
    }

>>>>>>> old1/master
    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword () {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }
}
