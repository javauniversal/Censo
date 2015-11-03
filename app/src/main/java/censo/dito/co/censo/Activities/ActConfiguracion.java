package censo.dito.co.censo.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.gc.materialdesign.views.ButtonRectangle;
import com.rengwuxian.materialedittext.MaterialEditText;

import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.Configuracion;
import censo.dito.co.censo.R;


public class ActConfiguracion extends AppCompatActivity implements View.OnClickListener {

    private MaterialEditText urlBase;
    private MaterialEditText intervalBase;
    private ButtonRectangle btnConfiguracion;
    private DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracion_configuracion);

        mydb = new DBHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        toolbar.setTitle(getResources().getString(R.string.drawer_ruta));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        urlBase = (MaterialEditText) findViewById(R.id.txtUrlBase);
        intervalBase = (MaterialEditText) findViewById(R.id.txtInterval);

        Configuracion configuracion = mydb.getConfiguracion();
        if(configuracion != null){
            urlBase.setText(configuracion.getUrl());
            intervalBase.setText(configuracion.getInterval()+"");
        }

        btnConfiguracion = (ButtonRectangle) findViewById(R.id.btnCon);
        btnConfiguracion.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_configuracion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCon:

                if (isValidNumber(urlBase.getText().toString())){
                    urlBase.setError("La URL es un campo requerido");
                    urlBase.setFocusableInTouchMode(true);
                    urlBase.requestFocus();
                }else if (isValidNumber(intervalBase.getText().toString())){
                    intervalBase.setError("El intervalo de tiempo es un campo requerido");
                    intervalBase.setFocusableInTouchMode(true);
                    intervalBase.requestFocus();
                }else{
                    mydb.deleteConfiguracion();
                        if (mydb.insertConfiguracion(urlBase.getText().toString(), Integer.parseInt(intervalBase.getText().toString()))){
                            Toast.makeText(this, "Configuración exitosa", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(this, "Problemas al guardar la configuración", Toast.LENGTH_SHORT).show();
                        }
                }

                break;
        }
    }

    private boolean isValidNumber(String number) {
        return number == null || number.length() == 0;
    }
}
