package com.example.food.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.apiModels.AnalyzedInstruction;

import java.util.List;

public class InstructionsAdapter extends RecyclerView.Adapter<instructionsViewHolder>{
    Context context;
    List<AnalyzedInstruction> list;

    public InstructionsAdapter(Context context, List<AnalyzedInstruction> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public instructionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new instructionsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull instructionsViewHolder holder, int position) {

        holder.instructionName.setText(list.get(position).name);
        holder.instructionStep.setHasFixedSize(true);

        holder.instructionStep.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        InstructionStepAdapter instructionStepAdapter = new InstructionStepAdapter(context,list.get(position).steps);
        holder.instructionStep.setAdapter(instructionStepAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
 class instructionsViewHolder extends RecyclerView.ViewHolder {
    TextView instructionName;
    RecyclerView instructionStep;


     public instructionsViewHolder(@NonNull View itemView) {
         super(itemView);
         instructionName = itemView.findViewById(R.id.TextView_instructionName);
         instructionStep = itemView.findViewById(R.id.Recycler_InstructionStep);
     }
 }
