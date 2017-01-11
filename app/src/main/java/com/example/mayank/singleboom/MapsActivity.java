package com.example.mayank.singleboom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
             private BoomMenuButton bmb;
    //To store longitude and latitude from map
    public double longitude;
    public double latitude;

    //Buttons
   // private ImageButton buttonSave;
   // private ImageButton buttonCurrent;
   // private ImageButton buttonView;

    //Google ApiClient
    private GoogleApiClient googleApiClient;


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
                            // When the boom-button corresponding this builder is clicked.
                            Toast.makeText(MapsActivity.this, "Clicked " + index, Toast.LENGTH_SHORT).show();
                        }
                    })

                    // Whether the image-view should rotate.
                    .rotateImage(false)

                    // Whether the text-view should rotate.
                    .rotateText(false)

                    // Whether the boom-button should have a shadow effect.
                    .shadowEffect(true)

                    // Set the horizontal shadow-offset of the boom-button.
                    .shadowOffsetX(20)

                    // Set the vertical shadow-offset of the boom-button.
                    .shadowOffsetY(0)

                    // Set the radius of shadow of the boom-button.
                    .shadowRadius(Util.dp2px(20))

                    // Set the color of the shadow of boom-button.

                    // Set the image resource when boom-button is at normal-state.
                    .normalImageRes(R.drawable.bee)

                    // Set the image drawable when boom-button is at normal-state.

                    // Set the image resource when boom-button is at highlighted-state.
                    .highlightedImageRes(R.drawable.bee)

                    // Set the image drawable when boom-button is at highlighted-state.


                    // Set the image resource when boom-button is at unable-state.
                    .unableImageRes(R.drawable.bee)

                    // Set the image drawable when boom-button is at unable-state.


                    // Set the rect of image.
                    // By this method, you can set the position and size of the image-view in boom-button.
                    // For example, builder.imageRect(new Rect(0, 50, 100, 100)) will make the
                    // image-view's size to be 100 * 50 and margin-top to be 50 pixel.


                    // Set the padding of image.
                    // By this method, you can control the padding in the image-view.
                    // For instance, builder.imagePadding(new Rect(10, 10, 10, 10)) will make the
                    // image-view content 10-pixel padding to itself.


                    // Set the text resource when boom-button is at normal-state.
                    .normalTextRes(R.string.text_outside_circle_button_text_normal)

                    // Set the text resource when boom-button is at highlighted-state.
                    .highlightedTextRes(R.string.text_outside_circle_button_text_highlighted)

                    // Set the text resource when boom-button is at unable-state.
                    .unableTextRes(R.string.text_outside_circle_button_text_unable)

                    // Set the text when boom-button is at normal-state.
                    .normalText("Put your normal text here")

                    // Set the text when boom-button is at highlighted-state.
                    .highlightedText("Put your highlighted text here")

                    // Set the text when boom-button is at unable-state.
                    .unableText("Unable!")

                    // Set the color of text when boom-button is at normal-state.


                    // Set the top-margin between text-view and the circle button.
                    // Don't need to mind the shadow, BMB will mind this in code.
                    .textTopMargin(20)

                    // The width of text-view.
                    .textWidth(200)

                    // The height of text-view
                    .textHeight(50)

                    // Set the padding of text.
                    // By this method, you can control the padding in the text-view.
                    // For instance, builder.textPadding(new Rect(10, 10, 10, 10)) will make the
                    // text-view content 10-pixel padding to itself.


                    // Set the typeface of the text.


                    // Set the maximum of the lines of text-view.
                    .maxLines(2)

                    // Set the gravity of text-view.
                    .textGravity(Gravity.CENTER)

                    // Set the ellipsize of the text-view.
                    .ellipsize(TextUtils.TruncateAt.MIDDLE)

                    // Set the text size of the text-view.
                    .textSize(10)

                    // Whether the boom-button should have a ripple effect.
                    .rippleEffect(true)

                    // The color of boom-button when it is at normal-state.


                    // Whether the boom-button is unable, default value is false.
                    .unable(true)

                    // The radius of boom-button, in pixel.
                    .buttonRadius(Util.dp2px(40));
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

