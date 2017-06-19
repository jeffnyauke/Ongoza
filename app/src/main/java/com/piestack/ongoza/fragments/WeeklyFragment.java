package com.piestack.ongoza.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.piestack.ongoza.R;
import com.piestack.ongoza.fragments.steps.TwoFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Jeffrey Nyauke on 3/23/2017.
 */

public class WeeklyFragment extends Fragment  {
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView=inflater.inflate(R.layout.fragment_weekly,container,false);
        unbinder = ButterKnife.bind(this, rootView);


        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    public void init(){

    }

    @OnClick(R.id.next_one_weekly)
    public void next(){
        Fragment fragment  = new TwoFragment();
        changeFragmentProcess(fragment);
    }

    private void changeFragmentProcess(Fragment fragment){

        Fragment oldFragment = new WeeklyFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                /*.setCustomAnimations(R.anim.trans_left_in,
                        R.anim.trans_left_out, R.anim.trans_right_in,R.anim.trans_right_out)*/
                .remove(oldFragment)
                .replace(R.id.content, fragment)
                .addToBackStack("Home")
                .commit();

    }

    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
