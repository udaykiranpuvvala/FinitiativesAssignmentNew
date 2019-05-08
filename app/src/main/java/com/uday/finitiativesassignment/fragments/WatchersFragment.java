package com.uday.finitiativesassignment.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.uday.finitiativesassignment.R;
import com.uday.finitiativesassignment.activities.BaseApplication;
import com.uday.finitiativesassignment.activities.MainActivity;
import com.uday.finitiativesassignment.adapters.WatcherAdapter;
import com.uday.finitiativesassignment.interfaces.IParseListener;
import com.uday.finitiativesassignment.entities.WatcherEntity;
import com.uday.finitiativesassignment.utilites.Constants;
import com.uday.finitiativesassignment.utilites.ServerResponse;
import com.uday.finitiativesassignment.utilites.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WatchersFragment extends Fragment implements IParseListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "WatchersFragment";
    private MainActivity mParent;
    private View rootView;
    RecyclerView recyclerViewWatches;
    ArrayList<WatcherEntity> watcherEntityArrayList = new ArrayList<>();
    TextView txtNoDataFound;
    private static boolean handlerflag = false;
    private Handler handler;
    private Runnable runnable;
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
        rootView = inflater.inflate(R.layout.fragment_watches, container, false);
        initUI();
        return rootView;
    }

    private void initUI() {
        txtNoDataFound = rootView.findViewById(R.id.txtNoDataFound);
        recyclerViewWatches = rootView.findViewById(R.id.recyclerViewWatches);
        recyclerViewWatches.setLayoutManager(new LinearLayoutManager(mParent));
        recyclerViewWatches.setHasFixedSize(true);

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        checkAndCallAPI();

        handlerflag = true;

        handler = new Handler();
        final int delay = 60000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                checkAndCallAPI();
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    private void startyourtime() {
        runnable = new Runnable() {
            @Override
            public void run() {
                //your code here
                checkAndCallAPI();
            }
        };
        handler.postDelayed(runnable, 10000);
    }

    @Override
    public void onPause() {
        super.onPause();
        handlerflag = false;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!handlerflag) {
            startyourtime();

        }
    }

    public void checkAndCallAPI() {
        if (Utility.isNetworkAvailable(mParent)) {
            if (BaseApplication.coinsModelArrayList.size() != 0) {
                txtNoDataFound.setVisibility(View.GONE);

                callAPIForCurrencies();
            } else {
                swipeRefreshLayout.setRefreshing(false);
                txtNoDataFound.setVisibility(View.VISIBLE);
                Toast.makeText(mParent, "Please Add Some Coins for Currencies", Toast.LENGTH_SHORT).show();
            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(mParent, mParent.getResources().getString(R.string.please_check_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void callAPIForCurrencies() {
        Utility.showLoadingDialog(mParent, "Loading...", false);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < BaseApplication.coinsModelArrayList.size(); i++) {
            stringBuilder.append(BaseApplication.coinsModelArrayList.get(i).getCoinId() + ",");
        }
        String ids = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();

        ServerResponse serverResponse = new ServerResponse();
        serverResponse.serviceRequestGet(mParent, Constants.WATCHES_URL + ids + "&vs_currencies=USD,INR", this, Constants.REQUESTCODE_WATCHERS);

        Utility.showLog("URL", "" + Constants.WATCHES_URL + ids + "&vs_currencies=USD,INR");
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
        Utility.hideLoadingDialog();
        if (requestCode == Constants.REQUESTCODE_WATCHERS) {

            try {
                JSONObject jsonObject = new JSONObject(response);
                watcherEntityArrayList.clear();
                for (int i = 0; i < BaseApplication.coinsModelArrayList.size(); i++) {
                    JSONObject jsonObjectValues = jsonObject.optJSONObject(BaseApplication.coinsModelArrayList.get(i).getCoinId());
                    String usd = jsonObjectValues.optString("usd");
                    String inr = jsonObjectValues.optString("inr");

                    WatcherEntity watcherEntity = new WatcherEntity();
                    watcherEntity.setInr(inr);
                    watcherEntity.setUsd(usd);
                    watcherEntity.setCoinname(BaseApplication.coinsModelArrayList.get(i).getCoinName());

                    watcherEntityArrayList.add(watcherEntity);
                }

                WatcherAdapter watcherAdapter = new WatcherAdapter(mParent, watcherEntityArrayList);
                recyclerViewWatches.setAdapter(watcherAdapter);
            } catch (JSONException e) {
                Utility.showLog("Error", "" + e);
            }
        }

    }

    @Override
    public void onRefresh() {
        initUI();
    }
}
