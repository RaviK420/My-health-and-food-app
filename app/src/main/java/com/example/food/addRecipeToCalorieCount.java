package com.example.food;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView; // updated import

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.food.Adapters.FavouriteAdapter;
import com.example.food.Adapters.RandomRecipeAdapter;
import com.example.food.Listener.RandomRecipeListener;
import com.example.food.Listener.RecipeClickListener;
import com.example.food.apiModels.GetRandomRecipeResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class addRecipeToCalorieCount extends AppCompatActivity {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    RecyclerView recyclerView;
    String userID,diet;
    ProgressDialog progressDialog;
    DocumentReference documentReference;
    ArrayList<String> favouriteID,favouriteImage,favouriteName,favouriteLikes,favouriteServing,favouriteTime;
    FavouriteAdapter favouriteAdapter;
    SearchView searchView;
    RandomRecipeAdapter randomRecipeAdapter;
    List<String> tags = new ArrayList<>();
    RequestManager requestManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_to_calorie_count);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();


        recyclerView = findViewById(R.id.recycler_calorie);
        documentReference = fStore.collection("users").document(userID);
        requestManager = new RequestManager(this);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                //Displays favourite recipes and also allows users to search for a specific recipe
                favouriteID = (ArrayList<String>) value.get("Favourite_ID");
                favouriteImage = (ArrayList<String>) value.get("Image");
                favouriteLikes = (ArrayList<String>) value.get("Likes");
                favouriteName = (ArrayList<String>) value.get("Name");
                favouriteServing = (ArrayList<String>) value.get("Serving");
                favouriteTime = (ArrayList<String>) value.get("Time");
                diet = value.getString("Diet");

                progressDialog.dismiss();

                recyclerView.setLayoutManager(new GridLayoutManager(addRecipeToCalorieCount.this,1));
                recyclerView.setHasFixedSize(true);
                favouriteAdapter = new FavouriteAdapter(addRecipeToCalorieCount.this,favouriteID,favouriteImage,favouriteLikes,favouriteName,favouriteServing,favouriteTime,recipeClickListener);
                recyclerView.setAdapter(favouriteAdapter);
                progressDialog.dismiss();

            }
        });

        searchView = findViewById(R.id.searchView_calorie);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                progressDialog.show();
                tags.clear();
                if (!diet.equals("none")){
                    tags.add(s+','+diet);
                }
                else{
                    tags.add(s);
                }
                requestManager.getRandomRecipes(randomRecipeListener,tags);
                progressDialog.dismiss();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }
    private final RandomRecipeListener randomRecipeListener= new  RandomRecipeListener(){

        @Override
        public void fetch(GetRandomRecipeResponse response, String message) {
            randomRecipeAdapter = new RandomRecipeAdapter(addRecipeToCalorieCount.this,response.recipes,recipeClickListener);
            recyclerView.setAdapter(randomRecipeAdapter);

        }

        @Override
        public void error(String message) {
            Toast.makeText(addRecipeToCalorieCount.this,message,Toast.LENGTH_SHORT);
        }
    };
    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void OnRecipeClicked(String id, String image, String likes, String name, String serving, String time) {
            startActivity(new Intent(addRecipeToCalorieCount.this,CalorieRecipeDetail.class)
                    .putExtra("id",id)
                    .putExtra("image",image)
                    .putExtra("likes",likes)
                    .putExtra("name",name)
                    .putExtra("serving",serving)
                    .putExtra("time",time));

        }
    };
}