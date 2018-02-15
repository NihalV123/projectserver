package a123.vaidya.nihal.foodcrunchserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import a123.vaidya.nihal.foodcrunchserver.Model.Food;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.FoodViewHolder;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fab;


    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;

    String categoryId="";

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    //for searching firebase
    FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //firebase code
        db = FirebaseDatabase.getInstance();
        foodList =db.getReference("Foods");

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get intent
        if (getIntent()!=null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty()&&categoryId != null)
        {
            loadListFood(categoryId);
        }

        //search
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
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
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //disable and enable for ui effect
//                if(!enabled)
//                    recycler_menu.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
//        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
//                Food.class,
//                R.layout.food_item,
//                FoodViewHolder.class,
//                foodList.orderByChild("Name").equalTo(text.toString()) //used to compaire name while search
//        ) {
//            @Override
//            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
//                viewHolder.food_name.setText(model.getName());
//                viewHolder.food_name.setText(model.getName());
//                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);
//
//                final Food local = model;
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View v, int position, boolean isLongClick) {
//                        //Toast.makeText(FoodList.this,""+local.getName(),Toast.LENGTH_SHORT).show();
//                        //this is the third activity
//                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
//                        //send to new activity
//                        foodDetail.putExtra("FoodId",searchAdapter.getRef(position).getKey());
//                        startActivity(foodDetail);
//
//
//                    }
//                });
//
//            }
//        };
//
//        recycler_menu.setAdapter(searchAdapter);


    }

    private void loadSuggest() {
//        foodList.orderByChild("MenuId").equalTo(categoryId)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot postDnapshot:dataSnapshot.getChildren())
//                        {
//                            Food item = postDnapshot.getValue(Food.class);
//                            suggestList.add(item.getName());
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
    }

    //select * from foods where menuid=&
    private void loadListFood(String categoryId) {
//        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
//                R.layout.food_item,FoodViewHolder.class,foodList.orderByChild("MenuId")
//                .equalTo(categoryId)) {
//            @Override
//            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
//                viewHolder.food_name.setText(model.getName());
//                viewHolder.food_name.setText(model.getName());
//                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);
//
//                final Food local = model;
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View v, int position, boolean isLongClick) {
//                        //Toast.makeText(FoodList.this,""+local.getName(),Toast.LENGTH_SHORT).show();
//                        //this is the third activity
//                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
//                        //send to new activity
//                        foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
//                        startActivity(foodDetail);
//
//
//                    }
//                });
//
//            }
//        };
//
//        //set adapter
//        recycler_menu.setAdapter(adapter);


    }
}
