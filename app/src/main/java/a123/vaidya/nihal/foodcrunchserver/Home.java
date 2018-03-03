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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import a123.vaidya.nihal.foodcrunchserver.Common.Common;
import a123.vaidya.nihal.foodcrunchserver.Model.Category;
import a123.vaidya.nihal.foodcrunchserver.Model.Token;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.MenuViewHolder;
import dmax.dialog.SpotsDialog;
import info.hoang8f.widget.FButton;
import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView txtFullName,txtemail,txtPhone;
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
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Management");
        setSupportActionBar(toolbar);

        //firebase code
        swipeRefreshLayout = findViewById(R.id.swipelayout1);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    loadMenu();

                    //send to token
                    updateToken(FirebaseInstanceId.getInstance().getToken());
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Please check your internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToInternet(getBaseContext())) {

                    loadMenu();

                    //send to token
                    updateToken(FirebaseInstanceId.getInstance().getToken());
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Please check your internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });
        Twitter.initialize(this);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("6ep60jj09lvUcHncYM3yCoIMr", "WXvH93jw1urHD9IzIk6FDRmKW0X5LGZgmMCDo67XFk2uDf2LGJ"))
                .debug(true)
                .build();
        Twitter.initialize(config);
        Paper.init(this);
        database = FirebaseDatabase.getInstance();
        categories =database.getReference("Category");

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(categories, Category.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item,parent,false);
                return  new MenuViewHolder(itemView);
            }
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder viewHolder, final int position, @NonNull Category model) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(Home.this).load(model.getImage())
                        .into(viewHolder.imageView);
                //event buttons
                viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteCategory(adapter.getRef(position).getKey());

                    }
                });
                viewHolder.btnGoIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //start new category adding activity
                        Intent foodList = new Intent(Home.this,FoodList.class);
                        foodList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });
                viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(),adapter.getItem(position));
                    }
                });

            }
        };
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set name for user
        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtemail = headerView.findViewById(R.id.txtEmail);
        txtPhone = headerView.findViewById(R.id.txtPhone);
        txtFullName.setText(Common.currentUser.getName());//slider name
        txtemail.setText(Common.currentUser.getEmail());
        txtPhone.setText(Common.currentUser.getPhone());

        //new view for server
        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        //animation code
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recycler_menu.getContext(),
                R.anim.layout_fall_down);
        recycler_menu.setLayoutAnimation(controller);
        if (Common.isConnectedToInternet(this)) {

            loadMenu();

            //send to token
            updateToken(FirebaseInstanceId.getInstance().getToken());

        }
        else
        {
            Toast.makeText(this,"Please check your internet connection",Toast.LENGTH_LONG).show();
        }
//        Intent services = new Intent(Home.this, ListenOrder.class);
//        startService(services);
    }

    private void updateToken(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token,true); //false as this reads frm client
        try {
            tokens.child(Common.currentUser.getPhone()).setValue(data);
            Toast.makeText(Home.this,"Welcome ",Toast.LENGTH_LONG).show();

        }
        catch(Exception e){
            Toast.makeText(Home.this,"your phone no is missing",Toast.LENGTH_LONG).show();
        }
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
        alertDailog.setCancelable(false);
        alertDailog.setView(add_menu_layout);
        alertDailog.setIcon(R.drawable.ic_add_to_photos_black_24dp);

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
            final SpotsDialog dialog = new SpotsDialog(Home.this);
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(Home.this,"Uploaded Successfully !!! Updating database",Toast.LENGTH_LONG).show();
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
            btnSelect.setText("IMAGE SELECTED! ->>");
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),Common.PICK_IMAGE_REQUEST);

    }

    private void loadMenu() {
        //started from swipeview
        //keep it commented
//        //new firebase code
//        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>()
//                .setQuery(categories, Category.class)
//                .build();
//
//        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
//            @Override
//            public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View itemView = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.menu_item,parent,false);
//                return  new MenuViewHolder(itemView);
//            }
//            @Override
//            protected void onBindViewHolder(@NonNull MenuViewHolder viewHolder, final int position, @NonNull Category model) {
//                viewHolder.txtMenuName.setText(model.getName());
//                Picasso.with(Home.this).load(model.getImage())
//                        .into(viewHolder.imageView);
//                //event buttons
//                viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        deleteCategory(adapter.getRef(position).getKey());
//
//                    }
//                });
//                viewHolder.btnGoIn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //start new category adding activity
//                        Intent foodList = new Intent(Home.this,FoodList.class);
//                        foodList.putExtra("CategoryId",adapter.getRef(position).getKey());
//                        startActivity(foodList);
//                    }
//                });
//                viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showUpdateDialog(adapter.getRef(position).getKey(),adapter.getItem(position));
//                    }
//                });
//
//            }
//        };
        adapter.startListening();
        recycler_menu.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
        //aniamtion begins
        recycler_menu.getAdapter().notifyDataSetChanged();
        recycler_menu.scheduleLayoutAnimation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMenu();
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMenu();
        if (adapter!= null){
            adapter.startListening();}
        adapter.notifyDataSetChanged();
        recycler_menu.setAdapter(adapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_menu:
                Toast.makeText(Home.this, "You are already in main menu", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_orders: {
                final SpotsDialog dialog = new SpotsDialog(Home.this);
                dialog.show();
                Intent orderIntent = new Intent(Home.this, OrderStatus.class);
                startActivity(orderIntent);
                dialog.dismiss();
                break;
            }
            case R.id.nav_send_login_details: {
                sendemailUSER();
                break;
            }
            case R.id.nav_about: {
                final SpotsDialog dialog = new SpotsDialog(Home.this);
                Intent about = new Intent(Home.this, About.class);
                startActivity(about);
                dialog.dismiss();
                break;
            }
            case R.id.nav_password: {
                showChangePasswordDialog();
                break;
            }
            case R.id.nav_banner: {
                final SpotsDialog dialog = new SpotsDialog(Home.this);
                dialog.show();
                Intent bannerIntent = new Intent(Home.this,BannerActivity.class);
                startActivity(bannerIntent);
                dialog.dismiss();
                break;
            }
            case R.id.nav_list: {
                final SpotsDialog dialog = new SpotsDialog(Home.this);
                Intent orderIntent = new Intent(Home.this, TodoList.class);
                startActivity(orderIntent);
                dialog.dismiss();
                break;}
            case R.id.nav_emailaddress:{
                showEmailAddressDialog();
                break;}
            case R.id.nav_name:{
                showChangeNameDialog();
                break;}
            case R.id.nav_logout: {
                //delete remmbered user details
                Paper.book().destroy();
                final SpotsDialog dialog = new SpotsDialog(Home.this);
                dialog.show();
                Intent signIn = new Intent(Home.this, Signin.class);
                signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signIn);
                dialog.dismiss();
                break;
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangeNameDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("CHANGE NAME");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.ic_child_care_black_24dp);
        alertDialog.setMessage("One time per session");
//        final SpotsDialog dialog1 = new SpotsDialog(Home.this);
//        dialog1.show();
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_name = inflater.inflate(R.layout.user_name_layout,null);
        final MaterialEditText edtName = layout_name.findViewById(R.id.edtuser_name);
        alertDialog.setView(layout_name);
        alertDialog.setPositiveButton("UPDATE!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Map<String, Object> NameUpdate = new HashMap<>();
                NameUpdate.put("name", edtName.getText().toString());
                DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                user.child(Common.currentUser.getPhone())
                        .updateChildren(NameUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                                dialog1.dismiss();
                                if (task.isSuccessful())
                                    Toast.makeText(Home.this, "Update name successful", Toast.LENGTH_LONG).show();

                            }
                        });
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showEmailAddressDialog() {
        AlertDialog.Builder alertDailog = new AlertDialog.Builder(Home.this);
        alertDailog.setTitle("CHANGE EMAIL ADDRESS");
        alertDailog.setCancelable(false);
        alertDailog.setIcon(R.drawable.ic_email_black_24dp);
        alertDailog.setMessage("One time per session");
        LayoutInflater inflater = LayoutInflater.from(this);
//        final SpotsDialog dialog1 = new SpotsDialog(Home.this);
//        dialog1.show();
        View layout_email = inflater.inflate(R.layout.email_address_layout,null);
        final MaterialEditText edtEmail = layout_email.findViewById(R.id.edtEmailAddress);
        alertDailog.setView(layout_email);
        alertDailog.setPositiveButton("UPDATE!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Map<String, Object> EmailUpdate = new HashMap<>();
                EmailUpdate.put("email", edtEmail.getText().toString());
                DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                user.child(Common.currentUser.getPhone())
                        .updateChildren(EmailUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                                dialog1.dismiss();
                                if (task.isSuccessful())
                                    Toast.makeText(Home.this, "Update email successful", Toast.LENGTH_LONG).show();

                            }
                        });
            }
        });
        alertDailog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDailog.show();
    }

    private void sendemailUSER() {
        //String[] CC = {user.getEmail().toString()};
        String[] TO = {Common.currentUser.getEmail().toString()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "You have created a staff account with FoodCrunch the anytime shopping app");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Here are your account details \n"+
                "The new account is created for the staff member \t" +
                (Common.currentUser.getName().toString())+
                "\n with email \t" +
                (Common.currentUser.getEmail().toString())+
                "\nand has been linked to your phone number \n" +
                "\n \n Your password is  \t" +
                (Common.currentUser.getPassword().toString())+
                "\n \n and your secure code is \t" +
                (Common.currentUser.getSecureCode().toString())+
                "\n\nPlease write down your secure code. \nIt will be used to recover your password");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            // Toast.makeText(Signup.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("CHANGE PASSWORD");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("One time per session");
        alertDialog.setIcon(R.drawable.ic_security_black_24dp);

        LayoutInflater inflater = LayoutInflater.from(this);
        View email_address_layout = inflater.inflate(R.layout.email_address_layout,null);
        View layout_pwd = inflater.inflate(R.layout.change_password_layout,null);
        final MaterialEditText edtPassword = layout_pwd.findViewById(R.id.edtPassword);
        final MaterialEditText edtNewPassword = layout_pwd.findViewById(R.id.edtNewPassword);
        final MaterialEditText edtRepeatPassword = layout_pwd.findViewById(R.id.edtRepeatNewPassword);

        alertDialog.setView(layout_pwd);
        alertDialog.setPositiveButton("UPDATE!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final SpotsDialog dialog1 = new SpotsDialog(Home.this);
                dialog1.show();
                //check old password
                if(edtPassword.getText().toString().equals(Common.currentUser.getPassword()))
                {
                    //check new password
                    if((edtNewPassword.getText().toString().equals(edtRepeatPassword.getText().toString()))
                            &&(! edtNewPassword.getText().toString().isEmpty())
                            )
                    {
                        if(! edtNewPassword.getText().toString().equals(edtPassword.getText().toString()))
                        {
                            Map<String, Object> passwordUpdate = new HashMap<>();
                            passwordUpdate.put("Password", edtNewPassword.getText().toString());
                            DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                            user.child(Common.currentUser.getPhone())
                                    .updateChildren(passwordUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            dialog1.dismiss();
                                            Toast.makeText(Home.this, "Your password was updated", Toast.LENGTH_LONG).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog1.dismiss();
                                            Toast.makeText(Home.this, "Problem updating password buddy", Toast.LENGTH_LONG).show();

                                        }
                                    })
                            ;
                        }
                        else
                        {
                            dialog1.dismiss();
                            Toast.makeText(Home.this,"New password is same as the old one",Toast.LENGTH_LONG).show();
                        }
                    }else
                    {
                        dialog1.dismiss();
                        Toast.makeText(Home.this,"Password doesnt match!\n Please try again",Toast.LENGTH_LONG).show();

                    }
                }else
                {
                    dialog1.dismiss();
                    Toast.makeText(Home.this,"Wrong Password!!\n Please try Again",Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void deleteCategory(final String key) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("SURE YOU WANT TO DELETE?");
        alertDialog.setCancelable(false);
        alertDialog.setIcon(R.drawable.ic_delete_forever_black_24dp);
        alertDialog.setMessage("Deleting a category WILL DELETE ALL \nfood items inside it!!\n \nThis cannot be undone!!");
        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int whichButton) {
             categories.child(key).removeValue();
             Toast.makeText(Home.this,"Category was deleted",Toast.LENGTH_LONG).show();
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


    private void showUpdateDialog(final String key, final Category item) {

        AlertDialog.Builder alertDailog = new AlertDialog.Builder(Home.this);
        alertDailog.setTitle("Update the Category");
        alertDailog.setCancelable(false);
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
        alertDailog.setIcon(R.drawable.ic_playlist_add_black_24dp);

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
            final SpotsDialog dialog = new SpotsDialog(Home.this);
            dialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(Home.this,"Uploaded Successfully !!! Updating database",Toast.LENGTH_LONG).show();
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