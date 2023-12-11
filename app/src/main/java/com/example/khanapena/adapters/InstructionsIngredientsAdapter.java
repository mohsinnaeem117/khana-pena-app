package com.example.khanapena.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.khanapena.R;
import com.example.khanapena.models.Ingredient;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionsIngredientsAdapter extends RecyclerView.Adapter<InstructionsIngredientsViewHolder> {

    Context context;
    List<Ingredient> list;

    public InstructionsIngredientsAdapter(Context context, List<Ingredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionsIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionsIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_step_items,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsIngredientsViewHolder holder, int position) {
        holder.textView_instruction_step_items.setText(list.get(position).name);
        holder.textView_instruction_step_items.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/"+ list.get(position).image).into(holder.imageView_instructions_step_items);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionsIngredientsViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView_instructions_step_items;
    TextView textView_instruction_step_items;
    public InstructionsIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);
        textView_instruction_step_items = itemView.findViewById(R.id.textView_instruction_step_items);
        imageView_instructions_step_items = itemView.findViewById(R.id.imageView_instructions_step_items);
    }
}
