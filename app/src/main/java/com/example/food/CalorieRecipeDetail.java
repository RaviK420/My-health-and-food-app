package com.example.food;

import static java.lang.Integer.parseInt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class CalorieRecipeDetail extends AppCompatActivity {
    int id;
    TextView mealName,mealSource,mealSummary,supermarketLink;
    ImageView mealImage;
    RecyclerView mealIngredients,similarMeal,instructions;
    RequestManager manager;
    ProgressDialog progress;
    IngredientsAdapter ingredientsAdapter;
    SimilarRecipeAdapter similarRecipeAdapter;
    InstructionsAdapter instructionsAdapter;
    Button add;
    String userID,image,likes,name,serving,time;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView summary;
    String strSummary;
    String calImage,strMealName;

    boolean isclicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_recipe_detail);
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
        add = findViewById(R.id.Add_Button);
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


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //allows user to add the recipe
                new AlertDialog.Builder(CalorieRecipeDetail.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to add this recipe")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                //Toast.makeText(CalorieRecipeDetail.this,Integer.toString(getCalories(strSummary)),Toast.LENGTH_SHORT).show();
                                documentReference.update("calories",FieldValue.arrayUnion(Integer.toString(getCalories(strSummary))));
                                documentReference.update("calImages",FieldValue.arrayUnion(calImage));
                                documentReference.update("calName",FieldValue.arrayUnion(strMealName));
                                documentReference.update("calID",FieldValue.arrayUnion(Integer.toString(id)));
                                documentReference.update("amount",FieldValue.arrayUnion(Integer.toString(id+1)));
                                startActivity(new Intent(getApplicationContext(),HomeScreenV2.class));


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(CalorieRecipeDetail.this,"Unfortunate",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
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
    private int getCalories(String s) {
        String[] words = s.split(" ");
        for (int i = words.length - 1; i >= 0; i--) {
            if (words[i].contains("calories")) {
                int j = i - 1;
                return Integer.parseInt(words[j]);
            }
        }
        return 21;
    }

    private final RecipeDetailsListener listener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            progress.dismiss();
            mealName.setText(response.title);
            mealSource.setText(response.sourceName);
            mealSummary.setText(Html.fromHtml(response.summary));
            strSummary = String.valueOf(Html.fromHtml(response.summary));
            mealSummary.setClickable(true);
            calImage = response.image;
            strMealName = response.title;

            Picasso.get().load(response.image).into(mealImage);


            mealIngredients.setHasFixedSize(true);
            mealIngredients.setLayoutManager(new LinearLayoutManager(CalorieRecipeDetail.this,LinearLayoutManager.HORIZONTAL,false));

            ingredientsAdapter = new IngredientsAdapter(CalorieRecipeDetail.this,response.extendedIngredients);
            mealIngredients.setAdapter(ingredientsAdapter);

        }

        @Override
        public void didError(String message) {
            Toast.makeText(CalorieRecipeDetail.this, message, Toast.LENGTH_SHORT).show();

        }
    };


    private final SimilarRecipesListener similarRecipesListener = new SimilarRecipesListener() {
        @Override
        public void didFetch(List<SimilarRecipesResponse> response, String message) {
            similarMeal.setHasFixedSize(true);
            similarMeal.setLayoutManager(new LinearLayoutManager(CalorieRecipeDetail.this,LinearLayoutManager.HORIZONTAL,false));
            similarRecipeAdapter = new SimilarRecipeAdapter(CalorieRecipeDetail.this,response,recipeClickListener);
            similarMeal.setAdapter(similarRecipeAdapter);

        }

        @Override
        public void didError(String message) {
            Toast.makeText(CalorieRecipeDetail.this, message, Toast.LENGTH_SHORT).show();

        }
    };
    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void OnRecipeClicked(String id,String image, String likes,String name, String serving,String time) {
            startActivity(new Intent(CalorieRecipeDetail.this,CalorieRecipeDetail.class)
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
            instructions.setLayoutManager(new LinearLayoutManager(CalorieRecipeDetail.this,LinearLayoutManager.VERTICAL,false));

            instructionsAdapter = new InstructionsAdapter(CalorieRecipeDetail.this,response);
            instructions.setAdapter(instructionsAdapter);

        }

        @Override
        public void didError(String Message) {

        }
    };
}