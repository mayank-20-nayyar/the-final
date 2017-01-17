package com.example.mayank.singleboom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener
         {


    private GoogleMap mMap;

    public double longitude;
    public double latitude;

             public int i;
             Button b;
             public Bitmap orginalBitmap;
             public List<Marker> markers = new ArrayList<Marker>();
             public List<Marker> markerNames = new ArrayList<Marker>();
             public List<String> friendName = new ArrayList<String>();
             public Map<String, String> contact = new HashMap<String, String>() ;



             public int kk = 0;
             public String toSend = "";

             HttpClient client;
             HttpPost post;
             HttpResponse response;
             // Context context;
             public String res;
             boolean check_flag = true;

    private GoogleApiClient googleApiClient;

             private BoomMenuButton bmb;

             private static int[] imageResources = new int[]{
                     R.drawable.bee,
                     R.drawable.bat,
                     R.drawable.bee,
                     R.drawable.bat,
             };
             private static String[] stringResources = new String[]{
                     "bee",
                     "bat",
                     "bee",
                     "bat",
             };
             private static int imageResourceIndex = 0;
             private static int stringResourceIndex = 0;

             static int getImageResource() {
                 if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
                 return imageResources[imageResourceIndex++];
             }

             static String getStringResource() {
                 if (stringResourceIndex >= stringResources.length) stringResourceIndex = 0;
                 return stringResources[stringResourceIndex++];
             }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bmb = (BoomMenuButton) findViewById(R.id.bmb);



        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            Log.e("listener","called "+index+"");
                            if(index == 0)
                                startFriendThread();
                        }
                    })
                    .normalImageRes(getImageResource())
                    .normalText(String.valueOf(getStringResource()));
            bmb.addBuilder(builder);
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();



    }
      //  moveMap();

        //Initializing views and adding onclick listeners
       // buttonSave = (ImageButton) findViewById(R.id.buttonSave);
    //    buttonCurrent = (ImageButton) findViewById(R.id.buttonCurrent);
       // buttonView = (ImageButton) findViewById(R.id.buttonView);
       // buttonSave.setOnClickListener(this);
  //      buttonCurrent.setOnClickListener(this);
       // buttonView.setOnClickListener(this);


    @Override
    protected void onStart() {
        googleApiClient.connect();

        super.onStart();
        startThread();
        startFriendThread();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    //Getting current location
    public void getCurrentLocation() {
        mMap.clear();
        //Creating a location object
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();
   mMap.animateCamera(CameraUpdateFactory.zoomTo(40));

            //moving the map to location
            moveMap();

        }
    }



    //Function to move the map
    public void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //Displaying current coordinates in toast
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

             public void updateMoveMap(){

                 String msg = latitude + ", "+longitude;


                 LatLng latLng = new LatLng(latitude, longitude);


                 mMap.addMarker(new MarkerOptions()
                         .position(latLng) //setting position
                         .draggable(true) //Making the marker draggable
                         .title("Current Location")); //Adding a title


                 mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                // mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


                 Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
             }

             public void getUpdatedOwnLocation(){
                 mMap.clear();

                 if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                     // TODO: Consider calling

                     return;
                 }
                 Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                 if (location != null) {

                     longitude = location.getLongitude();
                     latitude = location.getLatitude();
                     mMap.animateCamera(CameraUpdateFactory.zoomTo(40));


                     updateMoveMap();

                 }
             }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();

        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();


    }




    void startThread()
    {
        Thread th=new Thread(){

            @Override
            public void run(){

                try
                {
                    for (;;)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getUpdatedOwnLocation();
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread.sleep(2000);

                    }
                }
                catch (InterruptedException e) {
                }

            }
        };
        th.start();
    }
             public void getUpdatedCurrentLocation(double latitude, double longitude, String name) {
                 if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                     // TODO: Consider calling

                     Log.e("inside ","to do consider");
                     return;
                 }
                 Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);




                 LatLng latLngg = new LatLng(latitude, longitude);

                 Log.e("lat",latitude+"");
                 Log.e("long",longitude+"");

       /* Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);

        Bitmap resized = Bitmap.createScaledBitmap(bmp, 40, 40, true);
*/
        /*Bitmap bmpp = Bitmap.createScaledBitmap(orginalBitmap, 40, 40, true);
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
*/
                 //      Canvas canvas1 = new Canvas(resized);

                 //Bitmap bmpp = Bitmap.createScaledBitmap(orginalBitmap, 40, 40, true);
// paint defines the text color, stroke width and size
      /*  Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLUE);*/

// modify canvas
       /* canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.amir), 0,0, color);
*/
// add marker to Map
       /* Marker marker = mMap.addMarker(new MarkerOptions().position(latLngg)
                .icon(BitmapDescriptorFactory.fromBitmap(resized))
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.5f, 1));
       */    // mMap.addMarker(new MarkerOptions().position(latLngg).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                 /// Marker marker = mMap.addMarker(new MarkerOptions().position(latLngg).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("mayank nayyar"));
                 // Marker marker = mMap.addMarker(new MarkerOptions().position(latLngg).icon(BitmapDescriptorFactory.fromResource(R.drawable.mark)));
                 // marker.showInfoWindow();

/*
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),
                R.drawable.marker3);

        Bitmap bmp2 = bitmap1.copy(bitmap1.getConfig(), true);*/

       /* Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmap1 = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.markk));*/
       /* Bitmap bitmap2  = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.hot));

        Bitmap hott = bitmap2.copy(bitmap2.getConfig(), true);
       int bitmap1Width = bitmap1.getWidth();
        int bitmap1Height = bitmap1.getHeight();
        int bitmap2Width = bitmap2.getWidth();
        int bitmap2Height = bitmap2.getHeight();

        float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);
        float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);
*/

       /*Bitmap overlayBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),
               R.drawable.markk));
       */

        /*Canvas canvas = new Canvas(bmp2);

        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.markk), new Matrix(), null);
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
               R.drawable.hot), marginLeft, marginTop, null);
*/

                 String text = name;
                 Bitmap b = Bitmap.createBitmap(650, 240, Bitmap.Config.ARGB_8888);
                 Canvas c = new Canvas(b);
                 c.drawBitmap(b, 0, 0, null);
                 TextPaint textPaint = new TextPaint();
                 textPaint.setAntiAlias(true);
                 textPaint.setTextSize(getResources().getDimension(R.dimen.textsize));
                 StaticLayout sl= new StaticLayout(text, textPaint, b.getWidth()-8, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
                 c.translate(6, 40);
                 sl.draw(c);

                 Marker markerName = mMap.addMarker(new MarkerOptions().position(latLngg)
                         .icon(BitmapDescriptorFactory.fromBitmap(b))
                         .anchor(0.5f, 1));

                 Marker marker = mMap.addMarker(new MarkerOptions()
                         .position(latLngg)
                         .draggable(true)
                         .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

                 //marker.showInfoWindow();

                 mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngg));
                 mMap.getUiSettings().setScrollGesturesEnabled(true);


                 //Animating the camera
                 mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                 markers.add(marker);
                 markerNames.add(markerName);


             }

             void startFriendThread()
             {
                 Log.e("inside ","SFT");
                 Thread th=new Thread(){

                     @Override
                     public void run(){
                         //

                         try
                         {
                             Log.e("inside","try");
                             friendName.add("Mayank");
                             friendName.add("Vaibhav");
                             friendName.add("Dopu");
                             friendName.add("harshit");
                             friendName.add("may");
                             friendName.add("Dope");
                             friendName.add("M");
                             friendName.add("V");
                             friendName.add("D");

                             contact.put("9871656573", "Mayank");
                             contact.put("9643023359", "Vaibhav");
                             contact.put("999105616", "Deepali");
                             contact.put("7503104261", "Harshit");


                             for(String key : contact.keySet()){

                                 toSend += key.substring(key.length() - 10);
                             }

                             if (check_flag) {

                                 check_flag = makeConnection("http://192.168.43.3/signUp.php");
                             }

                             if (check_flag) {

                                 check_flag = generatePostRequest();
                             }

                             if (check_flag) {

                                 return getRequestResponse();
                             }

                             for (;;)
                             {
                                 Log.e("val of i ",i+"");
                                 runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         try {
                                             double latitude = 28.6139;
                                             double longitude = 77.2090;
                                             String name = "";

                                             if(i!=0) {
                                                 for (int k = 0; k < markers.size(); k++) {
                                                     Marker m = markers.get(k);
                                                     m.remove();
                                                     Marker mm = markerNames.get(k);
                                                     mm.remove();
                                                 }
                                             }
                                             for (int j = 0; j < 3; j++) {
                                                 if(i==0)
                                                 {
                                                     latitude = latitude/2;


                                                 }
                                                 if(i==1)
                                                 {
                                                     longitude = longitude/2;
                                                 }
                                                 if(i==2)
                                                 {
                                                     latitude = latitude/2;
                                                     longitude = longitude/2;
                                                 }
                                                 latitude = latitude + (3*j);
                                                 longitude += (4*j);
                                                 name = friendName.get(j + kk);
                                                 Log.e("the value of i+j",(j + kk) + "");
                                                 Log.e("the value of friend",name);
                                                 getUpdatedCurrentLocation(latitude, longitude, name);
                                             }
                                             kk += 3;
                                         }
                                         catch(Exception e)
                                         {
                                             e.printStackTrace();
                                         }
                                     }
                                 });
                                 Thread.sleep(5000);

                             }
                         }
                         catch (InterruptedException e) {
                         }

                     }
                 };
                 th.start();
             }


             boolean makeConnection(String url) {
                 try {
                     client = new DefaultHttpClient();
                     final HttpParams httpParams = client.getParams();
                     HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // 10 seconds
                     HttpConnectionParams.setSoTimeout(httpParams, 10000);

                     post = new HttpPost("http://192.168.43.3/signUp.php");

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





         }

class LoadTaskAgain extends AsyncTask<String, String, String> {

    MapsActivity ma  = new MapsActivity();
    boolean check_flag = true;
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
        }

        if (check_flag) {

            check_flag = ma.generatePostRequest();
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

              /* *//* Log.e("before","object");  ------
                Log.e("the value of ma.res",ma.res);
                JSONObject obj = new JSONObject(ma.res); ---------
               *//* //JSONArray j = new JSONArray();
                // /JSONArray arr = obj.getJSONArray("ress");
                *//*String pname = obj.getString("name"); -----
                String Lat = obj.getString("Lat");
                float Latf = Float.parseFloat(Lat);

                String Lng = obj.getString("Lng");
                float Longf = Float.parseFloat(Lng); ----
                *//*// int length = arr.length();
              *//*  Log.e("the value object",pname); ----
                Log.e("value of Lat",Lat);
                Log.e("value of Long",Lng); -------
              *//*  // String ss = arr.getString("mes");

                *//*String recvd_text = "";
                for (int i = 0; i < length; i++) {
                    JSONObject obj3 = (JSONObject) arr.get(i);
                    String s = obj3.getString("mes");
                    //String bname = obj3.getString("Email");
                    Log.e("ans--->> ",s);

                }
*//*
              //  ma.MMap(Latf, Longf, pname);

              */

                Log.e("before", "object");
                Log.e("the value of ma.res", ma.res);
                JSONObject obj = new JSONObject(ma.res);
                //Log.e("this is", obj+"");
                //JSONArray j = new JSONArray();
                // /JSONArray arr = obj.getJSONArray("ress");
                String ss = obj.getString("mes");
                // int length = arr.length();
                Log.e("the value object", ss + "this should be sex bro");
                if (ss.equals("abc")) {
                    Log.e("this is fucking", "idiotic");

                    //context.startActivity(new Intent(context, MapsActivity.class));

                }
                // String ss = arr.getString("mes");

                /*String recvd_text = "";
                for (int i = 0; i < length; i++) {
                    JSONObject obj3 = (JSONObject) arr.get(i);
                    String s = obj3.getString("mes");
                    //String bname = obj3.getString("Email");
                    Log.e("ans--->> ",s);

                }


*/
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
