package in.litico.unimate.managers;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.litico.unimate.views.general_card;

public class manager_misc {
    private static final String TAG = "MANAGER_MISC";

    private static final String dh_resolve_menus_js = "var dh_arr = {\"dh1\" : {\"breakfast\" : [], \"lunch\" : [], \"dinner\" : []}, \"dh2\" : {\"breakfast\" : [], \"lunch\" : [], \"dinner\" : []}};\n" +
            "\n" +
            "var dh1_ob = $(\"section#DH1\").find(\"table > tbody\");\n" +
            "var dh1_brkfst = dh1_ob.find(\"td\").eq(1);\n" +
            "var dh1_lunch = dh1_ob.find(\"td\").eq(2);\n" +
            "var dh1_dinner = dh1_ob.find(\"td\").eq(3);\n" +
            "\n" +
            "dh1_brkfst.find(\"p\").each(function()\n" +
            "{\n" +
            "\tvar item = $(this).text().trim();\n" +
            "\tdh_arr.dh1.breakfast.push(item);\n" +
            "});\n" +
            "\n" +
            "dh1_lunch.find(\"p\").each(function()\n" +
            "{\n" +
            "\tvar item = $(this).text().trim();\n" +
            "\tdh_arr.dh1.lunch.push(item);\n" +
            "});\n" +
            "\n" +
            "dh1_dinner.find(\"p\").each(function()\n" +
            "{\n" +
            "\tvar item = $(this).text().trim();\n" +
            "\tdh_arr.dh1.dinner.push(item);\n" +
            "});\n" +
            "\n" +
            "var dh2_ob = $(\"section#DH2\").find(\"table > tbody\");\n" +
            "var dh2_brkfst = dh2_ob.find(\"td\").eq(1);\n" +
            "var dh2_lunch = dh2_ob.find(\"td\").eq(2);\n" +
            "var dh2_dinner = dh2_ob.find(\"td\").eq(3);\n" +
            "\n" +
            "dh2_brkfst.find(\"p\").each(function()\n" +
            "{\n" +
            "\tvar item = $(this).text().trim();\n" +
            "\tdh_arr.dh2.breakfast.push(item);\n" +
            "});\n" +
            "\n" +
            "dh2_lunch.find(\"p\").each(function()\n" +
            "{\n" +
            "\tvar item = $(this).text().trim();\n" +
            "\tdh_arr.dh2.lunch.push(item);\n" +
            "});\n" +
            "\n" +
            "dh2_dinner.find(\"p\").each(function()\n" +
            "{\n" +
            "\tvar item = $(this).text().trim();\n" +
            "\tdh_arr.dh2.dinner.push(item);\n" +
            "});";

    public static void mess_menu(final Context context, final general_card dh1_card, final general_card dh2_card, final Activity act)
    {
        try {
            /* An instance of this class will be registered as a JavaScript interface */
            class MyJavaScriptInterface {
                @JavascriptInterface
                @SuppressWarnings("unused")
                public void processHTML(String html) {
                    JSONObject dh_ob;
                    try
                    {
                        dh_ob = new JSONObject(html);
                        inflate_mess_menu(context, dh_ob, dh1_card, dh2_card, act);
                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(context, "Mess Menu retrieval failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            final WebView browser = new WebView(context);
            /* JavaScript must be enabled if you want it to work, obviously */
            browser.getSettings().setJavaScriptEnabled(true);

            /* Register a new JavaScript interface called HTMLOUT */
            browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

            /* WebViewClient must be set BEFORE calling loadUrl! */
            browser.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    browser.loadUrl("javascript:" + dh_resolve_menus_js + "window.HTMLOUT.processHTML(JSON.stringify(dh_arr));");

                }
            });

            /* load a web page */
            browser.loadUrl("http://messmenu.snu.in/messMenu.php");
        }
        catch (Exception e) {
            Toast.makeText(context, "Mess Menu retrieval failed.", Toast.LENGTH_LONG).show();;
        }
    }

    public static void inflate_mess_menu(Context context, JSONObject dh_json, final general_card dh1_card, final general_card dh2_card, final Activity act)
    {
        String text = "Breakfast:\n";
        try {
            JSONArray db1_break = dh_json.getJSONObject("dh1").getJSONArray("breakfast");
            for(int i = 0; i < db1_break.length(); i++)
            {
                text = text + "\n" + db1_break.getString(i);
            }

            text = text + "\n\nLunch:\n";

            JSONArray db1_lunch = dh_json.getJSONObject("dh1").getJSONArray("lunch");
            for(int i = 0; i < db1_lunch.length(); i++)
            {
                text = text + "\n" + db1_lunch.getString(i);
            }

            text = text + "\n\nDinner:\n";

            JSONArray db1_dinner = dh_json.getJSONObject("dh1").getJSONArray("dinner");
            for(int i = 0; i < db1_dinner.length(); i++)
            {
                text = text + "\n" + db1_dinner.getString(i);
            }

            final String t_next = text;

            act.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    TextView n = new TextView(act);
                    ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    n.setLayoutParams(lparams);
                    n.setTextSize(15F);
                    n.setText(t_next);
                    dh1_card.replace_view(n);
                }
            });

            text = "Breakfast:\n";

            JSONArray db2_break = dh_json.getJSONObject("dh2").getJSONArray("breakfast");
            for(int i = 0; i < db2_break.length(); i++)
            {
                text = text + "\n" + db2_break.getString(i);
            }

            text = text + "\n\nLunch:\n";

            JSONArray db2_lunch = dh_json.getJSONObject("dh2").getJSONArray("lunch");
            for(int i = 0; i < db2_lunch.length(); i++)
            {
                text = text + "\n" + db2_lunch.getString(i);
            }

            text = text + "\n\nDinner:\n";

            JSONArray db2_dinner = dh_json.getJSONObject("dh2").getJSONArray("dinner");
            for(int i = 0; i < db2_dinner.length(); i++)
            {
                text = text + "\n" + db2_dinner.getString(i);
            }

            final String t_next_2 = text;

            act.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    TextView n = new TextView(act);
                    ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    n.setLayoutParams(lparams);
                    n.setTextSize(15F);
                    n.setText(t_next_2);
                    dh2_card.replace_view(n);
                }
            });
        }
        catch (JSONException e)
        {
            Toast.makeText(context, "JSON Object failed", Toast.LENGTH_LONG).show();
        }
    }

    /*public boolean verify_login(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences("snu_login", Context.MODE_PRIVATE);

        final String snu_username = sp.getString("snu_username", null);
        final String snu_password = sp.getString("snu_password", null);

        if (snu_username == null || snu_password == null)
        {
            return false;
        }


    }*/
}
