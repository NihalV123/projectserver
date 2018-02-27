package a123.vaidya.nihal.foodcrunchserver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Model.User;

public class Signup extends AppCompatActivity {

    MaterialEditText edtNmae,edtPhone,edtPasswd,edtSecureCode,edtEmail;
    Button BtnSignup;
    private FirebaseAuth mAuth;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtNmae = findViewById(R.id.edtName);
        edtPasswd = findViewById(R.id.edtPasswd);
        edtSecureCode = findViewById(R.id.edtSecureCode);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmails);
        BtnSignup = findViewById(R.id.btnSignup);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = rootRef.child("User");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        BtnSignup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if user doesnt exist in db

                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()&&
                                (edtPasswd.getText().toString() != null) &&(edtNmae.getText().toString() != null)
                                &&(edtSecureCode.getText().toString() != null)&&(edtPhone.getText().toString() != null)
                                && (edtEmail.getText().toString() != null)) {

                            DatabaseReference myRef = database.getReference("message");

                            myRef.setValue("No from sign up");
                            Toast.makeText(Signup.this, "User cannot be added \nPlease fill all fields\n User already exists", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(Signup.this, "Select the way you want to be notified", Toast.LENGTH_SHORT).show();

                            User user = new User(edtNmae.getText().toString(),edtPasswd.getText().toString(),edtSecureCode.getText().toString(),edtEmail.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            DatabaseReference myRef = database.getReference("message");
                            myRef.setValue("everythink ok");
                            String[] CC = {user.getEmail().toString()};
                            String[] TO = {user.getEmail().toString()};
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);

                            emailIntent.setData(Uri.parse("mailto:"));
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                            emailIntent.putExtra(Intent.EXTRA_CC, CC);
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "You have created a staff account with FoodCrunch the anytime shopping app");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Here are your account details \n"+
                                    "The new account is created for the staff member \t" +
                                    (user.getName().toString())+
                                    "\n with email \t" +
                                    (user.getEmail().toString())+
                                    "\nand has been linked to your phone number \n" +
                                    "\n \n Your password is  \t" +
                                    (user.getPassword().toString())+
                                    "\n \n and your secure code is \t" +
                                    (user.getSecureCode().toString())+
                                    "\n\nPlease write down your secure code. \nIt will be used to recover your password");
                            try {
                                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                               // Toast.makeText(Signup.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }

                            Toast.makeText(Signup.this, "SIGN UP successfull \n please select a way you want ot recieve your details", Toast.LENGTH_SHORT).show();
                           // finish();
                        }
                           }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                }else
                {
                    Toast.makeText(Signup.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
                }

            }


        });


        //firebase debug
        mAuth =FirebaseAuth.getInstance();

    }

}

