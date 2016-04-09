package censo.dito.co.censo.Fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
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
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import censo.dito.co.censo.Activities.EditTextRequired;
import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.CensuPointDataRequest;
import censo.dito.co.censo.Entity.LoginResponse;
import censo.dito.co.censo.Entity.Route;
import censo.dito.co.censo.Entity.TracePoint;
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void cargarFormulario(){

        cd = new ConnectionDetector(getActivity());
        ll = (LinearLayout) getActivity().findViewById(R.id.linearLayout2);
        route = new Route();
        List<Route> routeList = mydb.getRuta();

        for(int i = 0; i < routeList.size(); i++){
            if(routeList.get(i).getState() == 2){
                route.setStrucDataEntry(mydb.getRuta().get(i).getStrucDataEntry());
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

                        EditTextRequired et = new EditTextRequired(getActivity());
                        et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        et.setHint(eElement.getAttribute("c"));
                        et.setFilters(new InputFilter[]{
                                new InputFilter() {
                                    public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {

                                        if (src.equals("")) { // for backspace
                                            return src;
                                        }
                                        if (src.toString().matches("[a-zA-Z0-9 ]*")) //put your constraints here
                                        {
                                            return src;
                                        }
                                        return "";
                                    }
                                }
                        });
                        et.setId(Integer.parseInt(eElement.getAttribute("id")));
                        et.setIsRequire(Objects.equals(eElement.getAttribute("o"), "1"));
                        ll.addView(et);
                    }

                    // Edit Texto Largo.
                    if (eElement.getAttribute("t").equals("6")) {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(0, 70, 0, 0);

                        TextView tve = new TextView(getActivity());
                        tve.setText(eElement.getAttribute("c"));
                        tve.setLayoutParams(layoutParams);
                        tve.setId(Integer.parseInt(eElement.getAttribute("id")));
                        ll.addView(tve);

                        EditTextRequired et = new EditTextRequired(getActivity());
                        et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                        et.setHint(eElement.getAttribute("c"));
                        et.setLines(3);
                        et.setHorizontallyScrolling(false);
                        et.setMinWidth(5);
                        et.setFilters(new InputFilter[]{
                                new InputFilter() {
                                    public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {

                                        if (src.equals("")) { // for backspace
                                            return src;
                                        }
                                        if (src.toString().matches("[a-zA-Z0-9 ]*")) //put your constraints here
                                        {
                                            return src;
                                        }
                                        return "";
                                    }
                                }
                        });
                        et.setBackgroundResource(R.drawable.rounded_corner);
                        et.setMaxWidth(10);
                        et.setId(Integer.parseInt(eElement.getAttribute("id")));
                        et.setIsRequire(Objects.equals(eElement.getAttribute("o"), "1"));
                        ll.addView(et);

                    }

                    //Selecte checkboxes
                    if (eElement.getAttribute("t").equals("3")) {

                        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
                            cb.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for (int i = 0; i < ll.getChildCount(); i++) {
                                        View child = ll.getChildAt(i);
                                        if (child instanceof CheckBox) {
                                            CheckBox cb = (CheckBox) child;
                                            cb.setError(null);
                                        }
                                    }
                                }
                            });
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

        boolean bandera = true;
        String Productos = new String();
        String XmlData = new String();
        XmlData = "<f>";

        for(int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if(child instanceof EditTextRequired) {
                //Support for EditText
                EditTextRequired et = (EditTextRequired)child;

                if (et.isRequire() && et.getText().toString().equals("")){
                    et.setFocusable(true);
                    et.setFocusableInTouchMode(true);
                    et.requestFocus();
                    et.setError("Campo requerido");

                    bandera = false;

                    break;
                }

                XmlData = XmlData + "<e id=\""+et.getId()+"\"  t=\"2\" c=\""+ et.getHint() +"\"  v=\"" + et.getText() + "\" />";

            } else if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox)child;
                int answer = cb.isChecked() ? 1 : 0;
                if (answer == 1){
                    Productos = ConcatProducts(Productos, cb.getText().toString());
                }

            } else if (child instanceof Spinner){
                Spinner spinner = (Spinner)child;
                XmlData = XmlData + "<e id=\""+spinner.getTag()+"\" t=\"1\" c=\""+ spinner.getSelectedItem().toString() +"\" v=\"" + spinner.getSelectedItem().toString() + "\" />";
            }
        }

        XmlData = XmlData + "<e id=\""+11+"\" t=\"3\" c=\"Producto\" v=\"" + Productos + "\" />";
        XmlData = XmlData + "</f>";

        if (bandera && validateCheckBox(parent)){
            guardarCensoData(XmlData);
        }
    }

    public boolean validateCheckBox(ViewGroup parent){

        for(int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox) child;
                int answer = cb.isChecked() ? 1 : 0;
                if (answer == 1) {
                    return true;
                }
                //Toast.makeText(getActivity(), "Faltan campo requeridos por llenar", Toast.LENGTH_LONG).show();
                cb.setError("");
            }
        }

        return false;
    }


    public void guardarCensoData(String xml){

        CensuPointDataRequest censuPointDataRequest = new CensuPointDataRequest();
        TracePoint tracePoint = new TracePoint();
        ServiceLocation serviceLocation = new ServiceLocation(getActivity());
        censuPointDataRequest.setUserId(mydb.seletUser().getId());
        censuPointDataRequest.setRouteId(mydb.seletRouteActive().getId());
        censuPointDataRequest.setCompanyCode("001");
        censuPointDataRequest.setData(xml);

        Date pStart = new Date();

        tracePoint.setDateTime("/Date(" + pStart.getTime() + "+0200)/");
        tracePoint.setIdRoute(mydb.seletRouteActive().getId());
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
