package a123.vaidya.nihal.foodcrunchserver;

import android.os.Bundle;
<<<<<<< HEAD
<<<<<<< HEAD
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.OrderDetailsAdapter;
import a123.vaidya.nihal.foodcrunchserver.Model.Request;
=======
=======
>>>>>>> old2/master
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.OrderDetailsAdapter;
<<<<<<< HEAD
>>>>>>> old1/master
=======
>>>>>>> old2/master

public class OrderDetail extends AppCompatActivity {

    TextView order_id,order_phone,order_address,order_total,comment_details,order_status;
    String order_id_value ="";
    RecyclerView listFood;
<<<<<<< HEAD
<<<<<<< HEAD
=======
    SwipeRefreshLayout swipeRefreshLayout;
>>>>>>> old1/master
=======
    SwipeRefreshLayout swipeRefreshLayout;
>>>>>>> old2/master
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
<<<<<<< HEAD
<<<<<<< HEAD
        order_id = (TextView)findViewById(R.id.order_id);
        order_phone = (TextView)findViewById(R.id.order_phone);
        order_address = (TextView)findViewById(R.id.order_address);
        order_total = (TextView)findViewById(R.id.order_total);
        comment_details = (TextView)findViewById(R.id.comment_details);
        order_status = (TextView)findViewById(R.id.order_status);
        listFood = (RecyclerView)findViewById(R.id.listFood);
        listFood.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listFood.setLayoutManager(layoutManager);

=======
=======
>>>>>>> old2/master
        order_id = findViewById(R.id.order_id);
        order_phone = findViewById(R.id.order_phone);
        order_address = findViewById(R.id.order_address);
        order_total = findViewById(R.id.order_total);
        swipeRefreshLayout = findViewById(R.id.swipe_order_det);
        comment_details = findViewById(R.id.comment_details);
        order_status = findViewById(R.id.order_status);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark);

        Twitter.initialize(this);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("6ep60jj09lvUcHncYM3yCoIMr", "WXvH93jw1urHD9IzIk6FDRmKW0X5LGZgmMCDo67XFk2uDf2LGJ"))
                .debug(true)
                .build();
        listFood = findViewById(R.id.listFood);
        listFood.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listFood.setLayoutManager(layoutManager);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getIntent()!=null)
                    order_id_value = getIntent().getStringExtra("OrderId");
                //set  values
                order_id.setText("Order id : "+order_id_value);
                order_phone.setText("Phone No : "+Common.currentRequest.getPhone());
                order_total.setText("Total Ammount : Rs "+Common.currentRequest.getTotal());
                order_address.setText("Shipping Address : "+Common.currentRequest.getAddress());
                comment_details.setText("Comments : "+Common.currentRequest.getComment());

                OrderDetailsAdapter adapter = new OrderDetailsAdapter(Common.currentRequest.getFoods());
                adapter.notifyDataSetChanged();
                listFood.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(getIntent()!=null)
                    order_id_value = getIntent().getStringExtra("OrderId");
                //set  values
                order_id.setText("Order id : "+order_id_value);
                order_phone.setText("Phone No : "+Common.currentRequest.getPhone());
                order_total.setText("Total Ammount : Rs "+Common.currentRequest.getTotal());
                order_address.setText("Shipping Address : "+Common.currentRequest.getAddress());
                comment_details.setText("Comments : "+Common.currentRequest.getComment());

                OrderDetailsAdapter adapter = new OrderDetailsAdapter(Common.currentRequest.getFoods());
                adapter.notifyDataSetChanged();
                listFood.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
<<<<<<< HEAD
>>>>>>> old1/master
=======
>>>>>>> old2/master
        if(getIntent()!=null)
            order_id_value = getIntent().getStringExtra("OrderId");
        //set  values
        order_id.setText("Order id : "+order_id_value);
        order_phone.setText("Phone No : "+Common.currentRequest.getPhone());
        order_total.setText("Total Ammount : Rs "+Common.currentRequest.getTotal());
        order_address.setText("Shipping Address : "+Common.currentRequest.getAddress());
        comment_details.setText("Comments : "+Common.currentRequest.getComment());
<<<<<<< HEAD
<<<<<<< HEAD
        //order_status.setText(Common.currentRequest.getStatus());

=======
>>>>>>> old1/master
=======
>>>>>>> old2/master

        OrderDetailsAdapter adapter = new OrderDetailsAdapter(Common.currentRequest.getFoods());
        adapter.notifyDataSetChanged();
        listFood.setAdapter(adapter);

<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> old1/master
=======
>>>>>>> old2/master
    }
}
