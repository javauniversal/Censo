package censo.dito.co.censo.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.R;

public class FragmentSettings extends Fragment {

    private Button configurar;
    private DBHelper mydb;

    public FragmentSettings() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        configurar = (Button) view.findViewById(R.id.btnConfigurar);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mydb = new DBHelper(getActivity());

        /*configurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydb.deleteCenso();
                mydb.deleteDetalleRuta();
                mydb.deleteMapPoint();
                mydb.deleteRuta();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Calendar.YEAR, 2015);
                calendar.set(Calendar.DAY_OF_MONTH, 9);
                calendar.set(Calendar.MONTH, 7);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);

                Ruta ruta = new Ruta();

                ruta.set_nombre("Zona 2 - Aguacatala - Cr 42 a 48A");
                ruta.set_estadoRuta(String.valueOf(EstadoRutas.EnEjecucion));
                ruta.set_fechaInicio(dateFormat.format(calendar.getTime()));

                if (mydb.insertRuta(ruta)){

                    MapPoint mapPoint = new MapPoint();
                    int idRuta = mydb.idRutaInsert();

                    mapPoint.set_orden(0);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.197776);
                    mapPoint.set_longitud(-75.589886);
                    mydb.insertMapPoint(mapPoint);

                    mapPoint.set_orden(1);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.196560);
                    mapPoint.set_longitud(-75.585530);
                    mydb.insertMapPoint(mapPoint);

                    mapPoint.set_orden(2);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.195494);
                    mapPoint.set_longitud(-75.5852075);
                    mydb.insertMapPoint(mapPoint);

                    mapPoint.set_orden(3);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.194683);
                    mapPoint.set_longitud(-75.5759500);
                    mydb.insertMapPoint(mapPoint);

                    mapPoint.set_orden(4);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.194683);
                    mapPoint.set_longitud(-75.5759500);
                    mydb.insertMapPoint(mapPoint);

                    mapPoint.set_orden(5);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.192546);
                    mapPoint.set_longitud(-75.580013);
                    mydb.insertMapPoint(mapPoint);

                    mapPoint.set_orden(6);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.189698);
                    mapPoint.set_longitud(-75.580672);
                    mydb.insertMapPoint(mapPoint);


                    mapPoint.set_orden(7);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.187483);
                    mapPoint.set_longitud(-75.581672);
                    mydb.insertMapPoint(mapPoint);

                    mapPoint.set_orden(8);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.185058);
                    mapPoint.set_longitud(-75.583491);
                    mydb.insertMapPoint(mapPoint);

                    mapPoint.set_orden(9);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.188546);
                    mapPoint.set_longitud(-75.58587697);
                    mydb.insertMapPoint(mapPoint);

                    mapPoint.set_orden(10);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.191597);
                    mapPoint.set_longitud(-75.592221);
                    mydb.insertMapPoint(mapPoint);

                    mapPoint.set_orden(11);
                    mapPoint.set_idRuta(idRuta);
                    mapPoint.set_latitud(6.194829);
                    mapPoint.set_longitud(-75.590993);
                    mydb.insertMapPoint(mapPoint);



                    DetalleRuta detalleRuta = new DetalleRuta();

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 10);
                    calendar.set(Calendar.SECOND, 0);
                    detalleRuta.set_idRuta(idRuta);
                    detalleRuta.set_tiempoSeguimiento(dateFormat.format(calendar.getTime()));
                    detalleRuta.set_latitud(6.196155);
                    detalleRuta.set_longitud(-75.590132);
                    mydb.insertDetalleRuta(detalleRuta);


                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 11);
                    calendar.set(Calendar.SECOND, 0);
                    detalleRuta.set_idRuta(idRuta);
                    detalleRuta.set_tiempoSeguimiento(dateFormat.format(calendar.getTime()));
                    detalleRuta.set_latitud(6.196080);
                    detalleRuta.set_longitud(-75.589875);
                    mydb.insertDetalleRuta(detalleRuta);

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 12);
                    calendar.set(Calendar.SECOND, 0);
                    detalleRuta.set_idRuta(idRuta);
                    detalleRuta.set_tiempoSeguimiento(dateFormat.format(calendar.getTime()));
                    detalleRuta.set_latitud(6.195803);
                    detalleRuta.set_longitud(-75.588963);
                    mydb.insertDetalleRuta(detalleRuta);

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 13);
                    calendar.set(Calendar.SECOND, 0);
                    detalleRuta.set_idRuta(idRuta);
                    detalleRuta.set_tiempoSeguimiento(dateFormat.format(calendar.getTime()));
                    detalleRuta.set_latitud(6.195611);
                    detalleRuta.set_longitud(-75.588394);
                    mydb.insertDetalleRuta(detalleRuta);

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 14);
                    calendar.set(Calendar.SECOND, 0);
                    detalleRuta.set_idRuta(idRuta);
                    detalleRuta.set_tiempoSeguimiento(dateFormat.format(calendar.getTime()));
                    detalleRuta.set_latitud(6.195170);
                    detalleRuta.set_longitud(-75.588517);
                    mydb.insertDetalleRuta(detalleRuta);

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 15);
                    calendar.set(Calendar.SECOND, 0);
                    detalleRuta.set_idRuta(idRuta);
                    detalleRuta.set_tiempoSeguimiento(dateFormat.format(calendar.getTime()));
                    detalleRuta.set_latitud(6.194822);
                    detalleRuta.set_longitud(-75.588640);
                    mydb.insertDetalleRuta(detalleRuta);

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 16);
                    calendar.set(Calendar.SECOND, 0);
                    detalleRuta.set_idRuta(idRuta);
                    detalleRuta.set_tiempoSeguimiento(dateFormat.format(calendar.getTime()));
                    detalleRuta.set_latitud(6.194174);
                    detalleRuta.set_longitud(-75.588753);
                    mydb.insertDetalleRuta(detalleRuta);

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 17);
                    calendar.set(Calendar.SECOND, 0);
                    detalleRuta.set_idRuta(idRuta);
                    detalleRuta.set_tiempoSeguimiento(dateFormat.format(calendar.getTime()));
                    detalleRuta.set_latitud(6.193648);
                    detalleRuta.set_longitud(-75.588810);
                    mydb.insertDetalleRuta(detalleRuta);

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 18);
                    calendar.set(Calendar.SECOND, 0);
                    detalleRuta.set_idRuta(idRuta);
                    detalleRuta.set_tiempoSeguimiento(dateFormat.format(calendar.getTime()));
                    detalleRuta.set_latitud(6.192803);
                    detalleRuta.set_longitud(-75.588924);
                    mydb.insertDetalleRuta(detalleRuta);

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 19);
                    calendar.set(Calendar.SECOND, 0);
                    detalleRuta.set_idRuta(idRuta);
                    detalleRuta.set_tiempoSeguimiento(dateFormat.format(calendar.getTime()));
                    detalleRuta.set_latitud(6.192709);
                    detalleRuta.set_longitud(-75.588385);
                    mydb.insertDetalleRuta(detalleRuta);

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 20);
                    calendar.set(Calendar.SECOND, 0);
                    detalleRuta.set_idRuta(idRuta);
                    detalleRuta.set_tiempoSeguimiento(dateFormat.format(calendar.getTime()));
                    detalleRuta.set_latitud(6.192671);
                    detalleRuta.set_longitud(-75.588054);
                    mydb.insertDetalleRuta(detalleRuta);


                    Censo censo = new Censo();

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 10);
                    calendar.set(Calendar.SECOND, 0);
                    censo.set_idRuta(idRuta);
                    censo.set_tiempocenso(dateFormat.format(calendar.getTime()));
                    censo.set_latitud(6.196155);
                    censo.set_longitud(-75.590132);
                    censo.set_censoData("<d><e t=\"Zona\" v=\"2 - Aguacatala\"/><e t=\"Barrio\" v=\"Aguacatala\"/><e t=\"Negocio\" v=\"Minimercado Tian\"/><e t=\"Dir\" v=\"Cll 12 C Sur 51\"/><e t=\"Cliente\" v=\"Sebastian Molina\"/><e t=\"Tel\" v=\"2521023\"/><e t=\"Tipo\" v=\"2-Minimercado\"/><e t=\"Producto\" v=\"Aguardiente Ant Tradicional|Ron Medellin|Cerbeza\"/><e t=\"Distribuidor\" v=\"Dis 1\"/><e t=\"Frecuencia\" v=\"15D\"/><e t=\"Observaciones\" v=\"Ninguna\"/></d>");
                    mydb.insertCenso(censo);


                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 14);
                    calendar.set(Calendar.SECOND, 0);
                    censo.set_idRuta(idRuta);
                    censo.set_tiempocenso(dateFormat.format(calendar.getTime()));
                    censo.set_latitud(6.195170);
                    censo.set_longitud(-75.588517);
                    censo.set_censoData("<d><e t=\"Zona\" v=\"2 - Aguacatala\"/><e t=\"Barrio\" v=\"Aguacatala\"/><e t=\"Negocio\" v=\"Surtidos Manuel\"/><e t=\"Dir\" v=\"Cll 12 C Sur 85\"/><e t=\"Cliente\" v=\"Manuel Ospina\"/><e t=\"Tel\" v=\"2874532\"/><e t=\"Tipo\" v=\"1-Tienda\"/><e t=\"Producto\" v=\"Ron Medellin|Cerbeza\"/><e t=\"Distribuidor\" v=\"Dis 1\"/><e t=\"Frecuencia\" v=\"8D\"/><e t=\"Observaciones\" v=\"Ninguna\"/></d>\n");
                    mydb.insertCenso(censo);

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 15);
                    calendar.set(Calendar.SECOND, 0);
                    censo.set_idRuta(idRuta);
                    censo.set_tiempocenso(dateFormat.format(calendar.getTime()));
                    censo.set_latitud(6.194822);
                    censo.set_longitud(-75.588640);
                    censo.set_censoData("<d><e t=\"Zona\" v=\"2 - Aguacatala\"/><e t=\"Barrio\" v=\"Aguacatala\"/><e t=\"Negocio\" v=\"Tienda Trinidad\"/><e t=\"Dir\" v=\"Cr 51 A\"/><e t=\"Cliente\" v=\"Flor Angela\"/><e t=\"Tel\" v=\"3115462373\"/><e t=\"Tipo\" v=\"1-Tienda\"/><e t=\"Producto\" v=\"Cerbeza\"/><e t=\"Distribuidor\" v=\"Dis 3\"/><e t=\"Frecuencia\" v=\"10D\"/><e t=\"Observaciones\" v=\"Aumentar frecuencia de visita\"/></d>");
                    mydb.insertCenso(censo);

                    calendar.set(Calendar.HOUR_OF_DAY, 8);
                    calendar.set(Calendar.MINUTE, 19);
                    calendar.set(Calendar.SECOND, 0);
                    censo.set_idRuta(idRuta);
                    censo.set_tiempocenso(dateFormat.format(calendar.getTime()));
                    censo.set_latitud(6.192709);
                    censo.set_longitud(-75.588385);
                    censo.set_censoData("<d><e t=\"Zona\" v=\"2 - Aguacatala\"/><e t=\"Barrio\" v=\"Aguacatala\"/><e t=\"Negocio\" v=\"Licores Med\"/><e t=\"Dir\" v=\"Cr 51 A 14\"/><e t=\"Cliente\" v=\"Esteban Trujillo\"/><e t=\"Tel\" v=\"2329080\"/><e t=\"Tipo\" v=\"5-Billares\"/><e t=\"Producto\" v=\"Aguardiente Ant Tradicional|Aguardiente Ant Sin Azucar|Ron Medellin|Ron Caldas|Cerbeza\"/><e t=\"Distribuidor\" v=\"Dis 3\"/><e t=\"Frecuencia\" v=\"10D\"/><e t=\"Observaciones\" v=\"Aumentar frecuencia de visita\"/></d>");
                    mydb.insertCenso(censo);


                    ruta.set_nombre("Zona 2 - Patio Bonito - Cr 48 a 43A");
                    ruta.set_estadoRuta(String.valueOf(EstadoRutas.NoIniciada));

                    if(mydb.insertRuta(ruta)){
                        Toast.makeText(getActivity(), "Las Rutas se guardaron con Exito", Toast.LENGTH_SHORT).show();
                        configurar.setEnabled(false);
                        configurar.setClickable(false);
                        configurar.setFocusable(false);
                    }else {
                        Toast.makeText(getActivity(), "Problemas al guardar La ultima Ruta!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Problemas al guardar La primera Ruta!", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }
}
