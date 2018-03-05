package a123.vaidya.nihal.foodcrunchserver;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Database.Database;
import a123.vaidya.nihal.foodcrunchserver.Model.Food;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.FoodViewHolder;
import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;

public class SearchActivity extends AppCompatActivity {
    //for searching items in category
    private FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
    private final List<String> suggestList = new ArrayList<>();
    private MaterialSearchBar materialSearchBar;
    FButton btnSelect,btnUpload;
    private FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;
    MaterialEditText edtName,edtDescription,edtPrice,edtDiscount,edtVideo,edtRecepie,edtNutrition,edtQuantity;
    RelativeLayout rootLayout;
    StorageReference storageReference;
    String categoryId="";
    Uri saveUri;
    private RecyclerView recycler_menu;
    private RecyclerView.LayoutManager layoutManager;
    private String foodId="";
    private FirebaseDatabase database;
    private DatabaseReference foodList;

//    //favorite cache in search
//    private Database localDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        database = FirebaseDatabase.getInstance();
        foodList =database.getReference("Foods");
        //loacal db for search
//        localDB = new Database(this);
        rootLayout = findViewById(R.id.rootLayout);
        recycler_menu = findViewById(R.id.recycler_search);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        //search
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter the name of your food");
//        materialSearchBar.setSpeechMode(false);
        loadSuggest();

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();

                for(String search:suggestList)
                {
                    try{if (search.toLowerCase().contains(materialSearchBar.getText().toUpperCase()))
                        suggest.add(search);
                    else return;}catch (Exception e){}
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {
                List<String> suggest = new ArrayList<>();
                for(String search:suggestList)
                {
                    try{if (search.toLowerCase().contains(materialSearchBar.getText().toUpperCase()))
                        suggest.add(search);
                    else return;}catch (Exception e){}
                }
                materialSearchBar.setLastSuggestions(suggest);
            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //disable and enable for ui effect
                if(!enabled)
                    recycler_menu.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        //load all foods
        loadAllFoods();
    }

    private void loadAllFoods() {
        Query searchbyname =foodList;
        //options
        FirebaseRecyclerOptions<Food> foodoptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchbyname,Food.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(foodoptions) {
            @Override
            protected void onBindViewHolder(@NonNull final FoodViewHolder viewHolder, final int position, @NonNull final Food model) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);
            }
            @Override
            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.search_item,parent,false);
                return new FoodViewHolder(itemView);
            }
            //this is the first view before typing
            //below one is the after typed or selecred view
            //need bindviewholder for functions
        };
        adapter.startListening();
        //set adapter
        recycler_menu.setAdapter(adapter);
    }
    private void startSearch(CharSequence text) {
        //query search by name
        Query searchbyname =foodList.orderByChild("name").equalTo(text.toString());
        //options
        FirebaseRecyclerOptions<Food> foodoptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchbyname,Food.class)
                .build();
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(foodoptions) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder viewHolder, final int position, @NonNull Food model) {
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
            @Override
            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item,parent,false);
                return new FoodViewHolder(itemView);
            }
        };
        searchAdapter.startListening();
        recycler_menu.setAdapter(searchAdapter);
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

    private void changeImage(final Food item) {

        if(saveUri != null)
        {
            final SpotsDialog dialog = new SpotsDialog(SearchActivity.this);
            dialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(SearchActivity.this,"Uploaded Successfully!!!",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(SearchActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                            Snackbar.make(rootLayout,"Something gone wrong check logs",Snackbar.LENGTH_LONG).show();
                        }
                    });

        }


    }

    private void showUpdateFoodDoalog(final String key, final Food item) {
        AlertDialog.Builder alertDailog = new AlertDialog.Builder(SearchActivity.this);
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
        edtQuantity.setText((Double.valueOf(item.getQuantity())).toString());
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
                item.setQuantity(Double.valueOf(edtQuantity.getText().toString()));
                item.setRecepixes(edtRecepie.getText().toString());
                item.setDescription(edtDescription.getText().toString());
                item.setEmail(edtNutrition.getText().toString());
                item.setVideo(edtVideo.getText().toString());

                foodList.child(key).setValue(item);
                Snackbar.make(rootLayout,"New Category "+item.getName()+" was updated",Snackbar.LENGTH_LONG).show();
                Toast.makeText(SearchActivity.this,"The Category was updated!!",Toast.LENGTH_LONG).show();

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

    private void deleteFood(final String key) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SearchActivity.this);
        alertDialog.setTitle("SURE YOU WANT TO DELETE?");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.ic_delete_forever_black_24dp);
        alertDialog.setMessage("Deleting a food cannot be undone!!");
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                foodList.child(key).removeValue();
                Toast.makeText(SearchActivity.this,"The food was  deleted",Toast.LENGTH_LONG).show();
                // Snackbar.make(drawer,"The Category "+ newCategory.getName() +" was deleted",Snackbar.LENGTH_LONG).show();
                DatabaseReference foods = database.getReference("Foods");//get all list of food from database

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

    private void loadSuggest() {
        foodList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postDnapshot:dataSnapshot.getChildren())
                {
                    Food item = postDnapshot.getValue(Food.class);
                    suggestList.add(Objects.requireNonNull(item).getName());
                }
                materialSearchBar.setLastSuggestions(suggestList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
//    private void uploadImage() {
//
//        if(saveUri != null)
//        {final SpotsDialog dialog = new SpotsDialog(FoodList.this);
//            dialog.show();
//
//            String imageName = UUID.randomUUID().toString();
//            final StorageReference imageFolder = storageReference.child("images/"+imageName);
//            imageFolder.putFile(saveUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            dialog.dismiss();
//                            Toast.makeText(FoodList.this,"Uploaded Successfully !!! Updating database",Toast.LENGTH_LONG).show();
//                            Snackbar.make(rootLayout,"The Image was Uploaded",Snackbar.LENGTH_LONG).show();
//
//                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    //set value of new category to get download link
//                                    newFood = new Food();
//                                    newFood.setName(edtName.getText().toString());
//                                    newFood.setDescription(edtDescription.getText().toString());
//                                    newFood.setPrice(edtPrice.getText().toString());
//                                    newFood.setDiscount(edtDiscount.getText().toString());
//                                    newFood.setVideo(edtVideo.getText().toString());
//                                    newFood.setEmail(edtNutrition.getText().toString());
//                                    newFood.setRecepixes(edtRecepie.getText().toString());
//                                    newFood.setMenuId(categoryId);
//                                    newFood.setQuantity(Double.valueOf(edtQuantity.getText().toString()));
//                                    newFood.setImage(uri.toString());
//                                }
//                            });
//                        }
//
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            // dialog.dismiss();
//                            Toast.makeText(FoodList.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
//                            Snackbar.make(rootLayout,"Something gone wrong check logs ",Snackbar.LENGTH_LONG).show();
//                        }
//                    });
//
//        }
//
//    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onStop() {
        if(adapter != null)
            adapter.stopListening();
        if(searchAdapter != null)
            searchAdapter.stopListening();
        super.onStop();
    }
}
