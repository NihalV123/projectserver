package a123.vaidya.nihal.foodcrunchserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import a123.vaidya.nihal.foodcrunchserver.Model.Category;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.MenuViewHolder;

public class MainActivity extends AppCompatActivity {

    Button BtnSignIn,BtnSignUp,btnTest1,btnTest2,btnTest3,
            btnTest4,btnTest5,btnTest6,btnTest7,btnTest8;
    TextView txtSlogan;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

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

        btnTest1= findViewById(R.id.btntest1);
        btnTest2= findViewById(R.id.btntest2);
        btnTest3= findViewById(R.id.btntest3);
        btnTest4= findViewById(R.id.btntest4);


        txtSlogan= findViewById(R.id.txtslogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);

        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Signup= new Intent(MainActivity.this,Signup.class);
                startActivity(Signup);
            }
        });
        BtnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Signin= new Intent(MainActivity.this,Signin.class);
                startActivity(Signin);
            }
        });
        btnTest1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Signup= new Intent(MainActivity.this,Signup.class);
                startActivity(Signup);
            }
        });
        btnTest2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Signin= new Intent(MainActivity.this,Signin.class);
                startActivity(Signin);
            }
        });
        btnTest3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Signup= new Intent(MainActivity.this,Signup.class);
                startActivity(Signup);
            }
        });
        btnTest4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Signin= new Intent(MainActivity.this,Signin.class);
                startActivity(Signin);
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
