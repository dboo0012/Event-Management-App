package com.fit2081.fit2081_assignment_1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.fit2081_assignment_1.R;
import com.fit2081.fit2081_assignment_1.objects.EventCategory;

import java.util.ArrayList;

public class ListViewRecyclerAdapter extends RecyclerView.Adapter<ListViewRecyclerAdapter.CustomViewHolder>{
    ArrayList<EventCategory> data;

    public ListViewRecyclerAdapter(ArrayList<EventCategory> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ListViewRecyclerAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewRecyclerAdapter.CustomViewHolder holder, int position) {
        // Update card view with data
        // get the object at the position
        EventCategory eventCategory = this.data.get(position);

        // set the data to the view holder
        holder.rvCategoryId.setText(String.valueOf(eventCategory.getCategoryId()));
        holder.rvCategoryName.setText(eventCategory.getCategoryName());
        holder.rvEventCount.setText(String.valueOf(eventCategory.getEventCount()));
        holder.rvCategoryIsActive.setText(eventCategory.isCategoryActive() ? "Active" : "Inactive");
    }

    @Override
    public int getItemCount() {
        return this.data != null ? this.data.size() : 0;
    }

    public void updateData(ArrayList<EventCategory> newData) {
        this.data.clear();
        this.data.addAll(newData);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public TextView rvCategoryId;
        public TextView rvCategoryName;
        public TextView rvEventCount;
        public TextView rvCategoryIsActive;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            // Reference fields in the view holder layout (card_layout.xml)
            rvCategoryId = itemView.findViewById(R.id.rv_categoryId);
            rvCategoryName = itemView.findViewById(R.id.rv_categoryName);
            rvEventCount = itemView.findViewById(R.id.rv_eventCount);
            rvCategoryIsActive = itemView.findViewById(R.id.rv_categoryIsActive);
        }
    }
}
