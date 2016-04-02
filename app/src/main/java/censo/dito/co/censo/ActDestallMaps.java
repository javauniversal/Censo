package censo.dito.co.censo;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import censo.dito.co.censo.Activities.ActDatosCensoF;
import censo.dito.co.censo.Activities.ActEstadisticas;
import censo.dito.co.censo.Activities.AvtivityBase;
import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.Censu;
import censo.dito.co.censo.Entity.ListCensu;
import censo.dito.co.censo.Entity.RouteMapPoint;
import censo.dito.co.censo.Entity.TracePoint;

import static censo.dito.co.censo.Entity.LoginResponse.getLoginRequest;

public class ActDestallMaps extends AvtivityBase implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

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
    List<Censu> loginResponse;

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


        List<RouteMapPoint> mapPoints = myDB.getMapPoint(bundle.getInt("idRuta"));

        if (mapPoints.size() > 0) {
            regionLayer = new PolygonOptions();
            regionLayer.strokeWidth(5).strokeColor(Color.argb(20, 50, 0, 255)).fillColor(Color.argb(20, 50, 0, 255));

            for (int i = 0; i < mapPoints.size(); i++) {
                regionLayer.add(new LatLng(mapPoints.get(i).getLatitude(), mapPoints.get(i).getLongitude()));
            }

            LatLng ltl = new LatLng(mapPoints.get(0).getLatitude(), mapPoints.get(0).getLongitude());

            regionLayer.add(ltl);

            mMap.addPolygon(regionLayer);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(ltl)      // Sets the center of the map to LatLng (refer to previous snippet)
                    .zoom(13.5f)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

        //Seguimiento.
        List<TracePoint> pointDetalle = myDB.getTracePoint(bundle.getInt("idRuta"));
        if (pointDetalle.size() > 0) {
            lineSeguimiento = new PolylineOptions();
            lineSeguimiento.width(6).color(getResources().getColor(R.color.color_9));

            for (int i = 0; i < pointDetalle.size(); i++) {
                lineSeguimiento.add(new LatLng(pointDetalle.get(i).getLatitude(), pointDetalle.get(i).getLongitude()));
            }
        }

        getCensoPoint();

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

        if (SeguiLayerVisible) {
            if (lineSeguimiento != null)
                mMap.addPolyline(lineSeguimiento);
        }
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

    public void getCensoPoint() {

        String url = String.format("%1$s%2$s", "http://181.143.94.74:2080/dito/services/zenso/ZensoAdministration.svc/", "GetCensuData");
        requestQueue = Volley.newRequestQueue(this);

        try {

            HashMap<String, Object> postParameters = new HashMap<String, Object>();
            postParameters.put("userId", getLoginRequest().getUser().getId());
            postParameters.put("routeId", bundle.getInt("idRuta"));

            String jsonParameters = new Gson().toJson(postParameters);
            JSONObject jsonRootObject = new JSONObject(jsonParameters);

            JsonArrayRequest jsArrayRequest = new JsonArrayRequest(
                    Request.Method.POST,
                    url,
                    jsonRootObject,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            parseJSON(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(ActDestallMaps.this, "Error de tiempo de espera", Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(ActDestallMaps.this, "Error Servidor",Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(ActDestallMaps.this, "Server Error",Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(ActDestallMaps.this, "Error de red",Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(ActDestallMaps.this, "Error al serializar los datos",Toast.LENGTH_LONG).show();
                            }
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            requestQueue.add(jsArrayRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseJSON(JSONArray json) {

        try {
            Gson gson = new Gson();
            if (json == null || json.equals("")){
                Toast.makeText(this, "Problemas al recuperar la información", Toast.LENGTH_SHORT).show();
            }else {
                loginResponse = gson.fromJson(String.valueOf(json), ListCensu.class);
                if (loginResponse.size() > 0) {
                    censoLayer = new ArrayList<>();
                    for (int i = 0; i < loginResponse.size(); i++) {
                        censoLayer.add(new MarkerOptions().position(new LatLng(loginResponse.get(i).getLatitude(), loginResponse.get(i).getLongitude()))
                                .snippet("Censo")
                                .title(loginResponse.get(i).getDescriptionLine1())
                                .anchor(0.5f, 0.5f)
                                .flat(true)
                                .visible(true)
                                .draggable(false));
                    }
                }
            }

        }catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
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
        if (censoLayer != null){
            for (MarkerOptions marker : censoLayer) {
                mMap.addMarker(marker);
            }
        }
    }
}
