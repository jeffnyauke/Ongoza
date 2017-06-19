package com.piestack.ongoza.fragments.steps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.piestack.ongoza.R;
import com.piestack.ongoza.models.data.DataResponse;
import com.piestack.ongoza.models.data.Modality;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.models.data.ReportResponse;
import com.piestack.ongoza.models.data.SubSupporttheme;
import com.piestack.ongoza.models.data.SupportMode;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;

public class TwoFragment extends Fragment {

    private static final String ARGUMENT_NAME = "name";
    private Unbinder unbinder;
    private ReportResponse sortedResponses = null;
    private Report report = null;
    @BindView(R.id.spinner1)
    Spinner sSubTheme;
    @BindView(R.id.spinner2)
    Spinner sSupportMode;
    @BindView(R.id.spinner3)
    Spinner sFOE;
    Realm realm = null;

    private List<Modality> modalities = null;
    private List<SubSupporttheme> subSupportthemes = null;
    private List<SupportMode> supportModes = null;

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

        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    DataResponse dataResponse = realm.where(DataResponse.class).findFirstAsync();
                    modalities = dataResponse.getModality();
                    subSupportthemes = dataResponse.getSubSupporttheme();
                    supportModes = dataResponse.getSupportMode();
                    sortedResponses = realm.where(ReportResponse.class).findFirstAsync();
                    report = sortedResponses.getReport().last();
                    populateSpinnerSubSupportTheme(subSupportthemes);
                    populateSpinnerSupportMode(supportModes);
                    populateSpinnerEngagement(modalities);
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }
        /*try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    sortedResponses = realm.where(ReportResponse.class).findFirstAsync();
                    report = sortedResponses.getReport().last();
                    Toast.makeText(getActivity(), report.getCountyId()+" " +report.getPId() +" "+ report.getSId(), Toast.LENGTH_SHORT).show();
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }*/

        return view;
    }

    @OnClick(R.id.next_two_weekly)
    public void next(){



        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    DataResponse dataResponse = realm.where(DataResponse.class).findFirstAsync();
                    sortedResponses = realm.where(ReportResponse.class).findFirstAsync();
                    report = sortedResponses.getReport().last();
                    Modality modality = dataResponse.getModality().get(sFOE.getSelectedItemPosition());
                    SubSupporttheme subSupporttheme = dataResponse.getSubSupporttheme().get(sSubTheme.getSelectedItemPosition());
                    SupportMode supportMode = dataResponse.getSupportMode().get(sSupportMode.getSelectedItemPosition());
                    report.setSubId(subSupporttheme.getSubId());
                    report.setModalityId(modality.getModalityId());
                    report.setModeId(supportMode.getModeId());
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }
        Fragment fragment = new ThreeFragment();
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
    private void populateSpinnerSubSupportTheme(List<SubSupporttheme> list) {
        List<String> lables = new ArrayList<String>();
        List<SubSupporttheme> filtered = new ArrayList<>();
        if(report != null) {
            for (SubSupporttheme subSupporttheme : list) {
                if (Integer.valueOf(subSupporttheme.getSId()) == Integer.valueOf(report.getSId())){
                    filtered.add(subSupporttheme);
                }
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
        sSubTheme.setAdapter(spinnerAdapter);
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
     * */
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
    public void movetwo(){
        /*Fragment fragment = new ThreeFragment();
        changeFragmentProcess(fragment,true);
        MainActivity.changeFragmentProcess()*/
    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
