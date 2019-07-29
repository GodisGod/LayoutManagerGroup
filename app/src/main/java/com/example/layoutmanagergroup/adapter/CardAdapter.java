package com.example.layoutmanagergroup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.layoutmanagergroup.R;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {

    private List<Integer> ids;

    private LayoutInflater inflater;

    public CardAdapter(Context context, List<Integer> ids) {
        this.ids = ids;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        holder.imgCard.setImageResource(ids.get(position));
    }

    @Override
    public int getItemCount() {
        return ids.size();
    }

    public class CardHolder extends RecyclerView.ViewHolder {

        private ImageView imgCard;

        public CardHolder(@NonNull View itemView) {
            super(itemView);
            imgCard = itemView.findViewById(R.id.img_card);
        }


    }
}
