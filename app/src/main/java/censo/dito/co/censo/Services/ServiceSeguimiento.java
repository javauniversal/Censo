package censo.dito.co.censo.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.Seguimiento;
import censo.dito.co.censo.Entity.TracePoint;
import censo.dito.co.censo.R;

public class ServiceSeguimiento extends Service {


    private DBHelper mydb;
    private static final String TAG = ServiceSeguimiento.class.getSimpleName();
    TimerTask timerTask;

    @Override
    public void onCreate() {
        Log.d(TAG, "Servicio creado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Timer timer = new Timer();
        mydb = new DBHelper(getApplicationContext());
        timerTask = new TimerTask() {
            @Override
            public void run() {
                List<Seguimiento> seguimientoList = mydb.getCensuSeguimiento();
                if (seguimientoList.size() > 0) {
                    for (int i = 0; i < seguimientoList.size(); i++) {
                        HashMap<String, Object> postParameters = new HashMap<>();
                        postParameters.put("UserId", seguimientoList.get(i).getUser_id());
                        postParameters.put("RouteId", seguimientoList.get(i).getRute_id());

                        List<Seguimiento> seguimientoListPoint = mydb.getCensuSeguimiento(seguimientoList.get(i).getUser_id(), seguimientoList.get(i).getRute_id());
                        List<TracePoint> tracePointList = new ArrayList<>();
                        for (int e = 0; e < seguimientoListPoint.size(); e++) {
                            TracePoint tracePoint = new TracePoint();
                            tracePoint.setDateTime(seguimientoListPoint.get(e).getFecha());
                            tracePoint.setLatitude(seguimientoListPoint.get(e).getLatitud());
                            tracePoint.setLongitude(seguimientoListPoint.get(e).getLongitud());
                            tracePointList.add(tracePoint);
                        }

                        postParameters.put("Points", tracePointList);
                        String jsonParameters = new Gson().toJson(postParameters);

                        saveJsonService(jsonParameters, seguimientoList.get(i).getUser_id(), seguimientoList.get(i).getRute_id());
                    }
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 1200000);

        return super.onStartCommand(intent, flags, startId);
    }

    private void saveJsonService(String jsonRootObject, int user_id, int rute_id) {

        ServiceData serviceData = new ServiceData(getApplicationContext(), String.format("%1$s%2$s", getString(R.string.url_administration), "SaveTrace"), jsonRootObject);
        serviceData.PostClick();
        String datt = serviceData.dataServices;
        if (datt.equals("true")){
            mydb.deleteSeguimiento(user_id, rute_id);
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
