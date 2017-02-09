package com.example.mayank.singleboom;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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
             private static int[] imageResources = new int[]{
                     R.drawable.friends,
                     R.drawable.messages,
                     R.drawable.about,
                     R.drawable.signout,
             };
             private static String[] stringResources = new String[]{
                     "Friends",
                     "Message",
                     "About Us",
                     "Sign Out",
             };
             private static int imageResourceIndex = 0;
             private static int stringResourceIndex = 0;

             public int i;
             Button b;
             public Bitmap orginalBitmap;
             public List<Marker> markers = new ArrayList<Marker>();
             public List<Marker> markerNames = new ArrayList<Marker>();
             public List<String> friendName = new ArrayList<String>();
             public HashMap<String, String> contact = new HashMap<String, String>();
             public BoomMenuButton bmb;


             public int kk = 0;
             public String toSend = "";

             HttpClient client;
             HttpPost post;
             HttpResponse response;
             // Context context;
             public String res;
             public String ress;
             boolean check_flag = true;

            private GoogleApiClient googleApiClient;


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
                            {
                                Intent i1 = new Intent(getApplicationContext(), ContactActivity.class);
                                startActivityForResult(i1, 1);
                            }
                            if(index == 2) {
                                //Intent i2 = new Intent(getApplicationContext(), SignOutActivity.class);
                                //startActivity(i2);
                            }


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

             @Override
             protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                 Log.e("the request code", requestCode + "");
                 if (requestCode == 1) {
                     if(resultCode == Activity.RESULT_OK){

                         contact = (HashMap<String, String>)data.getSerializableExtra("result");
                         startFriendThread();

                     }
                     if (resultCode == Activity.RESULT_CANCELED) {

                     }
                 }
             }

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
        Log.e("inside","getcurrentlocation");
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
            Log.e("inside","getlocation");
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


                 mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngg));
                 mMap.getUiSettings().setScrollGesturesEnabled(true);


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



                             for(String key : contact.keySet()){

                                 toSend += key.substring(key.length() - 10);
                             }

                             if (check_flag) {

                                 check_flag = makeConnection("https://sporophoric-reservo.000webhostapp.com/retrieveContact.php");
                             }


                             for (;;)
                             {

                                 runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         try {

                                             if (check_flag) {

                                                 check_flag = generatePostRequest();
                                             }

                                             if (check_flag) {

                                                 ress  = getRequestResponse();
                                             }
                                             if (ress != null) {
                                                 try {


                                                     Log.e("before", "object");
                                                     Log.e("the value of friend", ress);
                                                     JSONObject obj = new JSONObject(ress);

                                                     String ss = obj.getString("mess");

                                                     Log.e("the value object", ss + "this should be sex bro");
                                                     if (ss.equals("yes")) {
                                                         Log.e("the value of the friend","yes");

                                                        //getUpdatedCurrentLocation();
                                                     }

                                                 } catch (JSONException e) {
                                                     e.printStackTrace();
                                                 }
                                             }
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

                     post = new HttpPost("https://sporophoric-reservo.000webhostapp.com/retrieveContact.php");

                     return true;
                 } catch (Exception e) {

                     e.printStackTrace();
                     return false;
                 }

             }

             public boolean generatePostRequest() {
                 try {

                     List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

                     nameValuePairs.add(new BasicNameValuePair("numberString", toSend));

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

