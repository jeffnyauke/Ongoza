package com.piestack.ongoza.fragments.steps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.piestack.ongoza.R;
import com.piestack.ongoza.app.Config;
import com.piestack.ongoza.app.MyApplication;
import com.piestack.ongoza.fragments.WeeklyFragment;
import com.piestack.ongoza.models.PostResponse;
import com.piestack.ongoza.models.data.DataResponse;
import com.piestack.ongoza.models.data.InternalProcess;
import com.piestack.ongoza.models.data.Pd;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.models.data.ReportResponse;
import com.piestack.ongoza.utils.General;
import com.piestack.ongoza.utils.L;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FourFragment extends Fragment {
    private Unbinder unbinder;

    @BindView(R.id.editText)
    EditText travel;
    @BindView(R.id.editText2)
    EditText bds;
    @BindView(R.id.editText3)
    EditText eneeds;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private ReportResponse sortedResponses = null;
    private Report report = null;

    private  String TAG = FourFragment.class.getSimpleName();


    Realm realm = null;


    public FourFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_four, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.submit_weekly)
    public void submit(){

        progressBar.setVisibility(View.VISIBLE);
        final String hours = bds.getText().toString().trim();
        final String travels = travel.getText().toString().trim();
        final String e_needs = eneeds.getText().toString().trim();

        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    sortedResponses = realm.where(ReportResponse.class).findFirstAsync();
                    report = sortedResponses.getReport().last();
                    report.setTravel(travels);
                    report.setHoursIp(hours);
                    report.setE_needs(e_needs);
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }

        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    sortedResponses = realm.where(ReportResponse.class).findFirstAsync();
                    report = sortedResponses.getReport().last();

                    Toast.makeText(getActivity(), report.getCountyId()+" " +report.getPId() +" "+ report.getSId()
                            +report.getModalityId()+ report.getSubId() + report.getModeId()
                            +report.getOipId()+ report.getPdId()
                            +report.getTravel() + report.getE_needs()+report.getHoursIp(),Toast.LENGTH_SHORT).show();


                    RequestBody formBody = new FormBody.Builder()
                            .add("id", MyApplication.getInstance().getPrefManager().getUser().getId().toString())
                            .add("county_id",report.getCountyId())
                            .add("exchange",report.getExchange() ? String.valueOf(1):String.valueOf(0))
                            .add("modality_id",report.getModalityId())
                            .add("oip_id",report.getOipId())
                            .add("p_id",report.getPId())
                            .add("pd_id",report.getPdId())
                            .add("mode_id",report.getModeId())
                            .add("sub_id",report.getSubId())
                            .add("s_id",report.getSId())
                            .add("travel",report.getTravel())
                            .add("e_needs",report.getE_needs())
                            .add("hours_ip",report.getHoursIp())
                            .build();

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url(Config.postUrl)
                            .post(formBody)
                            .build();

                    try{
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
                                        successPosting();
                                        General.backgroundThreadShortToast(getActivity(), "Posted succesfully!");

                                    }else{

                                        //progressBar.setVisibility(View.INVISIBLE);
                                        errorPosting();
                                        //Toast.makeText(getActivity(), "Submission unsuccessful", Toast.LENGTH_SHORT).show();
                                        L.e(TAG, postResponse.getErrorMsg());
                                        General.backgroundThreadShortToast(getActivity(), "Submission unsuccessful");
                                    }
                                }else {
                                    errorPosting();
                                    General.backgroundThreadShortToast(getActivity(), "Submission unsuccessful");
                                }





                            }
                        });


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }

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
    }

    public boolean isJSONValid(String test){
        try{
            Gson gson = new Gson();
            gson.fromJson(test, PostResponse.class);
            return  true;
        }catch (JsonSyntaxException ex){
            return false;
        }
    }

    private void errorPosting(){
        Fragment fragment = new ThanksFragmentBuilder("Could not post your Report right now! Will be saved as draft.").build();
        changeFragmentProcess(fragment);
    }

    private void successPosting(){
        Fragment fragment = new ThanksFragmentBuilder("Your Report has been submitted successfully!").build();
        changeFragmentProcess(fragment);
    }
}
