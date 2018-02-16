package a123.vaidya.nihal.foodcrunchserver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Model.User;
import io.paperdb.Paper;

public class Signin extends AppCompatActivity {

    MaterialEditText edtNmae,edtPhone,edtPasswd;
    Button BtnSignin;
    FirebaseDatabase db;
    DatabaseReference users;
    DrawerLayout drawer;
    CheckBox remember_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        edtNmae= findViewById(R.id.edtName);
        edtPasswd= findViewById(R.id.edtPasswd);
        edtPhone= findViewById(R.id.edtPhone);
        BtnSignin = findViewById(R.id.btnSignin);
        remember_button =(CheckBox)findViewById(R.id.remember_button);

        Paper.init(this);
        db =  FirebaseDatabase.getInstance();
        users = db.getReference("User");

        BtnSignin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    signinUser(edtPhone.getText().toString(),edtPasswd.getText().toString());

                };

        });

    }
    private void signinUser(String phone, String password) {
        final String localphone = phone;
        final String localpassword = password;

        if (Common.isConnectedToInternet(getBaseContext())) {
            //save user name and password
            if(remember_button.isChecked())
            {
                Paper.book().write(Common.USER_KEY,edtPhone.getText().toString());
                Paper.book().write(Common.PWD_KEY,edtPasswd.getText().toString());
            }
            users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //check if user doesnt exist in db
                if (dataSnapshot.child(localphone).exists()) {
                    //get user info
                    User user = dataSnapshot.child(localphone).getValue(User.class);
                    user.setPhone(localphone);

                    if(Boolean.parseBoolean(user.getIsStaff())) {
                        if ((user.getPassword().equals(edtPasswd.getText().toString())) )
                            //&&(user.getName().equals(edtNmae.getText().toString())) for verifying name and password
                        {
                            DatabaseReference myRef = db.getReference("message");
                            myRef.setValue("Hello from sign in ");

                            Intent homeIntent = new Intent(Signin.this,Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);

                        } else {
                            DatabaseReference myRef = db.getReference("message");
                            myRef.setValue("user doesnt exist check phone");
                            Toast.makeText(Signin.this, "Sign in FAILED login with staff account!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        DatabaseReference myRef = db.getReference("message");
                        myRef.setValue("user doesnt exist from sign in ");
                        Toast.makeText(Signin.this, "User Doesnt exist Please SIGN UP", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        }else
        {
            Toast.makeText(Signin.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
            return;
        }
    }
};


