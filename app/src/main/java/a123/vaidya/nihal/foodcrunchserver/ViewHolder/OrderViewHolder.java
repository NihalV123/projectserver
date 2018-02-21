package a123.vaidya.nihal.foodcrunchserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.rey.material.widget.Button;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Interface.ItemClickListener;
import a123.vaidya.nihal.foodcrunchserver.R;

/**
 * Created by nnnn on 28/12/2017.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder
        //implements
        //View.OnClickListener ,
        //View.OnLongClickListener,
       // View.OnCreateContextMenuListener
{

    public TextView txtOrderId,txtOrderStatus,txtOrderPhonw,txtOrderAddress,txtOrderComment;
    public android.widget.Button btnEdit,btnRemove,btnDirections,btnDetails;
   // private ItemClickListener itemClickListener;


    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderId =(TextView)itemView.findViewById(R.id.order_id);
        txtOrderStatus=(TextView)itemView.findViewById(R.id.order_status);
        txtOrderPhonw=(TextView)itemView.findViewById(R.id.order_phone);
        txtOrderAddress=(TextView)itemView.findViewById(R.id.order_address);
        txtOrderComment=(TextView)itemView.findViewById(R.id.comment_details);
        //order bottom buton
        btnEdit = (android.widget.Button)itemView.findViewById(R.id.btnEdit);
        btnDetails = (android.widget.Button)itemView.findViewById(R.id.btnDetails);
        btnDirections = (android.widget.Button)itemView.findViewById(R.id.btnDirections);
        btnRemove = (android.widget.Button)itemView.findViewById(R.id.btnRemove);


//        itemView.setOnClickListener(this);
//        itemView.setOnCreateContextMenuListener(this);
        //itemView.setOnLongClickListener(this);
    }

//    public void setItemClickListener(ItemClickListener itemClickListener) {
//        this.itemClickListener = itemClickListener;
//    }
//
//    @Override
//    public void onClick(View v) {
//        itemClickListener.onClick(v,getAdapterPosition(),false);
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        menu.setHeaderTitle("Select The Action");
//        menu.add(0,0,getAdapterPosition(),"UPDATE");
//        menu.add(0,1,getAdapterPosition(),"DELETE");
       // menu.add(0,1,getAdapterPosition(),"DETAILS");
       // menu.add(0,1,getAdapterPosition(),"DIRECTIONS");


//    }

}
