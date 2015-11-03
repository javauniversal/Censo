package censo.dito.co.censo;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import censo.dito.co.censo.Activities.ActDatosCensoF;
import censo.dito.co.censo.Activities.ActEstadisticas;
import censo.dito.co.censo.DataBase.DBHelper;

public class ActDestallMaps extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    public static final String TAG = ActDestallMaps.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private DBHelper myDB;
    private Bundle bundle;

    private PolygonOptions regionLayer;
    private ArrayList<MarkerOptions> censoLayer;
    private PolylineOptions lineSeguimiento;

    private CheckBox rutaChk;
    private CheckBox censoChk;
    private CheckBox seguimientoChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detalle_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(getResources().getString(R.string.drawer_ruta));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        myDB = new DBHelper(this);
        Intent intent = getIntent();
        bundle = intent.getExtras();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1000); // 1 second, in milliseconds

        addMapLayout();


        /*List<MapPoint> mapPoints = myDB.getMapPoint(bundle.getInt("idRuta"));
        if (mapPoints.size() > 0) {
            regionLayer = new PolygonOptions();
            regionLayer.strokeWidth(5).strokeColor(Color.argb(20, 50, 0, 255)).fillColor(Color.argb(20, 50, 0, 255));

            for (int i = 0; i < mapPoints.size(); i++) {
                regionLayer.add(new LatLng(mapPoints.get(i).get_latitud(), mapPoints.get(i).get_longitud()));
            }

            LatLng ltl = new LatLng(mapPoints.get(0).get_latitud(), mapPoints.get(0).get_longitud());

            regionLayer.add(ltl);

            mMap.addPolygon(regionLayer);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(ltl)      // Sets the center of the map to LatLng (refer to previous snippet)
                    .zoom(13.5f)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }*/

        //Seguimiento.
        /*List<DetalleRuta> pointDetalle = myDB.getDetalleRuta(bundle.getInt("idRuta"));
        if (pointDetalle.size() > 0) {
            lineSeguimiento = new PolylineOptions();
            lineSeguimiento.width(6).color(getResources().getColor(R.color.color_9));

            for (int i = 0; i < pointDetalle.size(); i++) {
                lineSeguimiento.add(new LatLng(pointDetalle.get(i).get_latitud(), pointDetalle.get(i).get_longitud()));
            }
        }

        //Censo.
        List<Censo> pointCenso = myDB.getCensoRuta(bundle.getInt("idRuta"));
        if (pointCenso.size() > 0) {
            censoLayer = new ArrayList<>();
            for (int i = 0; i < pointCenso.size(); i++) {
                censoLayer.add(new MarkerOptions().position(new LatLng(pointCenso.get(i).get_latitud(), pointCenso.get(i).get_longitud()))
                        .snippet(pointCenso.get(i).get_tiempocenso())
                        .anchor(0.5f, 0.5f)
                        .flat(true)
                        .visible(true)
                        .draggable(false));
            }
        }*/

        rutaChk = (CheckBox)findViewById(R.id.checRuta);
        rutaChk.setOnClickListener(this);
        censoChk = (CheckBox)findViewById(R.id.checCenso);
        censoChk.setOnClickListener(this);
        seguimientoChk = (CheckBox)findViewById(R.id.checSeguimiento);
        seguimientoChk.setOnClickListener(this);

    }

    private void ValidateMapLayers(boolean RegionLayerVisible, boolean CensoLayerVisible, boolean SeguiLayerVisible){
        mMap.clear();

        if (RegionLayerVisible)
            mMap.addPolygon(regionLayer);

        if (CensoLayerVisible)
            addAllMarkers();

        if (SeguiLayerVisible)
            mMap.addPolyline(lineSeguimiento);
    }

    private void addMapLayout() {

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView)).getMap();
            if (mMap != null) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                //PaintRegionMap
                mMap.setMyLocationEnabled(true);
            }
        }

        //PaintWalkMap
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_destall_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_estadisticas) {
            bundle.putInt("idRuta", bundle.getInt("idRuta"));
            startActivity(new Intent(this, ActEstadisticas.class).putExtras(bundle));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;

        }else if(id == R.id.action_datosCenso){
            bundle.putInt("idRuta", bundle.getInt("idRuta"));
            startActivity(new Intent(this, ActDatosCensoF.class).putExtras(bundle));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(Bundle bundle) {
        /*
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            onLocationChanged(location);
        }
        */
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng myCoordinates = new LatLng(location.getLatitude(),location.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myCoordinates)      // Sets the center of the map to LatLng (refer to previous snippet)
                .zoom(15)                   // Sets the zoom
                // .bearing(90)              // Sets the orientation of the camera to east
                // .tilt(30)                 // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.checRuta:
            case R.id.checCenso:
            case R.id.checSeguimiento:
                ValidateMapLayers(rutaChk.isChecked(), censoChk.isChecked(), seguimientoChk.isChecked());
                break;
        }
    }

    private void addAllMarkers() {
        for (MarkerOptions marker : censoLayer) {
            mMap.addMarker(marker);
        }
    }
}
