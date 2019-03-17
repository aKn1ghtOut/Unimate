package in.litico.unimate.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class manager_att {

    private static final String TAG = "Unimate";
    private static String att_cookie;

    private static String base_link = "https://markattendance.webapps.snu.edu.in/public/application/";
    private static String login_page = base_link + "login/login";
    private static String login_post_req = base_link + "login/loginAuthSubmit";

    private static String temp_valid_code = "";

    public static void login_sequence_token(final Context context, final SharedPreferences sp, final Object sup)
    {
        Log.d(TAG, "login_sequence_token: P1");
        final Context myApp = context;
        final manager_att inst = this;
        try {


            Log.d(TAG, "login_sequence_token: P2");

            String ret = "";
            /* An instance of this class will be registered as a JavaScript interface */
            class MyJavaScriptInterface {
                @JavascriptInterface
                @SuppressWarnings("unused")
                public void processHTML(String html) {
                    inst.temp_valid_code = html;
                    inst.login(context, sp, sup);

                    Log.d(TAG, "login_sequence_token: P4");
                }
            }

            final WebView browser = new WebView(myApp);
            /* JavaScript must be enabled if you want it to work, obviously */
            browser.getSettings().setJavaScriptEnabled(true);

            /* Register a new JavaScript interface called HTMLOUT */
            browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

            /* WebViewClient must be set BEFORE calling loadUrl! */
            browser.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    /* This call inject JavaScript into the page which just finished loading. */
                    Log.d(TAG, "login_sequence_token: P3");
                    browser.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementById('valid_code').attributes.value);");

                }
            });

            /* load a web page */
            browser.loadUrl(login_page);
        }
        catch (Exception e) {
            Toast.makeText(myApp, "Could not login. Check credentials and try again.", Toast.LENGTH_LONG).show();;
        }
    }

    private void login(Context context, SharedPreferences pref, Object sup)
    {
        final String username = pref.getString("snu_username", "0");
        final String password = pref.getString("snu_password", "0");

        if(username.equals("0") || password.equals("0")) {
            Log.e("Login info not found");
            return;
        }

        OkHttpClient loginer = new  OkHttpClient
                                    .Builder()
                                    .followRedirects(false)
                                    .followSslRedirects(false)
                                    .build();

        RequestBody req_body = new  FormBody.Builder()
                                    .add("login_user_name", username)
                                    .add("login_password", password)
                                    .add("valid_code", temp_valid_code)
                                    .build();

        Request req = new           Request.Builder()
                                    .url(login_post_req)
                                    .post(req_body)
                                    .build();

        loginer.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("Network Failure");

                if(sup instanceof eventListener)
                {
                    ((eventListener) sup).checkWiFi();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String loc = response.headers().get("Location");

                if(loc == "/public/application/index/attendance_day_view")
                {
                    att_cookie = response.headers().get("Set-Cookie");
                    //Logged In

                    if(sup instanceof eventListener)
                    {
                        ((eventListener) sup).attendanceLogin(1);
                    }
                }
                else
                {
                    //Could not log in
                    //Probably Username/Password issue
                    if(sup instanceof eventListener) {
                        ((eventListener) sup).attendanceLogin(0);
                    }
                }
            }
        });
    }

    public void get_course_wise()
    {
        Log.d(TAG, "get_course_wise: Stage 1");
        final Context myApp = myAppTemp;
        final manager_att inst = this;
        try {


            Log.d(TAG, "get_course_wise: Stage 2");

            String ret = "";
            /* An instance of this class will be registered as a JavaScript interface */
            class MyJavaScriptInterface {
                @JavascriptInterface
                @SuppressWarnings("unused")
                public void processHTML(String html) {
                    inst.temp_valid_code = html;
                    inst.login();

                    Log.d(TAG, "login_sequence_token: P4");
                }
            }

            final WebView browser = new WebView(myApp);
            /* JavaScript must be enabled if you want it to work, obviously */
            browser.getSettings().setJavaScriptEnabled(true);

            /* Register a new JavaScript interface called HTMLOUT */
            browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");

            /* WebViewClient must be set BEFORE calling loadUrl! */
            browser.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    /* This call inject JavaScript into the page which just finished loading. */
                    Log.d(TAG, "login_sequence_token: P3");
                    browser.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementById('valid_code').attributes.value);");

                }
            });

            /* load a web page */
            browser.loadUrl(login_page);
        }
        catch (Exception e) {
            Toast.makeText(myApp, "Could not get attendance status", Toast.LENGTH_LONG).show();;
        }
    }

    public interface eventListener
    {
        public void checkWiFi();
        public void attendanceLogin(int result);
        public void dailyAttendanceView(JSONArray classes);
        public void courseData(JSONArray courses);
    }
}
