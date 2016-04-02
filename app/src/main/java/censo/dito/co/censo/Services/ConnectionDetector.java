package censo.dito.co.censo.Services;


import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionDetector {

    private Context _context;

    public ConnectionDetector(Context context){
        this._context = context;
    }

    public boolean isConnected() {

        ConnectivityManager check = (ConnectivityManager) this._context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( check.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                check.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                check.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                check.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            //Toast.makeText(_context, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (check.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED || check.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            //Toast.makeText(_context, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }

        return false;
    }

}
