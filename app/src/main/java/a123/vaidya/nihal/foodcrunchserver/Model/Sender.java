package a123.vaidya.nihal.foodcrunchserver.Model;

public class Sender {
    public String to;
    public a123.vaidya.nihal.foodcrunchclient.Model.Notification notification;

    public Sender(String token, a123.vaidya.nihal.foodcrunchclient.Model.Notification notification) {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public a123.vaidya.nihal.foodcrunchclient.Model.Notification getNotification() {
        return notification;
    }

    public void setNotification(a123.vaidya.nihal.foodcrunchclient.Model.Notification notification) {
        this.notification = notification;
    }
}
