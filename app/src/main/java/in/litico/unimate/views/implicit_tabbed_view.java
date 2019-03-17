package in.litico.unimate.views;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

public class implicit_tabbed_view extends general_card {

    ArrayList<Fragment> tab_views;
    ViewPager v_p;

    public implicit_tabbed_view(Context context)
    {
        super(context);
    }
    public implicit_tabbed_view(Context context, ArrayList<Fragment> tabs, String[] titles)
    {
        super(context, titles[0], null);
        tab_views = tabs;

        v_p = new ViewPager(context);
    }
}