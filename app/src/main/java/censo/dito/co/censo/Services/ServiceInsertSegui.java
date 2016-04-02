package censo.dito.co.censo.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.CensuPointDataRequest;
import censo.dito.co.censo.Entity.Seguimiento;
import censo.dito.co.censo.R;

import static censo.dito.co.censo.Entity.LoginResponse.getLoginRequest;

public class ServiceInsertSegui extends Service {

    private static final String TAG = ServiceInsertSegui.class.getSimpleName();
    TimerTask timerTask;

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

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Date pStart = new Date();
                Seguimiento seguimiento = new Seguimiento();
                seguimiento.setUser_id(getLoginRequest().getUser().getId());
                seguimiento.setRute_id(mydb.idRuta());
                seguimiento.setFecha("/Date(" + pStart.getTime() + "+0200)/");
                seguimiento.setLatitud(serviceLocation.getLatitude());
                seguimiento.setLongitud(serviceLocation.getLongitude());
                mydb.insertSeguimiento(seguimiento);
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 240000);

        return super.onStartCommand(intent, flags, startId);
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
