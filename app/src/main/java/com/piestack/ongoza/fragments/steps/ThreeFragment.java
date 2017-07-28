package com.piestack.ongoza.fragments.steps;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.piestack.ongoza.R;
import com.piestack.ongoza.fragments.WeeklyFragment;
import com.piestack.ongoza.models.data.DataResponse;
import com.piestack.ongoza.models.data.InternalProcess;
import com.piestack.ongoza.models.data.Modality;
import com.piestack.ongoza.models.data.Pd;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.models.data.ReportResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;

public class ThreeFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.spinner2)
    Spinner sDelivering;
    @BindView(R.id.spinner3)
    Spinner sInternalProcess;

    private ReportResponse sortedResponses = null;
    private Report report = null;

    private List<Pd> pdList = null;
    private List<InternalProcess> internalProcesses = null;

    Realm realm = null;


    public ThreeFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_three, container, false);
        unbinder = ButterKnife.bind(this, view);

        Realm realm = null;
        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    DataResponse dataResponse = realm.where(DataResponse.class).findFirstAsync();

                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }


        return view;
    }

    @OnClick(R.id.submit_weekly)
    public void submit(){


        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    DataResponse dataResponse = realm.where(DataResponse.class).findFirstAsync();
                    sortedResponses = realm.where(ReportResponse.class).findFirst();
                    Pd pd = dataResponse.getPd().get(sDelivering.getSelectedItemPosition());
                    InternalProcess internalProcess = dataResponse.getInternalProcess().get(sInternalProcess.getSelectedItemPosition());
                    report = sortedResponses.getReport().last();
                    report.setPdId(pd.getPdId());
                    report.setOipId(internalProcess.getOipId());
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }



        Fragment fragment = new FourFragment();
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

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }



    /**
     * Adding spinner data
     * */
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
    private void populateInternalProcess(List<InternalProcess> pds) {
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
    }
    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
