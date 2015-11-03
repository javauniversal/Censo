package censo.dito.co.censo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.R;

public class ActEstadisticas extends AppCompatActivity {

    private Bundle bundle;
    private DBHelper myDb;
    private TextView txtCantidadCenso;
    private TextView txtTiempoCenso;
    private TextView txtPromedioCenso;
    private TextView txtFechaInicial;
    private TextView txtFechaFinal;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_estadisticas);
        myDb = new DBHelper(this);

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

        Intent intent = getIntent();
        bundle = intent.getExtras();

        //int cantidadCenso = myDb.getCountCensos(bundle.getInt("idRuta"));

        txtCantidadCenso = (TextView) findViewById(R.id.txtCantidadCenso);
        txtTiempoCenso = (TextView) findViewById(R.id.txtTiempoCenso);
        txtPromedioCenso = (TextView) findViewById(R.id.txtPromedioCenso);
        txtFechaInicial = (TextView) findViewById(R.id.txtFechaInicial);
        txtFechaFinal = (TextView) findViewById(R.id.txtFechaFinal);


        //txtCantidadCenso.setText(String.format("%s", cantidadCenso));

        //String fechaIni = myDb.getStarCensoDate(bundle.getInt("idRuta"));

        //String fechaFin = myDb.getEndCensoDate(bundle.getInt("idRuta"));

        /*if (fechaIni != null && fechaFin != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

            try {
                Date dateIni = dateFormat.parse(fechaIni);
                Date dateFinal = dateFormat.parse(fechaFin);

                long dif = dateFinal.getTime() - dateIni.getTime();
                txtTiempoCenso.setText(String.format("%s", TimeUnit.MILLISECONDS.toMinutes(dif)));

                double mLong = (double) TimeUnit.MILLISECONDS.toMinutes(dif) / cantidadCenso;

                DecimalFormat numberFormat = new DecimalFormat("#.00");
                txtPromedioCenso.setText(numberFormat.format(mLong));

                txtFechaInicial.setText(fechaIni);
                txtFechaFinal.setText(fechaFin);

            } catch (ParseException e) {
                e.printStackTrace();
            }*/
        //}
    }

}
