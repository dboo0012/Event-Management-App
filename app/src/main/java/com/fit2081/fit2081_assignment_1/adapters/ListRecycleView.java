package com.fit2081.fit2081_assignment_1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.objects.EventCategory;

import java.util.ArrayList;

public class ListRecycleView extends RecyclerView.Adapter<ListRecycleView.CustomViewHolder>{
    ArrayList<EventCategory> data = new ArrayList<EventCategory>();

    public void setData(ArrayList<EventCategory> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ListRecycleView.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
//        View view = View.inflate(parent.getContext(), R.layout.card_layout, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRecycleView.CustomViewHolder holder, int position) {
        // Update card view with data
        // get the object at the position
        // set the data to the view holder
    }

    @Override
    public int getItemCount() {
        return this.data != null ? this.data.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            // Reference fields in the view holder layout (card_layout.xml)
        }
    }
}
