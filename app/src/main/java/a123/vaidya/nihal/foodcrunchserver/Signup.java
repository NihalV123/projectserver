package a123.vaidya.nihal.foodcrunchserver;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import a123.vaidya.nihal.foodcrunchserver.Model.User;

public class Signup extends AppCompatActivity {

    MaterialEditText edtNmae,edtPhone,edtPasswd;
    Button BtnSignup;
    private FirebaseAuth mAuth;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtNmae= findViewById(R.id.edtName);
        edtPasswd= findViewById(R.id.edtPasswd);
        edtPhone= findViewById(R.id.edtPhone);
        BtnSignup = findViewById(R.id.btnSignup);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("User");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        BtnSignup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if user doesnt exist in db

                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            DatabaseReference myRef = database.getReference("message");

                            myRef.setValue("Hello from sign up");

                            Toast.makeText(Signup.this, "Phone Number already registered!", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            User user = new User(edtNmae.getText().toString(),edtPasswd.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            DatabaseReference myRef = database.getReference("message");

                            myRef.setValue("everythink ok");
                            Toast.makeText(Signup.this, "SIGN UP successfull welcome to the crew!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        //firebase debug
        mAuth =FirebaseAuth.getInstance();

    }

}

