package censo.dito.co.censo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.gson.Gson;
import java.util.HashMap;
import censo.dito.co.censo.DataBase.DBHelper;
import censo.dito.co.censo.Entity.Configuracion;
import censo.dito.co.censo.Entity.LoginRequest;
import censo.dito.co.censo.Entity.LoginResponse;
import censo.dito.co.censo.MapMain;
import censo.dito.co.censo.R;
import censo.dito.co.censo.Services.ServiceData;

public class ActLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText codeCompany;
    private EditText codeUser;
    private EditText password;
    private DBHelper mydb;
    private TextView configuarion;

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

    public void PostClick() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCiaId(codeCompany.getText().toString());
        loginRequest.setUser(codeUser.getText().toString());
        loginRequest.setPassword(password.getText().toString());

        HashMap<String, Object> postParameters = new HashMap<>();
        postParameters.put("CiaId", loginRequest.getCiaId());
        postParameters.put("User", loginRequest.getUser());
        postParameters.put("Password", loginRequest.getPassword());
        String jsonParameters = new Gson().toJson(postParameters);

        ServiceData serviceData = new ServiceData(this, String.format("%1$s%2$s", getString(R.string.url_authentication), "Login"), jsonParameters);
        serviceData.PostClick();
        parseJSON(serviceData.dataServices);

    }

    private void parseJSON(String json) {

        try {
            Gson gson = new Gson();

            if (json == null || json.equals("")){
                Toast.makeText(this, "Problemas al recuperar la información", Toast.LENGTH_SHORT).show();
            }else {
                LoginResponse loginResponse = gson.fromJson(json, LoginResponse.class);
                LoginResponse.setLoginRequest(loginResponse);

                if(loginResponse.isAuthenticated()){
                    //Insert Data Base.
                    mydb.deleteRuta();

                    if (mydb.insertRuta(loginResponse)){
                        startActivity(new Intent(ActLogin.this, MapMain.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }else{
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
                Configuracion configuracion = mydb.getConfiguracion();
                if(configuracion != null){
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
                        PostClick();
                    }
                }else{
                    Toast.makeText(this, "La aplicación no esta configurada!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.txtConfiguracion:
                startActivity(new Intent(ActLogin.this, ActConfiguracion.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }
}
