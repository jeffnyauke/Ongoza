package com.piestack.ongoza.fragments.steps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.piestack.ongoza.fragments.WeeklyFragment;
import com.piestack.ongoza.models.PostResponse;
import com.piestack.ongoza.models.data.DataResponse;
import com.piestack.ongoza.models.data.InternalProcess;
import com.piestack.ongoza.models.data.Modality;
import com.piestack.ongoza.models.data.Pd;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.models.data.ReportResponse;
import com.piestack.ongoza.models.data.SubSupporttheme;
import com.piestack.ongoza.models.data.SupportMode;
import com.piestack.ongoza.models.data.SupportTheme;
import com.piestack.ongoza.utils.General;
import com.piestack.ongoza.utils.L;
import com.piestack.ongoza.utils.Prefs;
import com.piestack.ongoza.utils.StringUtils;

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

public class TwoFragment extends Fragment {

    private static final String ARGUMENT_NAME = "name";
    @BindView(R.id.spinnerSM)
    Spinner sSupportMode;
    @BindView(R.id.spinnerFOE)
    Spinner sFOE;
    @BindView(R.id.spinnerPD)
    Spinner sDelivering;
    @BindView(R.id.spinnerPP)
    Spinner sPP;
    @BindView(R.id.spinnerSupport)
    Spinner sSupport;
    @BindView(R.id.spinnerSubSupport)
    Spinner sSubSupport;
    @BindView(R.id.bdstype)
    TextView tvType;
    @BindView(R.id.county)
    TextView tvCounty;
    @BindView(R.id.partner)
    TextView tvPartner;
    @BindView(R.id.textViewPP)
    TextView tPP;
    @BindView(R.id.latitude)
    TextView tvLatitude;
    @BindView(R.id.longitude)
    TextView tvLongitude;
    @BindView(R.id.tvTravel)
    EditText travel;
    @BindView(R.id.tvBDS)
    EditText bds;
    @BindView(R.id.tvEM)
    EditText eneeds;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;
    Realm realm = null;
    private Unbinder unbinder;
    private ReportResponse sortedResponses = null;
    private Report report = null;
    private int timeBDS;
    private int timeTravel;
    private String latitude;
    private String longitude;
    private boolean sPPd = true;
    private String e_needs = "No emerging needs";

    private List<Modality> modalities = null;
    private List<SupportMode> supportModes = null;
    private List<Pd> pdList = null;
    private List<InternalProcess> internalProcesses = null;
    private List<SupportTheme> supportThemes = null;
    private List<SubSupporttheme> subThemes = null;

    private String TAG = FourFragment.class.getSimpleName();


    public TwoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        unbinder = ButterKnife.bind(this, view);

        sPP.setVisibility(View.GONE);
        tPP.setVisibility(View.GONE);


        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    DataResponse dataResponse = realm.where(DataResponse.class).findFirstAsync();

                    supportModes = dataResponse.getSupportMode();
                    pdList = dataResponse.getPd();
                    internalProcesses = dataResponse.getInternalProcess();
                    supportThemes = dataResponse.getSupportTheme();
                    subThemes = dataResponse.getSubSupporttheme();
                    modalities = dataResponse.getModality();
                    populateSpinnerDelivering(pdList);
                    //populateInternalProcess(internalProcesses);
                    populateSpinnerSupportMode(supportModes);
                    populateSpinnerEngagement(modalities);
                    populateSpinnerSupportTheme();
                    //populateSpinnerSubSupportTheme(subSupportthemes);

                    sortedResponses = realm.where(ReportResponse.class).findFirstAsync();
                    report = sortedResponses.getReport().last();

                    tvCounty.setText("County: " + report.getCountyName());
                    tvPartner.setText("Partner: " + report.getPName());
                    if (report.getExchange()) {
                        tvType.setText("Exchange");
                    } else {
                        tvType.setText("Local");
                    }

                    latitude = Prefs.with(getContext()).getLatitude();
                    longitude = Prefs.with(getContext()).getLongitude();
                    tvLatitude.setText("Latitude: " + latitude);
                    tvLongitude.setText("Longitude: " +longitude);

                }
            });

            sSupport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    populateSpinnerSubSupportTheme(subThemes);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {/*if(realm != null) {
                realm.close();
            }*/
                    populateSpinnerSubSupportTheme(subThemes);
                }
            });
        } finally {

        }


        sDelivering.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try { // I could use try-with-resources here
                    realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            DataResponse dataResponse = realm.where(DataResponse.class).findFirst();
                            pdList = dataResponse.getPd();
                            Pd pd = pdList.get(sDelivering.getSelectedItemPosition());
                            if (pd.getPdId().equals("5") || pd.getPdId().equals("6")) {
                                sPP.setVisibility(View.VISIBLE);
                                tPP.setVisibility(View.VISIBLE);

                                List<String> lables = new ArrayList<String>();
                                lables.add("Yes");
                                lables.add("No");

                                // Creating adapter for spinner
                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_spinner_item, lables);

                                // Drop down layout style - list view with radio button
                                spinnerAdapter
                                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                sPP.setAdapter(spinnerAdapter);
                            } else {
                                sPPd = false;
                            }
                        }
                    });
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }

            }


        });


        return view;
    }

 /*   @OnClick(R.id.tvBDS)
    public void bds(){

        new DoubleDateAndTimePickerDialog.Builder(getActivity())
                .bottomSheet()
                .curved()
                .backgroundColor(Color.WHITE)
                .mainColor(Color.GRAY)
                .minutesStep(15)
                .title("Range")
                .tab0Text("Start")
                .tab1Text("End")
                .listener(new DoubleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(List<Date> dates) {
                        timeBDS = Integer.valueOf(Long.toString(dates.get(1).getTime() - dates.get(0).getTime()));
                    }
                }).display();
    }

    @OnClick(R.id.tvTravel)
    public void tr(){

        new DoubleDateAndTimePickerDialog.Builder(getActivity())
                .bottomSheet()
                .curved()
                .minutesStep(15)
                .title("Range")
                .tab0Text("Start")
                .tab1Text("End")
                .listener(new DoubleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(List<Date> dates) {
                        timeTravel = Integer.valueOf(Long.toString(dates.get(1).getTime() - dates.get(0).getTime()));
                    }
                }).display();
    }

    @OnClick(R.id.tvEM)
    public void em(){


    }*/


    @OnClick(R.id.next_two_weekly)
    public void next() {

        final String travels = travel.getText().toString();
        final String hours = bds.getText().toString();
        final String needss = eneeds.getText().toString();

        if (StringUtils.isNullorEmpty(travels) || StringUtils.isNullorEmpty(hours) || StringUtils.isNullorEmpty(needss)) {
            Toast.makeText(getActivity(), "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);


            try { // I could use try-with-resources here
                realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        DataResponse dataResponse = realm.where(DataResponse.class).findFirstAsync();
                        sortedResponses = realm.where(ReportResponse.class).findFirstAsync();
                        report = sortedResponses.getReport().last();

                        Modality modality = dataResponse.getModality().get(sFOE.getSelectedItemPosition());
                        SupportMode supportMode = dataResponse.getSupportMode().get(sSupportMode.getSelectedItemPosition());
                        SupportTheme supportTheme = dataResponse.getSupportTheme().get(sSupport.getSelectedItemPosition());
                        SubSupporttheme subSupporttheme = dataResponse.getSubSupporttheme().get(sSubSupport.getSelectedItemPosition());
                        report.setSubId(subSupporttheme.getSubId());
                        report.setSId(supportTheme.getSId());
                        report.setModalityId(modality.getModalityId());
                        report.setModeId(supportMode.getModeId());
                        Pd pd = dataResponse.getPd().get(sDelivering.getSelectedItemPosition());
                        //InternalProcess internalProcess = dataResponse.getInternalProcess().get(sInternalProcess.getSelectedItemPosition());
                        report.setPdId(pd.getPdId());
                        //report.setOipId(internalProcess.getOipId());

                        report.setTravel(StringUtils.isNullorEmpty(travels) ? "0" : travels);
                        report.setHoursIp(StringUtils.isNullorEmpty(hours) ? "0" : hours);
                        report.setE_needs(StringUtils.isNullorEmpty(needss) ? "0" : needss);
                    }
                });
            } finally {
                if (realm != null) {
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

                        final String sppdd;
                        if (sPPd) {
                            sppdd = StringUtils.isNullorEmpty(sPP.getSelectedItem().toString()) ? "No" : sPP.getSelectedItem().toString();

                        } else {

                            sppdd = "No";
                        }

                        RequestBody formBody = new FormBody.Builder()
                                .add("id", MyApplication.getInstance().getPrefManager().getUser().getId().toString())
                                .add("county_id", report.getCountyId())
                                .add("exchange", report.getExchange() ? String.valueOf(1) : String.valueOf(0))
                                .add("modality_id", report.getModalityId())
                                .add("oip_id", "2")
                                .add("p_id", report.getPId())
                                .add("pd_id", report.getPdId())
                                .add("mode_id", report.getModeId())
                                .add("sub_id", report.getSubId())
                                .add("s_id", report.getSId())
                                .add("travel", report.getTravel())
                                .add("e_needs", report.getE_needs())
                                .add("present", sppdd)
                                .add("hours_ip", report.getHoursIp())
                                .add("longitude", longitude)
                                .add("latitude", latitude)
                                .build();

                        OkHttpClient client = new OkHttpClient();

                        Request request = new Request.Builder()
                                .url(Config.postUrl)
                                .post(formBody)
                                .build();

                        try {
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
                                    L.e(TAG, responses);
                                    L.json(TAG, responses);

                                    //progressBar.setVisibility(View.INVISIBLE);
                                    if (isJSONValid(responses)) {
                                        Gson gson = new Gson();
                                        PostResponse postResponse = gson.fromJson(responses, PostResponse.class);

                                        if (!postResponse.getError()) {
                                            //Toast.makeText(getActivity(), "Posted succesfully!", Toast.LENGTH_SHORT).show();
                                            successPosting();
                                            General.backgroundThreadShortToast(getActivity(), "Posted succesfully!");

                                        } else {

                                            //progressBar.setVisibility(View.INVISIBLE);
                                            errorPosting();
                                            //Toast.makeText(getActivity(), "Submission unsuccessful", Toast.LENGTH_SHORT).show();
                                            L.e(TAG, postResponse.getErrorMsg());
                                            General.backgroundThreadShortToast(getActivity(), "Submission unsuccessful");
                                        }
                                    } else {
                                        errorPosting();
                                        General.backgroundThreadShortToast(getActivity(), "Submission unsuccessful");
                                    }


                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }

        }
    }

    private void changeFragmentProcess(Fragment fragment) {

        /*getActivity().getSupportFragmentManager().beginTransaction()
                *//*.setCustomAnimations(R.anim.trans_left_in,
                        R.anim.trans_left_out, R.anim.trans_right_in,R.anim.trans_right_out)*//*
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
*/
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


    private void populateSpinnerSupportMode(List<SupportMode> list) {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            lables.add(list.get(i).getModeName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sSupportMode.setAdapter(spinnerAdapter);
    }

    /**
     * Adding spinner data
     */
    private void populateSpinnerEngagement(List<Modality> modalities) {
        List<String> lables = new ArrayList<String>();

        for (int i = 0; i < modalities.size(); i++) {
            lables.add(modalities.get(i).getModalityName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sFOE.setAdapter(spinnerAdapter);
    }

    /**
     * Adding spinner data
     */
    private void populateSpinnerDelivering(List<Pd> pds) {
        List<String> lables = new ArrayList<String>();
        for (int i = 0; i < pds.size(); i++) {
            lables.add(pds.get(i).getPdName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sDelivering.setAdapter(spinnerAdapter);
    }

    /**
     * Adding spinner data
     * */
   /* private void populateInternalProcess(List<InternalProcess> pds) {
        List<String> lables = new ArrayList<String>();
        for (int i = 0; i < pds.size(); i++) {
            lables.add(pds.get(i).getOipName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sInternalProcess.setAdapter(spinnerAdapter);
    }*/

    /**
     * Adding spinner data
     */
    private void populateSpinnerSubSupportTheme(List<SubSupporttheme> list) {
        List<String> lables = new ArrayList<String>();
        List<SubSupporttheme> filtered = new ArrayList<>();
        if (list != null) {
            for (SubSupporttheme subSupporttheme : list) {
                if (Integer.valueOf(subSupporttheme.getSId()) == Integer.valueOf(supportThemes.get(sSupport.getSelectedItemPosition()).getSId())) {
                    filtered.add(subSupporttheme);
                }
            }
        }

        if (filtered.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                lables.add(list.get(i).getSubName());
            }
        } else {
            for (int i = 0; i < filtered.size(); i++) {
                lables.add(filtered.get(i).getSubName());
            }
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sSubSupport.setAdapter(spinnerAdapter);
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
                sSupport.setAdapter(spinnerAdapter);
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
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
    @OnClick(R.id.next_two_weekly)
    public void movetwo() {
        /*Fragment fragment = new ThreeFragment();
        changeFragmentProcess(fragment,true);
        MainActivity.changeFragmentProcess()*/
    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public boolean isJSONValid(String test) {
        try {
            Gson gson = new Gson();
            gson.fromJson(test, PostResponse.class);
            return true;
        } catch (JsonSyntaxException ex) {
            return false;
        }
    }

    private void errorPosting() {
        Fragment fragment = new ThanksFragmentBuilder("Could not post your Report right now! Will be saved as draft.").build();
        changeFragmentProcess(fragment);
    }

    private void successPosting() {
        Fragment fragment = new ThanksFragmentBuilder("Your Report has been submitted successfully!").build();
        changeFragmentProcess(fragment);
    }

    /*    final Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_MONTH, 1);
    calendar.set(Calendar.MONTH, 0);
    calendar.set(Calendar.YEAR, 2017);
    final Date minDate = calendar.getTime();
    calendar.set(Calendar.DAY_OF_MONTH, 5);
    final Date maxDate = calendar.getTime();

    DoubleDateAndTimePickerDialog.Builder doubleBuilder = new DoubleDateAndTimePickerDialog.Builder(getContext())
        //.bottomSheet()
        //.curved()
        //.backgroundColor(Color.BLACK)
        //.mainColor(Color.GREEN)
        .minutesStep(15)
        .mustBeOnFuture()
        .minDateRange(minDate)
        .maxDateRange(maxDate)
        .tab0Text("Start")
        .tab1Text("End")
        .listener(new DoubleDateAndTimePickerDialog.Listener() {
            @Override
            public void onDateSelected(List<Date> dates) {
                tvStartValue.setText(parseDate(dates.get(0)));
                tvFinishValue.setText(parseDate(dates.get(1)));
            }
        }
    );
    doubleBuilder.display();
}*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if(realm != null) {
            realm.close();
        }*/
    }
}
