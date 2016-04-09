package censo.dito.co.censo.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverOffOnd extends BroadcastReceiver {
    public ReceiverOffOnd() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        context.startService(new Intent(context, ServiceInsertTracing.class));

        context.startService(new Intent(context, ServiceSeguimiento.class));

    }
}
