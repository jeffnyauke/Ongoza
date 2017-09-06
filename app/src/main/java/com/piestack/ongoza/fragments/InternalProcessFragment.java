package com.piestack.ongoza.fragments;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.piestack.ongoza.R;
import com.piestack.ongoza.app.Config;
import com.piestack.ongoza.app.MyApplication;
import com.piestack.ongoza.fragments.steps.ThanksFragmentBuilder;
import com.piestack.ongoza.models.PostResponse;
import com.piestack.ongoza.models.data.DataResponse;
import com.piestack.ongoza.models.data.InternalProcess;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.models.data.ReportResponse;
import com.piestack.ongoza.utils.General;
import com.piestack.ongoza.utils.L;
import com.piestack.ongoza.utils.Prefs;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InternalProcessFragment extends Fragment {
    private Unbinder unbinder;

    @BindView(R.id.editText)
    EditText hours;
    @BindView(R.id.editText3)
    EditText eneeds;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;
    @BindView(R.id.latitude)
    TextView tvLatitude;
    @BindView(R.id.longitude)
    TextView tvLongitude;
    @BindView(R.id.submit_weekly)
    Button button;
    @BindView(R.id.spinner)
    Spinner sIP;
    private View view;
    //@BindView(R.id.appbar)
   // AppBarLayout appBarLayout;

    private ReportResponse sortedResponses = null;
    private List<InternalProcess> internalProcesses = null;
    private Report report = null;
    private String latitude;
    private String longitude;

    private  String TAG = InternalProcessFragment.class.getSimpleName();


    Realm realm = null;


    public InternalProcessFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_ip, container, false);
        unbinder = ButterKnife.bind(this, view);

        button.setEnabled(false);

        getActivity().setTitle("Create Internal Process");
        //((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Record the various internal processes of the business");
        //((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.date_icon);
        AppBarLayout layout =(AppBarLayout) getActivity().findViewById(R.id.appbar);
        //getActivity().getActionBar().setSubtitle();

        latitude = Prefs.with(getContext()).getLatitude();
        longitude = Prefs.with(getContext()).getLongitude();
        tvLatitude.setText("Latitude: " + latitude);
        tvLongitude.setText("Longitude: " +longitude);

        OkHttpUtils
                .get()//
                .url(Config.dataUrl)//
                .id(101)
                .build()//
                .execute(new MyStringCallback());
        return view;
    }

    @OnClick(R.id.submit_weekly)
    public void submit(){

        progressBar.setVisibility(View.VISIBLE);
        InternalProcess internalProcess = internalProcesses.get(sIP.getSelectedItemPosition());
        final String hourz = hours.getText().toString().trim();
        final String e_needs = eneeds.getText().toString().trim();

                    RequestBody formBody = new FormBody.Builder()
                            .add("id", MyApplication.getInstance().getPrefManager().getUser().getId().toString())
                            .add("oip_id",internalProcess.getOipId())
                            .add("e_needs",e_needs)
                            .add("hours_ip",hourz)
                            .add("longitude",longitude)
                            .add("latitude",latitude)
                            .build();

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(Config.oipUrl)
                            .post(formBody)
                            .build();


                        client.newCall(request).enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                L.e(TAG, e.getMessage());


                                //progressBar.setVisibility(View.INVISIBLE);
                                errorPosting();
                                General.backgroundThreadShortToast(getActivity(), "Check your internet connection");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                String responses = response.body().string();
                                L.e(TAG,responses);
                                L.json(TAG,responses);

                                //progressBar.setVisibility(View.INVISIBLE);

                                if(isJSONValid(responses)){
                                Gson gson = new Gson();
                                PostResponse postResponse =  gson.fromJson(responses,PostResponse.class);

                                if(!postResponse.getError()) {
                                    //Toast.makeText(getActivity(), "Posted succesfully!", Toast.LENGTH_SHORT).show();
                                    //General.backgroundThreadShortToast(getActivity(), "Posted succesfully!");
                                    successPosting();
                                }else{
                                    errorPosting();
                                            //progressBar.setVisibility(View.INVISIBLE);

                                    //Toast.makeText(getActivity(), "Submission unsuccessful", Toast.LENGTH_SHORT).show();
                                    L.e(TAG, postResponse.getErrorMsg());
                                    //General.backgroundThreadShortToast(getActivity(), "Submission unsuccessful");
                                }

                                }else{
                                    errorPosting();
                                }



                            }
                        });









    }

    private void changeFragmentProcess(Fragment fragment){
        Fragment oldFragment = new WeeklyFragment();
        getActivity().getSupportFragmentManager().popBackStack("Home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getSupportFragmentManager().beginTransaction()
                /*.setCustomAnimations(R.anim.trans_left_in,
                        R.anim.trans_left_out, R.anim.trans_right_in,R.anim.trans_right_out)*/
                .remove(oldFragment)
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }



    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        view = null;
    }

    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request, int id)
        {
            button.setEnabled(false);
        }


        @Override
        public void onAfter(int id)
        {
            //mTv.setText("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {
            if(view !=null) {
                progressBar.setVisibility(View.INVISIBLE);
                e.printStackTrace();
                String error = e.getMessage() == null ? "Null" : e.getMessage();
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onResponse(String response, int id)
        {
            if(view != null) {
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                String responses = response;
                L.e(TAG, "onResponse：complete" + responses);
if(isJSONValidd(responses)) {
    Gson gson = new Gson();
    DataResponse dataResponse = gson.fromJson(responses, DataResponse.class);
    internalProcesses = dataResponse.getInternalProcess();
    populateSpinner();
    button.setEnabled(true);


    final DataResponse update = dataResponse;
    update.setId(1);

    try { // I could use try-with-resources here
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(update);
            }
        });
    } finally {
        if (realm != null) {
            realm.close();
        }
    }

}else {
    Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
}
            }

        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.e(TAG, "inProgress:" + progress);
            //mProgressBar.setProgress((int) (100 * progress));
        }
    }

    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < internalProcesses.size(); i++) {
            lables.add(internalProcesses.get(i).getOipName());
        }

        // Creating adapter for spinner
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sIP.setAdapter(spinnerAdapter);
            }
        });
    }

    private boolean isJSONValid(String test){
        try{
            Gson gson = new Gson();
            gson.fromJson(test, PostResponse.class);
            return  true;
        }catch (JsonSyntaxException ex){
            return false;
        }
    }
    private boolean isJSONValidd(String test){
        try{
            Gson gson = new Gson();
            gson.fromJson(test, DataResponse.class);
            return  true;
        }catch (JsonSyntaxException ex){
            return false;
        }
    }

    private void errorPosting(){
        Fragment fragment = new ThanksFragmentBuilder("Could not post your Internal Process right now! Will be saved as draft.").build();
        changeFragmentProcess(fragment);
    }

    private void successPosting(){
        Fragment fragment = new ThanksFragmentBuilder("Your Internal Process has been submitted successfully!").build();
        changeFragmentProcess(fragment);
    }
}
