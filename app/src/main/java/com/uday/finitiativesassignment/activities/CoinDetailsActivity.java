package com.uday.finitiativesassignment.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.uday.finitiativesassignment.R;
import com.uday.finitiativesassignment.interfaces.IParseListener;
import com.uday.finitiativesassignment.utilites.Constants;
import com.uday.finitiativesassignment.utilites.ServerResponse;
import com.uday.finitiativesassignment.utilites.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class CoinDetailsActivity extends AppCompatActivity implements IParseListener {

    TextView txtTitle, txtName, txtSymbol, txtTime, txtDescription;
    String coinsId;
    ImageView ivCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);

        getIntentData();
        initUI();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            coinsId = intent.getStringExtra("coinsId");
        }
    }

    private void initUI() {
        txtTitle = findViewById(R.id.txtTitle);
        txtName = findViewById(R.id.txtName);
        txtSymbol = findViewById(R.id.txtSymbol);
        txtTime = findViewById(R.id.txtTime);
        txtDescription = findViewById(R.id.txtDescription);
        ivCoin = findViewById(R.id.ivCoin);

        if (!Utility.isValueNullOrEmpty(coinsId))
            callAPIForCoinDetails();
        else
            Utility.alertDialog(this, "No Coin Data is Available", null);
    }

    private void callAPIForCoinDetails() {
        if (Utility.isNetworkAvailable(this)) {
            Utility.showLoadingDialog(this, "Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestGet(this, Constants.COINDETAILS_URL + coinsId, this, Constants.REQUESTCODE_COINDETAILS);

        } else {
            Utility.alertDialog(this, getString(R.string.please_check_internet), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }


    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        Utility.hideLoadingDialog();
        Utility.alertDialog(this, getString(R.string.something_went_wrong), null);
    }


    @Override
    public void SuccessResponse(String response, int requestCode) {
        Utility.hideLoadingDialog();
        if (requestCode == Constants.REQUESTCODE_COINDETAILS) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String id = jsonObject.optString("id");
                String symbol = jsonObject.optString("symbol");
                String name = jsonObject.optString("name");
                int time = jsonObject.optInt("block_time_in_minutes");

                JSONObject jsonObjectDesc = jsonObject.optJSONObject("description");
                String desc = jsonObjectDesc.optString("en");


                JSONObject jsonObjectImage = jsonObject.optJSONObject("image");
                String image = jsonObjectImage.optString("large");
                txtDescription.setText(desc);
                txtName.setText(name);
                txtTitle.setText(name);
                txtTime.setText(""+time);
                txtSymbol.setText(symbol);

                Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.logo)
                        .into(ivCoin);

            } catch (JSONException e) {
                Utility.showLog("Error", "" + e);
            }
        }

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
