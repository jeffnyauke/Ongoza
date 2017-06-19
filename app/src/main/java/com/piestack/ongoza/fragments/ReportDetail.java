package com.piestack.ongoza.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.piestack.ongoza.R;
import com.piestack.ongoza.fragments.steps.TwoFragment;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.models.data.ReportResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;

/**
 * Created by Jeffrey Nyauke on 3/23/2017.
 */

@FragmentWithArgs
public class ReportDetail extends Fragment  {
    private Unbinder unbinder;
    @BindView(R.id.textView)
    TextView textView;

    @Arg
    String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView=inflater.inflate(R.layout.fragment_report_detail,container,false);
        unbinder = ButterKnife.bind(this, rootView);


        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        Realm realm  = null;
        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ReportResponse dataResponse = realm.where(ReportResponse.class).findFirstAsync();
                    Report report  = dataResponse.getReport().get(Integer.valueOf(id));
                    textView.setText(report.getPName()+"\n\n"+report.getCountyName()+"\n\n"+report.getSName()+"\n\n"+report
                    .getSubName()+"\n\n"+report.getModeName()+"\n\n"+report.getModalityName()+"\n\n"+report.getHoursIp()+"\n\n"+report.getE_needs()+"\n\n"+report.getTravel()+"\n\n"+
                    report.getOipName()+"\n\n"+report.getDor());
                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }
    }

    public void init(){

    }



    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
