package com.example.travelplan_yohai.recycler;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelplan_yohai.R;
import com.example.travelplan_yohai.model.Attraction;

import java.util.ArrayList;
import java.util.List;


public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.AttractionViewHolder> {

    private Context context;
    private final List<Attraction> attractions = new ArrayList<>();

    private final OnAttractionClickListener listener;

    public AttractionAdapter(Context context, OnAttractionClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public interface OnAttractionClickListener {


        void onAttractionClicked(Attraction attraction);
    }

    @NonNull
    @Override
    public AttractionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attraction, parent, false);
        return new AttractionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttractionViewHolder holder, int position) {
        Attraction attraction = attractions.get(position);
        holder.nameTextView.setText(attraction.getName());

        Glide.with(context)
                .load(attraction.getImageUrl())
                .into(holder.imageView);

        holder.imageView.setOnClickListener(v -> listener.onAttractionClicked(attraction));
    }

    @Override
    public int getItemCount() {
        return attractions.size();
    }

    public static class AttractionViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;

        public AttractionViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.destination_name);
            imageView = itemView.findViewById(R.id.destination_image);
        }
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions.clear();
        this.attractions.addAll(attractions);
        notifyDataSetChanged();
    }
}

