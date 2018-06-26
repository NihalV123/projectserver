package a123.vaidya.nihal.foodcrunchserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
<<<<<<< HEAD
<<<<<<< HEAD
import android.view.ContextMenu;
=======
>>>>>>> old1/master
=======
>>>>>>> old2/master
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

<<<<<<< HEAD
<<<<<<< HEAD
import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Interface.ItemClickListener;
=======
>>>>>>> old1/master
=======
>>>>>>> old2/master
import a123.vaidya.nihal.foodcrunchserver.R;

/**
 * Created by nnnn on 26/12/2017.
 */

<<<<<<< HEAD
<<<<<<< HEAD
public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener
{

public TextView txtMenuName;
public ImageView imageView;
private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MenuViewHolder(View itemView) {
=======
=======
>>>>>>> old2/master
        public class MenuViewHolder extends RecyclerView.ViewHolder
        //implements View.OnClickListener,View.OnCreateContextMenuListener
{

        public TextView txtMenuName;
        public ImageView imageView;
//      private ItemClickListener itemClickListener;
        public android.widget.Button btnUpdate,btnRemove,btnGoIn;
//      public void setItemClickListener(ItemClickListener itemClickListener) {
//        this.itemClickListener = itemClickListener;
//    }

        public MenuViewHolder(View itemView) {
<<<<<<< HEAD
>>>>>>> old1/master
=======
>>>>>>> old2/master
        super(itemView);

        txtMenuName = itemView.findViewById(R.id.menu_name);
        imageView = itemView.findViewById(R.id.menu_image);
<<<<<<< HEAD
<<<<<<< HEAD

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
=======
=======
>>>>>>> old2/master
        btnUpdate = itemView.findViewById(R.id.btnUpdate);
        btnGoIn = itemView.findViewById(R.id.btnGoIn);
        btnRemove = itemView.findViewById(R.id.btnRemove);
        //itemView.setOnCreateContextMenuListener(this);
        //itemView.setOnClickListener(this);

    }

//    @Override
//    public void onClick(View v) {
//        itemClickListener.onClick(v,getAdapterPosition(),false);
//    }
//
//    @Override
//    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        contextMenu.setHeaderTitle("Select the action");
//        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
//        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);
        //contextMenu.add(0,0,getAdapterPosition(), Common.DETAILS);
        //contextMenu.add(0,0,getAdapterPosition(), Common.DIRECTIONS);
//    }
<<<<<<< HEAD
>>>>>>> old1/master
=======
>>>>>>> old2/master
}
