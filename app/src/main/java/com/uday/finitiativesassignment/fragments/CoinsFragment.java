package com.uday.finitiativesassignment.fragments;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.uday.finitiativesassignment.R;
import com.uday.finitiativesassignment.activities.BaseApplication;
import com.uday.finitiativesassignment.activities.MainActivity;
import com.uday.finitiativesassignment.adapters.CoinsAdapter;
import com.uday.finitiativesassignment.entities.CoinsEntity;
import com.uday.finitiativesassignment.interfaces.IParseListener;
import com.uday.finitiativesassignment.utilites.Constants;
import com.uday.finitiativesassignment.utilites.FinitDatabase;
import com.uday.finitiativesassignment.utilites.ServerResponse;
import com.uday.finitiativesassignment.utilites.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CoinsFragment extends Fragment implements IParseListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "CoinsFragment";
    private MainActivity mParent;
    private View rootView;
    RecyclerView recyclerViewCoins;
    ArrayList<CoinsEntity> coinsModelArrayList = new ArrayList<>();
    FinitDatabase finitDatabase;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParent = (MainActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.fragment_coins, container, false);
        initUI();
        return rootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSelectedEvent(CoinsEntity coinsEntity) {
        Utility.showLog("coinsModel", "" + coinsEntity.getCoinId());

        if (BaseApplication.coinsModelArrayList.size() == 0) {
            BaseApplication.coinsModelArrayList.add(coinsEntity);
            Toast.makeText(mParent, "Added Successfully", Toast.LENGTH_SHORT).show();
        } else {

            if (BaseApplication.coinsModelArrayList.contains(coinsEntity)) {
                Toast.makeText(mParent, "Already Added", Toast.LENGTH_SHORT).show();
            } else {
                BaseApplication.coinsModelArrayList.add(coinsEntity);
                Toast.makeText(mParent, "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void initUI() {
        recyclerViewCoins = (RecyclerView) rootView.findViewById(R.id.recyclerViewCoins);
        recyclerViewCoins.setLayoutManager(new LinearLayoutManager(mParent));
        recyclerViewCoins.setHasFixedSize(true);

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        finitDatabase = Room.databaseBuilder(
                mParent.getApplicationContext(), FinitDatabase.class, "production")
                .allowMainThreadQueries()
                .build();

        if (Utility.isNetworkAvailable(mParent)) {

            Utility.showLoadingDialog(mParent, "Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestGetJSONArray(mParent, Constants.COINS_URL, this, Constants.REQUESTCODE_COINS);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(mParent, "As their is No Internet. Application is Running on Offline Mode", Toast.LENGTH_LONG).show();

            List<CoinsEntity> coinsEntityArrayList = finitDatabase.daoAccess().fetchAllCoinsData();
            setAdapterToRecyclerView((ArrayList<CoinsEntity>) coinsEntityArrayList);
        }


    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        Utility.hideLoadingDialog();
        swipeRefreshLayout.setRefreshing(false);
        Utility.alertDialog(mParent, mParent.getResources().getString(R.string.something_went_wrong), null);
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);
        if (requestCode == Constants.REQUESTCODE_COINS) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                coinsModelArrayList.clear();
                finitDatabase.daoAccess().deleteCoinsData();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObjectList = jsonArray.optJSONObject(i);

                    CoinsEntity coinsEntity = new CoinsEntity();
                    coinsEntity.setCoinId(jsonObjectList.optString("id"));
                    coinsEntity.setCoinName(jsonObjectList.optString("name"));
                    coinsEntity.setCoinSymbol(jsonObjectList.optString("symbol"));


                    coinsModelArrayList.add(coinsEntity);
                }

                finitDatabase.daoAccess().insertCoins((List<CoinsEntity>) coinsModelArrayList);
                Utility.hideLoadingDialog();

                setAdapterToRecyclerView(coinsModelArrayList);
            } catch (JSONException e) {
                Utility.showLog("Error", "" + e);
            }
        }

    }

    private void setAdapterToRecyclerView(ArrayList<CoinsEntity> coinsModelArrayList) {
        CoinsAdapter coinsAdapter = new CoinsAdapter(mParent, coinsModelArrayList);
        recyclerViewCoins.setAdapter(coinsAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        initUI();
    }
}
