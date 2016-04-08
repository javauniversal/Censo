package censo.dito.co.censo.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import censo.dito.co.censo.Services.IntentServiceSeguimiento;

public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private static final String TAG = NetworkUtil.class.getSimpleName();

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {

            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;

        }

        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        Log.d(TAG, "Estado:.................. "+conn);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Enviando datos de seguimiento";
            Intent intentServiceSeguimiento = new Intent(context, IntentServiceSeguimiento.class);
            context.startService(intentServiceSeguimiento);
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Enviando datos de seguimiento";
            Intent intentServiceSeguimiento = new Intent(context, IntentServiceSeguimiento.class);
            context.startService(intentServiceSeguimiento);
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }


}
