package com.example.khanapena.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khanapena.R;
import com.example.khanapena.models.InstructionsResponse;

import java.util.List;

public class InstructionsAdapter extends RecyclerView.Adapter<InstuctionsViewHolder> {

    Context context;
    List<InstructionsResponse> list;

    public InstructionsAdapter(Context context, List<InstructionsResponse> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstuctionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstuctionsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstuctionsViewHolder holder, int position) {
        holder.textView_instruction_name.setText(list.get(position).name);
        holder.recycler_instructions_steps.setHasFixedSize(true);
        holder.recycler_instructions_steps.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        InstructionStepAdapter instructionStepAdapter = new InstructionStepAdapter(context, list.get(position).steps);
        holder.recycler_instructions_steps.setAdapter(instructionStepAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstuctionsViewHolder extends RecyclerView.ViewHolder {

    TextView textView_instruction_name;
    RecyclerView recycler_instructions_steps;
    public InstuctionsViewHolder(@NonNull View itemView) {
        super(itemView);
        textView_instruction_name =itemView.findViewById(R.id.textView_ingredients_name);
        recycler_instructions_steps =itemView.findViewById(R.id.recycler_instructions_steps);
    }
}
