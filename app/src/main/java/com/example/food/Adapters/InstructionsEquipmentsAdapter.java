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
import com.example.food.apiModels.Equipment;
import com.example.food.apiModels.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionsEquipmentsAdapter extends RecyclerView.Adapter<InstructionEquipmentsViewHolder>{
    Context context;
    List<Equipment> list;

    public InstructionsEquipmentsAdapter(Context context, List<Equipment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionEquipmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionEquipmentsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_step_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionEquipmentsViewHolder holder, int position) {

        holder.instructionItemName.setText(list.get(position).name);
        holder.instructionItemName.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/equipment_250x250/"+list.get(position).image).into(holder.instructionItem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionEquipmentsViewHolder extends RecyclerView.ViewHolder{
    ImageView instructionItem;
    TextView instructionItemName;
    public InstructionEquipmentsViewHolder(@NonNull View itemView) {
        super(itemView);
        instructionItem = itemView.findViewById(R.id.ImageView_instructionsItems);
        instructionItemName = itemView.findViewById(R.id.TextView_instructionsStepItem);
    }
}