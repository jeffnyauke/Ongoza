package com.piestack.ongoza.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.piestack.ongoza.R;
import com.piestack.ongoza.adapters.ReportsAdapter;
import com.piestack.ongoza.app.Config;
import com.piestack.ongoza.app.MyApplication;
import com.piestack.ongoza.fragments.steps.InternalProcessFragment;
import com.piestack.ongoza.models.LoginResponse;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.models.data.ReportResponse;
import com.piestack.ongoza.utils.General;
import com.piestack.ongoza.utils.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.zhy.http.okhttp.log.LoggerInterceptor.TAG;

/**
 * Created by Jeffrey Nyauke on 3/23/2017.
 */

public class ReportFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener, ReportsAdapter.MessageAdapterListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private static final String TAG = "MainActivity";
    private List<Report> messages = new ArrayList<>();

    @BindView(R.id.textview) TextView mTv;
    private ImageView mImageView;
    private ProgressDialog mProgressBar;
    private Unbinder unbinder;
    private ReportsAdapter mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private View view;


    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            mTv.setText("loading...");
        }


        @Override
        public void onAfter(int id) {
            //mTv.setText("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            if (view != null)
            {
            swipeRefreshLayout.setRefreshing(false);
            e.printStackTrace();
        String error = e.getMessage() == null ? "Null" : e.getMessage();
            mTv.setText("onError:"+error);
    }
        }

        @Override
        public void onResponse(String response, int id)
        {
            if(view!=null) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e(TAG, "onResponse：complete");
                Log.e(TAG, "onResponse：complete" + response);
                //mTv.setText("onResponse:" + response);
                mTv.setVisibility(View.GONE);
                String responzes = response;
                if (isJSONValid(responzes)) {
                    Gson gson = new Gson();
                    //List<Report> reports =  gson.fromJson(response,new TypeToken<List<Report>>(){}.getType());
                    ReportResponse reportResponse = gson.fromJson(response, ReportResponse.class);
                    List<Report> reports = reportResponse.getReport();

                    // clear the inbox
                    messages.clear();

                    // add all the messages
                    // messages.addAll(response.body());

                    // TODO - avoid looping
                    // the loop was performed to add colors to each message
                    for (Report message : reports) {
                        // generate a random color
                        message.setColor(getRandomMaterialColor("400"));
                        messages.add(message);
                    }
                } else {
                    mTv.setText("Please try again later");
                }


                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

                switch (id) {
                    case 100:
                        Toast.makeText(getActivity(), "http", Toast.LENGTH_SHORT).show();
                        break;
                    case 101:
                        Toast.makeText(getActivity(), "https", Toast.LENGTH_SHORT).show();
                        break;
                }
            }


        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.e(TAG, "inProgress:" + progress);
            mProgressBar.setProgress((int) (100 * progress));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        view=inflater.inflate(R.layout.fragment_reports,container,false);
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
    }

    public void init(){

        swipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new ReportsAdapter(getActivity(), messages, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        getActivity().setTitle("Submitted reports");

        actionModeCallback = new ActionModeCallback();

        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        networkCall();
                    }
                }
        );

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

        swipeRefreshLayout.setRefreshing(true);
        /*OkHttpUtils
                .get()//
                .url(Config.reporstUrl)//
                .id(101)
                .build()//
                .execute(new MyStringCallback());*/

        RequestBody formBody = new FormBody.Builder()
                .add("id", MyApplication.getInstance().getPrefManager().getUser().getId().toString())
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Config.reporstUrl)
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e(TAG, e.getMessage());


                //progressBar.setVisibility(View.INVISIBLE);

                General.backgroundThreadShortToast(getActivity(), "Check your internet connection");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //mTv.setText("onResponse:" + response);

                if(view!=null) {

                    String responzes = response.body().string();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                            mTv.setVisibility(View.GONE);
                        }
                    });


                    Log.e(TAG, "onResponse：complete");
                    Log.e(TAG, "onResponse：complete" + responzes);
                    if (isJSONValid(responzes)) {
                        Gson gson = new Gson();
                        //List<Report> reports =  gson.fromJson(response,new TypeToken<List<Report>>(){}.getType());
                        final ReportResponse reportResponse = gson.fromJson(responzes, ReportResponse.class);
                        List<Report> reports = reportResponse.getReport();

                        Realm realm = null;

                        try { // I could use try-with-resources here
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(reportResponse);
                                }
                            });
                        } finally {
                            if (realm != null) {
                                realm.close();
                            }
                        }

                        // clear the inbox
                        messages.clear();

                        // add all the messages
                        // messages.addAll(response.body());

                        // TODO - avoid looping
                        // the loop was performed to add colors to each message
                        for (Report message : reports) {
                            // generate a random color
                            message.setColor(getRandomMaterialColor("400"));
                            messages.add(message);
                        }
                    } else {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                mTv.setText("Please try again later");
                            }
                        });

                    }


                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });

                }


            }
        });



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
        if (actionMode == null) {
            //actionMode = startSupportActionMode(actionModeCallback);
        }

        toggleSelection(position);
    }

    @Override
    public void onIconImportantClicked(int position) {
        // Star icon is clicked,
        // mark the message as important
        Report message = messages.get(position);
        message.setImportant(!message.isImportant());
        messages.set(position, message);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageRowClicked(int position) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated
        if (mAdapter.getSelectedItemCount() > 0) {
            enableActionMode(position);
        } else {
            // read the message which removes bold from the row
            Report message = messages.get(position);
            message.setRead(true);
            messages.set(position, message);
            mAdapter.notifyDataSetChanged();

            Fragment fragment  = new ReportDetailBuilder(String.valueOf(position)).build();
            getActivity().getSupportFragmentManager().beginTransaction()
                /*.setCustomAnimations(R.anim.trans_left_in,
                        R.anim.trans_left_out, R.anim.trans_right_in,R.anim.trans_right_out)*/
                    .replace(R.id.content, fragment)
                    .addToBackStack(null)
                    .commit();

            Toast.makeText(getActivity(), "Read: " + message.getPName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode
        enableActionMode(position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            //actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
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
}
