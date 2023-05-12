package com.example.food.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.Listener.RecipeClickListener;
import com.example.food.R;
import com.example.food.apiModels.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder> {

    Context context;
    List<Recipe> list;
    RecipeClickListener listener;

    public RandomRecipeAdapter(Context context, List<Recipe> list,RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RandomRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {


        //Displays the random recipes
        holder.titleText.setText(list.get(position).title);
        holder.titleText.setSelected(true);
        holder.likesText.setText(list.get(position).aggregateLikes + " Likes");
        holder.servingsText.setText(list.get(position).servings + " Servings");
        holder.timeText.setText(list.get(position).readyInMinutes + " Minutes");

        Picasso.get().load(list.get(position).image).into(holder.recipeImage);

        holder.randomRecipeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id),list.get(holder.getAdapterPosition()).image,String.valueOf(list.get(holder.getAdapterPosition()).aggregateLikes),list.get(holder.getAdapterPosition()).title,String.valueOf(list.get(holder.getAdapterPosition()).servings),String.valueOf(list.get(holder.getAdapterPosition()).readyInMinutes));
            }
        });


    }

    @Override
    public int getItemCount() {

        return list.size();
    }
}
class RandomRecipeViewHolder extends RecyclerView.ViewHolder {

    CardView randomRecipeContainer;
    TextView titleText, servingsText, likesText, timeText;
    ImageView recipeImage;
    public RandomRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        //Finds the default CardView
        randomRecipeContainer = itemView.findViewById(R.id.randomRecipeContainer);
        titleText = itemView.findViewById(R.id.titleText);
        servingsText = itemView.findViewById(R.id.servingsText);
        likesText = itemView.findViewById(R.id.likesText);
        timeText = itemView.findViewById(R.id.timeText);
        recipeImage = itemView.findViewById(R.id.recipeImage);
    }

}
