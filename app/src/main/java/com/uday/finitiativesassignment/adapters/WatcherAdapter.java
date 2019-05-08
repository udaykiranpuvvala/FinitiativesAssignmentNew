package com.uday.finitiativesassignment.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uday.finitiativesassignment.R;
import com.uday.finitiativesassignment.entities.WatcherEntity;

import java.util.ArrayList;

public class WatcherAdapter extends RecyclerView.Adapter<WatcherAdapter.MyHolder> {
    ArrayList<WatcherEntity> watcherEntityArrayList;
    Context context;

    public WatcherAdapter(Context context, ArrayList<WatcherEntity> watcherEntityArrayList) {
        this.context = context;
        this.watcherEntityArrayList = watcherEntityArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_watchers, viewGroup, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        myHolder.txtInr.setText("INR : " + watcherEntityArrayList.get(i).getInr());
        myHolder.txtUsd.setText("USD : " + watcherEntityArrayList.get(i).getUsd());
        myHolder.txtCoinname.setText("Coin Name : " + watcherEntityArrayList.get(i).getCoinname());
    }

    @Override
    public int getItemCount() {
        return watcherEntityArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtCoinname,txtInr, txtUsd;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtCoinname=itemView.findViewById(R.id.txtCoinname);
            txtInr = itemView.findViewById(R.id.txtInr);
            txtUsd = itemView.findViewById(R.id.txtUsd);
        }
    }
}
