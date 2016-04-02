package censo.dito.co.censo.Activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.RequestQueue;

public class AvtivityBase extends AppCompatActivity {

    public RequestQueue requestQueue;
    public static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    public Boolean validate(String data){
        return data == null || data.length() == 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null)
            requestQueue.cancelAll("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (requestQueue != null)
            requestQueue.cancelAll("");
    }

    //region Retroceder
    @Override
    public void onBackPressed() {

        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else { Toast.makeText(getBaseContext(), "Pulse otra vez para salir", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
    //endregion
}
