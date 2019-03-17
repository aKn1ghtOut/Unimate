package in.litico.unimate.managers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import android.webkit.CookieManager;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.annotations.EverythingIsNonNull;

public class manager_fast_track {

    private static final String TAG = "UNIMATE_MANAGER_FAST_TRACK";

    private String domain = "https://fastrack.webapps.snu.edu.in";
    private String login_post_req = "https://fastrack.webapps.snu.edu.in/public/application/login/login_auth";

    private SharedPreferences pref;
    private Context myAppTemp;

    private String COOKIE_VAL;

    private Object superListener;
    manager_fast_track(Context cont, SharedPreferences spResource, Object super_ob)
    {
        myAppTemp = cont;
        pref = spResource;
        superListener = super_ob;
    }

    public void login()
    {
        final String username = pref.getString("snu_username", "0");
        final String password = pref.getString("snu_password", "0");

        OkHttpClient client = new   OkHttpClient
                                    .Builder()
                                    .followRedirects(false)
                                    .followSslRedirects(false)
                                    .build();


        RequestBody req_body = new  MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("net_id", username)
                                    .addFormDataPart("user_password", password)
                                    .build();

        Request request = new   Request.Builder()
                                .url(login_post_req)
                                .post(req_body)
                                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override @EverythingIsNonNull
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "Failed to log in");
                if(superListener instanceof fast_track_events)
                {
                    ((fast_track_events) superListener).ft_loggedIn(0, null);
                }
            }

            @Override @EverythingIsNonNull
            public void onResponse(Call call, Response response) throws IOException {
                String loc = response.header("Location");
                String req_cookie;
                if(loc != null && loc.equalsIgnoreCase("/public/application/index/index")){
                    Log.d(TAG, "Logged in successfully");
                    req_cookie = response.headers().get("Set-Cookie");

                    if(superListener instanceof fast_track_events)
                    {
                        ((fast_track_events) superListener).ft_loggedIn(1, req_cookie);
                    }
                }
                else
                {
                    Log.d(TAG, "Failed to log in");
                    if(superListener instanceof fast_track_events)
                    {
                        ((fast_track_events) superListener).ft_loggedIn(0, null);
                    }
                }
            }
        });
    }

    public void apply_personal(){
        int result = 1;
        if(superListener instanceof fast_track_events)
        {
            ((fast_track_events) superListener).ft_submitter(result);
        }
    }
    public void apply_official(){
        int result = 1;
        if(superListener instanceof fast_track_events)
        {
            ((fast_track_events) superListener).ft_submitter(result);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void make_home(final Context context, String cookie)
    {
        COOKIE_VAL = cookie;
        if(context == null)
            return;
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CookieManager.getInstance().setCookie(domain, cookie);
                final String ft_resolve_js = "var ft_arr=[];var ft_ob=$(\"table#stud_list > tbody\");ft_ob.find(\"tr\").each(function()\n" +
                        "{var obj={};obj.id=$(this).find(\"td\").eq(0).text();obj.type=$(this).find(\"td\").eq(1).text();obj.purpose=$(this).find(\"td\").eq(2).text();obj.from=$(this).find(\"td\").eq(3).text();obj.to=$(this).find(\"td\").eq(4).text();obj.status=$(this).find(\"td\").eq(5).text();obj.addedOn=$(this).find(\"td\").eq(6).text();ft_arr.push(obj)});";
                try {

                    String ret = "";
                    /* An instance of this class will be registered as a JavaScript interface */
                    class MyJavaScriptInterface {
                        @JavascriptInterface
                        @SuppressWarnings("unused")
                        public void processHTML(String html) {
                            JSONArray res;
                            Log.e(TAG, "Interface Called");
                            try
                            {
                                res = new JSONArray(html);
                                if(superListener instanceof fast_track_events)
                                {
                                    ((fast_track_events) superListener).ft_homepage(res);
                                }
                            }
                            catch (JSONException e)
                            {
                                Log.e(TAG, "Fast Track table retrieval failed.");
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
                            browser.loadUrl("javascript:" + ft_resolve_js + "HTMLOUT.processHTML(JSON.stringify(ft_arr));");

                        }
                    });

                    /* load a web page */
                    browser.loadUrl("https://fastrack.webapps.snu.edu.in/public/application/index/index");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Cookies not attached to WebView");
                }
            }
        });
    }

    public interface fast_track_events
    {
        void ft_homepage(JSONArray past_data);
        void ft_submitter(int result);
        void ft_loggedIn(int result, String cookie);
    }
}
