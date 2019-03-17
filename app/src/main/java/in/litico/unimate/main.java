package in.litico.unimate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONArray;

import in.litico.unimate.managers.manager_att;

public class main extends AppCompatActivity implements
        fragment_home.OnFragmentInteractionListener,
        fragment_shouter.OnFragmentInteractionListener,
        manager_att.eventListener
{

    private int logo_clicks = 0;

    private fragment_home homeFrag;
    private FragmentTransaction main_ft;
    private fragment_shouter shout_frag;
    private fragment_fast_track ft_frag;

    public SharedPreferences sp;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            move_bottom_bar_out(null);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    main_ft = getSupportFragmentManager().beginTransaction();
                    main_ft.replace(R.id.frag_holder, homeFrag);
                    main_ft.commit();
                    return true;
                case R.id.navigation_att:
                    return true;
                case R.id.navigation_ft:
                    main_ft = getSupportFragmentManager().beginTransaction();
                    main_ft.replace(R.id.frag_holder, ft_frag);
                    main_ft.commit();
                    return true;
                case R.id.navigation_schedule:
                    return true;
                case R.id.shout_out:
                    main_ft = getSupportFragmentManager().beginTransaction();
                    main_ft.replace(R.id.frag_holder, shout_frag);
                    main_ft.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        __BottomNavigationViewHelper.disableShiftMode(navigation);

        homeFrag = new fragment_home();
        shout_frag = new fragment_shouter();
        ft_frag = new fragment_fast_track();

        main_ft = getSupportFragmentManager().beginTransaction();


        main_ft.replace(R.id.frag_holder, homeFrag);
        main_ft.commit();

        sp = getSharedPreferences("snu_login", Context.MODE_PRIVATE);

        sp.edit()
                .putString("snu_username", "ab342")
                .putString("snu_password", "helperThanks0")
                .apply();

    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public void initiate_att_login(View v)
    {
        logo_clicks++;
        if (logo_clicks >= 7)
        {
            logo_clicks = 0;
            Intent easter = new Intent(this, activity_easter_egg.class);
            startActivity(easter);
            return;
        }
        if(logo_clicks >= 4)
        {
            Toast.makeText(this, (7-logo_clicks) + " time(s) more", Toast.LENGTH_SHORT).show();
        }
    }

    public void move_bottom_bar_out(View v)
    {
        float height = this.getWindow().getDecorView().getHeight();
        ConstraintLayout bottom_bar = findViewById(R.id.bottom_bar);
        bottom_bar.animate().translationY(height);
    }
    public void snulinks(View v)
    {
        String snul = "https://snulinks.snu.edu.in/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(snul));
        startActivity(i);
    }
    public void move_bottom_bar_up(View v)
    {
        float height = this.getWindow().getDecorView().getHeight();
        FrameLayout bottom_bar_cont = findViewById(R.id.content_container);
        ConstraintLayout bottom_bar = findViewById(R.id.bottom_bar);
        bottom_bar_cont.removeAllViews();
        bottom_bar_cont.addView(v);
        bottom_bar.animate().setDuration(100).translationY(0);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            float height = this.getWindow().getDecorView().getHeight();
            ConstraintLayout bottom_bar = findViewById(R.id.bottom_bar);
            bottom_bar.animate().setDuration(0).translationY(height);
        }
    }


    //manager_att interface functions start
    public void checkWiFi()
    {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            if(cm == null)
            throw new NullPointerException();

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;

            if(!isWiFi)
            {

            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Couldn't get network info", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void attendanceLogin(int result)
    {
        if (result == 1)
        {
            //Logged in
        }
        else
        {
            //Couldn't Login
        }
    }

    public void dailyAttendanceView(JSONArray classes)
    {}

    public void courseData(JSONArray courses)
    {}
    //manager_att interface functions end
}
