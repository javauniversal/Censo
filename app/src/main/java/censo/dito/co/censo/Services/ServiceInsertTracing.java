package censo.dito.co.censo.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.Seguimiento;
import censo.dito.co.censo.Entity.TracePoint;
import censo.dito.co.censo.R;

public class ServiceInsertTracing extends Service {

    private static final String TAG = ServiceInsertTracing.class.getSimpleName();
    TimerTask timerTask = null;

    private DBHelper mydb;
    ServiceLocation serviceLocation;

    @Override
    public void onCreate() {
        Log.d(TAG, "Servicio creado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Timer timer = new Timer();
        mydb = new DBHelper(getApplicationContext());
        serviceLocation = new ServiceLocation(getApplicationContext());

        if (timerTask == null){
            timerTask = new TimerTask() {
                @Override
                public void run() {

                    HashMap<String, Object> postParameters = new HashMap<>();
                    postParameters.put("UserId", mydb.seletUser().getId());
                    postParameters.put("RouteId", mydb.idRuta());

                    Date pStart = new Date();
                    List<TracePoint> tracePointList = new ArrayList<>();
                    TracePoint tracePoint = new TracePoint();
                    tracePoint.setDateTime("/Date(" + pStart.getTime() + "+0200)/");
                    tracePoint.setIdRoute(mydb.idRuta());
                    tracePoint.setLatitude(serviceLocation.getLatitude());
                    tracePoint.setLongitude(serviceLocation.getLongitude());
                    tracePointList.add(tracePoint);

                    postParameters.put("Points", tracePointList);
                    String jsonParameters = new Gson().toJson(postParameters);

                    saveJsonServiceIntace(jsonParameters);

                    Log.d(TAG, "LLamada del seguimiento cada minuto");

                }
            };

            timer.scheduleAtFixedRate(timerTask, 0, 60000);

        }

        return START_STICKY;
    }

    private void saveJsonServiceIntace(String jsonParameters) {

        ServiceData serviceData = new ServiceData(getApplicationContext(), String.format("%1$s%2$s", getString(R.string.url_administration), "SaveTrace"), jsonParameters);
        serviceData.PostClick();
        String datt = serviceData.dataServices;

        if (datt == null) {
            Date pStart = new Date();
            Seguimiento seguimiento = new Seguimiento();
            seguimiento.setUser_id(mydb.seletUser().getId());
            seguimiento.setRute_id(mydb.idRuta());
            seguimiento.setFecha("/Date(" + pStart.getTime() + "+0200)/");
            seguimiento.setLatitud(serviceLocation.getLatitude());
            seguimiento.setLongitud(serviceLocation.getLongitude());
            mydb.insertSeguimiento(seguimiento);
            Log.d(TAG, "Insert Data seguimiento no tiene internet");
        } else {
            if (!datt.equals("true")) {
                Date pStart = new Date();
                Seguimiento seguimiento = new Seguimiento();
                seguimiento.setUser_id(mydb.seletUser().getId());
                seguimiento.setRute_id(mydb.idRuta());
                seguimiento.setFecha("/Date(" + pStart.getTime() + "+0200)/");
                seguimiento.setLatitud(serviceLocation.getLatitude());
                seguimiento.setLongitud(serviceLocation.getLongitude());
                mydb.insertSeguimiento(seguimiento);
                Log.d(TAG, "Insert Data seguimiento no tiene internet");
            }
        }
    }

    @Override
    public void onDestroy() {
        timerTask.cancel();
        Log.d(TAG, "Servicio destruido...");
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
