package a123.vaidya.nihal.foodcrunchserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Model.DataMessage;
import a123.vaidya.nihal.foodcrunchserver.Model.MyResponse;
//import a123.vaidya.nihal.foodcrunchserver.Model.Notification;
//import a123.vaidya.nihal.foodcrunchserver.Model.Sender;
import a123.vaidya.nihal.foodcrunchserver.Model.Token;
import a123.vaidya.nihal.foodcrunchserver.Remote.APIService;
import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMessage extends AppCompatActivity {

    MaterialEditText edtMessage,edtTitle;
    FButton btnsend;
    APIService mservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        mservice = Common.getFCMClient();

        edtMessage=(MaterialEditText)findViewById(R.id.edtMessage);
        edtTitle=(MaterialEditText)findViewById(R.id.edtTitle);
        btnsend =(FButton)findViewById(R.id.btnSend);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create notification message
//                Token serverToken = postSnapShot.getValue(Token.class);
//                Map<String,String> datasend = new HashMap<>();
//                datasend.put("title","From "+Common.currentUser.getName().toString()+" tap to manage it!!!");
//                datasend.put("message","You have new Order : "+order_number);
//                DataMessage dataMessage = new DataMessage(serverToken.getToken(),datasend);


//                Notification notification = new Notification(edtTitle.getText().toString()+"  TAP TO LOG IN","FOOD CRUNCH : "+edtMessage.getText().toString());
//                Sender toTopic =new Sender();
//                toTopic.to = new StringBuilder("/topics/").append(Common.topicName).toString();
//                toTopic.notification = notification;

//                mservice.sendNotification(toTopic)
//                        .enqueue(new Callback<MyResponse>() {
//                            @Override
//                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                                if(response.isSuccessful())
//                                {
//                                    Toast.makeText(SendMessage.this,"MESSAGE SENT",Toast.LENGTH_LONG).show();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<MyResponse> call, Throwable t) {
//                                Toast.makeText(SendMessage.this,"UNFORTUNATELY MESSAGE WAS NOT SENT!!!",Toast.LENGTH_LONG).show();
//
//                            }
//                        });
            }
        });


    }
}
