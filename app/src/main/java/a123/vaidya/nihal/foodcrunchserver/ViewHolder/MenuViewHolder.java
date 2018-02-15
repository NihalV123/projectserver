package a123.vaidya.nihal.foodcrunchserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import a123.vaidya.nihal.foodcrunchserver.Interface.ItemClickListener;
import a123.vaidya.nihal.foodcrunchserver.R;

/**
 * Created by nnnn on 26/12/2017.
 */

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

public TextView txtMenuName;
public ImageView imageView;
private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MenuViewHolder(View itemView) {
        super(itemView);

        txtMenuName = itemView.findViewById(R.id.menu_name);
        imageView = itemView.findViewById(R.id.menu_image);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
