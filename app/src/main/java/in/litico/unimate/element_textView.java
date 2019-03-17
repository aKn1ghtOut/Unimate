package in.litico.unimate;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;

public class element_textView extends AppCompatTextView {
    element_textView(Context context, int TypefaceStyle)
    {
        super(context);
        setTypeface(getResources().getFont(R.font.open_sans));
    }
}
