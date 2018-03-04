package a123.vaidya.nihal.foodcrunchserver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.app.AlertDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.UUID;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Model.Food;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.FoodViewHolder;
import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;
    MaterialEditText edtName,edtDescription,edtPrice,edtDiscount,edtVideo,edtRecepie,edtNutrition,edtQuantity;
    FButton btnSelect,btnUpload;
    TextView textView;
    SwipeRefreshLayout rootLayout;
    Food newFood;
    //Integer Quantity = 100;

    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId="";
    Uri saveUri;
    private final int PICK_IMAGE_REQUEST= 71;
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //firebase code
        db = FirebaseDatabase.getInstance();
        foodList =db.getReference("Foods");
        storage = FirebaseStorage.getInstance();

        storageReference = storage.getReference();
        rootLayout = findViewById(R.id.rootLayout);
        rootLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark);
        rootLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent()!=null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if (!categoryId.isEmpty()&&categoryId != null)
                {
                    if (Common.isConnectedToInternet(getBaseContext()))
                        loadListFood(categoryId);
                    else
                        Toast.makeText(FoodList.this,"Please check your" +
                                " internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        rootLayout.post(new Runnable() {
            @Override
            public void run() {
                if (getIntent()!=null)
                    categoryId = getIntent().getStringExtra("CategoryId");
                if (!categoryId.isEmpty()&&categoryId != null)
                {
                    if (Common.isConnectedToInternet(getBaseContext()))
                        loadListFood(categoryId);
                    else
                        Toast.makeText(FoodList.this,"Please check your" +
                                " internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });
        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        textView = findViewById(R.id.textView3);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();
            }
        });

        //get intent
        if (getIntent()!=null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty()&&categoryId != null)
        {
            if (Common.isConnectedToInternet(getBaseContext()))

                loadListFood(categoryId);
            else
                Toast.makeText(FoodList.this,"Please check your internet connection",Toast.LENGTH_LONG).show();
        }

    }

    private void showAddFoodDialog() {
        AlertDialog.Builder alertDailog = new AlertDialog.Builder(FoodList.this);
//        alertDailog.setTitle("Add New Food");
//        alertDailog.setMessage("Please fill all fields");
//replace this now

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout,null);
        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        edtDiscount = add_menu_layout.findViewById(R.id.edtDiscount);
        edtQuantity = add_menu_layout.findViewById(R.id.edtQuantity);
        edtRecepie =  add_menu_layout.findViewById(R.id.edtRecepies);
        edtNutrition =  add_menu_layout.findViewById(R.id.edtnutritionalvalue);
        edtVideo=  add_menu_layout.findViewById(R.id.edtvideo);

        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

        //event for button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();   //select image from galery and save url
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();   //select image from galary and save url
            }
        });

        alertDailog.setView(add_menu_layout);
        alertDailog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set button
        alertDailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //create a new category
                if(newFood != null)
                {
                    foodList.push().setValue(newFood);
                    Snackbar.make(rootLayout,"New Category "+newFood.getName()+" was added",Snackbar.LENGTH_LONG).show();
                    Toast.makeText(FoodList.this,"New Category Created!!",Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDailog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDailog.show();

    }

    private void uploadImage() {

        if(saveUri != null)
        {final SpotsDialog dialog = new SpotsDialog(FoodList.this);
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(FoodList.this,"Uploaded Successfully !!! Updating database",Toast.LENGTH_LONG).show();
                            Snackbar.make(rootLayout,"The Image was Uploaded",Snackbar.LENGTH_LONG).show();

                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value of new category to get download link
                                    newFood = new Food();
                                    newFood.setName(edtName.getText().toString());
                                    newFood.setDescription(edtDescription.getText().toString());
                                    newFood.setPrice(edtPrice.getText().toString());
                                    newFood.setDiscount(edtDiscount.getText().toString());
                                    newFood.setVideo(edtVideo.getText().toString());
                                    newFood.setEmail(edtNutrition.getText().toString());
                                    newFood.setRecepixes(edtRecepie.getText().toString());
                                    newFood.setMenuId(categoryId);
                                    newFood.setQuantity(Integer.valueOf(edtQuantity.getText().toString()));
                                    newFood.setImage(uri.toString());
                                }
                            });
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // dialog.dismiss();
                            Toast.makeText(FoodList.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                            Snackbar.make(rootLayout,"Something gone wrong check logs ",Snackbar.LENGTH_LONG).show();
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

    //select * from foods where menuid=&
    private void loadListFood(String categoryId) {
        Query searchbyname =foodList.orderByChild("menuId").equalTo(categoryId);
        //options
        FirebaseRecyclerOptions<Food> foodoptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchbyname,Food.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(foodoptions) {
            @Override
            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item,parent,false);
                return new FoodViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull final FoodViewHolder viewHolder, final int position, @NonNull final Food model) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);
                //event buttons
                viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFood(adapter.getRef(position).getKey());

                    }
                });
                viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateFoodDoalog(adapter.getRef(position).getKey(),adapter.getItem(position));
                    }
                });

                    }
        };
        adapter.startListening();

        //set adapter
        recyclerView.setAdapter(adapter);

        rootLayout.setRefreshing(false);
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadListFood(categoryId);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadListFood(categoryId);
        if (adapter!= null){
            adapter.startListening();}
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
    @Override
    protected void onStop() {
        super.onStop();
        //searchAdapter.stopListening();
        //adapter.stopListening();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("IMAGE SELECTED! ->>");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateFoodDoalog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else
        if(item.getTitle().equals(Common.DELETE))
        {
            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteFood(final String key) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("SURE YOU WANT TO DELETE?");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.ic_delete_forever_black_24dp);
        alertDialog.setMessage("Deleting a food cannot be undone!!");
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                foodList.child(key).removeValue();
        Toast.makeText(FoodList.this,"The food was  deleted",Toast.LENGTH_LONG).show();
        // Snackbar.make(drawer,"The Category "+ newCategory.getName() +" was deleted",Snackbar.LENGTH_LONG).show();
        DatabaseReference foods = db.getReference("Foods");//get all list of food from database

        Query foodInCategory = foods.orderByChild("menuId").equalTo(key);
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


    private void showUpdateFoodDoalog(final String key, final Food item) {
        AlertDialog.Builder alertDailog = new AlertDialog.Builder(FoodList.this);
//        alertDailog.setTitle("Edit Food");
//        alertDailog.setMessage("Please fill all fields");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout,null);
        edtName = add_menu_layout.findViewById(R.id.edtName);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        edtDiscount = add_menu_layout.findViewById(R.id.edtDiscount);
        edtRecepie = add_menu_layout.findViewById(R.id.edtRecepies);
        edtQuantity = add_menu_layout.findViewById(R.id.edtQuantity);
        edtNutrition =add_menu_layout.findViewById(R.id.edtnutritionalvalue);
        edtVideo=add_menu_layout.findViewById(R.id.edtvideo);
        //default values
        edtName.setText(item.getName());
        edtDescription.setText(item.getDescription());
        edtPrice.setText(item.getPrice());
        edtDiscount.setText(item.getDiscount());
        edtQuantity.setText("'"+Integer.valueOf(item.getQuantity())+"'");
        edtRecepie.setText(item.getRecepixes());
        edtVideo.setText(item.getVideo());
        edtNutrition.setText(item.getEmail());

        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

        //event for button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();   //select image from galery and save url
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);   //select image from galary and save url
            }
        });

        alertDailog.setView(add_menu_layout);
        alertDailog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //set button
        alertDailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                    //updating info
                    item.setName(edtName.getText().toString());
                    item.setPrice(edtPrice.getText().toString());
                    item.setDiscount(edtDiscount.getText().toString());
                    item.setQuantity(Integer.valueOf(edtQuantity.getText().toString()));
                    item.setRecepixes(edtRecepie.getText().toString());
                    item.setDescription(edtDescription.getText().toString());
                    item.setEmail(edtNutrition.getText().toString());
                    item.setVideo(edtVideo.getText().toString());

                    foodList.child(key).setValue(item);
                    Snackbar.make(rootLayout,"New Category "+item.getName()+" was updated",Snackbar.LENGTH_LONG).show();
                    Toast.makeText(FoodList.this,"The Category was updated!!",Toast.LENGTH_LONG).show();

            }
        });
        alertDailog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDailog.show();

    }

    private void changeImage(final Food item) {

        if(saveUri != null)
        {
            final SpotsDialog dialog = new SpotsDialog(FoodList.this);
            dialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(FoodList.this,"Uploaded Successfully!!!",Toast.LENGTH_LONG).show();
                            Snackbar.make(rootLayout,"The Image was Uploaded",Snackbar.LENGTH_LONG).show();



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
                            Toast.makeText(FoodList.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                            Snackbar.make(rootLayout,"Something gone wrong check logs",Snackbar.LENGTH_LONG).show();
                        }
                    });

        }


    }


}
