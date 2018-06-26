package a123.vaidya.nihal.foodcrunchserver;

import android.app.ProgressDialog;
import android.content.Intent;
<<<<<<< HEAD
<<<<<<< HEAD
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
=======
=======
import android.content.SharedPreferences;
>>>>>>> old2/master
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
<<<<<<< HEAD
=======
import android.preference.PreferenceManager;
>>>>>>> old2/master
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
<<<<<<< HEAD
>>>>>>> old1/master
=======
>>>>>>> old2/master
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

<<<<<<< HEAD
<<<<<<< HEAD
=======
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

>>>>>>> old1/master
=======
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

>>>>>>> old2/master
import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Model.Category;
import a123.vaidya.nihal.foodcrunchserver.Model.User;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.MenuViewHolder;
<<<<<<< HEAD
<<<<<<< HEAD
=======
import dmax.dialog.SpotsDialog;
>>>>>>> old1/master
=======
import dmax.dialog.SpotsDialog;
>>>>>>> old2/master
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    MaterialEditText edtNmae,edtPhone,edtPasswd;
    Button BtnSignin;
    Button BtnSignIn,BtnSignUp,btnTest1,btnTest2,btnTest3,
            btnTest4,btnTest5,btnTest6,btnTest7,btnTest8;
    TextView txtSlogan;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
<<<<<<< HEAD
<<<<<<< HEAD

=======
=======
>>>>>>> old2/master
    //important for social logins search key in logcat for sha1
    private void printKeyHash() {
        try{
            PackageInfo info =getPackageManager().getPackageInfo("a123.vaidya.nihal.foodcrunchclient",
                    PackageManager.GET_SIGNATURES);

            for(Signature signature:info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
<<<<<<< HEAD
>>>>>>> old1/master
=======
>>>>>>> old2/master
    //firebase init
    FirebaseDatabase database;
    DatabaseReference categories;
    FirebaseRecyclerAdapter <Category,MenuViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnSignIn= findViewById(R.id.btnSignin);
        BtnSignUp= findViewById(R.id.btnSignup);

//        btnTest1= findViewById(R.id.btntest1);
//        btnTest2= findViewById(R.id.btntest2);
//        btnTest3= findViewById(R.id.btntest3);
//        btnTest4= findViewById(R.id.btntest4);
<<<<<<< HEAD
<<<<<<< HEAD

=======
        printKeyHash();
>>>>>>> old1/master
=======
        printKeyHash();

        //app intro
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean isFirstStart = getPref.getBoolean("firstStart",true);
                if(isFirstStart)
                {
                    startActivity(new Intent(MainActivity.this,MyIntro.class));
                    SharedPreferences.Editor e= getPref.edit();
                    e.putBoolean("firstStart",false);
                    e.apply();
                }

            }
        });

        thread.start();
>>>>>>> old2/master

        txtSlogan= findViewById(R.id.txtslogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);
        //cache user details
        Paper.init(this);
        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
<<<<<<< HEAD
<<<<<<< HEAD
                Intent Signup= new Intent(MainActivity.this,Signup.class);
                startActivity(Signup);
=======
=======
>>>>>>> old2/master
                final SpotsDialog dialog = new SpotsDialog(MainActivity.this);
                dialog.show();
                Intent Signup= new Intent(MainActivity.this,Signup.class);
                startActivity(Signup);
                dialog.dismiss();
<<<<<<< HEAD
>>>>>>> old1/master
=======
>>>>>>> old2/master
            }
        });
        BtnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
<<<<<<< HEAD
<<<<<<< HEAD
                Intent Signin= new Intent(MainActivity.this,Signin.class);
                startActivity(Signin);
            }
        });
//        btnTest1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent Signup= new Intent(MainActivity.this,Signup.class);
//                startActivity(Signup);
//            }
//        });
//        btnTest2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent Signin= new Intent(MainActivity.this,Signin.class);
//                startActivity(Signin);
//            }
//        });
//        btnTest3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent Signup= new Intent(MainActivity.this,Signup.class);
//                startActivity(Signup);
//            }
//        });
//        btnTest4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent Signin= new Intent(MainActivity.this,Signin.class);
//                startActivity(Signin);
//            }
//        });
=======
=======
>>>>>>> old2/master
                final SpotsDialog dialog = new SpotsDialog(MainActivity.this);
                dialog.show();
                Intent Signin= new Intent(MainActivity.this,Signin.class);
                startActivity(Signin);
                dialog.dismiss();
            }
        });
<<<<<<< HEAD
>>>>>>> old1/master
=======
>>>>>>> old2/master
        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
        if(user != null && pwd != null)
        {
            if(!user.isEmpty() && !pwd.isEmpty())
                login(user,pwd);
        }
    }

    private void login(final String phone, final String pwd) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        edtNmae= findViewById(R.id.edtName);
        edtPasswd= findViewById(R.id.edtPasswd);
        edtPhone= findViewById(R.id.edtPhone);
        BtnSignin = findViewById(R.id.btnSignin);

        if (Common.isConnectedToInternet(getBaseContext())) {
            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //check if user doesnt exist in db
                    if (dataSnapshot.child(phone).exists()) {
                        //get user info
                        User user = dataSnapshot.child(phone).getValue(User.class);

                        user.setPhone(phone);

                        if ((user.getPassword().equals(pwd))) {
                            DatabaseReference myRef = database.getReference("message");
                            myRef.setValue("Hello from sign in ");
<<<<<<< HEAD
<<<<<<< HEAD

                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
=======
=======
>>>>>>> old2/master
                            final SpotsDialog dialog = new SpotsDialog(MainActivity.this);
                            dialog.show();
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            dialog.dismiss();
<<<<<<< HEAD
>>>>>>> old1/master
=======
>>>>>>> old2/master

                        } else {
                            DatabaseReference myRef = database.getReference("message");
                            myRef.setValue("user doesnt exist check phone");
                            Toast.makeText(MainActivity.this, "Sign in FAILED Check your Credentials", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        DatabaseReference myRef = database.getReference("message");
                        myRef.setValue("user doesnt exist from sign in ");
                        Toast.makeText(MainActivity.this, "User Doesnt exist Please SIGN UP", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else
        {
            Toast.makeText(MainActivity.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
<<<<<<< HEAD
<<<<<<< HEAD
            return;
=======
>>>>>>> old1/master
=======
>>>>>>> old2/master
        }


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
