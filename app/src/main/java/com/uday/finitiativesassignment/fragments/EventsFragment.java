package com.uday.finitiativesassignment.fragments;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.uday.finitiativesassignment.R;
import com.uday.finitiativesassignment.activities.MainActivity;
import com.uday.finitiativesassignment.adapters.EventsAdapter;
import com.uday.finitiativesassignment.entities.EventsEntity;
import com.uday.finitiativesassignment.interfaces.IParseListener;
import com.uday.finitiativesassignment.utilites.Constants;
import com.uday.finitiativesassignment.utilites.FinitDatabase;
import com.uday.finitiativesassignment.utilites.ServerResponse;
import com.uday.finitiativesassignment.utilites.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventsFragment extends Fragment implements View.OnClickListener, IParseListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "EventsFragment";
    private MainActivity mParent;
    private View rootView;
    Button btnStartDate;
    RecyclerView recyclerViewEvents;
    String startDate = "", endDate = "";
    ArrayList<EventsEntity> eventsModelArrayList = new ArrayList<>();

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
        rootView = inflater.inflate(R.layout.fragment_events, container, false);
        initUI();
        return rootView;
    }

    private void initUI() {
        btnStartDate = rootView.findViewById(R.id.btnStartDate);
        recyclerViewEvents = rootView.findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(mParent));
        recyclerViewEvents.setHasFixedSize(true);

        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        btnStartDate.setOnClickListener(this);

        finitDatabase = Room.databaseBuilder(
                mParent.getApplicationContext(), FinitDatabase.class, "production")
                .allowMainThreadQueries()
                .build();

        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartDate: {
                showStartDatePicker();
                // endDate = "";
                break;
            }

            default:
                break;
        }
    }

    private void callAPIForEvents() {
        if (Utility.isNetworkAvailable(mParent)) {

            Utility.showLoadingDialog(mParent, "Loading...", false);

            ServerResponse serverResponse = new ServerResponse();
            serverResponse.serviceRequestGet(mParent, Constants.EVENTS_URL + startDate + "&to_date=" + endDate, this, Constants.REQUESTCODE_EVENTS);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(mParent, "As their is No Internet. Application is Running on Offline Mode", Toast.LENGTH_LONG).show();

            List<EventsEntity> eventsEntityArrayList = finitDatabase.daoAccess().fetchAllEventsData();
            setAdapterToRecyclerView((ArrayList<EventsEntity>) eventsEntityArrayList);
        }

    }

    private void setAdapterToRecyclerView(ArrayList<EventsEntity> eventsEntityArrayList) {
        EventsAdapter eventsAdapter = new EventsAdapter(mParent, eventsEntityArrayList);
        recyclerViewEvents.setAdapter(eventsAdapter);
    }

    @Override
    public void ErrorResponse(VolleyError volleyError, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);
        Utility.hideLoadingDialog();
        Utility.alertDialog(mParent, mParent.getResources().getString(R.string.something_went_wrong), null);
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        swipeRefreshLayout.setRefreshing(false);
        Utility.hideLoadingDialog();
        if (requestCode == Constants.REQUESTCODE_EVENTS) {

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.optJSONArray("data");

                if (jsonArray != null) {
                    eventsModelArrayList.clear();
                    finitDatabase.daoAccess().deleteEventsData();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectData = jsonArray.optJSONObject(i);

                        EventsEntity eventsEntity = new EventsEntity();

                        eventsEntity.setType(jsonObjectData.optString("type"));
                        eventsEntity.setTitle(jsonObjectData.optString("title"));
                        eventsEntity.setDescription(jsonObjectData.optString("description"));
                        eventsEntity.setOrganizer(jsonObjectData.optString("organizer"));
                        eventsEntity.setStart_date(jsonObjectData.optString("start_date"));
                        eventsEntity.setEnd_date(jsonObjectData.optString("end_date"));
                        eventsEntity.setWebsite(jsonObjectData.optString("website"));
                        eventsEntity.setEmail(jsonObjectData.optString("email"));
                        eventsEntity.setVenue(jsonObjectData.optString("venue"));
                        eventsEntity.setAddress(jsonObjectData.optString("address"));
                        eventsEntity.setCity(jsonObjectData.optString("city"));
                        eventsEntity.setCountry(jsonObjectData.optString("country"));
                        eventsEntity.setScreenshot(jsonObjectData.optString("screenshot"));

                        eventsModelArrayList.add(eventsEntity);

                    }
                    finitDatabase.daoAccess().insertEvents(eventsModelArrayList);
                    setAdapterToRecyclerView((ArrayList<EventsEntity>) eventsModelArrayList);
                } else {
                    Utility.alertDialog(mParent, "No Data Available", null);
                }

            } catch (JSONException e) {
                Utility.showLog("Error", "" + e);
            }
        }

    }


    private void showStartDatePicker() {
        Calendar mCalender = Calendar.getInstance();
        int c_date = mCalender.get(Calendar.DAY_OF_MONTH);
        int c_month = mCalender.get(Calendar.MONTH);
        int c_year = mCalender.get(Calendar.YEAR);
        if (Utility.isMarshmallowOS()) {

            final DatePickerDialog dpd = new DatePickerDialog(mParent, android.R.style.ThemeOverlay_Material_Dialog,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            btnStartDate.setText("DATE : " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            startDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            endDate = year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth + 5);


                            callAPIForEvents();

                        }
                    }, c_year, c_month, c_date);
            dpd.show();
        } else {
            final DatePickerDialog dpd = new DatePickerDialog(mParent,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            btnStartDate.setText("DATE : " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            startDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            endDate = year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth + 5);


                            callAPIForEvents();


                        }
                    }, c_year, c_month, c_date);
            dpd.show();
        }
    }

    @Override
    public void onRefresh() {
        initUI();
    }

   /*
        calendarStart = Calendar.getInstance();
                            calendarStart.set(year, monthOfYear, dayOfMonth);
    private void showEndDatePicker() {
        Calendar mCalender = Calendar.getInstance();
        int c_date = mCalender.get(Calendar.DAY_OF_MONTH);
        int c_month = mCalender.get(Calendar.MONTH);
        int c_year = mCalender.get(Calendar.YEAR);
        if (Utility.isMarshmallowOS()) {

            final DatePickerDialog dpd = new DatePickerDialog(mParent, android.R.style.ThemeOverlay_Material_Dialog,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            btnEndDate.setText("DATE : " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            endDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;


                            callAPIForEvents();


                        }
                    }, c_year, c_month, c_date);
            dpd.getDatePicker().setMinDate(calendarStart.getTimeInMillis());
            dpd.show();
        } else {
            final DatePickerDialog dpd = new DatePickerDialog(mParent,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            btnEndDate.setText("DATE : " + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            endDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            callAPIForEvents();

                        }
                    }, c_year, c_month, c_date);
            dpd.getDatePicker().setMinDate(calendarStart.getTimeInMillis());
            dpd.show();
        }

    }*/


}
