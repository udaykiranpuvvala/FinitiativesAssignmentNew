package com.uday.finitiativesassignment.utilites;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.uday.finitiativesassignment.interfaces.IParseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class ServerResponse {
    private int requestCode;
    private RequestQueue requestQueue;

    public void serviceRequest(Context mContext, final String url, final JSONObject params,
                               final IParseListener iParseListener, final int requestCode) {
        this.requestCode = requestCode;

        Utility.showLog("Params is", ""+params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Utility.showLog("response is", ""+response);
                iParseListener.SuccessResponse(""+response, requestCode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.showLog("Error is", "" + error);
                iParseListener.ErrorResponse(error, requestCode);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext);
        }
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void serviceRequestGet(Context context,String url,final IParseListener iParseListener, final int requestCode) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utility.showLog("response is", ""+response);
                        iParseListener.SuccessResponse(""+response, requestCode);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.showLog("Error is", "" + error);
                iParseListener.ErrorResponse(error, requestCode);
            }
        });
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjReq);
    }


    public void serviceRequestGetJSONArray(Context context,String url,final IParseListener iParseListener, final int requestCode) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Utility.showLog("response is", ""+response);
                        iParseListener.SuccessResponse(""+response, requestCode);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Utility.showLog("Error is", "" + error);
                iParseListener.ErrorResponse(error, requestCode);
            }
        });
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }
}
