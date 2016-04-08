package censo.dito.co.censo.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import censo.dito.co.censo.Activities.NetworkUtil;

public class ReceiverBroadcastReceiver extends BroadcastReceiver {

    public ReceiverBroadcastReceiver() { }

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}
