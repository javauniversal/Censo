package censo.dito.co.censo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import censo.dito.co.censo.R;

public class ActDetalleCenso extends AppCompatActivity {

    TextView zona;
    TextView barrio;
    TextView negocio;
    TextView direccion;
    TextView cliente;
    TextView tel;
    TextView tipo;
    TextView producto;
    TextView distribuidor;
    TextView frecuencias;
    TextView observaciones;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detalle_censo);

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

        zona = (TextView) findViewById(R.id.txtZona);
        barrio = (TextView) findViewById(R.id.txtBarrio);
        negocio = (TextView) findViewById(R.id.txtNegocio);
        direccion = (TextView) findViewById(R.id.txtDireccion);
        cliente = (TextView) findViewById(R.id.txtCliente);
        tel = (TextView) findViewById(R.id.txtTelefono);
        tipo = (TextView) findViewById(R.id.txtTipo);
        producto = (TextView) findViewById(R.id.txtProducto);
        distribuidor = (TextView) findViewById(R.id.txtDistribuidor);
        frecuencias = (TextView) findViewById(R.id.txtFrecuencia);
        observaciones = (TextView) findViewById(R.id.txtObservaciones);

        String Xml = bundle.getString("xml");

        if (Xml != null && Xml.length() > 0) {

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                InputSource is = new InputSource(new StringReader(bundle.getString("xml")));

                Document doc = builder.parse(is);
                doc.getDocumentElement().normalize();

                NodeList nList = doc.getElementsByTagName("e");

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        if (temp == 0) {
                            zona.setText(eElement.getAttribute("v"));
                        } else if (temp == 1) {
                            barrio.setText(eElement.getAttribute("v"));
                        } else if (temp == 2) {
                            negocio.setText(eElement.getAttribute("v"));
                        } else if (temp == 3) {
                            direccion.setText(eElement.getAttribute("v"));
                        } else if (temp == 4) {
                            cliente.setText(eElement.getAttribute("v"));
                        } else if (temp == 5) {
                            tel.setText(eElement.getAttribute("v"));
                        } else if (temp == 6) {
                            tipo.setText(eElement.getAttribute("v"));
                        } else if (temp == 7) {
                            producto.setText(eElement.getAttribute("v"));
                        } else if (temp == 8) {
                            distribuidor.setText(eElement.getAttribute("v"));
                        } else if (temp == 9) {
                            frecuencias.setText(eElement.getAttribute("v"));
                        } else if (temp == 10) {
                            observaciones.setText(eElement.getAttribute("v"));
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
