package a123.vaidya.nihal.foodcrunchserver.Service;

<<<<<<< HEAD
<<<<<<< HEAD
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

=======
>>>>>>> old1/master
=======
>>>>>>> old2/master
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Model.Token;

public class MyFireBaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
<<<<<<< HEAD
<<<<<<< HEAD
        if(Common.currentUser != null)
=======
       // if(Common.currentUser != null)
>>>>>>> old1/master
=======
       // if(Common.currentUser != null)
>>>>>>> old2/master
        updateTokenToFirebase(tokenRefreshed);
    }

    private void updateTokenToFirebase(String tokenRefreshed) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token token = new Token(tokenRefreshed,true); //true as this reads frm server
<<<<<<< HEAD
<<<<<<< HEAD
        tokens.child(Common.currentUser.getPhone()).setValue(token);
=======
        try{
            tokens.child(Common.currentUser.getPhone()).setValue(token);}
        catch (Exception e){}
>>>>>>> old1/master
=======
        try{
            tokens.child(Common.currentUser.getPhone()).setValue(token);}
        catch (Exception e){}
>>>>>>> old2/master
    }
}
