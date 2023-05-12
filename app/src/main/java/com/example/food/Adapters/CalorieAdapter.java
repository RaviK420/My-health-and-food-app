package com.example.food.Adapters;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.Listener.CalorieClickListener;
import com.example.food.Listener.RecipeClickListener;
import com.example.food.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CalorieAdapter extends RecyclerView.Adapter<calorieRecipeViewHolder>{
    Context context;
    ArrayList<String> name,images,calories,amount,calID;
    CalorieClickListener listener;


    public CalorieAdapter(Context context, ArrayList<String> name, ArrayList<String> images, ArrayList<String> calories,ArrayList<String> amount, ArrayList<String> calID,CalorieClickListener listener) {
        this.context = context;
        this.name = name;
        this.images = images;
        this.calories = calories;
        this.amount = amount;
        this.calID = calID;
        this.listener = listener;
    }

    @NonNull
    @Override
    public calorieRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new calorieRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_calorie_recipe,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull calorieRecipeViewHolder holder, int position) {
        holder.recipeName.setText(name.get(position));
        holder.calories.setText(calories.get(position)+" Calories");

        Picasso.get().load(images.get(position)).into(holder.recipeImage);

        int trueAmount = Integer.parseInt(amount.get(position)) - Integer.parseInt(calID.get(position));
        holder.textAmount.setText("x"+Integer.toString(trueAmount));

        holder.calorie_recipe_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                listener.onclicklistener(name.get(pos), images.get(pos),calories.get(pos),amount.get(pos),calID.get(pos));
            }
        });

    }

    @Override
    public int getItemCount() {
        return name.size();
    }
}

class calorieRecipeViewHolder extends RecyclerView.ViewHolder{
    CardView calorie_recipe_container;
    TextView calories,recipeName,textAmount;
    ImageView recipeImage;


    public calorieRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        calorie_recipe_container = itemView.findViewById(R.id.calorieRecipeContainer);
        calories = itemView.findViewById(R.id.calorieText);
        recipeName = itemView.findViewById(R.id.titleText);
        recipeImage = itemView.findViewById(R.id.recipeImage);
        textAmount = itemView.findViewById(R.id.TextView_amount);

    }
}