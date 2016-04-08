package censo.dito.co.censo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.LoginRequest;
import censo.dito.co.censo.Entity.LoginResponse;
import censo.dito.co.censo.Entity.Route;
import censo.dito.co.censo.MapMain;
import censo.dito.co.censo.R;

public class ActLogin extends AvtivityBase implements View.OnClickListener {

    private EditText codeCompany;
    private EditText codeUser;
    private EditText password;
    private DBHelper mydb;
    private TextView configuarion;
    private Route route = new Route();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        mydb = new DBHelper(this);

        codeCompany = (EditText) findViewById(R.id.codeCompany);
        codeUser = (EditText) findViewById(R.id.codeUser);
        password = (EditText) findViewById(R.id.password);
        configuarion = (TextView) findViewById(R.id.txtConfiguracion);

        String htmlString="<u>Configuración</u>";
        configuarion.setText(Html.fromHtml(htmlString));
        configuarion.setOnClickListener(this);

        codeCompany.setText("001");
        codeUser.setText("censousu1");
        password.setText("censo");

        ButtonRectangle login = (ButtonRectangle) findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    public void validateNegocio(){

        String url = String.format("%1$s%2$s", "http://181.143.94.74:2080/dito/services/zenso/Authentication.svc/", "Login");
        requestQueue = Volley.newRequestQueue(this);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCiaId(codeCompany.getText().toString());
        loginRequest.setUser(codeUser.getText().toString());
        loginRequest.setPassword(password.getText().toString());

        try {

            HashMap<String, Object> postParameters = new HashMap<>();
            postParameters.put("CiaId", loginRequest.getCiaId());
            postParameters.put("User", loginRequest.getUser());
            postParameters.put("Password", loginRequest.getPassword());

            String jsonParameters = new Gson().toJson(postParameters);
            JSONObject jsonRootObject = new JSONObject(jsonParameters);

            JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonRootObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            parseJSON(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(ActLogin.this, "Error de tiempo de espera",Toast.LENGTH_LONG).show();
                            } else if (error instanceof AuthFailureError) {
                                Toast.makeText(ActLogin.this, "Error Servidor",Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                Toast.makeText(ActLogin.this, "Server Error",Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                Toast.makeText(ActLogin.this, "Error de red",Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                Toast.makeText(ActLogin.this, "Error al serializar los datos",Toast.LENGTH_LONG).show();
                            }
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };

            requestQueue.add(jsArrayRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void parseJSON(JSONObject json) {

        try {
            Gson gson = new Gson();

            if (json == null || json.equals("")) {
                Toast.makeText(this, "Problemas al recuperar la información", Toast.LENGTH_SHORT).show();
            } else {
                LoginResponse loginResponse = gson.fromJson(String.valueOf(json), LoginResponse.class);

                if(loginResponse.isAuthenticated()){

                    loginResponse.getUser().setPassword(password.getText().toString());

                    if (mydb.insertUser(loginResponse.getUser()) && mydb.insertRouteActive(loginResponse.getUser().getActiveRoute()) && mydb.insertRuta(loginResponse)) {
                        startActivity(new Intent(ActLogin.this, MapMain.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } else {
                        Toast.makeText(this, "Problemas al guardar La primera Ruta!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Usuario/Password incorrecto", Toast.LENGTH_SHORT).show();
                }
            }

        }catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
        //return loginResponse;
    }

    private boolean isValidNumber(String number) {
        return number == null || number.length() == 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                //Configuracion configuracion = mydb.getConfiguracion();
                //if(configuracion != null){
                    if (isValidNumber(codeCompany.getText().toString())) {
                        codeCompany.setError(getResources().getString(R.string.validate_company));
                        codeCompany.setFocusableInTouchMode(true);
                        codeCompany.requestFocus();
                    } else if (isValidNumber(codeUser.getText().toString())) {
                        codeUser.setError(getResources().getString(R.string.validate_user));
                        codeUser.setFocusableInTouchMode(true);
                        codeUser.requestFocus();
                    } else if (isValidNumber(password.getText().toString())) {
                        password.setError(getResources().getString(R.string.validate_password));
                        password.setFocusableInTouchMode(true);
                        password.requestFocus();
                    } else {

                        route = mydb.seletRouteActive();
                        if (route.getName() != null) {
                            // HAY datos

                            if (password.getText().toString().equals(mydb.seletUser().getPassword())){
                                //Ingreso.
                                startActivity(new Intent(ActLogin.this, MapMain.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            } else {
                                Toast.makeText(this, "Usuario/Password incorrecto", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // NO hay datos
                            validateNegocio();
                        }
                    }
                //}else{
                  //  Toast.makeText(this, "La aplicación no esta configurada!", Toast.LENGTH_SHORT).show();
               // }

                break;
            case R.id.txtConfiguracion:
                startActivity(new Intent(ActLogin.this, ActConfiguracion.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }
}
