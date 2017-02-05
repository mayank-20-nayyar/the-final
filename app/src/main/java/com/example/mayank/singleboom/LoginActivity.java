package com.example.mayank.singleboom;

/**
 * Created by Mayank on 2/4/2017.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    AnimationDrawable rocketAnimation;

    @InjectView(R.id.input_email) EditText emailText;
    @InjectView(R.id.input_password) EditText passwordText;
    @InjectView(R.id.btn_login) Button loginButton;
    @InjectView(R.id.link_signup) TextView signupLink;
    @InjectView(R.id.input_no) EditText mobile;

    String email;
    String number;
    String password;

    HttpClient client;
    HttpPost post;
    HttpResponse response;
    // Context context;
    public String res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);




        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        email = emailText.getText().toString();
        password = passwordText.getText().toString();
        number = mobile.getText().toString();

        // TODO: Implement your own authentication logic here.

        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
        */
        startAsync();




    }

    void startAsync(){
        new LoadTaskLogin(this).execute("https://sporophoric-reservo.000webhostapp.com/signUp.php");
    }
    boolean makeConnection(String url) {
        try {
            client = new DefaultHttpClient();
            final HttpParams httpParams = client.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // 10 seconds
            HttpConnectionParams.setSoTimeout(httpParams, 10000);

            //post = new HttpPost("http://192.168.43.3/signUp.php");
            post = new HttpPost("https://sporophoric-reservo.000webhostapp.com/signUp.php");

            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

    }

    public boolean generatePostRequest() {
        try {
            //String nameStr = name.toString();
            String nameStr = "this is  mayank the sex and handjob";
            String eMailStr = "sex@600";//eMail.toString();
            String passWordStr = "sexyy";//passWord.toString();

            Log.e("this is email",eMailStr);
            Log.e("this is pass",passWordStr);
            Log.e("this is name",nameStr);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("name", nameStr));
            nameValuePairs.add(new BasicNameValuePair("email", eMailStr));
            nameValuePairs.add(new BasicNameValuePair("password", passWordStr));


           /* List<NameValuePair> urlParameters = new ArrayList<NameValuePair>(1);
            urlParameters.add(new BasicNameValuePair("name", name));
           */// urlParameters.add(new BasicNameValuePair("phone", aphone));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = client.execute(post);
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
            return false;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String getRequestResponse() {
        //String res;

        try {
            res = EntityUtils.toString(response.getEntity());
            return res;
        } catch (org.apache.http.ParseException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void nextStep(){
        Log.e("this is in nextStep","this");
        //Log.e("app context", getApplicationContext()+"");
        Intent i = new Intent(LoginActivity.this, MapsActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(false);
        Intent i=new Intent(this,ChooseActivity.class);
        startActivity(i);
        // super.onBackPressed();
        finish();
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        Intent i=new Intent(this,MapsActivity.class);
        startActivity(i);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}


class LoadTaskLogin extends AsyncTask<String, String, String> {

    LoginActivity ma ;
    boolean check_flag = true;

    public LoadTaskLogin(LoginActivity ma) {
        this.ma = ma;
    }




   /* Context context;
    public LoadTask(Context context) {
        this.context = context.getApplicationContext();
    }*/


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("inside","pre");
    }

    protected String doInBackground(String... params) {
        if (check_flag) {

            check_flag = ma.makeConnection(params[0]);
            Log.e("f", check_flag + "");
        }

        if (check_flag) {

            check_flag = ma.generatePostRequest();
            Log.e("s", check_flag + "");
        }

        if (check_flag) {

            return ma.getRequestResponse();

        }

        return null; // null is returned if request failed


    }

    @Override
    protected void onPostExecute(String res) {

        if (ma.res != null) {
            try {



                Log.e("before", "object");
                Log.e("the value of ma.res", ma.res);
                JSONObject obj = new JSONObject(ma.res);
                //Log.e("this is", obj+"");
                //JSONArray j = new JSONArray();
                // /JSONArray arr = obj.getJSONArray("ress");
                String ss = obj.getString("mes");
                // int length = arr.length();
                Log.e("the value object", ss + "this should be sex bro");
                if (ss.equals("yes")) {
                    Log.e("this is fucking", "idiotic");

                    //context.startActivity(new Intent(context, MapsActivity.class));
                    ma.nextStep();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Toast.makeText(this,ma.id,Toast.LENGTH_LONG).show();
        Log.e("task done","bitchess!!!1");

        super.onPostExecute(ma.res);

    }

//    @Override
//    public void onAttach(Activity activity){
//        this.activity = activity;
//

}




