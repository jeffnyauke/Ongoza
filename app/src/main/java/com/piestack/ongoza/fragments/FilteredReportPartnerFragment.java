package com.piestack.ongoza.fragments;

import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.piestack.ongoza.R;
import com.piestack.ongoza.adapters.FilteredReportsAdapter;
import com.piestack.ongoza.adapters.FilteredReportsPartnerAdapter;
import com.piestack.ongoza.models.Summary;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.models.data.ReportResponse;
import com.piestack.ongoza.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Jeffrey Nyauke on 3/23/2017.
 */
@FragmentWithArgs
public class FilteredReportPartnerFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener, FilteredReportsPartnerAdapter.MessageAdapterListener, SearchView.OnQueryTextListener{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private static final String TAG = "MainActivity";
    private List<Report> messages = new ArrayList<>();
    private List<Report> messagesQ = new ArrayList<>();
    private List<Summary> summaries = new ArrayList<>();

    private ImageView mImageView;
    private ProgressDialog mProgressBar;
    private Unbinder unbinder;
    private FilteredReportsPartnerAdapter mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private View view;

    @Arg
    String  responses;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.filtered_fragment_partner_reports,container,false);
        unbinder = ButterKnife.bind(this, view);

        init();
        mProgressBar = new ProgressDialog(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        networkCall();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        setHasOptionsMenu(true);
    }

    public void init(){

        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new FilteredReportsPartnerAdapter(getActivity(), summaries, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        getActivity().setTitle("Filtered reports");

        actionModeCallback = new ActionModeCallback();

        // show loader and fetch messages
       /* swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        networkCall();
                    }
                }
        );*/

    }

    /**
     * chooses a random color from array.xml
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getActivity().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    private void networkCall(){

        swipeRefreshLayout.setRefreshing(false);

        if(responses != null) {

            // clear the inbox
            messages.clear();
            Gson gson = new Gson();
            final ReportResponse reportResponse = gson.fromJson(responses, ReportResponse.class);
            List<Report> reports = reportResponse.getReport();

            for(Report message : reports){

                Log.e("Report", message.getSId()+" "+message.getSName());

                if(summaries.isEmpty() || summaries == null){
                    Summary summary = new Summary();
                    summary.setId(message.getSId());
                    summary.setStheme(message.getSName());
                    summary.setPartnerName(message.getPName());
                    summaries.add(summary);
                    Log.e("Summary First" , message.getSId()+" "+message.getSName());

                }else{
                    Boolean bool = true;
                    for(Summary summary1: summaries){
                        Log.e("Loops", "count");
                        if(summary1.getPartnerName().equals(message.getPName()) ) {
                            Log.e("Boolean", "false");
                            bool = false;
                        }
                    }

                if(bool) {
                    Summary summary = new Summary();
                    summary.setId(message.getSId());
                    summary.setStheme(message.getSName());
                    summary.setPartnerName(message.getPName());
                    summaries.add(summary);
                    Log.e("Summary Latter", message.getSId() + " " + message.getSName());
                }
                }

            }


            for (Summary summary1 : summaries){
                double bds= 0;
                double travel = 0;
                double stheme = 0;
                for(Report report : reports){
                    if(summary1.getStheme().equals(report.getSName())){
                        bds+=Double.valueOf(StringUtils.isNullorEmpty(report.getHoursIp()) ? "0": report.getHoursIp());
                        travel+=Double.valueOf(StringUtils.isNullorEmpty(report.getTravel()) ? "0": report.getTravel());
                        stheme+=1;
                    }
                }
                summary1.setHrs_bds(String.valueOf(bds));
                summary1.setHrs_travel(String.valueOf(travel));
                summary1.setStheme(String.valueOf(stheme));
            }

            double bds= 0;
            double travel = 0;
            for (Summary summary1 : summaries){
                bds+=Double.valueOf(StringUtils.isNullorEmpty(summary1.getHrs_bds()) ? "0": summary1.getHrs_bds());
                travel+=Double.valueOf(StringUtils.isNullorEmpty(summary1.getHrs_travel()) ? "0": summary1.getHrs_travel());
            }

            Summary summary = new Summary();
            summary.setId("10101");
            summary.setStheme("-");
            summary.setHrs_bds(String.valueOf(bds));
            summary.setHrs_travel(String.valueOf(travel));
            summary.setPartnerName("Total");

            summaries.add(summary);

        }




    }


    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        view = null;
    }

    @Override
    public void onRefresh() {
        // swipe refresh is performed, fetch the messages again
        networkCall();
    }

    @Override
    public void onIconClicked(int position) {
        /*if (actionMode == null) {
            //actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);*/
    }

    @Override
    public void onIconImportantClicked(int position) {
        /*// Star icon is clicked,
        // mark the message as important
        Report message = messages.get(position);
        message.setImportant(!message.isImportant());
        messages.set(position, message);
        mAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onMessageRowClicked(int position) {
     /*   // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            // read the message which removes bold from the row
            Report message = messages.get(position);
            message.setRead(true);
            messages.set(position, message);
            mAdapter.notifyDataSetChanged();



            //Toast.makeText(getActivity(), "Read: " + message.getPName(), Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
        //enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            //actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
       /* mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }*/
    }


    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // disable swipe refresh if action mode is enabled
            swipeRefreshLayout.setEnabled(false);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // delete all the selected messages
                    deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            swipeRefreshLayout.setEnabled(true);
            actionMode = null;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // deleting the messages from recycler view
    private void deleteMessages() {
        mAdapter.resetAnimationIndex();
        List<Integer> selectedItemPositions =
                mAdapter.getSelectedItems();
        for (int i = selectedItemPositions.size() - 1; i >= 0; i--) {
            mAdapter.removeData(selectedItemPositions.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }

    public boolean isJSONValid(String test){
        try{
            Gson gson = new Gson();
            gson.fromJson(test, ReportResponse.class);
            return  true;
        }catch (JsonSyntaxException ex){
            return false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*inflater.inflate(R.menu.search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);*/
    }

    @Override
    public boolean onQueryTextChange(String query) {
        /*messages = messagesQ;
        final List<Report> filteredModelList = filter(messages, query);
        L.e("full",messages.toString());
        L.e("filtered",filteredModelList.toString());
        mAdapter.animateTo(filteredModelList);
        recyclerView.scrollToPosition(0);*/
        return true;
    }

    private List<Report> filter(List<Report> reports, String query) {
        query = query.toLowerCase();

        final List<Report> filteredModelList = new ArrayList<>();
        for (Report report : reports) {
            final String county = report.getCountyName().toLowerCase();
            final String partner = report.getPName().toLowerCase();
            final String sname = report.getSName().toLowerCase();
            final String subname = report.getSubName().toLowerCase();
            if (county.contains(query) || partner.contains(query) || sname.contains(query) || subname.contains(query)) {
                if(!filteredModelList.contains(report)){
                    filteredModelList.add(report);
                }
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
