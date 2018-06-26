package a123.vaidya.nihal.foodcrunchserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import a123.vaidya.nihal.foodcrunchserver.Model.Order;
import a123.vaidya.nihal.foodcrunchserver.R;

class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView name,quantity,price,discount;

    public MyViewHolder(View itemView) {
        super(itemView);
<<<<<<< HEAD
        name = (TextView)itemView.findViewById(R.id.product_name);
        quantity = (TextView)itemView.findViewById(R.id.product_quantitiy);
        price = (TextView)itemView.findViewById(R.id.product_price);
        discount = (TextView)itemView.findViewById(R.id.product_discount);
=======
        name = itemView.findViewById(R.id.product_name);
        quantity = itemView.findViewById(R.id.product_quantitiy);
        price = itemView.findViewById(R.id.product_price);
        discount = itemView.findViewById(R.id.product_discount);
>>>>>>> old1/master

    }
}

public class OrderDetailsAdapter  extends RecyclerView.Adapter<MyViewHolder>{
    List<Order> myOrders;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_detail_layout,parent,false);
        return new MyViewHolder(itemView);
<<<<<<< HEAD
    };
=======
    }
>>>>>>> old1/master

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = myOrders.get(position);
        holder.name.setText(String.format("Name : %s",order.getProductName()));
        holder.quantity.setText(String.format("Quantity : %s",order.getQuantity()));
        holder.discount.setText(String.format("Discount : %s",order.getDiscount()));
        holder.price.setText(String.format("Price : %s",order.getPrice()));
    }

    public OrderDetailsAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
