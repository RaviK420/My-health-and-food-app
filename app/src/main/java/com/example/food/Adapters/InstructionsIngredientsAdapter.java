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
import com.example.food.apiModels.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionsIngredientsAdapter extends RecyclerView.Adapter<InstructionIngredientsViewHolder>{
    Context context;
    List<Ingredient> list;

    public InstructionsIngredientsAdapter(Context context, List<Ingredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_step_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionIngredientsViewHolder holder, int position) {

        holder.instructionItemName.setText(list.get(position).name);
        holder.instructionItemName.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_250x250/"+list.get(position).image).into(holder.instructionItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionIngredientsViewHolder extends RecyclerView.ViewHolder{
    ImageView instructionItem;
    TextView instructionItemName;
    public InstructionIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        instructionItem = itemView.findViewById(R.id.ImageView_instructionsItems);
        instructionItemName = itemView.findViewById(R.id.TextView_instructionsStepItem);
    }
}