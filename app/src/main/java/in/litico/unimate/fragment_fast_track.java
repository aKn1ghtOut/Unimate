package in.litico.unimate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import in.litico.unimate.managers.manager_fast_track;
import in.litico.unimate.views.general_card;


public class fragment_fast_track extends Fragment  implements manager_fast_track.fast_track_events {

    manager_fast_track ft_man;
    String TAG = "FRAGMENT_FAST_TRACK";
    LinearLayout ft_cont;
    int apply_boolean_state = 0; //0 is hidden, 1 is open
    public fragment_fast_track() {
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
        return inflater.inflate(R.layout.fragment_fast_track, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        ft_cont = getView().findViewById(R.id.ft_cont);
        try {
            ft_man = new manager_fast_track(getActivity(), ((main) getActivity()).sp, this);

            ft_man.login();
        }
        catch (NullPointerException E)
        {
            Toast.makeText(getActivity(), "Could not get SharedPreferences", Toast.LENGTH_LONG).show();
        }

        getView().findViewById(R.id.add_request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle_apply_group(v);
            }
        });
    }



    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void toggle_apply_group(View v)
    {
        LinearLayout l = getView().findViewById(R.id.apply_group);

        if(apply_boolean_state == 0)
        {
            apply_boolean_state = 1;
            expand(l);
        }
        else
        {
            apply_boolean_state = 0;
            collapse(l);
        }
    }

    /*
    Implements callback methods for manager_fast_track
    These methods are used to communicate from the manager_fast_track class with this class, as and when the server responses are received.
     */

    public void ft_loggedIn(int result, String cookie)
    {
        if(result == 1 && cookie != null)
        {
            ft_man.make_home(getActivity(), cookie);
        }
        else
        {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "Couldn't Log in", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void ft_homepage(final JSONArray past_info)
    {
        final JSONArray past_info_arr = past_info;
        if(past_info.length() <= 0)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "No previous records found", Toast.LENGTH_SHORT).show();
                }
            });
        else {
            if(getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ft_cont.removeAllViews();

                    JSONObject gate_pass;
                    try {
                        for(int i = 0; i < past_info.length(); i++) {
                            gate_pass = past_info.getJSONObject(i);
                            TextView txt = new TextView(getActivity());
                            txt.setText("To : " + gate_pass.getString("to"));
                            txt.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                            txt.setPadding(10,10,10,10);
                            general_card c_new = new general_card(getActivity(), "From : " + gate_pass.getString("from"), txt);
                            if(!gate_pass.getString("status").equalsIgnoreCase("Approved"))
                                c_new.set_color(R.color.ft_status_err, R.color.ft_status_err, getActivity());
                            else
                                c_new.set_color(R.color.ft_status_cnf, R.color.ft_status_cnf, getActivity());
                            c_new.setTag(i);
                            c_new.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int index = (int)v.getTag();
                                    try {
                                        ((main) getActivity()).move_bottom_bar_up(new view_ft_info_box(getActivity(), past_info_arr.getJSONObject(index)));
                                    }catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            ft_cont.addView(c_new);
                        }
                        Snackbar.make(ft_cont, "Requests found", Snackbar.LENGTH_LONG).show();
                    }
                    catch (Exception E)
                    {
                        E.printStackTrace();
                    }
                }
            });
        }
    }

    public void ft_submitter(int result)
    {}

    /* End manager_fast_track implementation */

}
