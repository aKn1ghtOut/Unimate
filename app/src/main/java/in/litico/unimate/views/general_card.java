package in.litico.unimate.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.litico.unimate.R;

public class general_card extends LinearLayout{
    public static int
            color_red = R.color.redder,
            color_red_darker = R.color.redder_dark,

            color_green = R.color.green,
            color_green_darker = R.color.green_dark,

            color_blue = R.color.sky_blue,
            color_blue_darker = R.color.sky_blue_dark,

            color_yellow = R.color.yellow,
            color_yellow_darker = R.color.yellow_dark,

            color_grey = R.color.grey,
            color_grey_darker = R.color.grey_dark;

    private TextView
            view_title,
            view_timings,
            view_class;

    private LinearLayout view_cont_holder;
    private CardView view_card_holder, view_dark_bg;

    private Context app_context;

    public general_card(Context context) {
        super(context);
    }
    public general_card(Context context, String title, @Nullable View content) {
        super(context);

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        inflate(getContext(), R.layout.view_general_card, this);

        view_title = (TextView)findViewById(R.id.title);
        view_card_holder = (CardView)findViewById(R.id.card_holder);
        view_dark_bg = (CardView) findViewById(R.id.view_dark);
        view_cont_holder = (LinearLayout)findViewById(R.id.cont_holder);

        if(content != null)
        view_cont_holder.addView(content);

        view_title.setText(title);
    }

    public general_card(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public general_card(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void set_color(@ColorRes int color_code, @ColorRes int color_code_dark, Context context)
    {
        view_dark_bg.setCardBackgroundColor(ContextCompat.getColor(context, color_code_dark));
        view_card_holder.setCardBackgroundColor(ContextCompat.getColor(context, color_code));
    }

    public void add_content(View to_add){view_cont_holder.addView(to_add);}

    public void set_title(String title){view_title.setText(title);}

    public void replace_view(View to_add)
    {
        view_cont_holder.removeAllViews();
        view_cont_holder.addView(to_add);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
