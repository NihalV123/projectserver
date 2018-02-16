package a123.vaidya.nihal.foodcrunchserver.Common;

import android.net.Uri;

import a123.vaidya.nihal.foodcrunchserver.Model.Request;
import a123.vaidya.nihal.foodcrunchserver.Model.User;

/**
 * Created by nnnn on 26/12/2017.
 */

public class Common {
    public static User currentUser;
    public static Request currentRequest;

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static String convertCodeToStatus(String code)
    {
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "Your food is on the way ";
        else
            return "Shipped!!";
    }
    public static final int PICK_IMAGE_REQUEST= 71;
}
