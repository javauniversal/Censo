package censo.dito.co.censo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.CensuPointDataRequest;
import censo.dito.co.censo.Entity.LoginResponse;
import censo.dito.co.censo.Entity.Route;
import censo.dito.co.censo.Entity.ShippingForm;
import censo.dito.co.censo.Entity.TracePoint;
import censo.dito.co.censo.MapMain;
import censo.dito.co.censo.R;
import censo.dito.co.censo.Services.ConnectionDetector;
import censo.dito.co.censo.Services.ServiceData;
import censo.dito.co.censo.Services.ServiceLocation;

public class FragmentCenso extends Fragment implements View.OnClickListener{

    private static final int MY_BUTTON = 9000+1;
    private static final String MY_TEXT = "ENVIAR";
    private Route route;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    private LinearLayout ll;
    private DBHelper mydb;

    public static FragmentCenso newInstance(Bundle bundle) {
        FragmentCenso fragment = new FragmentCenso();
        fragment.setArguments(bundle);
        return fragment;
    }
    public FragmentCenso() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_censo, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mydb = new DBHelper(getActivity());

        cargarFormulario();
    }

    public void cargarFormulario(){

        cd = new ConnectionDetector(getActivity());
        ll = (LinearLayout) getActivity().findViewById(R.id.linearLayout2);
        route = new Route();
        for(int i = 0; i < LoginResponse.getLoginRequest().getRoutes().size(); i++){
            if(LoginResponse.getLoginRequest().getRoutes().get(i).getState() == 2){
                route.setStrucDataEntry(LoginResponse.getLoginRequest().getRoutes().get(i).getStrucDataEntry());
                break;
            }
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputSource is = new InputSource(new StringReader(route.getStrucDataEntry()));
            Document doc = builder.parse(is);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("e");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    //EditText Normal
                    if (eElement.getAttribute("t").equals("2")) {

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 70, 0, 0);

                        TextView tve = new TextView(getActivity());
                        tve.setText(eElement.getAttribute("c"));
                        tve.setLayoutParams(layoutParams);
                        tve.setId(Integer.parseInt(eElement.getAttribute("id")));
                        ll.addView(tve);

                        EditText et = new EditText(getActivity());
                        et.setInputType(InputType.TYPE_CLASS_TEXT);
                        et.setHint(eElement.getAttribute("c"));
                        et.setId(Integer.parseInt(eElement.getAttribute("id")));
                        ll.addView(et);
                    }

                    //Selecte checkboxes
                    if (eElement.getAttribute("t").equals("3")) {

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 70, 0, 0);

                        //add checkboxes
                        TextView tv = new TextView(getActivity());
                        tv.setText(eElement.getAttribute("c"));
                        tv.setLayoutParams(layoutParams);
                        ll.addView(tv);

                        int idCheckboxes = 0;
                        StringTokenizer st = new StringTokenizer(eElement.getAttribute("v"),"|");
                        while (st.hasMoreTokens()){
                            CheckBox cb = new CheckBox(getActivity());
                            cb.setText(st.nextToken());
                            cb.setId(idCheckboxes + 1);
                            ll.addView(cb);
                            idCheckboxes++;
                        }
                    }

                    //EditText Numerico
                    if (eElement.getAttribute("t").equals("4")) {

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 70, 0, 0);

                        TextView tvn = new TextView(getActivity());
                        tvn.setLayoutParams(layoutParams);
                        tvn.setText(eElement.getAttribute("c"));
                        ll.addView(tvn);

                        EditText etn = new EditText(getActivity());
                        etn.setInputType(InputType.TYPE_CLASS_NUMBER);
                        etn.setHint(eElement.getAttribute("c"));
                        etn.setId(Integer.parseInt(eElement.getAttribute("id")));
                        ll.addView(etn);
                    }

                    //Spinner generador
                    if (eElement.getAttribute("t").equals("1")){

                        TextView tvs = new TextView(getActivity());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 70, 0, 0);
                        tvs.setLayoutParams(layoutParams);
                        tvs.setText(eElement.getAttribute("c"));
                        ll.addView(tvs);

                        Spinner _spinner = new Spinner(getActivity());
                        StringTokenizer dataLista = new StringTokenizer(eElement.getAttribute("v"),"|");
                        ArrayList<String> al = new ArrayList<>();

                        while (dataLista.hasMoreTokens()){
                            al.add(dataLista.nextToken());
                        }

                        ArrayAdapter<String> lista = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, al);
                        lista.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                        _spinner.setAdapter(lista);
                        _spinner.setTag(eElement.getAttribute("id"));
                        _spinner.setSelection(0);

                        ll.addView(_spinner);
                    }
                }
            }

            Button btnEnviar = new Button(getActivity());
            btnEnviar.setText(MY_TEXT);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 100, 0, 0);
            btnEnviar.setLayoutParams(layoutParams);
            btnEnviar.setId(MY_BUTTON);
            btnEnviar.setOnClickListener(this);
            ll.addView(btnEnviar);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case MY_BUTTON:

                isInternetPresent = cd.isConnected();

                if (isInternetPresent){
                    saveAnswers();
                } else {
                    Toast.makeText(getActivity(), "Por favor, inténtalo de nuevo cuando esté conectado a Internet", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    public void saveAnswers() {
        LinearLayout root = (LinearLayout) getActivity().findViewById(R.id.linearLayout2); //or whatever your root control is
        loopQuestions(root);
    }

    private void loopQuestions(ViewGroup parent) {
        String Productos = new String();
        String XmlData = new String();
        XmlData = "<f>";
        for(int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if(child instanceof EditText) {
                //Support for EditText
                EditText et = (EditText)child;
                XmlData = XmlData + "<e id=\""+et.getId()+"\"  t=\"2\" c=\""+ et.getHint() +"\"  v=\"" + et.getText() + "\" />";

            }else if (child instanceof CheckBox){
                CheckBox cb = (CheckBox)child;
                int answer = cb.isChecked() ? 1 : 0;
                if (answer == 1){
                    Productos = ConcatProducts(Productos, cb.getText().toString());
                }
            }else if (child instanceof Spinner){
                Spinner spinner = (Spinner)child;
                XmlData = XmlData + "<e id=\""+spinner.getTag()+"\" t=\"1\" c=\""+ spinner.getSelectedItem().toString() +"\" v=\"" + spinner.getSelectedItem().toString() + "\" />";
            }
        }

        XmlData = XmlData + "<e id=\""+11+"\" t=\"3\" c=\"Producto\" v=\"" + Productos + "\" />";
        XmlData = XmlData + "</f>";

        guardarCensoData(XmlData);
    }

    public void guardarCensoData(String xml){

        CensuPointDataRequest censuPointDataRequest = new CensuPointDataRequest();
        TracePoint tracePoint = new TracePoint();
        ServiceLocation serviceLocation = new ServiceLocation(getActivity());
        censuPointDataRequest.setUserId(LoginResponse.getLoginRequest().getUser().getId());
        censuPointDataRequest.setRouteId(1);
        censuPointDataRequest.setCompanyCode("001");
        censuPointDataRequest.setData(xml);

        Date pStart = new Date();

        tracePoint.setDateTime("/Date(" + pStart.getTime() + "+0200)/");

        tracePoint.setLatitude(serviceLocation.getLatitude());
        tracePoint.setLongitude(serviceLocation.getLongitude());

        censuPointDataRequest.setCoordenadas(tracePoint);

        HashMap<String, Object> postParameters = new HashMap<>();
        postParameters.put("UserId", censuPointDataRequest.getUserId());
        postParameters.put("RouteId", censuPointDataRequest.getRouteId());
        postParameters.put("CompanyCode", censuPointDataRequest.getCompanyCode());
        postParameters.put("Data", censuPointDataRequest.getData());
        postParameters.put("Coordenadas", censuPointDataRequest.getCoordenadas());

        String jsonParameters = new Gson().toJson(postParameters);

        ServiceData serviceData = new ServiceData(getActivity(), String.format("%1$s%2$s", getString(R.string.url_administration), "SaveCensu"), jsonParameters);
        serviceData.PostClick();
        String datt = serviceData.dataServices;

        if (datt == null){
            Toast.makeText(getActivity(), "Por favor, inténtalo de nuevo cuando esté conectado a Internet", Toast.LENGTH_LONG).show();

            if (mydb.insertCensoFormulario(censuPointDataRequest)) {
                Toast.makeText(getActivity(), "Censo almacenado en la base de datos", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Problemas al guardar el censo en la base de datos", Toast.LENGTH_LONG).show();
            }

            ll.removeAllViews();
            cargarFormulario();

        } else if (datt.equals("true")){
            Toast.makeText(getActivity(), "Censo guardado exitosamente", Toast.LENGTH_LONG).show();

            ll.removeAllViews();
            cargarFormulario();
        } else if (datt.equals("false")) {
            Toast.makeText(getActivity(), "No se pudo guardar el censo", Toast.LENGTH_LONG).show();
        }
    }

    public String ConcatProducts(String productos, String ProductDescription) {
        if (productos.length() > 0) {
            productos = productos + "|" + ProductDescription;
        } else {
            productos = ProductDescription;
        }
        return productos;
    }

}
