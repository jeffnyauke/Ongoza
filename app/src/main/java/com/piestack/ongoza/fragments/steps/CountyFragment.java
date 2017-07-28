package com.piestack.ongoza.fragments.steps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piestack.ongoza.R;
import com.piestack.ongoza.app.Config;
import com.piestack.ongoza.app.MyApplication;
import com.piestack.ongoza.models.data.County;
import com.piestack.ongoza.models.data.CountyResponse;
import com.piestack.ongoza.models.data.DataResponse;
import com.piestack.ongoza.models.data.Partner;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.models.data.ReportResponse;
import com.piestack.ongoza.models.data.SubSupporttheme;
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


public class CountyFragment extends Fragment {
    private  Unbinder unbinder;
    private String TAG = CountyFragment.class.getSimpleName();
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;
    private View view;

    private CountyResponse countyResponse;
    List<County> counties = new ArrayList<>();
    List<County> fcounties = new ArrayList<>();


    @BindView(R.id.spinnerCounty)
    Spinner sCounties;
    @BindView(R.id.spinnerPartner)
    Spinner sPartners;
    @BindView(R.id.spinnerType)
    Spinner sType;
    @BindView(R.id.next_one_weekly)
    Button button;

    private String county_id;
    private String county_name;

    Realm realm = null;
    private List<Partner> partners = null;
    private List<Partner> partnersF = new ArrayList<>();


    public CountyFragment() {
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
        view = inflater.inflate(R.layout.fragment_county, container, false);
        unbinder = ButterKnife.bind(this, view);

        getActivity().setTitle("Create a new BDA");
        //getActivity().getActionBar().setSubtitle();

        county_id = MyApplication.getInstance().getPrefManager().getUser().getCounty_id().toString();
        populateSpinnerType();
        OkHttpUtils
                .get()//
                .url(Config.dataUrl)//
                .id(101)
                .build()//
                .execute(new MyStringCallback());

        sCounties.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                County county = fcounties.get(sCounties.getSelectedItemPosition());
                county_id = county.getCountyId();
                populateSpinnerPartners();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                County county = fcounties.get(sCounties.getSelectedItemPosition());
                county_id = county.getCountyId();
                populateSpinnerPartners();

            }
        });

        sType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    //sPartners.setSelection();
                    populateSpinnerCountiesLocal();
                }else{
                    populateSpinnerCounties();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        Partner partner = partnersF.get(sPartners.getSelectedItemPosition());
        County county = fcounties.get(sCounties.getSelectedItemPosition());

        final Report report = new Report();

        report.setPId(partner.getPId());
        report.setPName(partner.getPName());
        if(sType.getSelectedItemPosition() == 0) {
            report.setExchange(false);
            report.setCountyId(county.getCountyId());
            report.setCountyName(county.getCountyName());
        }else{
            report.setExchange(true);
            report.setCountyId(county.getCountyId());
            report.setCountyName(county.getCountyName());
        }


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
    private void populateSpinnerCounties() {
        fcounties.clear();
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < counties.size(); i++) {
            if (!counties.get(i).getCountyId().equals(MyApplication.getInstance().getPrefManager().getUser().getCounty_id().toString())) {
                lables.add(counties.get(i).getCountyName());
                fcounties.add(counties.get(i));
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
                sCounties.setAdapter(spinnerAdapter);
            }
        });
    }

    private void populateSpinnerCountiesLocal() {
        fcounties.clear();
        if(counties!=null) {
            List<String> lables = new ArrayList<String>();

            for (int i = 0; i < counties.size(); i++) {
                if (counties.get(i).getCountyId().equals(MyApplication.getInstance().getPrefManager().getUser().getCounty_id().toString())) {
                    lables.add(counties.get(i).getCountyName());

                    fcounties.add(counties.get(i));
                    Log.e(counties.get(i).getCountyName(),MyApplication.getInstance().getPrefManager().getUser().getCounty_id().toString());
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
                    sCounties.setAdapter(spinnerAdapter);
                }
            });
        }
    }

    /**
     * Adding spinner data
     * */
    private void populateSpinnerType() {
        List<String> lables = new ArrayList<String>();
        lables.add("Local");
        lables.add("Exchange");


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
                sType.setAdapter(spinnerAdapter);
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
            if(view != null) {
                progressBar.setVisibility(View.INVISIBLE);
                e.printStackTrace();
                String error = e.getMessage() == null ? "Null" : e.getMessage();
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onResponse(String response, int id)
        {
            if (view != null) {
                progressBar.setVisibility(View.INVISIBLE);
                button.setEnabled(true);
                String responses = response;
                Log.e(TAG, "onResponse：complete" + responses);

                Gson gson = new Gson();
                DataResponse dataResponse = gson.fromJson(responses, DataResponse.class);
                counties = dataResponse.getCounty();
                populateSpinnerCountiesLocal();
                partners = dataResponse.getPartner();
                populateSpinnerPartners();

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
            }


        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.e(TAG, "inProgress:" + progress);
            //mProgressBar.setProgress((int) (100 * progress));
        }
    }


    /**
     * Adding spinner data
     * */
    private void populateSpinnerPartners() {
        List<Partner> filtered = new ArrayList<>();
        List<String> lablesP = new ArrayList<String>();
        partnersF.clear();


        if(partners != null) {
            for (Partner partner : partners) {
                if (Integer.valueOf(partner.getCountyId()) == Integer.valueOf(county_id == null ? MyApplication.getInstance().getPrefManager().getUser().getCounty_id().toString() : county_id)){
                    filtered.add(partner);
                }
            }
        }

        if(filtered.isEmpty()) {
            for (int i = 0; i < partners.size(); i++) {
                lablesP.add(partners.get(i).getPName());
                partnersF.add(partners.get(i));
            }
        }else{
            for (int i = 0; i < filtered.size(); i++) {
                lablesP.add(filtered.get(i).getPName());
                partnersF.add(filtered.get(i));
            }
        }


        // Creating adapter for spinner
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lablesP);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinne


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sPartners.setAdapter(spinnerAdapter);
            }
        });
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