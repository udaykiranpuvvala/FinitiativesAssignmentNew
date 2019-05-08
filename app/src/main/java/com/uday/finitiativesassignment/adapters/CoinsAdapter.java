package com.uday.finitiativesassignment.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uday.finitiativesassignment.R;
import com.uday.finitiativesassignment.activities.CoinDetailsActivity;
import com.uday.finitiativesassignment.entities.CoinsEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class CoinsAdapter extends RecyclerView.Adapter<CoinsAdapter.MyHolder> {
    Context context;
    ArrayList<CoinsEntity> coinsEntityArrayList;

    public CoinsAdapter(Context context, ArrayList<CoinsEntity> coinsModelArrayList) {
        this.context = context;
        this.coinsEntityArrayList = coinsModelArrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_coins, viewGroup, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder myHolder, final int i) {

        myHolder.txtName.setText("Name   : " + coinsEntityArrayList.get(i).getCoinName());
        myHolder.txtSymbol.setText("Symbol : " + coinsEntityArrayList.get(i).getCoinSymbol());


        myHolder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().postSticky(coinsEntityArrayList.get(i));
               /* for (int j = 0; j < BaseApplication.coinsModelArrayList.size(); j++) {
                    if (!coinsModelArrayList.get(i).getId().equals(BaseApplication.coinsModelArrayList.get(j).getId())) {
                        BaseApplication.coinsModelArrayList.add(coinsModelArrayList.get(i));
                        Toast.makeText(context, "Added Succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Already Added Succesfully", Toast.LENGTH_SHORT).show();
                    }
                }*/
            }
        });

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CoinDetailsActivity.class);
                intent.putExtra("coinsId",""+coinsEntityArrayList.get(i).getCoinId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return coinsEntityArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtSymbol;
        ImageView ivAdd;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtSymbol = itemView.findViewById(R.id.txtSymbol);
            ivAdd = itemView.findViewById(R.id.ivAdd);
        }
    }
}
