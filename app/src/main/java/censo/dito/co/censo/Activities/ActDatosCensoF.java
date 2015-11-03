package censo.dito.co.censo.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import censo.dito.co.censo.Fragments.FragmentCensoData;
import censo.dito.co.censo.R;

public class ActDatosCensoF extends AppCompatActivity {

    Bundle arguments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_datos_censo_f);

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

        if (savedInstanceState == null) {
            arguments = getIntent().getExtras();
            arguments.putInt("idRuta", arguments.getInt("idRuta"));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content, FragmentCensoData.newInstance(arguments))
                    .commit();
        }
    }
}
