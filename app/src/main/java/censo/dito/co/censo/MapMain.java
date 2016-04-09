package censo.dito.co.censo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import censo.dito.co.censo.Activities.ActDatosCensoF;
import censo.dito.co.censo.Activities.ActEstadisticas;
import censo.dito.co.censo.Activities.ActLogin;
import censo.dito.co.censo.Activities.AvtivityBase;
import censo.dito.co.censo.Adapters.DrawerItemAdapter;
import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.Censu;
import censo.dito.co.censo.Entity.DrawerItem;
import censo.dito.co.censo.Entity.DrawerMenu;
import censo.dito.co.censo.Entity.ListCensu;
import censo.dito.co.censo.Entity.LoginRequest;
import censo.dito.co.censo.Entity.LoginResponse;
import censo.dito.co.censo.Entity.RouteMapPoint;
import censo.dito.co.censo.Fragments.FragmentCenso;
import censo.dito.co.censo.Fragments.FragmentCensoData;
import censo.dito.co.censo.Fragments.FragmentRoute;
import censo.dito.co.censo.Fragments.FragmentSettings;
import censo.dito.co.censo.Services.ReceiverBroadcastReceiver;
import censo.dito.co.censo.Services.ServiceInsertTracing;
import censo.dito.co.censo.Services.ServiceSeguimiento;

public class MapMain extends AvtivityBase implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private DBHelper myDB;
    private GoogleMap mMap;
    private PolygonOptions regionLayer;
    private ArrayList<MarkerOptions> censoLayer;
    private PolylineOptions lineSeguimiento;

    private FragmentCensoData fragCensoData = null;
    private FragmentRoute fragRoute = null;

    public static final String TAG = MapMain.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private View maps;
    private CheckBox rutaChk;
    private CheckBox censoChk;
    private CheckBox seguimientoChk;
    private LinearLayout linearLayout;
    List<Censu> loginResponse;
    private Intent intentMemoryService;
    private Intent intentServiceSeguimiento;
    private Intent intentServiceCenso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutPrincipal);

        //registerReceiver(new ReceiverBroadcastReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        maps = findViewById(R.id.mapView);
        FrameLayout pages = (FrameLayout) findViewById(R.id.pages);

        //intentServiceCenso = new Intent(getApplicationContext(), ServiceCoodenadas.class);
        //startService(intentServiceCenso);

        myDB = new DBHelper(this);

        if (!myDB.IntroPrimeraVes()){
            myDB.insertIntroPrimeraVes("Primeraves");
            intentMemoryService = new Intent(getApplicationContext(), ServiceInsertTracing.class);
            startService(intentMemoryService);

            intentServiceSeguimiento = new Intent(getApplicationContext(), ServiceSeguimiento.class);
            startService(intentServiceSeguimiento);
        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final RecyclerView drawerOptions = (RecyclerView) findViewById(R.id.drawer_options);
        setSupportActionBar(toolbar);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, Gravity.START);
        drawerLayout.setStatusBarBackground(R.color.color_principal);
        drawerLayout.setDrawerListener(drawerToggle);

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

        List<DrawerItem> drawerItems = new java.util.ArrayList<>(Arrays.asList(
                new DrawerItem(DrawerItem.Type.HEADER),
                new DrawerMenu().setIconRes(R.mipmap.ic_action_home).setText(getString(R.string.drawer_inicio, 1)),
                //new DrawerItem(DrawerItem.Type.DIVIDER),
                new DrawerMenu().setIconRes(R.mipmap.ic_action_paste).setText(getString(R.string.drawer_censo, 2)),
                //new DrawerItem(DrawerItem.Type.DIVIDER),
                new DrawerMenu().setIconRes(R.mipmap.ic_action_copy).setText(getString(R.string.drawer_censo_data, 3)),
                //new DrawerItem(DrawerItem.Type.DIVIDER),
                new DrawerMenu().setIconRes(R.mipmap.ic_action_location).setText(getString(R.string.drawer_ruta, 4)),
                //new DrawerItem(DrawerItem.Type.DIVIDER),
                //new DrawerMenu().setIconRes(R.mipmap.ic_action_gear).setText(getString(R.string.drawer_configuracion, 5)),
                //new DrawerItem(DrawerItem.Type.DIVIDER),
                new DrawerMenu().setIconRes(R.mipmap.ic_action_exit).setText(getString(R.string.drawer_salir, 5))));

        drawerOptions.setLayoutManager(new LinearLayoutManager(this));
        DrawerItemAdapter adapter = new DrawerItemAdapter(drawerItems, this);
        adapter.setOnItemClickListener(new DrawerItemAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                onDrawerMenuSelected(position);
            }
        });

        drawerOptions.setAdapter(adapter);
        drawerOptions.setHasFixedSize(true);

        onDrawerMenuSelected(1);

        toolbar.setTitle(getResources().getString(R.string.drawer_inicio));


        addMapLayout();

        //Ruta
        /*List<MapPoint> mapPoints = myDB.getMapPoint(myDB.idRuta());*/

        if (myDB.seletRouteActive() != null) {

            List<RouteMapPoint> routeMapPointList = myDB.seletRouteActive().getMap();
            regionLayer = new PolygonOptions();
            regionLayer.strokeWidth(5).strokeColor(Color.argb(20, 50, 0, 255)).fillColor(Color.argb(20, 50, 0, 255));

            for (int i = 0; i < routeMapPointList.size(); i++) {
                regionLayer.add(new LatLng(routeMapPointList.get(i).getLatitude(),
                        routeMapPointList.get(i).getLongitude()));
            }

            LatLng ltl = new LatLng(routeMapPointList.get(0).getLatitude(), routeMapPointList.get(0).getLongitude());

            regionLayer.add(ltl);
            mMap.addPolygon(regionLayer);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(ltl)      // Sets the center of the map to LatLng (refer to previous snippet)
                    .zoom(13.5f)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

        rutaChk = (CheckBox)findViewById(R.id.checRuta);
        rutaChk.setOnClickListener(this);
        censoChk = (CheckBox)findViewById(R.id.checCenso);
        censoChk.setOnClickListener(this);
        seguimientoChk = (CheckBox)findViewById(R.id.checSeguimiento);
        seguimientoChk.setOnClickListener(this);

    }

    private void addMapLayout() {

        maps.getLayoutParams().height = FrameLayout.LayoutParams.FILL_PARENT;
        maps.requestLayout();
        linearLayout.setVisibility(View.VISIBLE);
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
            postParameters.put("userId", myDB.seletUser().getId());
            postParameters.put("routeId", myDB.idRuta());

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
                                Toast.makeText(MapMain.this, "Error de tiempo de espera",Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(MapMain.this, "Error Servidor",Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(MapMain.this, "Server Error",Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(MapMain.this, "Error de red",Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(MapMain.this, "Error al serializar los datos",Toast.LENGTH_LONG).show();
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

    public void getSeguimientoRuteActive(){

        String url = String.format("%1$s%2$s", "http://181.143.94.74:2080/dito/services/zenso/Authentication.svc/", "Login");
        requestQueue = Volley.newRequestQueue(this);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCiaId("001");
        loginRequest.setUser(myDB.seletUser().getUserId());
        loginRequest.setPassword(myDB.seletUser().getPassword());

        try {

            HashMap<String, Object> postParameters = new HashMap<>();
            postParameters.put("CiaId", loginRequest.getCiaId());
            postParameters.put("User", loginRequest.getUser());
            postParameters.put("Password", loginRequest.getPassword());

            String jsonParameters = new Gson().toJson(postParameters);
            JSONObject jsonRootObject = new JSONObject(jsonParameters);

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonRootObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            parseJSONSeguimiento(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(MapMain.this, "Error de tiempo de espera",Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(MapMain.this, "Error Servidor",Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(MapMain.this, "Server Error",Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(MapMain.this, "Error de red",Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(MapMain.this, "Error al serializar los datos",Toast.LENGTH_LONG).show();
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

    private void parseJSONSeguimiento(JSONObject json) {
        try {
            Gson gson = new Gson();

            if (json == null || json.equals("")) {
                Toast.makeText(this, "Problemas al recuperar la información", Toast.LENGTH_SHORT).show();
            } else {
                LoginResponse loginResponse = gson.fromJson(String.valueOf(json), LoginResponse.class);

                if(loginResponse.isAuthenticated()){
                    if (loginResponse.getUser().getActiveRoute().getTrackingDetail().size() > 0) {

                        lineSeguimiento = new PolylineOptions();
                        lineSeguimiento.width(9).color(getResources().getColor(R.color.color_9));

                        for (int i = 0; i < loginResponse.getUser().getActiveRoute().getTrackingDetail().size(); i++) {
                            lineSeguimiento.add(new LatLng(loginResponse.getUser().getActiveRoute().getTrackingDetail().get(i).getLatitude(), loginResponse.getUser().getActiveRoute().getTrackingDetail().get(i).getLongitude()));
                        }
                    }

                }else{
                    Toast.makeText(this, "Usuario/Password incorrecto", Toast.LENGTH_SHORT).show();
                }
            }

        }catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }


    private void onDrawerMenuSelected(int position) {
        switch (position){
            case 1:
                toolbar.setTitle(getResources().getString(R.string.drawer_inicio));
                addMapLayout();

                getSeguimientoRuteActive();

                getCensoPoint();

                break;
            case 2:
                if (!myDB.validarInicioRuta(2)){
                    toolbar.setTitle(getResources().getString(R.string.drawer_censo));
                    ShowCensoLayout();
                }else {
                    Toast.makeText(this, "No es posible realizar una toma de datos sin ruta", Toast.LENGTH_LONG).show();
                }
                break;
            case 3:
                toolbar.setTitle(getResources().getString(R.string.drawer_censo_data));
                ShowCensoDataLayout();
                break;
            case 4:
                toolbar.setTitle(getResources().getString(R.string.drawer_ruta));
                ShowRouteLayout();
                break;
            case 5:
                //toolbar.setTitle(getResources().getString(R.string.drawer_configuracion));
                //ShowSettingsLayout();
                Exit();
                break;
            case 6:
                //Exit();
                break;
            default:

        }
        drawerLayout.closeDrawers();
    }

    private void ShowCensoLayout() {
        FragmentCenso fragCenso = new FragmentCenso();
        ShowFragment(fragCenso);
    }

    private void ShowSettingsLayout() {
        FragmentSettings fragSettings = new FragmentSettings();
        ShowFragment(fragSettings);
    }

    private void ShowRouteLayout() {
        if (fragRoute == null) {
            fragRoute = new FragmentRoute();
        }

        ShowFragment(fragRoute);
    }

    private void ShowCensoDataLayout() {

        if (fragCensoData == null) {
            fragCensoData = new FragmentCensoData();
        }

        maps.getLayoutParams().height = 0;
        maps.requestLayout();
        linearLayout.setVisibility(View.GONE);

        Bundle arguments = new Bundle();
        FragmentManager fragmentManager = getSupportFragmentManager();
        arguments.putInt("idRuta", myDB.seletRouteActive().getId());
        fragmentManager.beginTransaction()
                .replace(R.id.pages, fragCensoData.newInstance(arguments))
                .commit();

        //ShowFragment(fragCensoData);
    }

    public void Exit(){
        new MaterialDialog.Builder(this)
                .title(R.string.title)
                .content(R.string.content)
                .positiveText(R.string.agree)
                .backgroundColor(getResources().getColor(R.color.color_2))
                .positiveColor(getResources().getColor(R.color.color_negro))
                .negativeColor(getResources().getColor(R.color.color_negro))
                .negativeText(R.string.disagree)
                .callback(new MaterialDialog.ButtonCallback() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //stopService(new Intent(MapMain.this, ServiceCoodenadas.class));
                        //stopService(intentMemoryService);
                        //stopService(intentServiceSeguimiento);
                        //stopService(intentServiceCenso);
                        finishAffinity();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void ShowFragment(Fragment FragmentToShow) {
        maps.getLayoutParams().height = 0;
        maps.requestLayout();
        linearLayout.setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.pages, FragmentToShow)
                .commit();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            onLocationChanged(location);
        }
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
                        //.bearing(90)              // Sets the orientation of the camera to east
                        //.tilt(30)                 // Sets the tilt of the camera to 30 degrees
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
    public void onBackPressed() {
        Exit();
    }

    private void addAllMarkers() {
        if (censoLayer != null){
            for (MarkerOptions marker : censoLayer) {
                mMap.addMarker(marker);
            }
        }
    }

    private void ValidateMapLayers(boolean RegionLayerVisible, boolean CensoLayerVisible, boolean SeguiLayerVisible) {
        mMap.clear();

        if (RegionLayerVisible)
            mMap.addPolygon(regionLayer);

        if (CensoLayerVisible)
            addAllMarkers();

        if (SeguiLayerVisible){
            if (lineSeguimiento != null)
                mMap.addPolyline(lineSeguimiento);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_destall_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Bundle bundle = new Bundle();
        if (id == R.id.action_estadisticas) {
            bundle.putInt("idRuta", myDB.idRuta());
            startActivity(new Intent(this, ActEstadisticas.class).putExtras(bundle));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            return true;

        }else if(id == R.id.action_datosCenso){
            bundle.putInt("idRuta", myDB.idRuta());
            startActivity(new Intent(this, ActDatosCensoF.class).putExtras(bundle));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
