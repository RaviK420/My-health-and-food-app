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
import com.example.food.apiModels.SimilarRecipesResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarRecipeAdapter extends RecyclerView.Adapter<SimilarRecipeViewHolder> {
    Context context;
    List<SimilarRecipesResponse> list;
    RecipeClickListener listener;

    public SimilarRecipeAdapter(Context context, List<SimilarRecipesResponse> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimilarRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimilarRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_similar_recipe,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarRecipeViewHolder holder, int position) {
        holder.title.setText(list.get(position).title);
        holder.title.setSelected(true);
        holder.serving.setText(list.get(position).servings+" Persons");
        Picasso.get().load("https://spoonacular.com/recipeImages/"+list.get(position).id+"-556x370."+list.get(position).imageType).into(holder.image);

        holder.similarHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id),"https://spoonacular.com/recipeImages/"+list.get(position).id+"-556x370."+list.get(position).imageType,"10",list.get(holder.getAdapterPosition()).title,String.valueOf(list.get(holder.getAdapterPosition()).servings),String.valueOf(list.get(holder.getAdapterPosition()).readyInMinutes));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class SimilarRecipeViewHolder extends RecyclerView.ViewHolder{
    CardView similarHolder;
    TextView title,serving;
    ImageView image;
    public SimilarRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        similarHolder = itemView.findViewById(R.id.similarRecipeHolder);
        title = itemView.findViewById(R.id.TextView_SimilarName);
        serving = itemView.findViewById(R.id.TextView_SimilarServing);
        image = itemView.findViewById(R.id.ImageView_SimilarImage);
    }
}
