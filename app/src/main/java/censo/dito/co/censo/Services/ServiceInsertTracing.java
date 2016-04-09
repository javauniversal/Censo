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

                    Date pStart = new Date();
                    Seguimiento seguimiento = new Seguimiento();
                    seguimiento.setUser_id(mydb.seletUser().getId());
                    seguimiento.setRute_id(mydb.idRuta());
                    seguimiento.setFecha("/Date(" + pStart.getTime() + "+0200)/");
                    seguimiento.setLatitud(serviceLocation.getLatitude());
                    seguimiento.setLongitud(serviceLocation.getLongitude());
                    mydb.insertSeguimiento(seguimiento);
                    Log.d(TAG, "Insertando Datos Local: Usuario: " + mydb.seletUser().getId() +
                            " Ruta: " + mydb.idRuta() +" Latitud: "+ serviceLocation.getLatitude()+ " Longitud;: "+serviceLocation.getLongitude());

                }
            };

            timer.scheduleAtFixedRate(timerTask, 0, 60000);

        }

        return START_STICKY;
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
