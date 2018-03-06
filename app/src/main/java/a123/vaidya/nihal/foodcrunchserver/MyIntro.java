package a123.vaidya.nihal.foodcrunchserver;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;

public class MyIntro extends AppIntro2{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntro2Fragment.newInstance("FOOD CRUNCH SERVER","MANAGE ORDERS"
                ,R.drawable.ic_restaurant_big,
                Color.parseColor("#4fd7ff")));

        addSlide(AppIntro2Fragment.newInstance("FOOD CRUNCH SERVER","MANAGE DIRECTIONS"
                ,R.drawable.ic_directions_big,
                Color.parseColor("#8c50e3")));

        addSlide(AppIntro2Fragment.newInstance("FOOD CRUNCH SERVER","MANAGE APPEARANCE"
                ,R.drawable.ic_child_care_big,
                Color.parseColor("#4fd7ff")));

        addSlide(AppIntro2Fragment.newInstance("FOOD CRUNCH SERVER","MANAGE YOURSELF"
                ,R.drawable.ic_thumb_up_big,
                Color.parseColor("#8c50e3")));

            showStatusBar(false);
          //  setBarColor(Color.parseColor("#333639"));

    }

    @Override
    public void onDonePressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        Toast.makeText(MyIntro.this,"You cannot skip from here HEHE ;) ,",Toast.LENGTH_LONG).show();
    }

}
