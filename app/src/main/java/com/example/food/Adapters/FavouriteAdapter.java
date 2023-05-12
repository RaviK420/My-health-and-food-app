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

import com.example.food.Listener.RecipeClickListener;
import com.example.food.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteRecipeViewHolder>{
    Context context;
    ArrayList<String> id,image,likes,name,serving,time;
    RecipeClickListener listener;

    public FavouriteAdapter(Context context, ArrayList<String> id, ArrayList<String> image, ArrayList<String> likes, ArrayList<String> name, ArrayList<String> serving, ArrayList<String> time,RecipeClickListener listener) {
        this.context = context;
        this.id = id;
        this.image = image;
        this.likes = likes;
        this.name = name;
        this.serving = serving;
        this.time = time;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavouriteRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouriteRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_favourite_recipe,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteRecipeViewHolder holder, int position) {


        if (position>=0 && position<id.size()) {
            holder.title.setText(name.get(position));
            holder.title.setSelected(true);

            holder.likes.setText(Integer.toString(parseInt(likes.get(position)) - parseInt(id.get(position))) + " Likes");
            holder.servings.setText(Integer.toString(parseInt(serving.get(position)) - parseInt(id.get(position))) + " Servings");
            holder.time.setText(Integer.toString(parseInt(time.get(position)) - parseInt(id.get(position))) + " minutes");

            Picasso.get().load(image.get(position)).into(holder.imageView);
        }
        holder.favourite_recipe_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if (pos>=0 && pos<id.size()) {
                    listener.OnRecipeClicked(id.get(pos), image.get(pos),
                            Integer.toString(parseInt(likes.get(pos)) - parseInt(id.get(pos))), name.get(pos),
                            Integer.toString(parseInt(serving.get(pos)) - parseInt(id.get(pos))),
                            Integer.toString(parseInt(time.get(pos)) - parseInt(id.get(pos))));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return id.size();
    }
}
class FavouriteRecipeViewHolder extends RecyclerView.ViewHolder{
    CardView favourite_recipe_container;
    TextView title,servings,likes,time;
    ImageView imageView;

    public FavouriteRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        favourite_recipe_container = itemView.findViewById(R.id.favouriteRecipeContainer);
        title = itemView.findViewById(R.id.titleText);
        servings = itemView.findViewById(R.id.servingsText);
        likes = itemView.findViewById(R.id.likesText);
        time = itemView.findViewById(R.id.timeText);
        imageView = itemView.findViewById(R.id.recipeImage);
    }
}
