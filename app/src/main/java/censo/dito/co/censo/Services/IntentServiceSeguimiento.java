package censo.dito.co.censo.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.Seguimiento;
import censo.dito.co.censo.Entity.TracePoint;
import censo.dito.co.censo.R;

public class IntentServiceSeguimiento extends IntentService {

    public IntentServiceSeguimiento() {
        super("IntentServiceSeguimiento");
    }

    private static final String TAG = IntentServiceSeguimiento.class.getSimpleName();

    DBHelper mydb;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mydb = new DBHelper(getApplicationContext());
            List<Seguimiento> seguimientoList = mydb.getCensuSeguimiento();
            if (seguimientoList.size() > 0) {
                Log.d(TAG, "Estoy enviando datos por el broadcast");
                for (int i = 0; i < seguimientoList.size(); i++) {
                    HashMap<String, Object> postParameters = new HashMap<>();
                    postParameters.put("UserId", seguimientoList.get(i).getUser_id());
                    postParameters.put("RouteId", seguimientoList.get(i).getRute_id());

                    List<Seguimiento> seguimientoListPoint = mydb.getCensuSeguimiento(seguimientoList.get(i).getUser_id(), seguimientoList.get(i).getRute_id());
                    List<TracePoint> tracePointList = new ArrayList<>();
                    for (int e = 0; e < seguimientoListPoint.size(); e++) {
                        TracePoint tracePoint = new TracePoint();
                        tracePoint.setDateTime(seguimientoListPoint.get(e).getFecha());
                        tracePoint.setIdRoute(seguimientoList.get(i).getRute_id());
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

    }

    private void saveJsonService(String jsonRootObject, int user_id, int rute_id) {

        ServiceData serviceData = new ServiceData(getApplicationContext(), String.format("%1$s%2$s", getString(R.string.url_administration), "SaveTrace"), jsonRootObject);
        serviceData.PostClick();
        String datt = serviceData.dataServices;
        if (datt != null) {
            if (datt.equals("true")) {
                mydb.deleteSeguimiento(user_id, rute_id);
                Log.d(TAG, "Elimina datos des pues de enviar.");
            }
        } else {
            Log.d(TAG, "No hay datos para enviar o no tiene internet");
        }

    }

}
