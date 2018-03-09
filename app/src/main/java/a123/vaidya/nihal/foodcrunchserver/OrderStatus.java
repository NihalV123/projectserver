package a123.vaidya.nihal.foodcrunchserver;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.app.AlertDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Model.DataMessage;
import a123.vaidya.nihal.foodcrunchserver.Model.MyResponse;
//import a123.vaidya.nihal.foodcrunchserver.Model.Notification;
import a123.vaidya.nihal.foodcrunchserver.Model.Request;
//import a123.vaidya.nihal.foodcrunchserver.Model.Sender;
import a123.vaidya.nihal.foodcrunchserver.Model.Token;
import a123.vaidya.nihal.foodcrunchserver.Remote.APIService;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.OrderViewHolder;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public  RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;
    FirebaseDatabase db;
    DatabaseReference requests;
    MaterialSpinner spinner;
    SwipeRefreshLayout swipeRefreshLayout;
    APIService mAPIService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        //service
        mAPIService = Common.getFCMClient();

        //firebase
        swipeRefreshLayout = findViewById(R.id.swipe_order_list);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //loadOrders(Common.currentUser.getPhone());
                loadOrders();
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //loadOrders(Common.currentUser.getPhone());
                loadOrders();
            }
        });
        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Requests");

        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //loadOrders(Common.currentUser.getPhone());
        loadOrders();


    }

    private void loadOrders() {
        Query getOrderByUserQuery = requests;
        FirebaseRecyclerOptions<Request> orderoptions = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(getOrderByUserQuery, Request.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(orderoptions) {
            @Override
            public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemview = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout, parent, false);
                return new OrderViewHolder(itemview);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, final int position, @NonNull final Request model) {
                viewHolder.txtOrderId.setText("Order Id : " + adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText("Status : " + Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText("EMAIL : " + model.getEmail());
               // viewHolder.txtOrderPhonw.setText("Phone No : " + model.getPhone());
                //viewHolder.txtOrderComment.setText("Comment : " + model.getComment());

                //event button
                viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(),adapter.getItem(position));
                    }
                });
                viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(adapter.getRef(position).getKey(),adapter.getItem(position));
                        adapter.notifyDataSetChanged();//update view

                    }
                });
                viewHolder.btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final SpotsDialog dialog = new SpotsDialog(OrderStatus.this);
                        dialog.show();
                        Intent orderDetail = new Intent(OrderStatus.this,OrderDetail.class);
                        Common.currentRequest = model;
                        orderDetail.putExtra("OrderId",adapter.getRef(position).getKey());
                        startActivity(orderDetail);
                        dialog.dismiss();
                    }
                });
                viewHolder.btnDirections.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final SpotsDialog dialog = new SpotsDialog(OrderStatus.this);
                        dialog.show();
                        Intent trackingOrder = new Intent(OrderStatus.this,TrackingOrder.class);
                        Common.currentRequest = model;
                        startActivity(trackingOrder);
                        dialog.dismiss();
                    }
                });
            }

        };
                adapter.startListening();
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadOrders(Common.currentUser.getPhone());
        loadOrders();
        if (adapter!= null){
            adapter.startListening();}
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.UPDATE))
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        else
        if(item.getTitle().equals(Common.DELETE))
        {
            deleteOrder(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        return super.onContextItemSelected(item);
    }


    private void deleteOrder(String key, final Request item) {
        //final Request item = null;
        requests.child(key).removeValue();
        sendorderstatustoUSER(key,item);
        Toast.makeText(OrderStatus.this,"   The order was deleted   ",Toast.LENGTH_LONG).show();
        Snackbar.make(swipeRefreshLayout, " The order was deleted  ",Snackbar.LENGTH_LONG).show();

    }

    private void showUpdateDialog(String key, final Request item) {

        final AlertDialog.Builder alertDialog  = new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update order");
        alertDialog.setMessage("Please choose status");
        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout,null);
        spinner = view.findViewById(R.id.statusspinner);
        spinner.setItems("Placed","On the way","Shipped!!");
        alertDialog.setView(view);
        final String localKey = key;
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
                requests.child(localKey).setValue(item);
                adapter.notifyDataSetChanged();//to update item size
                sendorderstatustoUSER(localKey,item);
                sendordersemailUSER(localKey,item);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void sendordersemailUSER(String localKey, final Request item) {
        DatabaseReference tokens = db.getReference("Tokens");
        tokens.orderByKey().equalTo(item.getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                        {

                            Common.currentRequest = item;
                            Token token = postSnapshot.getValue(Token.class);
                            String[] TO = {item.getEmail().toString()};
                            String[] CC = {""};
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);

                            emailIntent.setData(Uri.parse("mailto:"));
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                            emailIntent.putExtra(Intent.EXTRA_CC, CC);
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your order has been updated");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Here are your order details \n"+
                                    "The order for user  \n" +
                                    (item.getName())+
                                    "\nwith phone no \n" +
                                    (item.getPhone())+
                                    "\nhas been updated to \n" +
                                    (Common.convertCodeToStatus(item.getStatus()))+
                                    "\nand has been sent for address\n" +
                                    (item.getAddress())+
                                    "\nwith coordinates\n" +
                                    (item.getLatlng())+
                                    "\nand your comment\n" +
                                    (item.getComment())+
                                    "\nhas been noted\n" +
                                    "\nyour total ammount\n" +
                                    (item.getTotal())+
                                    "\nhas the status\n" +
                                    (item.getPaymentState())+
                                    "\nthank you for shopping");
                            try {
                                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(OrderStatus.this, "There are no email clients installed.", Toast.LENGTH_SHORT);
                            }
                        }                               // Toast.makeText(OrderStatus.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });                                Toast.makeText(OrderStatus.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();

    }

    private void sendorderstatustoUSER(final String key, final Request item) {
        DatabaseReference tokens = db.getReference("Tokens");
        tokens.orderByKey().equalTo(item.getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                        {
                            Token token = postSnapshot.getValue(Token.class);
                            //raw payload for notification

//                            Notification notification = new Notification("By "+Common.currentUser.getName().toString()+" tap " +
//                                    "to check it out!!","Your order  "+key+"  was updated ");
//                            Sender content = new Sender(token.getToken(),notification);

                            Map<String,String> datasend = new HashMap<>();
                            datasend.put("title","Your order  "+key+"  was updated ");
                            datasend.put("message","By "+Common.currentUser.getName().toString()+" tap to check it out!!");
                            DataMessage dataMessage = new DataMessage(token.getToken(),datasend);



                            mAPIService.sendNotification(dataMessage)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if(response.code() == 200){
                                                if(response.body().success ==1)
                                                {
                                                    Toast.makeText(OrderStatus.this, "Order updated notification sent",Toast.LENGTH_LONG).show();
                                                    Snackbar.make(swipeRefreshLayout,"Order updated notification sent",Snackbar.LENGTH_LONG).show();
                                                }else{
                                                    Toast.makeText(OrderStatus.this,"Updated but Failed to send notification",Toast.LENGTH_LONG).show();
                                                    Snackbar.make(swipeRefreshLayout,"Updated but Failed to send notification",Snackbar.LENGTH_LONG).show();
                                                }}
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Toast.makeText(OrderStatus.this,"notification failure",Toast.LENGTH_LONG).show();
                                            Snackbar.make(swipeRefreshLayout,"notification failure",Snackbar.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
