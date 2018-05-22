package com.navigation.com.nb;

/**
 * Created by Admin on 3/18/2018.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Admin on 3/17/2018.
 */

public class MapsActivityGeo_shortest extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;
    private GoogleMap mMap;
    private GoogleMap mMap1;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    Polyline polylinefinal;
    Marker marker1,marker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_geoshortest);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<>();
        Button btn = (Button) findViewById(R.id.button1);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showhelp();
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap1 = googleMap;
        mMap1.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap1.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap1.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap1.setMyLocationEnabled(true);
        }

        mMap1.setMyLocationEnabled(true);
        mMap1.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //Reset marker when already 2
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(latLng);
                if (listPoints.size() == 2) {
                    listPoints.clear();
                    polylinefinal.remove();
                    marker1.remove();
                    marker2.remove();
                }
                //Save first point select
                listPoints.add(latLng);
                //Create marker


                if (listPoints.size() == 1) {
                    //Add first marker to the map
                    markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    marker1=mMap1.addMarker(markerOption);
                } else {
                    //Add second marker to the map
                    markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    marker2=mMap1.addMarker(markerOption);
                }
                if (listPoints.size() == 2) {
                    //Create the URL to get request from first marker to second marker
                    String url = getRequestUrl(listPoints.get(0), listPoints.get(1));
                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                    taskRequestDirections.execute(url);
                }
            }
        });
        mMap1.setMapType(mMap1.MAP_TYPE_HYBRID);
        mMap.setMapType(mMap.MAP_TYPE_HYBRID);
    }
    public void showhelp() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Help")
                .setMessage("1. Single click on the marker for seing the image and name of the Building.\n"+
                        "2. Long press on any location to set a marker for finding the route between two markers.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
// continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        //mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);


        InfoWindowData info_mba = new InfoWindowData();
        info_mba.setImage("mba1");
        LatLng mba = new LatLng(23.842846, 91.421994);
        Marker m_mba = mMap.addMarker(new MarkerOptions().position(mba).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("MBA Building"));
        m_mba.setTag(info_mba);

        InfoWindowData info_kp = new InfoWindowData();
        info_kp.setImage("kp1");
        LatLng kpark = new LatLng(23.840949, 91.421292);
        Marker m_kp = mMap.addMarker(new MarkerOptions().position(kpark).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Knowledge Park"));
        m_kp.setTag(info_kp);


        InfoWindowData info_gh = new InfoWindowData();
        info_gh.setImage("igh1");
        LatLng guest = new LatLng(23.841106, 91.427740);
        Marker m_gh = mMap.addMarker(new MarkerOptions().position(guest).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("International Guest House"));
        m_gh.setTag(info_gh);

        InfoWindowData info_cse = new InfoWindowData();
        info_cse.setImage("cse1");
        LatLng cse = new LatLng(23.840876, 91.423615);
        Marker m_cse = mMap.addMarker(new MarkerOptions().position(cse).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("CSE Building"));
        m_cse.setTag(info_cse);

        InfoWindowData info_maincircle = new InfoWindowData();
        info_maincircle.setImage("main_circle1");
        LatLng maincircle = new LatLng(23.838500, 91.422290);
        Marker m_maincircle = mMap.addMarker(new MarkerOptions().position(maincircle).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Main Circle"));
        m_maincircle.setTag(info_maincircle);

        InfoWindowData info_ece = new InfoWindowData();
        info_ece.setImage("ece1");
        LatLng ece = new LatLng(23.839428, 91.423341);
        Marker m_ece = mMap.addMarker(new MarkerOptions().position(ece).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("ECE Department"));
        m_ece.setTag(info_ece);

        InfoWindowData info_maingate = new InfoWindowData();
        info_maingate.setImage("main_gate1");
        LatLng maingate = new LatLng(23.837271, 91.422204);
        Marker m_maingate = mMap.addMarker(new MarkerOptions().position(maingate).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Main Gate"));
        m_maingate.setTag(info_maingate);

        InfoWindowData info_TNP = new InfoWindowData();
        info_TNP.setImage("tp1");
        LatLng TNP = new LatLng(23.839867,91.421404);
        Marker m_TNP = mMap.addMarker(new MarkerOptions().position(TNP).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Training and Placment Centre"));
        m_TNP.setTag(info_TNP);

        InfoWindowData info_maingate2 = new InfoWindowData();
        info_maingate2.setImage("mg21");
        LatLng maingate2 = new LatLng(23.842065,91.417553);
        Marker m_mg2 = mMap.addMarker(new MarkerOptions().position(maingate2).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Maingate Second"));
        m_mg2.setTag(info_maingate2);

        InfoWindowData info_mainbuilding = new InfoWindowData();
        info_mainbuilding.setImage("mb1");
        LatLng mainbuilding = new LatLng(23.840260,91.420739);
        Marker m_mainbuilding = mMap.addMarker(new MarkerOptions().position(mainbuilding).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Main Building"));
        m_mainbuilding.setTag(info_mainbuilding);

        InfoWindowData info_Xeroxshop = new InfoWindowData();
        info_Xeroxshop.setImage("xeroxshop1");
        LatLng Xeroxshop = new LatLng(23.840179,91.420235);
        Marker m_Xeroxshop = mMap.addMarker(new MarkerOptions().position(Xeroxshop).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Xerox Shop Centre"));
        m_Xeroxshop.setTag(info_Xeroxshop);

        InfoWindowData info_coffeehouse = new InfoWindowData();
        info_coffeehouse.setImage("coffee_house1");
        LatLng coffeehouse = new LatLng(23.839723,91.423588);
        Marker m_coffeehouse = mMap.addMarker(new MarkerOptions().position(coffeehouse).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Coffee House"));
        m_coffeehouse.setTag(info_coffeehouse);


        InfoWindowData info_Sacbuilding = new InfoWindowData();
        info_Sacbuilding.setImage("sac1");
        LatLng Sacbuilding = new LatLng(23.840733,91.421099);
        Marker m_Sacbuilding = mMap.addMarker(new MarkerOptions().position(Sacbuilding).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Student Activity Centre"));
        m_Sacbuilding.setTag(info_Sacbuilding);

        InfoWindowData info_foodies = new InfoWindowData();
        info_foodies.setImage("foodies1");
        LatLng foodies = new LatLng(23.840851,91.421049);
        Marker m_foodies = mMap.addMarker(new MarkerOptions().position(foodies).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Foodies"));
        m_foodies.setTag(info_foodies);

        InfoWindowData info_ab = new InfoWindowData();
        info_ab.setImage("academic1");
        LatLng ab = new LatLng(23.840390, 91.421614);
        Marker m_ab = mMap.addMarker(new MarkerOptions().position(ab).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Academic Building"));
        m_ab.setTag(info_ab);

        InfoWindowData info_PNB = new InfoWindowData();
        info_PNB.setImage("pnb1");
        LatLng PNB = new LatLng(23.841303,91.422000);
        Marker m_PNB = mMap.addMarker(new MarkerOptions().position(PNB).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("PNB ATM"));
        m_PNB.setTag(info_PNB);

        InfoWindowData info_KVbuilding = new InfoWindowData();
        info_KVbuilding.setImage("kv1");
        LatLng KVbuilding = new LatLng(23.842176,91.418567);
        Marker m_KVbuilding = mMap.addMarker(new MarkerOptions().position(KVbuilding).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Kendriya Vidhyalaya Building"));
        m_KVbuilding.setTag(info_KVbuilding);


        InfoWindowData info_db = new InfoWindowData();
        info_db.setImage("db1");
        LatLng db = new LatLng(23.840933,91.428633);
        Marker m_db = mMap.addMarker(new MarkerOptions().position(db).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Director Bungalow"));
        m_db.setTag(info_db);

        InfoWindowData info_Cdacbuilding = new InfoWindowData();
        info_Cdacbuilding.setImage("cdac1");
        LatLng Cdacbuilding = new LatLng(23.841209,91.420695);
        Marker m_Cdacbuilding = mMap.addMarker(new MarkerOptions().position(Cdacbuilding).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("CDAC Building"));
        m_Cdacbuilding.setTag(info_Cdacbuilding);


        InfoWindowData info_bnp = new InfoWindowData();
        info_bnp.setImage("postofficencanara1");
        LatLng bnp = new LatLng(23.843511,91.419213);
        Marker m_bnp = mMap.addMarker(new MarkerOptions().position(bnp).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Post Office"));
        m_bnp.setTag(info_bnp);

        InfoWindowData info_sim = new InfoWindowData();
        info_sim.setImage("southmess1");
        LatLng sim = new LatLng(23.843096,91.421793);
        Marker m_sim = mMap.addMarker(new MarkerOptions().position(sim).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("South Indian Mess"));
        m_sim.setTag(info_sim);

        InfoWindowData info_audi = new InfoWindowData();
        info_audi.setImage("audi1");
        LatLng audi = new LatLng(23.842098,91.421989);
        Marker m_audi = mMap.addMarker(new MarkerOptions().position(audi).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Auditorium"));
        m_audi.setTag(info_audi);



        InfoWindowData info_foodcourt = new InfoWindowData();
        info_foodcourt.setImage("foodcourt1");
        LatLng foodcourt = new LatLng(23.841344,91.422251);
        Marker m_foodcourt = mMap.addMarker(new MarkerOptions().position(foodcourt).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Food Court"));
        m_foodcourt.setTag(info_foodcourt);

        InfoWindowData info_NitaHospital = new InfoWindowData();
        info_NitaHospital.setImage("nitah1");
        LatLng NitaHospital = new LatLng(23.842389,91.424576);
        Marker m_NitaHospital = mMap.addMarker(new MarkerOptions().position(NitaHospital).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("NITA Hospital"));
        m_NitaHospital.setTag(info_NitaHospital);


        InfoWindowData info_newbuilding = new InfoWindowData();
        info_newbuilding.setImage("nb1");
        LatLng newbuilding = new LatLng(23.840718, 91.420387);
        Marker m_newbuilding = mMap.addMarker(new MarkerOptions().position(newbuilding).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("New Building"));
        m_newbuilding.setTag(info_newbuilding);

        InfoWindowData info_workshop = new InfoWindowData();
        info_workshop.setImage("workshop1");
        LatLng workshop = new LatLng(23.841842, 91.420444);
        Marker m_workshop = mMap.addMarker(new MarkerOptions().position(workshop).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Workshop"));
        m_workshop.setTag(info_workshop);


        InfoWindowData info_facultyquarter = new InfoWindowData();
        info_facultyquarter.setImage("fq1");
        LatLng facultyquarter = new LatLng(23.842479, 91.430248);
        Marker m_facultyquarter = mMap.addMarker(new MarkerOptions().position(facultyquarter).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Faculty Quarter"));
        m_facultyquarter.setTag(info_facultyquarter);


        InfoWindowData info_aryabhattaHostel = new InfoWindowData();
        info_aryabhattaHostel.setImage("aryabhatta1");
        LatLng aryabhattaHostel = new LatLng(23.844060, 91.421131);
        Marker m_aryabhattaHostel = mMap.addMarker(new MarkerOptions().position(aryabhattaHostel).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Aryabhatta Hostel"));
        m_aryabhattaHostel.setTag(info_aryabhattaHostel);


        InfoWindowData info_cb = new InfoWindowData();
        info_cb.setImage("canaratm1");
        LatLng cb = new LatLng(23.842166, 91.422161);
        Marker m_cb = mMap.addMarker(new MarkerOptions().position(cb).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Canara Bank Atm"));
        m_cb.setTag(info_cb);

        InfoWindowData info_TsrCircle = new InfoWindowData();
        info_TsrCircle.setImage("tsrcircle1");
        LatLng TsrCircle = new LatLng(23.846248, 91.428995);
        Marker m_TsrCircle = mMap.addMarker(new MarkerOptions().position(TsrCircle).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("TSR Circle"));
        m_TsrCircle.setTag(info_TsrCircle);

        InfoWindowData info_DhalaiHostel = new InfoWindowData();
        info_DhalaiHostel.setImage("dhalai1");
        LatLng DhalaiHostel = new LatLng(23.844811, 91.423159);
        Marker m_DhalaiHostel = mMap.addMarker(new MarkerOptions().position(DhalaiHostel).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Dhalai Hostel"));
        m_DhalaiHostel.setTag(info_DhalaiHostel);

        InfoWindowData info_Collegepoint = new InfoWindowData();
        info_Collegepoint.setImage("cp1");
        LatLng Collegepoint = new LatLng(23.845635, 91.427375);
        Marker m_Collegepoint = mMap.addMarker(new MarkerOptions().position(Collegepoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("College Point"));
        m_Collegepoint.setTag(info_Collegepoint);

        InfoWindowData info_sc = new InfoWindowData();
        info_sc.setImage("shopping1");
        LatLng sc = new LatLng(23.849271, 91.431066);
        Marker m_sc = mMap.addMarker(new MarkerOptions().position(sc).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Shopping Complex"));
        m_sc.setTag(info_sc);

        InfoWindowData info_amul = new InfoWindowData();
        info_amul.setImage("amul1");
        LatLng amul = new LatLng(23.843437, 91.426340);
        Marker m_amul = mMap.addMarker(new MarkerOptions().position(amul).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Amul Canteen"));
        m_amul.setTag(info_amul);

        InfoWindowData info_OldGuesthouse = new InfoWindowData();
        info_OldGuesthouse.setImage("oldgh1");
        LatLng OldGuesthouse = new LatLng(23.843041, 91.426239);
        Marker m_OldGuesthouse = mMap.addMarker(new MarkerOptions().position(OldGuesthouse).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Old Guest House"));
        m_OldGuesthouse.setTag(info_OldGuesthouse);

        InfoWindowData info_raimaHostel = new InfoWindowData();
        info_raimaHostel.setImage("raimahostel1");
        LatLng raimaHostel = new LatLng(23.842850, 91.425764);
        Marker m_raimaHostel = mMap.addMarker(new MarkerOptions().position(raimaHostel).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Raima Hostel"));
        m_raimaHostel.setTag(info_raimaHostel);

        InfoWindowData info_ns = new InfoWindowData();
        info_ns.setImage("ground1");
        LatLng ns = new LatLng(23.844080, 91.425479);
        Marker m_ns = mMap.addMarker(new MarkerOptions().position(ns).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Play Ground"));
        m_ns.setTag(info_ns);

        InfoWindowData info_SportsComplex = new InfoWindowData();
        info_SportsComplex.setImage("sportscomplex1");
        LatLng SportsComplex = new LatLng(23.848854, 91.426935);
        Marker m_SportsComplex = mMap.addMarker(new MarkerOptions().position(SportsComplex).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Sports Complex"));
        m_SportsComplex.setTag(info_SportsComplex);

        InfoWindowData info_Fruitshop = new InfoWindowData();
        info_Fruitshop.setImage("fruit1");
        LatLng Fruitshop = new LatLng(23.845091, 91.426141);
        Marker m_Fruitshop = mMap.addMarker(new MarkerOptions().position(Fruitshop).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Fruit Shop"));
        m_Fruitshop.setTag(info_Fruitshop);



        InfoWindowData info_GargiHostel = new InfoWindowData();
        info_GargiHostel.setImage("gargi1");
        LatLng GargiHostel = new LatLng(23.844536, 91.428695);
        Marker m_GargiHostel = mMap.addMarker(new MarkerOptions().position(GargiHostel).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Gargi hostel"));
        m_GargiHostel.setTag(info_GargiHostel);


        InfoWindowData info_GomatiHostel = new InfoWindowData();
        info_GomatiHostel.setImage("gomti1");
        LatLng GomatiHostel = new LatLng(23.843805, 91.423926);
        Marker m_GomatiHostel = mMap.addMarker(new MarkerOptions().position(GomatiHostel).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Gomati Hostel"));
        m_GomatiHostel.setTag(info_GomatiHostel);



        InfoWindowData info_Sbi = new InfoWindowData();
        info_Sbi.setImage("sbiatm1");
        LatLng Sbi = new LatLng(23.842034, 91.421911);
        Marker m_Sbi = mMap.addMarker(new MarkerOptions().position(Sbi).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("SBI ATM"));
        m_Sbi.setTag(info_Sbi);

        InfoWindowData info_library = new InfoWindowData();
        info_library.setImage("library1");
        LatLng library = new LatLng(23.841216, 91.424326);
        Marker m_library = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).position(library).title("Library"));
        m_library.setTag(info_library);


        InfoWindowData info_Sbi_bank = new InfoWindowData();
        info_Sbi_bank.setImage("sbibank1");
        LatLng Sbi_bank = new LatLng(23.843457, 91.419275);
        Marker m_Sbi_bank = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).position(Sbi_bank).title("State Bank Of India"));
        m_Sbi_bank.setTag(info_Sbi_bank);


        InfoWindowData info_canara_bank = new InfoWindowData();
        info_canara_bank.setImage("cb1");
        LatLng canara_bank = new LatLng(23.843606, 91.419151);
        Marker m_canara_bank = mMap.addMarker(new MarkerOptions().position(canara_bank).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Canara Bank"));
        m_canara_bank.setTag(info_canara_bank);





        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        /*if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }*/
                        mMap.setMyLocationEnabled(true);

                        //mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(23.843770,91.424849) , 14.0f) );
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }

                CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
                mMap.setInfoWindowAdapter(customInfoWindow);


                InfoWindowData info_mba = new InfoWindowData();
                info_mba.setImage("mba1");
                LatLng mba = new LatLng(23.842846, 91.421994);
                Marker m_mba = mMap.addMarker(new MarkerOptions().position(mba).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("MBA Building"));
                m_mba.setTag(info_mba);

                InfoWindowData info_kp = new InfoWindowData();
                info_kp.setImage("kp1");
                LatLng kpark = new LatLng(23.840949, 91.421292);
                Marker m_kp = mMap.addMarker(new MarkerOptions().position(kpark).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Knowledge Park"));
                m_kp.setTag(info_kp);


                InfoWindowData info_gh = new InfoWindowData();
                info_gh.setImage("igh1");
                LatLng guest = new LatLng(23.841106, 91.427740);
                Marker m_gh = mMap.addMarker(new MarkerOptions().position(guest).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("International Guest House"));
                m_gh.setTag(info_gh);

                InfoWindowData info_cse = new InfoWindowData();
                info_cse.setImage("cse1");
                LatLng cse = new LatLng(23.840876, 91.423615);
                Marker m_cse = mMap.addMarker(new MarkerOptions().position(cse).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("CSE Building"));
                m_cse.setTag(info_cse);

                InfoWindowData info_maincircle = new InfoWindowData();
                info_maincircle.setImage("main_circle1");
                LatLng maincircle = new LatLng(23.838500, 91.422290);
                Marker m_maincircle = mMap.addMarker(new MarkerOptions().position(maincircle).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Main Circle"));
                m_maincircle.setTag(info_maincircle);

                InfoWindowData info_ece = new InfoWindowData();
                info_ece.setImage("ece1");
                LatLng ece = new LatLng(23.839428, 91.423341);
                Marker m_ece = mMap.addMarker(new MarkerOptions().position(ece).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("ECE Department"));
                m_ece.setTag(info_ece);

                InfoWindowData info_maingate = new InfoWindowData();
                info_maingate.setImage("main_gate1");
                LatLng maingate = new LatLng(23.837271, 91.422204);
                Marker m_maingate = mMap.addMarker(new MarkerOptions().position(maingate).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Main Gate"));
                m_maingate.setTag(info_maingate);

                InfoWindowData info_TNP = new InfoWindowData();
                info_TNP.setImage("tp1");
                LatLng TNP = new LatLng(23.839867,91.421404);
                Marker m_TNP = mMap.addMarker(new MarkerOptions().position(TNP).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Training and Placment Centre"));
                m_TNP.setTag(info_TNP);

                InfoWindowData info_maingate2 = new InfoWindowData();
                info_maingate2.setImage("mg21");
                LatLng maingate2 = new LatLng(23.842065,91.417553);
                Marker m_mg2 = mMap.addMarker(new MarkerOptions().position(maingate2).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Maingate Second"));
                m_mg2.setTag(info_maingate2);

                InfoWindowData info_mainbuilding = new InfoWindowData();
                info_mainbuilding.setImage("mb1");
                LatLng mainbuilding = new LatLng(23.840260,91.420739);
                Marker m_mainbuilding = mMap.addMarker(new MarkerOptions().position(mainbuilding).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Main Building"));
                m_mainbuilding.setTag(info_mainbuilding);

                InfoWindowData info_Xeroxshop = new InfoWindowData();
                info_Xeroxshop.setImage("xeroxshop1");
                LatLng Xeroxshop = new LatLng(23.840179,91.420235);
                Marker m_Xeroxshop = mMap.addMarker(new MarkerOptions().position(Xeroxshop).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Xerox Shop Centre"));
                m_Xeroxshop.setTag(info_Xeroxshop);

                InfoWindowData info_coffeehouse = new InfoWindowData();
                info_coffeehouse.setImage("coffee_house1");
                LatLng coffeehouse = new LatLng(23.839723,91.423588);
                Marker m_coffeehouse = mMap.addMarker(new MarkerOptions().position(coffeehouse).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Coffee House"));
                m_coffeehouse.setTag(info_coffeehouse);


                InfoWindowData info_Sacbuilding = new InfoWindowData();
                info_Sacbuilding.setImage("sac1");
                LatLng Sacbuilding = new LatLng(23.840733,91.421099);
                Marker m_Sacbuilding = mMap.addMarker(new MarkerOptions().position(Sacbuilding).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Student Activity Centre"));
                m_Sacbuilding.setTag(info_Sacbuilding);

                InfoWindowData info_foodies = new InfoWindowData();
                info_foodies.setImage("foodies1");
                LatLng foodies = new LatLng(23.840851,91.421049);
                Marker m_foodies = mMap.addMarker(new MarkerOptions().position(foodies).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Foodies"));
                m_foodies.setTag(info_foodies);

                InfoWindowData info_ab = new InfoWindowData();
                info_ab.setImage("academic1");
                LatLng ab = new LatLng(23.840390, 91.421614);
                Marker m_ab = mMap.addMarker(new MarkerOptions().position(ab).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Academic Building"));
                m_ab.setTag(info_ab);

                InfoWindowData info_PNB = new InfoWindowData();
                info_PNB.setImage("pnb1");
                LatLng PNB = new LatLng(23.841303,91.422000);
                Marker m_PNB = mMap.addMarker(new MarkerOptions().position(PNB).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("PNB ATM"));
                m_PNB.setTag(info_PNB);

                InfoWindowData info_KVbuilding = new InfoWindowData();
                info_KVbuilding.setImage("kv1");
                LatLng KVbuilding = new LatLng(23.842176,91.418567);
                Marker m_KVbuilding = mMap.addMarker(new MarkerOptions().position(KVbuilding).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Kendriya Vidhyalaya Building"));
                m_KVbuilding.setTag(info_KVbuilding);


                InfoWindowData info_db = new InfoWindowData();
                info_db.setImage("db1");
                LatLng db = new LatLng(23.840933,91.428633);
                Marker m_db = mMap.addMarker(new MarkerOptions().position(db).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Director Bungalow"));
                m_db.setTag(info_db);

                InfoWindowData info_Cdacbuilding = new InfoWindowData();
                info_Cdacbuilding.setImage("cdac1");
                LatLng Cdacbuilding = new LatLng(23.841209,91.420695);
                Marker m_Cdacbuilding = mMap.addMarker(new MarkerOptions().position(Cdacbuilding).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("CDAC Building"));
                m_Cdacbuilding.setTag(info_Cdacbuilding);


                InfoWindowData info_bnp = new InfoWindowData();
                info_bnp.setImage("postofficencanara1");
                LatLng bnp = new LatLng(23.843511,91.419213);
                Marker m_bnp = mMap.addMarker(new MarkerOptions().position(bnp).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Post Office"));
                m_bnp.setTag(info_bnp);

                InfoWindowData info_sim = new InfoWindowData();
                info_sim.setImage("southmess1");
                LatLng sim = new LatLng(23.843096,91.421793);
                Marker m_sim = mMap.addMarker(new MarkerOptions().position(sim).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("South Indian Mess"));
                m_sim.setTag(info_sim);

                InfoWindowData info_audi = new InfoWindowData();
                info_audi.setImage("audi1");
                LatLng audi = new LatLng(23.842098,91.421989);
                Marker m_audi = mMap.addMarker(new MarkerOptions().position(audi).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Auditorium"));
                m_audi.setTag(info_audi);



                InfoWindowData info_foodcourt = new InfoWindowData();
                info_foodcourt.setImage("foodcourt1");
                LatLng foodcourt = new LatLng(23.841344,91.422251);
                Marker m_foodcourt = mMap.addMarker(new MarkerOptions().position(foodcourt).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Food Court"));
                m_foodcourt.setTag(info_foodcourt);

                InfoWindowData info_NitaHospital = new InfoWindowData();
                info_NitaHospital.setImage("nitah1");
                LatLng NitaHospital = new LatLng(23.842389,91.424576);
                Marker m_NitaHospital = mMap.addMarker(new MarkerOptions().position(NitaHospital).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("NITA Hospital"));
                m_NitaHospital.setTag(info_NitaHospital);


                InfoWindowData info_newbuilding = new InfoWindowData();
                info_newbuilding.setImage("nb1");
                LatLng newbuilding = new LatLng(23.840718, 91.420387);
                Marker m_newbuilding = mMap.addMarker(new MarkerOptions().position(newbuilding).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("New Building"));
                m_newbuilding.setTag(info_newbuilding);

                InfoWindowData info_workshop = new InfoWindowData();
                info_workshop.setImage("workshop1");
                LatLng workshop = new LatLng(23.841842, 91.420444);
                Marker m_workshop = mMap.addMarker(new MarkerOptions().position(workshop).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Workshop"));
                m_workshop.setTag(info_workshop);


                InfoWindowData info_facultyquarter = new InfoWindowData();
                info_facultyquarter.setImage("fq1");
                LatLng facultyquarter = new LatLng(23.842479, 91.430248);
                Marker m_facultyquarter = mMap.addMarker(new MarkerOptions().position(facultyquarter).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Faculty Quarter"));
                m_facultyquarter.setTag(info_facultyquarter);


                InfoWindowData info_aryabhattaHostel = new InfoWindowData();
                info_aryabhattaHostel.setImage("aryabhatta1");
                LatLng aryabhattaHostel = new LatLng(23.844060, 91.421131);
                Marker m_aryabhattaHostel = mMap.addMarker(new MarkerOptions().position(aryabhattaHostel).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Aryabhatta Hostel"));
                m_aryabhattaHostel.setTag(info_aryabhattaHostel);


                InfoWindowData info_cb = new InfoWindowData();
                info_cb.setImage("canaratm1");
                LatLng cb = new LatLng(23.842166, 91.422161);
                Marker m_cb = mMap.addMarker(new MarkerOptions().position(cb).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Canara Bank Atm"));
                m_cb.setTag(info_cb);

                InfoWindowData info_TsrCircle = new InfoWindowData();
                info_TsrCircle.setImage("tsrcircle1");
                LatLng TsrCircle = new LatLng(23.846248, 91.428995);
                Marker m_TsrCircle = mMap.addMarker(new MarkerOptions().position(TsrCircle).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("TSR Circle"));
                m_TsrCircle.setTag(info_TsrCircle);

                InfoWindowData info_DhalaiHostel = new InfoWindowData();
                info_DhalaiHostel.setImage("dhalai1");
                LatLng DhalaiHostel = new LatLng(23.844811, 91.423159);
                Marker m_DhalaiHostel = mMap.addMarker(new MarkerOptions().position(DhalaiHostel).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Dhalai Hostel"));
                m_DhalaiHostel.setTag(info_DhalaiHostel);

                InfoWindowData info_Collegepoint = new InfoWindowData();
                info_Collegepoint.setImage("cp1");
                LatLng Collegepoint = new LatLng(23.845635, 91.427375);
                Marker m_Collegepoint = mMap.addMarker(new MarkerOptions().position(Collegepoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("College Point"));
                m_Collegepoint.setTag(info_Collegepoint);

                InfoWindowData info_sc = new InfoWindowData();
                info_sc.setImage("shopping1");
                LatLng sc = new LatLng(23.849271, 91.431066);
                Marker m_sc = mMap.addMarker(new MarkerOptions().position(sc).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Shopping Complex"));
                m_sc.setTag(info_sc);

                InfoWindowData info_amul = new InfoWindowData();
                info_amul.setImage("amul1");
                LatLng amul = new LatLng(23.843437, 91.426340);
                Marker m_amul = mMap.addMarker(new MarkerOptions().position(amul).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Amul Canteen"));
                m_amul.setTag(info_amul);

                InfoWindowData info_OldGuesthouse = new InfoWindowData();
                info_OldGuesthouse.setImage("oldgh1");
                LatLng OldGuesthouse = new LatLng(23.843041, 91.426239);
                Marker m_OldGuesthouse = mMap.addMarker(new MarkerOptions().position(OldGuesthouse).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Old Guest House"));
                m_OldGuesthouse.setTag(info_OldGuesthouse);

                InfoWindowData info_raimaHostel = new InfoWindowData();
                info_raimaHostel.setImage("raimahostel1");
                LatLng raimaHostel = new LatLng(23.842850, 91.425764);
                Marker m_raimaHostel = mMap.addMarker(new MarkerOptions().position(raimaHostel).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Raima Hostel"));
                m_raimaHostel.setTag(info_raimaHostel);

                InfoWindowData info_ns = new InfoWindowData();
                info_ns.setImage("ground1");
                LatLng ns = new LatLng(23.844080, 91.425479);
                Marker m_ns = mMap.addMarker(new MarkerOptions().position(ns).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Play Ground"));
                m_ns.setTag(info_ns);

                InfoWindowData info_SportsComplex = new InfoWindowData();
                info_SportsComplex.setImage("sportscomplex1");
                LatLng SportsComplex = new LatLng(23.848854, 91.426935);
                Marker m_SportsComplex = mMap.addMarker(new MarkerOptions().position(SportsComplex).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Sports Complex"));
                m_SportsComplex.setTag(info_SportsComplex);

                InfoWindowData info_Fruitshop = new InfoWindowData();
                info_Fruitshop.setImage("fruit1");
                LatLng Fruitshop = new LatLng(23.845091, 91.426141);
                Marker m_Fruitshop = mMap.addMarker(new MarkerOptions().position(Fruitshop).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Fruit Shop"));
                m_Fruitshop.setTag(info_Fruitshop);



                InfoWindowData info_GargiHostel = new InfoWindowData();
                info_GargiHostel.setImage("gargi1");
                LatLng GargiHostel = new LatLng(23.844536, 91.428695);
                Marker m_GargiHostel = mMap.addMarker(new MarkerOptions().position(GargiHostel).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Gargi hostel"));
                m_GargiHostel.setTag(info_GargiHostel);


                InfoWindowData info_GomatiHostel = new InfoWindowData();
                info_GomatiHostel.setImage("gomti1");
                LatLng GomatiHostel = new LatLng(23.843805, 91.423926);
                Marker m_GomatiHostel = mMap.addMarker(new MarkerOptions().position(GomatiHostel).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Gomati Hostel"));
                m_GomatiHostel.setTag(info_GomatiHostel);



                InfoWindowData info_Sbi = new InfoWindowData();
                info_Sbi.setImage("sbiatm1");
                LatLng Sbi = new LatLng(23.842034, 91.421911);
                Marker m_Sbi = mMap.addMarker(new MarkerOptions().position(Sbi).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("SBI ATM"));
                m_Sbi.setTag(info_Sbi);

                InfoWindowData info_library = new InfoWindowData();
                info_library.setImage("library1");
                LatLng library = new LatLng(23.841216, 91.424326);
                Marker m_library = mMap.addMarker(new MarkerOptions().position(library).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Library"));
                m_library.setTag(info_library);


                InfoWindowData info_Sbi_bank = new InfoWindowData();
                info_Sbi_bank.setImage("sbibank1");
                LatLng Sbi_bank = new LatLng(23.843457, 91.419275);
                Marker m_Sbi_bank = mMap.addMarker(new MarkerOptions().position(Sbi_bank).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("State Bank Of India"));
                m_Sbi_bank.setTag(info_Sbi_bank);



                InfoWindowData info_canara_bank = new InfoWindowData();
                info_canara_bank.setImage("cb1");
                LatLng canara_bank = new LatLng(23.843606, 91.419151);
                Marker m_canara_bank = mMap.addMarker(new MarkerOptions().position(canara_bank).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon)).title("Canara Bank"));
                m_canara_bank.setTag(info_canara_bank);



                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private String getRequestUrl(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude+","+dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";
        //Build the full param
        String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode;
        //Output format
        String output = "json";
        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @SuppressLint("MissingPermission")

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                polylinefinal=mMap1.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
