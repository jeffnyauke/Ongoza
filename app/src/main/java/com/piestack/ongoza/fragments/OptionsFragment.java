package com.piestack.ongoza.fragments;

import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.piestack.ongoza.R;
import com.piestack.ongoza.adapters.OptionsAdapter;
import com.piestack.ongoza.fragments.steps.CountyFragment;
import com.piestack.ongoza.fragments.steps.InternalProcessFragment;
import com.piestack.ongoza.fragments.steps.OneFragmentExchange;
import com.piestack.ongoza.fragments.steps.OneFragmentLocal;
import com.piestack.ongoza.models.Options;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Jeffrey Nyauke on 3/23/2017.
 */

public class OptionsFragment extends Fragment implements  OptionsAdapter.OptionsAdapterListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private static final String TAG = "OptionsFragment";
    private List<Options> optionsList = new ArrayList<>();
    private Unbinder unbinder;
    private OptionsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView=inflater.inflate(R.layout.fragment_options,container,false);
        unbinder = ButterKnife.bind(this, rootView);

        init();

        getActivity().setTitle("Ongoza");


        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // clear the options
        optionsList.clear();

        Options options1 = new Options();
        options1.setId(1);
        options1.setTitle("Create New BDA");
        options1.setSubtitle("Record your interactions with various partners");
        options1.setColor(getRandomMaterialColor("400"));
        options1.setPicture(R.drawable.bg_circle);

        optionsList.add(options1);

        Options options2 = new Options();
        options2.setId(2);
        options2.setTitle("View submitted BDAs");
        options2.setSubtitle("View all your successfully submitted BDAs");
        options2.setColor(getRandomMaterialColor("400"));
        options2.setPicture(R.drawable.bg_circle);

        optionsList.add(options2);

        Options options3 = new Options();
        options3.setId(3);
        options3.setTitle("Update Pending BDA");
        options3.setSubtitle("Update your pending interactions with various partners");
        options3.setColor(getRandomMaterialColor("400"));
        options3.setPicture(R.drawable.bg_circle);

        optionsList.add(options3);


        Options options4 = new Options();
        options4.setId(3);
        options4.setTitle("Internal Process");
        options4.setSubtitle("Record the various internal processes of the business");
        options4.setColor(getRandomMaterialColor("400"));
        options4.setPicture(R.drawable.bg_circle);

        optionsList.add(options4);


        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void init(){

        mAdapter = new OptionsAdapter(getActivity(), optionsList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//      recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

    }

    /**
     * chooses a random color from array.xml
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getActivity().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }


    // When binding a fragment in onCreateView, set the views to null in onDestroyView.
    // ButterKnife returns an Unbinder on the initial binding that has an unbind method to do this automatically.
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onOptionRowClicked(int position) {

        final Fragment oldFragment = new OptionsFragment();

        if(position ==0){
            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose an option");

            // add a list
            String[] animals = {"Local", "Exchange"};
            builder.setItems(animals, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        //case 0: // local
                        //Toast.makeText(getActivity(), "Local", Toast.LENGTH_SHORT).show();
                        Fragment fragment = new OneFragmentLocal();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .remove(oldFragment)
                                .replace(R.id.content, fragment)
                                .addToBackStack("Home")
                                .commit();
                    }else if(which == 1) {
                        //case 1: // exchange
                           // Toast.makeText(getActivity(), "Exchange", Toast.LENGTH_SHORT).show();
                            Fragment fragmente = new CountyFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                /*.setCustomAnimations(R.anim.trans_left_in,
                        R.anim.trans_left_out, R.anim.trans_right_in,R.anim.trans_right_out)*/
                                    .remove(oldFragment)
                                    .replace(R.id.content, fragmente)
                                    .addToBackStack("Home")
                                    .commit();
                    }

                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();

        }else if(position == 1){
            Fragment fragment  = new ReportFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(oldFragment)
                    .replace(R.id.content, fragment)
                    .addToBackStack("Home")
                    .commit();
        }else if(position == 2){
            Fragment fragment  = new OneFragmentLocal();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(oldFragment)
                    .replace(R.id.content, fragment)
                    .addToBackStack("Home")
                    .commit();
        }else if(position == 3){
            Fragment fragment  = new InternalProcessFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(oldFragment)
                    .replace(R.id.content, fragment)
                    .addToBackStack("Home")
                    .commit();
        }

    }

    @Override
    public void onRowLongClicked(int position) {
        // long press is performed, enable action mode

    }

}
