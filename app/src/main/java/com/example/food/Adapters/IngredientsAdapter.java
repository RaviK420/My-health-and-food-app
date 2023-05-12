package com.example.food.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.apiModels.ExtendedIngredient;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<ingredientsViewHolder>{
    Context context;
    List<ExtendedIngredient> list;

    public IngredientsAdapter(Context context, List<ExtendedIngredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ingredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ingredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_meal_ingredients,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ingredientsViewHolder holder, int position) {
        holder.ingredientsName.setText(list.get(position).name);
        holder.ingredientsName.setSelected(true);
        holder.ingredientsQuantity.setText(list.get(position).original);
        holder.ingredientsQuantity.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/"+list.get(position).image).into(holder.ingredientsImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class ingredientsViewHolder extends RecyclerView.ViewHolder{
    TextView ingredientsQuantity,ingredientsName;
    ImageView ingredientsImage;
    public ingredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        ingredientsQuantity = itemView.findViewById(R.id.TextView_amount);
        ingredientsName = itemView.findViewById(R.id.ingredient_Name);
        ingredientsImage = itemView.findViewById(R.id.imageView_Ingredients);
    }
}
