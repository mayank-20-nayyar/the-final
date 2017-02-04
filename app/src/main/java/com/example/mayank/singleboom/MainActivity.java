package com.example.mayank.singleboom;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Mayank on 1/16/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.Application;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Button b;
    public EditText passWord, eMail, name;
    HttpClient client;
    HttpPost post;
    HttpResponse response;
    // Context context;
    public String res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = (Button) findViewById(R.id.button);
        name = (EditText) findViewById(R.id.editText);
        eMail = (EditText) findViewById(R.id.editText3);
        passWord = (EditText) findViewById(R.id.editText2);
        final Context context;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("the  but","is clicked");
                startAsync();

            }
        });
    }

    void startAsync(){
        new LoadTask(this).execute("https://sporophoric-reservo.000webhostapp.com/signUp.php");
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

    @Override
    protected void onStart(){
        super.onStart();
        Log.e("the activity","started");
    }

    public void nextStep(){
        Log.e("this is in nextStep","this");
        //Log.e("app context", getApplicationContext()+"");
        Intent i = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(i);
    }


}

class LoadTask extends AsyncTask<String, String, String> {

    MainActivity ma ;
    boolean check_flag = true;

    public LoadTask(MainActivity ma) {
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




