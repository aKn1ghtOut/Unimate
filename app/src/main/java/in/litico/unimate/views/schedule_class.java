package in.litico.unimate.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.litico.unimate.R;

/**
 * TODO: document your custom view class.
 */
public class schedule_class extends LinearLayout {

    public int
            color_red,
            color_red_darker,

            color_blue,
            color_blue_darker,

            color_green,
            color_green_darker,

            color_yellow,
            color_yellow_darker;

    private TextView
            view_title,
            view_timings,
            view_class;

    private View        view_dark_bg;
    private CardView    view_card_holder;

    private Context app_context;

    public schedule_class(Context context) {
        super(context);
    }
    public schedule_class(Context context, String title, String venue, String time) {
        super(context);

        color_red = R.color.redder;
        color_red_darker = R.color.redder_dark;

        color_green = R.color.green;
        color_green_darker = R.color.green_dark;

        color_blue = R.color.sky_blue;
        color_blue_darker = R.color.sky_blue_dark;

        color_yellow = R.color.yellow;
        color_yellow_darker = R.color.yellow_dark;

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        inflate(getContext(), R.layout.view_schedule_class, this);

        view_title = (TextView)findViewById(R.id.class_title);
        view_class = (TextView)findViewById(R.id.class_venue);
        view_timings = (TextView)findViewById(R.id.class_timing);
        view_card_holder = (CardView)findViewById(R.id.card_holder);
        view_dark_bg = (View)findViewById(R.id.view_dark);

        view_title.setText(title);
        view_class.setText(venue);
        view_timings.setText(time);

        char a = venue.charAt(0);

        switch (a)
        {
            case 'A':
                view_dark_bg.setBackgroundColor(ContextCompat.getColor(context, color_red_darker));
                view_card_holder.setCardBackgroundColor(ContextCompat.getColor(context, color_red));
                break;

            case 'B':
                view_dark_bg.setBackgroundColor(ContextCompat.getColor(context, color_yellow_darker));
                view_card_holder.setCardBackgroundColor(ContextCompat.getColor(context, color_yellow));
                break;

            case 'C':
                view_dark_bg.setBackgroundColor(ContextCompat.getColor(context, color_green_darker));
                view_card_holder.setCardBackgroundColor(ContextCompat.getColor(context, color_green));
                break;

            default:
                view_dark_bg.setBackgroundColor(ContextCompat.getColor(context, color_blue_darker));
                view_card_holder.setCardBackgroundColor(ContextCompat.getColor(context, color_blue));
                break;
        }
    }

    public schedule_class(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public schedule_class(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void set_color(@ColorRes int color_code, @ColorRes int color_code_dark, Context context)
    {
        view_dark_bg.setBackgroundColor(ContextCompat.getColor(context, color_code));
        view_card_holder.setCardBackgroundColor(ContextCompat.getColor(context, color_code_dark));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
