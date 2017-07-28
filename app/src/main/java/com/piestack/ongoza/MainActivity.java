package com.piestack.ongoza;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.piestack.ongoza.app.MyApplication;
import com.piestack.ongoza.fragments.MonthlyFragment;
import com.piestack.ongoza.fragments.WeeklyFragment;
import com.piestack.ongoza.fragments.OptionsFragment;
import com.zhy.http.okhttp.OkHttpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    private TextView mTextMessage;
    Fragment newFragment = null;

    /*private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_reports:
                    changeFragment(0);
                    return true;
                case R.id.navigation_weekly:
                    changeFragment(1);
                    return true;
                case R.id.navigation_monthly:
                    changeFragment(2);
                    return true;
            }
            return false;
        }

    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        changeFragment(0);

        /*BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/
    }

    /**
     * To load fragments for sample
     * @param position menu index
     */
    private void changeFragment(int position) {

        if (position == 0) {
            newFragment = new OptionsFragment();
        } else if (position == 1) {
            newFragment = new WeeklyFragment();
        } else {
            newFragment = new MonthlyFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(
                R.id.content, newFragment)
                .commit();
    }

    public void changeFragmentProcess(Fragment fragment, boolean isStart){

        if(isStart){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, fragment)
                    .addToBackStack("Home")
                    .commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, fragment)
                    .addToBackStack(null)
                    .commit();
        }


    }

    public void changeFragmentEnd(Fragment fragment){

        getSupportFragmentManager().popBackStack("Home", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment)
                .addToBackStack(null)
                .commit();
    }

/*    @OnClick(R.id.next_one_weekly)
    public void moveone(){
        Fragment fragment = new TwoFragment();
        changeFragmentProcess(fragment,true);
    }



    @OnClick(R.id.submit_weekly)
    public void submitweekly(){
        Fragment fragment = new WeeklyFragment();
        changeFragmentEnd(fragment);
    }*/

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//
//            Intent intent  = new Intent(MainActivity.this, SettingsActivity.class);
//            startActivity(intent);
//
//            return true;
//        }else

            if(id == R.id.action_logout){
            MyApplication.getInstance().logout();
        }

        return super.onOptionsItemSelected(item);
    }


}
