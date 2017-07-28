package com.piestack.ongoza.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.piestack.ongoza.MainActivity;
import com.piestack.ongoza.R;
import com.piestack.ongoza.app.Config;
import com.piestack.ongoza.app.MyApplication;
import com.piestack.ongoza.fragments.steps.ThanksFragmentBuilder;
import com.piestack.ongoza.fragments.steps.TwoFragment;
import com.piestack.ongoza.models.PostResponse;
import com.piestack.ongoza.models.data.DataResponse;
import com.piestack.ongoza.models.data.Partner;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.models.data.ReportResponse;
import com.piestack.ongoza.models.data.SubSupporttheme;
import com.piestack.ongoza.models.data.SupportTheme;
import com.piestack.ongoza.models.data.SupportThemeResponse;
import com.piestack.ongoza.utils.General;
import com.piestack.ongoza.utils.L;
import com.piestack.ongoza.utils.StringUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.SimpleDayPickerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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


public class SummariesFragment extends Fragment  {

    private  Unbinder unbinder;
    private String TAG = SummariesFragment.class.getSimpleName();
    private List<SupportTheme> supportThemes = null;
    private View view;
    private Calendar begin;
    private Calendar end;
    private DatePickerDialog datePickerDialoga;
    private DatePickerDialog datePickerDialogb;
    private Date beginning;
    private Date ending;

    //@BindView(R.id.spinner) Spinner sThemes;
    @BindView(R.id.search) Button button;
    @BindView(R.id.start_date) TextView startDate;
    @BindView(R.id.end_date) TextView endDate;
    @BindView(R.id.progressBar) ProgressBar progressBar;



    public SummariesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.start_date)
    public void dialogStart(){
        datePickerDialoga.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @OnClick(R.id.end_date)
    public void dialogEnd(){
        datePickerDialogb.show(getActivity().getFragmentManager(), "Datepickerdialogb");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_summaries, container, false);
        unbinder = ButterKnife.bind(this, view);

        getActivity().setTitle("Summary");
        progressBar.setVisibility(View.INVISIBLE);
        //getActivity().getActionBar().setSubtitle();

        /*OkHttpUtils
                .get()//
                .url(Config.themesUrl)//
                .id(101)
                .build()//
                .execute(new MyStringCallback());*/

        begin = Calendar.getInstance();
        end = Calendar.getInstance();
        final SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy");
        sdf.setTimeZone(TimeZone.getDefault());
        ending = new Date();
        endDate.setText(sdf.format(new Date()));

        datePickerDialoga = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                             @Override
                                                             public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dateOfMonth) {
                                                                 Calendar startTime = Calendar.getInstance();
                                                                 startTime.set(year,monthOfYear,dateOfMonth);
                                                                 beginning = startTime.getTime();
                                                                 startDate.setText(sdf.format(beginning));
                                                             }
                                                         },
                begin.get(Calendar.YEAR),
                begin.get(Calendar.MONTH),
                begin.get(Calendar.DAY_OF_MONTH));
        datePickerDialoga.setTitle("Start Date");

        datePickerDialogb = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                             @Override
                                                             public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dateOfMonth) {
                                                                 Calendar endTime = Calendar.getInstance();
                                                                 endTime.set(year,monthOfYear,dateOfMonth);
                                                                 ending = endTime.getTime();
                                                                 endDate.setText(sdf.format(ending));
                                                             }
                                                         },
                end.get(Calendar.YEAR),
                end.get(Calendar.MONTH),
                end.get(Calendar.DAY_OF_MONTH));
        datePickerDialoga.setTitle("End Date");
        endDate.setText("Today");

        return view;
    }


    @OnClick(R.id.search)
    public void next(){

        if(beginning == null){
            Toast.makeText(getActivity(), "Please select start date", Toast.LENGTH_SHORT).show();
        }else {

        progressBar.setVisibility(View.VISIBLE);

        //SupportTheme supportTheme = supportThemes.get(sThemes.getSelectedItemPosition());
        //String s_id = supportTheme.getSId();
        String start = beginning.toString();
        String end = ending.toString();
        //Toast.makeText(getActivity(), s_id+"\n"+start+"\n"+end, Toast.LENGTH_LONG).show();


        RequestBody formBody = new FormBody.Builder()
                .add("id", MyApplication.getInstance().getPrefManager().getUser().getId().toString())
                .add("sdate",start)
                .add("edate",end)
                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Config.summariesUrl)
                .post(formBody)
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                L.e(TAG, e.getMessage());
                errorPosting();
                General.backgroundThreadShortToast(getActivity(), "Check your internet connection");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responses = response.body().string();
                L.e(TAG,responses);
                L.json(TAG,responses);

                if(isJSONValid(responses)){
                        successPosting(responses);
//progressBar.setVisibility(View.INVISIBLE);
                    Fragment fragment = new FilteredReportFragmentBuilder(responses).build();
                    changeFragmentProcess(fragment);


                }else{
                    errorPosting();
                }



            }
        });
        }

    }

    private void changeFragmentProcess(Fragment fragment){

        getActivity().getSupportFragmentManager().beginTransaction()
                /*.setCustomAnimations(R.anim.trans_left_in,
                        R.anim.trans_left_out, R.anim.trans_right_in,R.anim.trans_right_out)*/
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();

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


        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sThemes.setAdapter(spinnerAdapter);
            }
        });*/
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
            e.printStackTrace();
progressBar.setVisibility(View.INVISIBLE);
            String error = e.getMessage() == null ? "Null" : e.getMessage();
            Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onResponse(String response, int id)
        {
            if(view !=null){

            button.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
            Log.e(TAG, "onResponse：complete"+ response);

            if(isJSONValid(response)){
                Gson gson = new Gson();
                SupportThemeResponse dataResponse =  gson.fromJson(response,SupportThemeResponse.class);
                supportThemes = dataResponse.getSupportTheme();
                populateSpinnerSupportTheme();

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
    public boolean isJSONValidd(String test){
        try{
            Gson gson = new Gson();
            gson.fromJson(test, ReportResponse.class);
            return  true;
        }catch (JsonSyntaxException ex){
            return false;
        }
    }


    /**
     * Adding spinner data
     * */
    private void populateSpinnerSubSupportTheme(List<SubSupporttheme> list) {
        List<String> lables = new ArrayList<String>();
        List<SubSupporttheme> filtered = new ArrayList<>();
        if(list != null) {
            for (SubSupporttheme subSupporttheme : list) {
                /*if (Integer.valueOf(subSupporttheme.getSId()) == Integer.valueOf(supportThemes.get(sThemes.getSelectedItemPosition()).getSId())){
                    filtered.add(subSupporttheme);
                }*/
            }
        }

        if(filtered.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                lables.add(list.get(i).getSubName());
            }
        }else{
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
        //sThemes.setAdapter(spinnerAdapter);
    }

    private void errorPosting(){
        //progressBar.setVisibility(View.INVISIBLE);
        General.backgroundThreadLongToast(getActivity(), "Check your internet connection");
    }

    private void successPosting(String response){
        //General.backgroundThreadLongToast(getActivity(), response);

        //Fragment fragment = new ThanksFragmentBuilder("Your Internal Process has been submitted successfully!").build();
        //changeFragmentProcess(fragment);
    }

}
