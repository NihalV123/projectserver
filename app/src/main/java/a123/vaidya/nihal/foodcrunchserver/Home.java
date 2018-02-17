package a123.vaidya.nihal.foodcrunchserver;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import a123.vaidya.nihal.foodcrunchserver.Interface.ItemClickListener;
import a123.vaidya.nihal.foodcrunchserver.Model.Category;
import a123.vaidya.nihal.foodcrunchserver.Service.ListenOrder;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.MenuViewHolder;
import info.hoang8f.widget.FButton;
import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView txtfullname;
    MaterialEditText edtName;
    FButton btnUpload,btnSelect;
    Category newCategory;
    Uri saveUri;
    private final int PICK_IMAGE_REQUEST= 71;
    DrawerLayout drawer;

    //firebase init
    FirebaseDatabase database;
    DatabaseReference categories;
    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Management");
        setSupportActionBar(toolbar);

        //firebase code

        database = FirebaseDatabase.getInstance();
        categories =database.getReference("Category");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDialog();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set name for user
        View headerView = navigationView.getHeaderView(0);
        txtfullname = (TextView)headerView.findViewById(R.id.txtFullName);
        txtfullname.setText(Common.currentUser.getName());


        //new view for server
        recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        if (Common.isConnectedToInternet(this)) {

            loadMenu();


        }
        else
        {
            Toast.makeText(this,"Please check your internet connection",Toast.LENGTH_LONG).show();
            return;
        }
        Intent services = new Intent(Home.this, ListenOrder.class);
        startService(services);
    }

    private void showDialog() {
        AlertDialog.Builder alertDailog = new AlertDialog.Builder(Home.this);
        alertDailog.setTitle("Add New Category");
        alertDailog.setMessage("Please fill all fields");


        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout,null);
        edtName = add_menu_layout.findViewById(R.id.edtName);
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
                if(newCategory != null)
                {
                    categories.push().setValue(newCategory);
                    Snackbar.make(drawer,"New Category "+newCategory.getName()+" was added",Snackbar.LENGTH_LONG).show();
                    Toast.makeText(Home.this,"New Category Created!!",Toast.LENGTH_LONG).show();
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
        {
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           // dialog.dismiss();
                            Toast.makeText(Home.this,"Uploaded Successfully Just a sec",Toast.LENGTH_LONG).show();
                            Snackbar.make(drawer,"The Image was Uploaded",Snackbar.LENGTH_LONG).show();

                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                  //set value of new category to get download link
                                  newCategory = new Category(edtName.getText().toString(),uri.toString());

                                }
                            });
                        }

                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // dialog.dismiss();
                    Toast.makeText(Home.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                    Snackbar.make(drawer,"Something gone wrong check logs ",Snackbar.LENGTH_LONG).show();
                }
            });



        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("IMAGE SELECTED!");
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Common.PICK_IMAGE_REQUEST);

    }

    private void loadMenu() {
        adapter= new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
                Category.class,
                R.layout.menu_item,
                MenuViewHolder.class,
                categories
        ) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(Home.this).load(model.getImage())
                        .into(viewHolder.imageView);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View v, int position, boolean isLongClick) {
                        //start new category adding activity
                        Intent foodList = new Intent(Home.this,FoodList.class);
                        foodList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(foodList);

                    }
                });
            }
        };
        adapter.notifyDataSetChanged();   // for refreshing data
        recycler_menu.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == R.id.refresh)
        {
            loadMenu();
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_menu) {
        } else if (id == R.id.nav_cart) {
            //Intent cartIntent = new Intent (Home.this,Cart.class);
            //startActivity(cartIntent);
        } else if (id == R.id.nav_orders) {
            Intent orderIntent = new Intent (Home.this,OrderStatus.class);
            startActivity(orderIntent);
        } else if (id == R.id.nav_logout) {
            //delete remmbered user details
            Paper.book().destroy();
            Intent signIn = new Intent (Home.this,Signin.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //code for update and delete
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else  if(item.getTitle().equals(Common.DELETE))
        {
           deleteCategory(adapter.getRef(item.getOrder()).getKey());

        }

        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {
        categories.child(key).removeValue();
        Toast.makeText(this,"The category was  deleted",Toast.LENGTH_LONG).show();
       Snackbar.make(drawer,"The Category "+ newCategory.getName() +" was deleted",Snackbar.LENGTH_LONG).show();
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

    }


    private void showUpdateDialog(final String key, final Category item) {

        AlertDialog.Builder alertDailog = new AlertDialog.Builder(Home.this);
        alertDailog.setTitle("Update the Category");
        alertDailog.setMessage("Please fill all fields");


        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout,null);
        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);



        //set default name
        edtName.setText(item.getName());

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
                categories.child(key).setValue(item);
                Snackbar.make(drawer,"New Category "+item.getName()+" was updated",Snackbar.LENGTH_LONG).show();
                Toast.makeText(Home.this,"The Category was updated!!",Toast.LENGTH_LONG).show();
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

    private void changeImage(final Category item) {

        if(saveUri != null)
        {
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // dialog.dismiss();
                            Toast.makeText(Home.this,"Uploaded Successfully Just a sec",Toast.LENGTH_LONG).show();
                            Snackbar.make(drawer,"The Image was Uploaded",Snackbar.LENGTH_LONG).show();
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
                            Toast.makeText(Home.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                            Snackbar.make(drawer,"Something gone wrong check logs",Snackbar.LENGTH_LONG).show();
                        }
                    });



        }

    }
}
