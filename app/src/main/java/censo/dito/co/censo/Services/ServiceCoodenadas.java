package censo.dito.co.censo.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.CensuPointDataRequest;
import censo.dito.co.censo.R;

public class ServiceCoodenadas extends Service {

    MyTask myTask;
    private DBHelper mydb;

    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this, "Servicio creado!", Toast.LENGTH_SHORT).show();
        myTask = new MyTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTask.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servicio destruído!", Toast.LENGTH_SHORT).show();
        myTask.cancel(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class MyTask extends AsyncTask<String, String, String> {

        private boolean cent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mydb = new DBHelper(getApplicationContext());
            cent  = true;
        }

        @Override
        protected String doInBackground(String... params) {

            while (cent){

                String parran = "null";

                List<CensuPointDataRequest> censuPointDataRequest = mydb.getCenso();

                if (censuPointDataRequest.size() > 0) {
                    for (int i = 0; i < censuPointDataRequest.size(); i++) {
                        HashMap<String, Object> postParameters = new HashMap<>();
                        postParameters.put("UserId", censuPointDataRequest.get(i).getUserId());
                        postParameters.put("RouteId", censuPointDataRequest.get(i).getRouteId());
                        postParameters.put("CompanyCode", censuPointDataRequest.get(i).getCompanyCode());
                        postParameters.put("Data", censuPointDataRequest.get(i).getData());
                        postParameters.put("Coordenadas", censuPointDataRequest.get(i).getCoordenadas());

                        String jsonParameters = new Gson().toJson(postParameters);

                        ServiceData serviceData = new ServiceData(getApplicationContext(), String.format("%1$s%2$s", getString(R.string.url_administration), "SaveCensu"), jsonParameters);
                        serviceData.PostClick();
                        String datt = serviceData.dataServices;
                        if (datt != null){
                            if (mydb.deleteCenso(censuPointDataRequest.get(i).getId_censo())){
                                parran = "true";
                            }else{

                            }
                        }
                    }
                }

                try {
                    publishProgress(parran);
                    // Stop 1hora = 3600000
                    // Stop 30minutos = 1800000
                    Thread.sleep(1800000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (!values[0].equals("null")){
                Toast.makeText(getApplicationContext(), "Censo guardado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
