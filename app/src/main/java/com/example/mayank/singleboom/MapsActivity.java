package com.example.mayank.singleboom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener
         {

    //Our Map
    private GoogleMap mMap;

    //To store longitude and latitude from map
    public double longitude;
    public double latitude;

    //Buttons
   // private ImageButton buttonSave;
   // private ImageButton buttonCurrent;
   // private ImageButton buttonView;

    //Google ApiClient
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


            //getCurrentLocation();
            //new updatemymapp().execute();



    void startThread()
    {
        Thread th=new Thread(){

            @Override
            public void run(){
                //
                try
                {
                    for (;;)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {


                                getCurrentLocation();



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
                             for (i =0;i<3;i++)
                             {
                                 Log.e("val of i ",i+"");
                                 runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         try {
                                             double latitude = 28.6139;
                                             double longitude = 77.2090;


                                             if(i!=0) {
                                                 for (int k = 0; k < markers.size(); k++) {
                                                     Marker m = markers.get(k);
                                                     m.remove();
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
                                                 getUpdatedCurrentLocation(latitude, longitude);
                                             }
                                             for(int x=0;x<markers.size();x++)
                                             {
                                                 Marker mm = markers.get(x);
                                                 mm.showInfoWindow();
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

             public void getUpdatedCurrentLocation(double latitude, double longitude) {
                 if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                     // TODO: Consider calling

                     Log.e("inside ","to do consider");
                     return;
                 }
                 Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                 //Getting longitude and latitude


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


                 Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),
                         R.drawable.marker3);

                 Bitmap bmp2 = bitmap1.copy(bitmap1.getConfig(), true);

       /* Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmap1 = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.markk));*/
                 Bitmap bitmap2  = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),
                         R.drawable.hot));

                 Bitmap hott = bitmap2.copy(bitmap2.getConfig(), true);
                 int bitmap1Width = bitmap1.getWidth();
                 int bitmap1Height = bitmap1.getHeight();
                 int bitmap2Width = bitmap2.getWidth();
                 int bitmap2Height = bitmap2.getHeight();

                 float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);
                 float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);


       /*Bitmap overlayBitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(),
               R.drawable.markk));
       */ Canvas canvas = new Canvas(bmp2);

        /*canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.markk), new Matrix(), null);
        */canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                         R.drawable.hot), marginLeft, marginTop, null);

                 Marker marker = mMap.addMarker(new MarkerOptions().position(latLngg)
                         .icon(BitmapDescriptorFactory.fromBitmap(bmp2))
                         // Specifies the anchor to be at a particular point in the marker image.
                         .anchor(0.5f, 1));

                 mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngg));

                 //Animating the camera
                 mMap.animateCamera(CameraUpdateFactory.zoomTo(2));

                 markers.add(marker);



             }






         }

/*
class updatemymapp extends AsyncTask<Void,Void,Void>
{
    MapsActivity ma = new MapsActivity();



    @Override
    protected Void doInBackground(Void... params) {
        int t=200;
        while(t!=0) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ma.getCurrentLocation();
        */
/*    ma.latitude = marker.getPosition().latitude;
            ma.longitude = marker.getPosition().longitude;
            ma.moveMap();*//*

            t--;
        }
        return null;
    }
}*/

