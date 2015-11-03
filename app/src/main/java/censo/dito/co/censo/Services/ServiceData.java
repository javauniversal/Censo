package censo.dito.co.censo.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;

public class ServiceData extends Service {

    private Context ctx;
    private String url;
    private String data;
    private HttpClient client;
    private HttpPost request;
    public String dataServices;

    public ServiceData() {
        super();this.ctx = getApplicationContext();
    }

    public ServiceData(Context _context, String url, String data){
        super();
        this.ctx = _context;
        this.url = url;
        this.data = data;

    }

    public void PostClick() {

        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
            HttpConnectionParams.setSoTimeout(httpParams, 30000);
            client = new DefaultHttpClient(httpParams);
            request = new HttpPost(url);
            ByteArrayEntity entity = new ByteArrayEntity(data.getBytes());
            entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            entity.setContentType("application/json");
            request.setEntity(entity);
        } catch (Throwable t) {
            Toast.makeText(ctx, "Request failed: " + t.toString(), Toast.LENGTH_LONG).show();
        }

        try {
            HttpResponse response = client.execute(request);
            HttpEntity entityReturn = response.getEntity();
            InputStream instream = entityReturn.getContent();
            String myString = IOUtils.toString(instream, "UTF-8");
            getDataServices(myString);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getDataServices(String data){

        if (data.length() != 0 & data != null){
            dataServices = data;
        }else {
            dataServices = "No hay datos!";
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
