package com.piestack.ongoza.fragments.steps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.piestack.ongoza.R;
import com.piestack.ongoza.app.Config;
import com.piestack.ongoza.app.MyApplication;
import com.piestack.ongoza.models.data.DataResponse;
import com.piestack.ongoza.models.data.Partner;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.models.data.ReportResponse;
import com.piestack.ongoza.models.data.SupportTheme;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Request;


public class OneFragmentLocal extends Fragment {
    private  Unbinder unbinder;
    private String TAG = OneFragmentLocal.class.getSimpleName();
    private  String responses;
    private Realm realm = null;

    private List<Partner> partners = null;
    private List<SupportTheme> supportThemes = null;

    @BindView(R.id.progressBar2)
    ProgressBar progressBar;

    @BindView(R.id.spinner)
    Spinner sPartners;
    @BindView(R.id.spinner2)
    Spinner sTheme;
    @BindView(R.id.next_one_weekly)
    Button button;
    private View view;


    public OneFragmentLocal() {
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
        view = inflater.inflate(R.layout.fragment_one, container, false);
        unbinder = ButterKnife.bind(this, view);

        getActivity().setTitle("Create a new BDA");
        //getActivity().getActionBar().setSubtitle();

        OkHttpUtils
                .get()//
                .url(Config.dataUrl)//
                .id(101)
                .build()//
                .execute(new MyStringCallback());


        return view;
    }


    @OnClick(R.id.next_one_weekly)
    public void next(){

        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if(realm.where(ReportResponse.class).findFirst() == null) {
                        realm.insert(new ReportResponse());
                    }
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }

        Partner partner = partners.get(sPartners.getSelectedItemPosition());
        SupportTheme supportTheme = supportThemes.get(sTheme.getSelectedItemPosition());

        final Report report = new Report();

        report.setPId(partner.getPId());
        report.setExchange(false);
        report.setCountyId(MyApplication.getInstance().getPrefManager().getUser().getCounty_id().toString());
        report.setSId(supportTheme.getSId());

        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ReportResponse sortedResponses = realm.where(ReportResponse.class).findFirst();
                    sortedResponses.getReport().add(report);
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }

        //Bundle bundle = new Bundle();
        //bundle.putParcelable("",Parcels.wrap(reportResponse));
        Fragment fragment  = new TwoFragment();
        changeFragmentProcess(fragment);
    }

    private void changeFragmentProcess(Fragment fragment){

        getActivity().getSupportFragmentManager().beginTransaction()
                /*.setCustomAnimations(R.anim.trans_left_in,
                        R.anim.trans_left_out, R.anim.trans_right_in,R.anim.trans_right_out)*/
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();

    }

    /**
     * Adding spinner data
     * */
    private void populateSpinnerPartners() {
        List<String> lables = new ArrayList<String>();


        List<Partner> filtered = new ArrayList<>();
        if(partners != null) {
            for (Partner partner : partners) {
                if (Integer.valueOf(partner.getCountyId()) == MyApplication.getInstance().getPrefManager().getUser().getCounty_id()){
                    filtered.add(partner);
                }
            }
        }

        if(filtered.isEmpty()) {
            for (int i = 0; i < partners.size(); i++) {
                lables.add(partners.get(i).getPName());
            }
        }else{
            for (int i = 0; i < filtered.size(); i++) {
                lables.add(filtered.get(i).getPName());
            }
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
                sPartners.setAdapter(spinnerAdapter);
            }
        });
    }

    private void populateSpinnerSupportTheme() {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < supportThemes.size(); i++) {
            lables.add(supportThemes.get(i).getSName());
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
                sTheme.setAdapter(spinnerAdapter);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

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
            if(view != null){
            progressBar.setVisibility(View.INVISIBLE);
            e.printStackTrace();
            String error = e.getMessage() == null ? "Null" : e.getMessage();
            Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onResponse(String response, int id)
        {
            if(view !=null){

                progressBar.setVisibility(View.INVISIBLE);

            button.setEnabled(true);
            responses = response;
            Log.e(TAG, "onResponse：complete"+responses);

            if(isJSONValid(responses)){
                Gson gson = new Gson();
                DataResponse dataResponse =  gson.fromJson(responses,DataResponse.class);
                partners = dataResponse.getPartner();
                populateSpinnerPartners();
                supportThemes = dataResponse.getSupportTheme();
                populateSpinnerSupportTheme();

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
                    if(realm != null) {
                        realm.close();
                    }
                }
            }else{
                Toast.makeText(getActivity(), "Please try again later", Toast.LENGTH_LONG).show();
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
    public boolean isJSONValid(String test){
        try{
            Gson gson = new Gson();
            gson.fromJson(test, DataResponse.class);
            return  true;
        }catch (JsonSyntaxException ex){
            return false;
        }
    }

}
/*
*
*
*
* String id = Integer.toString(MyApplication.getInstance().getPrefManager().getUser().getCounty_id());
        RequestBody formBody = new FormBody.Builder()
                .add("county_id", id)
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Config.partnersUrl)
                .post(formBody)
                .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    L.e(TAG, e.getMessage());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                    General.backgroundThreadShortToast(getActivity(), "Check your internet connection");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    responses = response.body().string();
                    L.json(TAG,responses);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                    Gson gson = new Gson();
                    PartnerResponse loginResponse =  gson.fromJson(responses,PartnerResponse.class);
                    List<Partner> partners = loginResponse.getPartner();
                    populateSpinnerPartners(partners);

                }
            });
            */