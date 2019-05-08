package com.uday.finitiativesassignment.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uday.finitiativesassignment.R;
import com.uday.finitiativesassignment.entities.EventsEntity;
import com.uday.finitiativesassignment.utilites.Utility;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyHolder> {
    ArrayList<EventsEntity> eventsEntityArrayList;
    Context context;

    public EventsAdapter(Context context, ArrayList<EventsEntity> eventsEntityArrayList) {
        this.context = context;
        this.eventsEntityArrayList = eventsEntityArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_events, viewGroup, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        Picasso.get()
                .load(eventsEntityArrayList.get(i).getScreenshot())
                .placeholder(R.drawable.logo)
                .into(myHolder.ivEvent);

        myHolder.txtTitle.setText(eventsEntityArrayList.get(i).getTitle());
        if (!Utility.isValueNullOrEmpty(eventsEntityArrayList.get(i).getOrganizer())) {

            myHolder.txtOrganizer.setVisibility(View.VISIBLE);
            myHolder.txtOrganizer.setText(eventsEntityArrayList.get(i).getOrganizer());
        } else {
            myHolder.txtOrganizer.setVisibility(View.GONE);
        }
        myHolder.txtDescription.setText(eventsEntityArrayList.get(i).getDescription());

      /*  myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(eventsEntityArrayList.get(i));
                context.startActivity(new Intent(context, EventsDetailsActivity.class));
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return eventsEntityArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView ivEvent;
        TextView txtTitle, txtOrganizer, txtDescription;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivEvent = itemView.findViewById(R.id.ivEvent);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtOrganizer = itemView.findViewById(R.id.txtOrganizer);
            txtDescription = itemView.findViewById(R.id.txtDescription);
        }
    }
}
