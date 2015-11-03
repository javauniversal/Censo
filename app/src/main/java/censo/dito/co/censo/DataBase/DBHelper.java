package censo.dito.co.censo.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import censo.dito.co.censo.Entity.Configuracion;
import censo.dito.co.censo.Entity.LoginResponse;
import censo.dito.co.censo.Entity.Route;
import censo.dito.co.censo.R;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    private Context context;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlRuta = context.getString(R.string.query_ruta);

        String sqlCenso = context.getString(R.string.query_censo);

        String sqlDetallRuta = context.getString(R.string.query_detalleRuta);

        String sqlMapPonit = context.getString(R.string.query_mapPoint);

        String sqlConfiguracion = context.getString(R.string.query_configuracionbase);

        db.execSQL(sqlRuta);
        db.execSQL(sqlCenso);
        db.execSQL(sqlDetallRuta);
        db.execSQL(sqlMapPonit);
        db.execSQL(sqlConfiguracion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS Ruta");
        db.execSQL("DROP TABLE IF EXISTS Censo");
        db.execSQL("DROP TABLE IF EXISTS DestalleRuta");
        db.execSQL("DROP TABLE IF EXISTS MapPoint");
        db.execSQL("DROP TABLE IF EXISTS ConfiguracionApp");
        this.onCreate(db);
    }

    //Insert configuracion.
    public boolean insertConfiguracion (String url, int interval){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("urlBase", url);
            values.put("intervalBase", interval);

            db.insert("ConfiguracionApp", null, values);
            db.close();
        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }
        return true;
    }

    //Eliminamos las rutas del usuario.
    public boolean deleteConfiguracion(){
        SQLiteDatabase db = this.getWritableDatabase();
        int p = db.delete("ConfiguracionApp", null, null);
        db.close();
        return p > 0;
    }

    //Insert las rutas del usuario .
    public boolean insertRuta (LoginResponse data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < data.getRoutes().size(); i++) {
                values.put("id", data.getRoutes().get(i).getId());
                values.put("nombreRuta", data.getRoutes().get(i).getDescription());
                values.put("fechainicial", data.getRoutes().get(i).getStart());
                values.put("fechafinal", data.getRoutes().get(i).getEnd());
                values.put("estadoruta", data.getRoutes().get(i).getState());
                db.insert("Ruta", null, values);
            }
            Log.d("Ruta", data.toString());
            db.close();

        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }
        return true;
    }

    //Eliminamos las rutas del usuario.
    public boolean deleteRuta(){
        SQLiteDatabase db = this.getWritableDatabase();
        int p = db.delete("Ruta", null, null);
        db.close();
        return p > 0;
    }

    //Recuperamos la lista de rutas.
    public Configuracion getConfiguracion() {
        String sql = "SELECT id, urlBase, intervalBase FROM ConfiguracionApp";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        Configuracion confg = null;
        if (cursor.moveToFirst()) {
            do {
                confg = new Configuracion();
                confg.setUrl(cursor.getString(1));
                confg.setInterval(Integer.parseInt(cursor.getString(2)));
            } while(cursor.moveToNext());
        }
        return confg;
    }

    //Validamos si tiene rutas activas
    public boolean validarInicioRuta(int estado){
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        Cursor cursor;
        String columns[] = {"id", "nombreRuta", "fechainicial", "fechafinal", "estadoruta"};
        cursor = db.query("Ruta", columns, "estadoruta = ?", new String[] {String.valueOf(estado)}, null, null, null, null);
        return cursor.getCount() <= 0;
    }

    public boolean validarInicioRuta(int estado, int idRuta){
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        Cursor cursor;
        String columns[] = {"id", "nombreRuta", "fechainicial", "fechafinal", "estadoruta"};
        cursor = db.query("Ruta", columns, "estadoruta = ? and id = ?", new String[] {String.valueOf(estado), String.valueOf(idRuta)}, null, null, null, null);
        return cursor.getCount() <= 0;
    }

    //Recuperamos el id de la ruta iniciada.
    public int idRuta(){
        int _idRuta = 0;
        String sql = "SELECT * FROM Ruta WHERE estadoruta = 2 LIMIT 1 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                _idRuta = Integer.parseInt(cursor.getString(0));
            } while(cursor.moveToNext());
        }

        return _idRuta;
    }

    //Recuperamos la lista de rutas.
    public List<Route> getRuta() {
        ArrayList<Route> addRutas = new ArrayList<>();
        String sql = "SELECT id, nombreRuta, fechainicial, fechafinal, estadoruta FROM Ruta";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        Route ruta;
        if (cursor.moveToFirst()) {
            do {
                ruta = new Route();
                ruta.setId(Integer.parseInt(cursor.getString(0)));
                ruta.setDescription(cursor.getString(1));
                ruta.setStart(cursor.getString(2));
                ruta.setEnd(cursor.getString(3));
                ruta.setState(Integer.parseInt(cursor.getString(4)));
                addRutas.add(ruta);
            } while(cursor.moveToNext());
        }
        return addRutas;
    }

    //Valida que las rutas esten iniciadas.
    public boolean validateCanEndRoute(int idRuta){
        String sql = "SELECT * FROM Ruta WHERE id = " + idRuta + " and estadoruta = 2 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        return cursor.getCount() > 0;
    }

    //Actualiza el estado de la rutas.
    public boolean updateRuta(Route data, int estado){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fechainicial", data.getStart());
        values.put("estadoruta", estado);
        try {
            db.update("Ruta", values, "id = ?", new String[] {String.valueOf(data.getId())});
            db.close();
        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to update word,", e);
            return false;
        }
        return true;
    }

}

