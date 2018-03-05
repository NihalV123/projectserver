package a123.vaidya.nihal.foodcrunchserver;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import a123.vaidya.nihal.foodcrunchserver.Database.Database;
import a123.vaidya.nihal.foodcrunchserver.Model.Food;
import a123.vaidya.nihal.foodcrunchserver.ViewHolder.FoodViewHolder;

public class SearchActivity extends AppCompatActivity {
    //for searching items in category
    private FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
    private final List<String> suggestList = new ArrayList<>();
    private MaterialSearchBar materialSearchBar;

    private FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    private RecyclerView recycler_menu;
    private RecyclerView.LayoutManager layoutManager;
    private String foodId="";
    private FirebaseDatabase database;
    private DatabaseReference foodList;
    private String categoryId="";

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
                        .inflate(R.layout.food_item,parent,false);
                return new FoodViewHolder(itemView);
            }
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
            protected void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int position, @NonNull Food model) {
                viewHolder.food_name.setText(model.getName());
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);
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

    @Override
    protected void onStop() {
        if(adapter != null)
            adapter.stopListening();
        if(searchAdapter != null)
            searchAdapter.stopListening();
        super.onStop();
    }
}
