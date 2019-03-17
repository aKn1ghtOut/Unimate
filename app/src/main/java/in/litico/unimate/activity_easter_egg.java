package in.litico.unimate;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class activity_easter_egg extends AppCompatActivity {

    private int CURRENT_STATE = 0;

    private long TIME_CHECK = 0;

    private TextView SCORE, HIGH_SCORE;
    int CURR_SCORE, HIGHEST_SCORE;

    private ImageButton play_but, replay_but;

    public final int
        STATE_PLAY      =   1,
        STATE_PAUSED    =   0;

    private SharedPreferences sp;

    private final String HIGH_SCORE_MSG = "BEST SCORE : ";

    private boolean TO_STATE_CHECK = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_easter_egg);


        play_but = findViewById(R.id.play_button);
        replay_but = findViewById(R.id.replay_button);

        SCORE = findViewById(R.id.score_txtv);
        HIGH_SCORE = findViewById(R.id.high_score);

        sp = getSharedPreferences("UNIMATE_EASTER_EGG", MODE_PRIVATE);

        HIGHEST_SCORE = sp.getInt("high__score", 999);

        if(HIGHEST_SCORE == 999)
            HIGH_SCORE.setText(HIGH_SCORE_MSG + "?");
        else
            HIGH_SCORE.setText(HIGH_SCORE_MSG + HIGHEST_SCORE);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus && TO_STATE_CHECK){

            TO_STATE_CHECK = false;

            ImageView v = findViewById(R.id.rotate_this);
            float random = (float)(15F + Math.random() *150F);
            float deg = v.getRotation() + random;
            v.animate().rotation(deg).setDuration(0).setInterpolator(new AccelerateDecelerateInterpolator());
        }
    }

    protected void clicked(View view)
    {
        if (CURRENT_STATE == STATE_PAUSED)
            return;

        ImageView v = findViewById(R.id.rotate_this);
        float deg = v.getRotation() + 180F;
        v.animate().rotation(deg).setDuration(1000).setInterpolator(new AccelerateDecelerateInterpolator());

        final long time_to_check = System.currentTimeMillis() + 1000;

        TIME_CHECK = time_to_check;

        CURR_SCORE++;

        SCORE.setText("" + CURR_SCORE);

        (new Thread(() -> {
            long timer = time_to_check;
            try {
                Thread.sleep(1000);
                if(timer == TIME_CHECK)
                {
                    check_rotation();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        })).start();
    }

    protected void check_rotation()
    {
        ImageView v = findViewById(R.id.rotate_this);
        float deg = (v.getRotation() % 360F);
        float delta = 360F - deg;

        if(deg <= 30F || delta <= 30F)
        {
            set_paused_state();
        }
    }

    protected void set_paused_state()
    {
        CURRENT_STATE = STATE_PAUSED;

        replay_but.setVisibility(View.VISIBLE);


        int get_score = CURR_SCORE;

        if(get_score < HIGHEST_SCORE)
        {
            HIGHEST_SCORE = get_score;
            sp.edit().putInt("high__score", get_score).apply();
            HIGH_SCORE.setText(HIGH_SCORE_MSG + HIGHEST_SCORE);
        }
    }

    protected void set_play_state(View v)
    {
        CURRENT_STATE = STATE_PLAY;

        v.setVisibility(View.INVISIBLE);

        CURR_SCORE = 0;
        SCORE.setText("0");
    }
}
