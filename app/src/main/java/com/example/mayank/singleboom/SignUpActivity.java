package com.example.mayank.singleboom;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static final int REQUEST_LOGIN = 0;


    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;
    @InjectView(R.id.input_no) EditText _mobile;

    String email;
    String number;
    String password;
    String name;
    HttpClient client;
    HttpPost post;
    HttpResponse response;
    // Context context;
    public String res;
//SessionManagement session;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
  //      session = new SessionManagement(getApplicationContext());
    //    session.checkLogin();

        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent,REQUEST_LOGIN);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        name = _nameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        number = _mobile.getText().toString();

        // TODO: Implement your own signup logic here.

        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/

        startAsync();
    }

    void startAsync(){
        new LoadTaskLoginSignUp(this).execute("https://sporophoric-reservo.000webhostapp.com/signUp.php");
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
            String nameStr = _nameText.getText().toString();
            String eMailStr = _emailText.getText().toString();
            String passWordStr = _passwordText.getText().toString();
            String phoneStr=_mobile.getText().toString();

            Log.e("this is email",eMailStr);
            Log.e("this is pass",passWordStr);
            Log.e("this is name",nameStr);
            Log.e("this is phone",phoneStr);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("name", nameStr));
            nameValuePairs.add(new BasicNameValuePair("email", eMailStr));
            nameValuePairs.add(new BasicNameValuePair("password", passWordStr));
            nameValuePairs.add(new BasicNameValuePair("phoneNumber", phoneStr));


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
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
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


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

class LoadTaskLoginSignUp extends AsyncTask<String, String, String> {

    SignUpActivity ma ;
    boolean check_flag = true;

    public LoadTaskLoginSignUp(SignUpActivity ma) {
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
                else
                {
                    ma.onSignupFailed();
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




