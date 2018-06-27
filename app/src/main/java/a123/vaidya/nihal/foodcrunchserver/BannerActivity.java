package a123.vaidya.nihal.foodcrunchserver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Model.Banner;
import a123.vaidya.nihal.foodcrunchserver.Model.Food;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.BannerViewHolder;
import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;

public class BannerActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;
    MaterialEditText edtName,edtFoodId;
    FButton btnSelect1,btnUpload1;
    TextView textView;
    SwipeRefreshLayout rootLayout1;
    Food newFood;
    Banner newbanner;
    FirebaseDatabase db;
    DatabaseReference banners;
    FirebaseStorage storage;
    StorageReference storageReference;

    String bannerId="";
    Uri filepath;
    private final int PICK_IMAGE_REQUEST= 71;
    FirebaseRecyclerAdapter<Banner,BannerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        //firebase code
        db = FirebaseDatabase.getInstance();
        banners =db.getReference("Banner");
        storage = FirebaseStorage.getInstance();

        recyclerView = findViewById(R.id.recycler_banner);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddBannerDialog();
            }
        });

        loadlistBanner();

        storageReference = storage.getReference();
        rootLayout1 = findViewById(R.id.rootLayout1);
        rootLayout1.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark);



        rootLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectedToInternet(getBaseContext()))
                    loadlistBanner();
                else
                    Toast.makeText(BannerActivity.this,"Please check your" +
                            " internet connection",Toast.LENGTH_LONG).show();
                rootLayout1.setRefreshing(false);
            }
        });

        rootLayout1.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToInternet(getBaseContext()))
                    loadlistBanner();
                else
                    Toast.makeText(BannerActivity.this,"Please check your" +
                            " internet connection",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadlistBanner() {

        //options
        FirebaseRecyclerOptions<Banner> allbanner = new FirebaseRecyclerOptions.Builder<Banner>()
                .setQuery(banners,Banner.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Banner, BannerViewHolder>(allbanner) {
            @Override
            protected void onBindViewHolder(@NonNull BannerViewHolder holder, final int position, @NonNull Banner model) {
                holder.banner_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.banner_image);
                //event buttons
                holder.btnRemove2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteBanner(adapter.getRef(position).getKey());

                    }
                });
                holder.btnUpdate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateBannerDoalog(adapter.getRef(position).getKey(),adapter.getItem(position));
                    }
                });
            }

            @Override
            public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.banner_layout,parent,false);
                return new BannerViewHolder(itemView);
            }

        };
        adapter.startListening();
        //set adapter
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        loadlistBanner();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void showAddBannerDialog() {
        AlertDialog.Builder alertDailog = new AlertDialog.Builder(BannerActivity.this);
        alertDailog.setTitle("Add New Banner Food");
        alertDailog.setMessage("Please fill all fields");


        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_banner,null);
        edtFoodId = add_menu_layout.findViewById(R.id.edtfoodid);
        edtName = add_menu_layout.findViewById(R.id.edtfoodname);

        btnSelect1 = add_menu_layout.findViewById(R.id.btnSelect1);
        btnUpload1 = add_menu_layout.findViewById(R.id.btnUpload1);

        //event for button
        btnSelect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();   //select image from galery and save url
            }
        });

        btnUpload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();   //select image from galary and save url
            }
        });

        alertDailog.setView(add_menu_layout);
        alertDailog.setIcon(R.drawable.ic_theaters_black_24dp);

        //set button
        alertDailog.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //create a new category
                if(newbanner != null)
                {
                    banners.push()
                            .setValue(newbanner);
                    //Snackbar.make(rootLayout1,"New Category "+newFood.getName()+" was added",Snackbar.LENGTH_LONG).show();
                    Toast.makeText(BannerActivity.this,"New Banner Created!!",Toast.LENGTH_LONG).show();
                loadlistBanner();
                }
            }
        });
        alertDailog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                newbanner = null;
                loadlistBanner();
            }
        });
        alertDailog.show();


    }

    private void uploadImage() {

        if(filepath != null)
        {final SpotsDialog dialog = new SpotsDialog(BannerActivity.this);
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(BannerActivity.this,"Uploaded Successfully !!! Updating database",Toast.LENGTH_LONG).show();
                            Snackbar.make(rootLayout1,"The Image was Uploaded",Snackbar.LENGTH_LONG).show();

                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value of new category to get download link
                                    newbanner = new Banner();
                                    newbanner.setName(edtName.getText().toString());
                                    newbanner.setId(edtFoodId.getText().toString());
                                    newbanner.setImage(uri.toString());
                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // dialog.dismiss();
                            Toast.makeText(BannerActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                            Snackbar.make(rootLayout1,"Something gone wrong check logs ",Snackbar.LENGTH_LONG).show();
                        }
                    });

        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateBannerDoalog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else
        if(item.getTitle().equals(Common.DELETE))
        {
            deleteBanner(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteBanner(final String key) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BannerActivity.this);
        alertDialog.setTitle("SURE YOU WANT TO DELETE?");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.ic_delete_forever_black_24dp);
        alertDialog.setMessage("Deleting a banner cannot be undone!!");
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                banners.child(key).removeValue();
                Toast.makeText(BannerActivity.this,"The banner item was  deleted",Toast.LENGTH_LONG).show();
                // Snackbar.make(drawer,"The Category "+ newCategory.getName() +" was deleted",Snackbar.LENGTH_LONG).show();
                DatabaseReference foods = db.getReference("Banner");//get all list of food from database

                Query foodInCategory = foods.orderByChild("id").equalTo(key);
                foodInCategory.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapShot:dataSnapshot.getChildren() )

                            postSnapShot.getRef().removeValue();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }})
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void showUpdateBannerDoalog(final String key, final Banner item) {
        AlertDialog.Builder alertDailog = new AlertDialog.Builder(BannerActivity.this);
        alertDailog.setTitle("Edit Banner");
        alertDailog.setMessage("Please fill all fields");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_banner = inflater.inflate(R.layout.add_new_banner,null);
        edtName = add_banner.findViewById(R.id.edtfoodname);
        edtFoodId = add_banner.findViewById(R.id.edtfoodid);
        //edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        //edtDiscount = add_menu_layout.findViewById(R.id.edtDiscount);

        //default values
        edtName.setText(item.getName());
        edtFoodId.setText(item.getId());
        //edtPrice.setText(item.getPrice());
        //edtDiscount.setText(item.getDiscount());

        btnSelect1 = add_banner.findViewById(R.id.btnSelect1);
        btnUpload1 = add_banner.findViewById(R.id.btnUpload1);

        //event for button
        btnSelect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();   //select image from galery and save url
            }
        });

        btnUpload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBanner(item);
                //select image from galary and save url
            }
        });

        alertDailog.setView(add_banner);
        alertDailog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set button
        alertDailog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //updating info
                item.setName(edtName.getText().toString());
                //item.setPrice(edtPrice.getText().toString());
                //item.setDiscount(edtDiscount.getText().toString());
                item.setId(edtFoodId.getText().toString());

                //how to make real update to firebase
                Map<String,Object> update = new HashMap<>();
                update.put("name",item.getName());
                update.put("id",item.getId());
                update.put("image",item.getImage());

                banners.child(key)
                        .updateChildren(update)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Snackbar.make(rootLayout1,"New Banner "+item.getName()+" was updated",Snackbar.LENGTH_LONG).show();
                                Toast.makeText(BannerActivity.this,"The Banner was updated!!",Toast.LENGTH_LONG).show();
                                loadlistBanner();
                            }
                        });
                loadlistBanner();
                        //.setValue(item);

            }
        });
        alertDailog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                loadlistBanner();
            }
        });
        alertDailog.show();


    }

    private void changeBanner(final Banner item) {

        if(filepath != null)
        {
            final SpotsDialog dialog = new SpotsDialog(BannerActivity.this);
            dialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(BannerActivity.this,"Uploaded Successfully!!!",Toast.LENGTH_LONG).show();
                            Snackbar.make(rootLayout1,"The Image was Uploaded",Snackbar.LENGTH_LONG).show();



                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value of new category to get download link
                                    item.setImage(uri.toString());
                                    // /newCategory = new Category(edtName.getText().toString(),uri.toString());

                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // dialog.dismiss();
                            Toast.makeText(BannerActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                            Snackbar.make(rootLayout1,"Something gone wrong check logs",Snackbar.LENGTH_LONG).show();
                        }
                    });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filepath = data.getData();
            btnSelect1.setText("IMAGE SELECTED! ->>");
        }
    }
}
