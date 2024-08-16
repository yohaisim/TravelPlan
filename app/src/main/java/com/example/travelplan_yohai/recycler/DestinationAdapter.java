package com.example.travelplan_yohai.recycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travelplan_yohai.R;
import com.example.travelplan_yohai.activities.MyAttractionsActivity;
import com.example.travelplan_yohai.model.Destination;

import java.util.ArrayList;
import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private final Context context;
    private final List<Destination> mList = new ArrayList<>();

    private final OnDestinationAddClickListener listener;

    private final Boolean shouldDisplayAddButton;

    // Interface for button click listener
    public interface OnDestinationAddClickListener {
        void onPlusButtonClick(Destination destination);

        void onImageClick(Destination destination);
    }

    public DestinationAdapter(Context context, OnDestinationAddClickListener listener, Boolean shouldDisplayAddButton) {
        this.context = context;
        this.listener = listener;
        this.shouldDisplayAddButton = shouldDisplayAddButton;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = mList.get(position);
        holder.nameTextView.setText(destination.getName());
        Glide.with(context)
                .load(destination.getImageUrl())
                .into(holder.imageView);
        if (shouldDisplayAddButton) {
            holder.addButton.setVisibility(View.VISIBLE);
            holder.addButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPlusButtonClick(destination);
                }
            });
        } else {
            holder.imageView.setOnClickListener(v -> listener.onImageClick(destination));
            holder.addButton.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(List<Destination> destinations) {
        mList.clear();
        mList.addAll(destinations);
        notifyDataSetChanged();
    }

    public static class DestinationViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        ImageView imageView;
        public ImageView addButton;

        public DestinationViewHolder(@NonNull View itemView, final OnDestinationAddClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.destination_name);
            imageView = itemView.findViewById(R.id.destination_image);
            addButton = itemView.findViewById(R.id.add_button);

        }
    }

}