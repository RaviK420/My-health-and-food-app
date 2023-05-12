package com.example.food;

import static java.lang.Integer.parseInt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food.Adapters.IngredientsAdapter;
import com.example.food.Adapters.InstructionsAdapter;
import com.example.food.Adapters.SimilarRecipeAdapter;
import com.example.food.Listener.InstructionsListener;
import com.example.food.Listener.RecipeClickListener;
import com.example.food.Listener.RecipeDetailsListener;
import com.example.food.Listener.SimilarRecipesListener;
import com.example.food.apiModels.AnalyzedInstruction;
import com.example.food.apiModels.RecipeDetailsResponse;
import com.example.food.apiModels.SimilarRecipesResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {
    int id;
    TextView mealName,mealSource,mealSummary,supermarketLink;
    ImageView mealImage;
    RecyclerView mealIngredients,similarMeal,instructions;
    RequestManager manager;
    ProgressDialog progress;
    IngredientsAdapter ingredientsAdapter;
    SimilarRecipeAdapter similarRecipeAdapter;
    InstructionsAdapter instructionsAdapter;
    Button favourite;
    String userID,image,likes,name,serving,time;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ArrayList<String> favouriteItems;

    boolean isclicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        isclicked = false;

        mealName = findViewById(R.id.TextView_MealName);;
        mealSource = findViewById(R.id.TextView_MealSource);
        mealSummary = findViewById(R.id.TextView_MealSummary);
        mealImage = findViewById(R.id.ImageView_MealImage);
        mealIngredients = findViewById(R.id.Recycler_MealIngredients);
        similarMeal = findViewById(R.id.Recycler_similarMeal);
        instructions = findViewById(R.id.Recycler_Instructions);
        favourite = findViewById(R.id.Favourite_Button);
        supermarketLink = findViewById(R.id.supermarket);


        id = parseInt(getIntent().getStringExtra("id"));
        image = getIntent().getStringExtra("image");
        likes = getIntent().getStringExtra("likes");
        name = getIntent().getStringExtra("name");
        serving = getIntent().getStringExtra("serving");
        time = getIntent().getStringExtra("time");


        manager = new RequestManager(this);
        manager.getRecipeDetails(listener,id);
        manager.getSimilarRecipes(similarRecipesListener,id);
        manager.getInstructions(instructionsListener,id);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading...");
        progress.show();

        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                favouriteItems = (ArrayList<String>) value.get("Favourite_ID");
                if(favouriteItems.contains(Integer.toString(id)) == true){
                    isclicked = true;
                    favourite.setBackground(getDrawable(R.drawable.ic_baseline_favorite_fill));
                }

            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isclicked == false){
                    isclicked = true;
                    favourite.setBackground(getDrawable(R.drawable.ic_baseline_favorite_fill));

                    documentReference.update("Favourite_ID", FieldValue.arrayUnion(Integer.toString(id)));
                    documentReference.update("Image",FieldValue.arrayUnion(image));

                    documentReference.update("Likes",FieldValue.arrayUnion(Integer.toString(parseInt(likes)+id)));
                    documentReference.update("Name",FieldValue.arrayUnion(name));
                    documentReference.update("Serving",FieldValue.arrayUnion(Integer.toString(parseInt(serving)+id)));
                    documentReference.update("Time",FieldValue.arrayUnion(Integer.toString(parseInt(time)+id)));

                }
                else{
                    isclicked = false;
                    favourite.setBackground(getDrawable(R.drawable.ic_baseline_favorite_border_24));

                    documentReference.update("Favourite_ID",FieldValue.arrayRemove(Integer.toString(id)));
                    documentReference.update("Image",FieldValue.arrayRemove(image));
                    documentReference.update("Likes",FieldValue.arrayRemove(Integer.toString(parseInt(likes)+id)));
                    documentReference.update("Name",FieldValue.arrayRemove(name));
                    documentReference.update("Serving",FieldValue.arrayRemove(Integer.toString(parseInt(serving)+id)));
                    documentReference.update("Time",FieldValue.arrayRemove(Integer.toString(parseInt(time)+id)));

                }
            }
        });

        supermarketLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri supermarketUri = Uri.parse("geo:0,0?q=supermarkets near me");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW,supermarketUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    private final RecipeDetailsListener listener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            progress.dismiss();
            mealName.setText(response.title);
            mealSource.setText(response.sourceName);
            mealSummary.setText(Html.fromHtml(response.summary));
            mealSummary.setClickable(true);

            Picasso.get().load(response.image).into(mealImage);

            mealIngredients.setHasFixedSize(true);
            mealIngredients.setLayoutManager(new LinearLayoutManager(RecipeDetailActivity.this,LinearLayoutManager.HORIZONTAL,false));

            ingredientsAdapter = new IngredientsAdapter(RecipeDetailActivity.this,response.extendedIngredients);
            mealIngredients.setAdapter(ingredientsAdapter);

        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };


    private final SimilarRecipesListener similarRecipesListener = new SimilarRecipesListener() {
        @Override
        public void didFetch(List<SimilarRecipesResponse> response, String message) {
            similarMeal.setHasFixedSize(true);
            similarMeal.setLayoutManager(new LinearLayoutManager(RecipeDetailActivity.this,LinearLayoutManager.HORIZONTAL,false));
            similarRecipeAdapter = new SimilarRecipeAdapter(RecipeDetailActivity.this,response,recipeClickListener);
            similarMeal.setAdapter(similarRecipeAdapter);

        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };
    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void OnRecipeClicked(String id,String image, String likes,String name, String serving,String time) {
            startActivity(new Intent(RecipeDetailActivity.this,RecipeDetailActivity.class)
                    .putExtra("id",id)
                    .putExtra("image",image)
                    .putExtra("likes",likes)
                    .putExtra("name",name)
                    .putExtra("serving",serving)
                    .putExtra("time",time));

        }
    };
    private final InstructionsListener instructionsListener = new InstructionsListener() {
        @Override
        public void didFetch(List<AnalyzedInstruction> response, String message) {
            instructions.setHasFixedSize(true);
            instructions.setLayoutManager(new LinearLayoutManager(RecipeDetailActivity.this,LinearLayoutManager.VERTICAL,false));

            instructionsAdapter = new InstructionsAdapter(RecipeDetailActivity.this,response);
            instructions.setAdapter(instructionsAdapter);

        }

        @Override
        public void didError(String Message) {

        }
    };
}