package a123.vaidya.nihal.foodcrunchserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import a123.vaidya.nihal.foodcrunchserver.R;

public class BannerViewHolder extends RecyclerView.ViewHolder

    {

        public TextView banner_name;
        public ImageView banner_image;
        public android.widget.Button btnUpdate2,btnRemove2;

    public BannerViewHolder(View itemView) {
        super(itemView);

        banner_name = itemView.findViewById(R.id.banner_name);
        banner_image = itemView.findViewById(R.id.banner_image);
        btnUpdate2 = itemView.findViewById(R.id.btnUpdate2);
        btnRemove2 = itemView.findViewById(R.id.btnRemove2);

    }

    }
