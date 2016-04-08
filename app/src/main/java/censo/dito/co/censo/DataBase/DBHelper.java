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

import censo.dito.co.censo.Entity.Censu;
import censo.dito.co.censo.Entity.CensuPointDataRequest;
import censo.dito.co.censo.Entity.Configuracion;
import censo.dito.co.censo.Entity.LoginResponse;
import censo.dito.co.censo.Entity.Route;
import censo.dito.co.censo.Entity.RouteMapPoint;
import censo.dito.co.censo.Entity.Seguimiento;
import censo.dito.co.censo.Entity.ShippingForm;
import censo.dito.co.censo.Entity.TracePoint;
import censo.dito.co.censo.Entity.User;
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

        String sql_usuario = context.getString(R.string.query_user);
        String sql_rooute_active = context.getString(R.string.query_route_active);
        String sql_rooute_map_point_active = context.getString(R.string.query_route_map_point_active);
        String sql_trace_point_active = context.getString(R.string.query_trace_point_active);
        String sqlRutas = context.getString(R.string.query_rutas);
        String sqlTrackingDetail = context.getString(R.string.query_tracking);
        String sqlMapPonit = context.getString(R.string.query_mapPoint);
        String sqlSeguimiento = context.getString(R.string.query_seguimiento);
        String sqlCensoDb = context.getString(R.string.query_censodd);

        db.execSQL(sql_usuario);
        db.execSQL(sql_rooute_active);
        db.execSQL(sql_rooute_map_point_active);
        db.execSQL(sql_trace_point_active);
        db.execSQL(sqlRutas);
        db.execSQL(sqlTrackingDetail);
        db.execSQL(sqlMapPonit);
        db.execSQL(sqlSeguimiento);
        db.execSQL(sqlCensoDb);



        /*String sqlCenso = context.getString(R.string.query_censo);
        String sqlDetallRuta = context.getString(R.string.query_detalleRuta);

        String sqlConfiguracion = context.getString(R.string.query_configuracionbase);




        db.execSQL(sqlRuta);
        db.execSQL(sqlCenso);
        db.execSQL(sqlDetallRuta);
        db.execSQL(sqlMapPonit);
        db.execSQL(sqlConfiguracion);
        db.execSQL(sqlCensoDb);
        db.execSQL(sqlSeguimiento);
        db.execSQL(sqlTrackingDetail);*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS route_active");
        db.execSQL("DROP TABLE IF EXISTS route_mappoint_active");
        db.execSQL("DROP TABLE IF EXISTS trace_point_active");
        db.execSQL("DROP TABLE IF EXISTS Ruta");
        db.execSQL("DROP TABLE IF EXISTS tracking");
        db.execSQL("DROP TABLE IF EXISTS MapPoint");
        db.execSQL("DROP TABLE IF EXISTS seguimiento");
        db.execSQL("DROP TABLE IF EXISTS censobb");
        this.onCreate(db);

        /*db.execSQL("DROP TABLE IF EXISTS Ruta");
        db.execSQL("DROP TABLE IF EXISTS Censo");
        db.execSQL("DROP TABLE IF EXISTS DestalleRuta");
        db.execSQL("DROP TABLE IF EXISTS MapPoint");
        db.execSQL("DROP TABLE IF EXISTS ConfiguracionApp");
        db.execSQL("DROP TABLE IF EXISTS censoDb");
        db.execSQL("DROP TABLE IF EXISTS seguimiento");
        db.execSQL("DROP TABLE IF EXISTS tracking");
        this.onCreate(db);*/
    }

    public boolean insertRouteActive(Route route){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();
        ContentValues values3 = new ContentValues();

        try {
            values.put("Idrouteactive", route.getId());
            values.put("name", route.getName());
            values.put("Description", route.getDescription());
            values.put("Start", route.getStart());
            values.put("End", route.getEnd());
            values.put("State", route.getState());
            values.put("StrucDataEntry", route.getStrucDataEntry());
            db.insert("route_active", null, values);

            for (int e = 0; e < route.getMap().size(); e++){
                values2.put("orden", route.getMap().get(e).getOrder());
                values2.put("id_orden", route.getMap().get(e).getIdRoute());
                values2.put("latitude", route.getMap().get(e).getLatitude());
                values2.put("longitude", route.getMap().get(e).getLongitude());
                db.insert("route_mappoint_active", null, values2);
            }

            for (int f = 0; f < route.getTrackingDetail().size(); f++){
                values3.put("date_time", route.getTrackingDetail().get(f).getDateTime());
                values3.put("id_route", route.getTrackingDetail().get(f).getIdRoute());
                values3.put("latitude", route.getTrackingDetail().get(f).getLatitude());
                values3.put("longitude", route.getTrackingDetail().get(f).getLongitude());
                db.insert("trace_point_active", null, values3);
            }

            db.close();

        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;

    }

    public Route seletRouteActive() {
        String sql = "SELECT id, Idrouteactive, name, Description, Start, End, State, StrucDataEntry FROM route_active";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        Route route = new Route();

        List<RouteMapPoint> routeMapPointList = new ArrayList<>();
        RouteMapPoint routeMapPoint;

        List<TracePoint> tracePointList = new ArrayList<>();
        TracePoint tracePoint;

        if (cursor.moveToFirst()) {
            do {
                route.setId(Integer.parseInt(cursor.getString(1)));
                route.setName(cursor.getString(2));
                route.setDescription(cursor.getString(3));
                route.setStart(cursor.getString(4));
                route.setEnd(cursor.getString(5));
                route.setState(Integer.parseInt(cursor.getString(6)));
                route.setStrucDataEntry(cursor.getString(7));

                String sql_route_map_point_active = "SELECT id, orden, id_orden, latitude, longitude FROM route_mappoint_active";
                Cursor cursor_route_map_point_active = db.rawQuery(sql_route_map_point_active, null);
                if (cursor_route_map_point_active.moveToFirst()) {
                    do {
                        routeMapPoint = new RouteMapPoint();
                        routeMapPoint.setOrder(Integer.parseInt(cursor_route_map_point_active.getString(1)));
                        routeMapPoint.setIdRoute(Integer.parseInt(cursor_route_map_point_active.getString(2)));
                        routeMapPoint.setLatitude(Double.parseDouble(cursor_route_map_point_active.getString(3)));
                        routeMapPoint.setLongitude(Double.parseDouble(cursor_route_map_point_active.getString(4)));
                        routeMapPointList.add(routeMapPoint);
                    } while(cursor_route_map_point_active.moveToNext());
                }

                route.setMap(routeMapPointList);

                String sql_trace_point_active = "SELECT id, date_time, id_route, latitude, longitude FROM trace_point_active";
                Cursor cursor_trace_point_active = db.rawQuery(sql_trace_point_active, null);
                if (cursor_trace_point_active.moveToFirst()) {
                    do {
                        tracePoint = new TracePoint();
                        tracePoint.setDateTime(cursor_trace_point_active.getString(1));
                        tracePoint.setIdRoute(Integer.parseInt(cursor_trace_point_active.getString(2)));
                        tracePoint.setLatitude(Double.parseDouble(cursor_trace_point_active.getString(3)));
                        tracePoint.setLongitude(Double.parseDouble(cursor_trace_point_active.getString(4)));
                        tracePointList.add(tracePoint);
                    } while(cursor_trace_point_active.moveToNext());
                }

                route.setTrackingDetail(tracePointList);

            } while(cursor.moveToNext());
        }
        return route;
    }

    public boolean insertUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("Id_", user.getId());
            values.put("UserId", user.getUserId());
            values.put("FullName", user.getFullName());
            values.put("password", user.getPassword());
            values.put("PhotoURL", user.getPhotoURL());

            db.insert("user", null, values);
            db.close();
        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;

    }

    public User seletUser(){
        String sql = "SELECT id, Id_, UserId, FullName, password, PhotoURL FROM user";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        User user = new User();
        if (cursor.moveToFirst()) {
            do {
                user.setId(Integer.parseInt(cursor.getString(1)));
                user.setUserId(cursor.getString(2));
                user.setFullName(cursor.getString(3));
                user.setPassword(cursor.getString(4));
                user.setPhotoURL(cursor.getString(5));
            } while(cursor.moveToNext());
        }
        return user;
    }


    //Insert censo formulario.
    public boolean insertCensoFormulario (CensuPointDataRequest data) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put("userid", data.getUserId());
            values.put("datatime", data.getCoordenadas().getDateTime());
            values.put("latitud", data.getCoordenadas().getLatitude());
            values.put("longitud", data.getCoordenadas().getLongitude());
            values.put("data", data.getData());
            values.put("companycode", data.getCompanyCode());
            values.put("routeid", data.getRouteId());

            db.insert("censoDb", null, values);
            db.close();

        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }
        return true;
    }

    //Recuperamos los censos que no se pudieron enviar.
    public List<CensuPointDataRequest> getCenso() {

        List<CensuPointDataRequest> lists = new ArrayList<>();

        String sql = "SELECT userid, datatime, latitud, longitud, data, companycode, routeid, id  FROM censobb";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        CensuPointDataRequest censo = null;
        TracePoint tracePoint = null;
        if (cursor.moveToFirst()) {
            do {

                censo = new CensuPointDataRequest();
                tracePoint = new TracePoint();

                censo.setId_censo(Integer.parseInt(cursor.getString(7)));
                censo.setUserId(Integer.parseInt(cursor.getString(0)));
                censo.setData(cursor.getString(4));
                censo.setCompanyCode(cursor.getString(5));
                censo.setRouteId(Integer.parseInt(cursor.getString(6)));

                tracePoint.setDateTime(cursor.getString(1));
                tracePoint.setLatitude(Double.parseDouble(cursor.getString(2)));
                tracePoint.setLongitude(Double.parseDouble(cursor.getString(3)));

                censo.setCoordenadas(tracePoint);

                lists.add(censo);

            } while(cursor.moveToNext());
        }

        return lists;

    }

    //Eliminamos las rutas del usuario.
    public boolean deleteCenso(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int p = db.delete("censoDb", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return p > 0;
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

    //Insert las rutas del usuario.
    public boolean insertRuta (LoginResponse data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();
        ContentValues values3 = new ContentValues();

        try {
            for (int i = 0; i < data.getRoutes().size(); i++) {
                values.put("id", data.getRoutes().get(i).getId());
                values.put("nombreRuta", data.getRoutes().get(i).getDescription());
                values.put("fechainicial", data.getRoutes().get(i).getStart());
                values.put("fechafinal", data.getRoutes().get(i).getEnd());
                values.put("estadoruta", data.getRoutes().get(i).getState());

                db.insert("Ruta", null, values);

                for (int e = 0; e < data.getRoutes().get(i).getMap().size(); e++){
                    values2.put("orden", data.getRoutes().get(i).getMap().get(e).getOrder());
                    values2.put("idRuta", data.getRoutes().get(i).getId());
                    values2.put("latitud", data.getRoutes().get(i).getMap().get(e).getLatitude());
                    values2.put("longitud", data.getRoutes().get(i).getMap().get(e).getLongitude());

                    db.insert("MapPoint", null, values2);
                }

                for (int f = 0; f < data.getRoutes().get(i).getTrackingDetail().size(); f++){
                    values3.put("idRuta", data.getRoutes().get(i).getId());
                    values3.put("latitud", data.getRoutes().get(i).getTrackingDetail().get(f).getLatitude());
                    values3.put("longitud", data.getRoutes().get(i).getTrackingDetail().get(f).getLongitude());
                    values3.put("datetime", data.getRoutes().get(i).getTrackingDetail().get(f).getDateTime());

                    db.insert("tracking", null, values3);
                }

            }

            Log.d("Ruta", data.toString());
            db.close();

        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;
    }

    public List<TracePoint> getTracePoint(int idRuta) {
        ArrayList<TracePoint> tracePointArrayList = new ArrayList<>();
        String sql = "SELECT idRuta, latitud, longitud, datetime FROM tracking WHERE idRuta = "+idRuta+" ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        TracePoint tracePoint;
        if (cursor.moveToFirst()) {
            do {
                tracePoint = new TracePoint();
                tracePoint.setIdRoute(Integer.parseInt(cursor.getString(0)));
                tracePoint.setLatitude(Double.parseDouble(cursor.getString(1)));
                tracePoint.setLongitude(Double.parseDouble(cursor.getString(2)));
                tracePoint.setDateTime(cursor.getString(3));

                tracePointArrayList.add(tracePoint);
            } while(cursor.moveToNext());
        }
        return tracePointArrayList;
    }

    //Recuperamos la lista de rutas.
    public List<RouteMapPoint> getMapPoint(int idRuta) {
        ArrayList<RouteMapPoint> routeMapPointArrayList = new ArrayList<>();
        String sql = "SELECT orden, idRuta, latitud, longitud FROM MapPoint WHERE idRuta = "+idRuta+" ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        RouteMapPoint routeMapPoint;
        if (cursor.moveToFirst()) {
            do {
                routeMapPoint = new RouteMapPoint();
                routeMapPoint.setOrder(Integer.parseInt(cursor.getString(0)));
                routeMapPoint.setIdRoute(Integer.parseInt(cursor.getString(1)));
                routeMapPoint.setLatitude(Double.parseDouble(cursor.getString(2)));
                routeMapPoint.setLongitude(Double.parseDouble(cursor.getString(3)));
                routeMapPointArrayList.add(routeMapPoint);
            } while(cursor.moveToNext());
        }
        return routeMapPointArrayList;
    }

    //Eliminamos las rutas del usuario.
    public boolean deleteRuta(){
        SQLiteDatabase db = this.getWritableDatabase();
        int a = db.delete("Ruta", null, null);
        int b = db.delete("MapPoint", null, null);
        int c = db.delete("tracking", null, null);
        db.close();
        return c > 0;
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

    //Recuperamos el seguimiento para guardar en base de datos.
    public List<Seguimiento> getCensuSeguimiento() {

        ArrayList<Seguimiento> addSeguimientos = new ArrayList<>();

        String sql = "SELECT DISTINCT userid, ruteid FROM seguimiento";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        Seguimiento seguimiento;
        if (cursor.moveToFirst()) {
            do {
                seguimiento = new Seguimiento();
                seguimiento.setUser_id(Integer.parseInt(cursor.getString(0)));
                seguimiento.setRute_id(Integer.parseInt(cursor.getString(1)));
                addSeguimientos.add(seguimiento);
            } while(cursor.moveToNext());
        }
        return addSeguimientos;
    }

    public List<Seguimiento> getCensuSeguimiento(int userId, int ruteid) {

        ArrayList<Seguimiento> addSeguimientos = new ArrayList<>();

        String sql = "SELECT userid, ruteid, fecha, latitud, longitud FROM seguimiento WHERE userid = "+userId+" AND ruteid = "+ruteid+" ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        Seguimiento seguimiento;
        if (cursor.moveToFirst()) {
            do {
                seguimiento = new Seguimiento();
                seguimiento.setUser_id(Integer.parseInt(cursor.getString(0)));
                seguimiento.setRute_id(Integer.parseInt(cursor.getString(1)));
                seguimiento.setFecha(cursor.getString(2));
                seguimiento.setLatitud(Double.parseDouble(cursor.getString(3)));
                seguimiento.setLongitud(Double.parseDouble(cursor.getString(4)));
                addSeguimientos.add(seguimiento);
            } while(cursor.moveToNext());
        }

        return addSeguimientos;

    }

    //Insert el seguimiento del usuario.
    public boolean insertSeguimiento (Seguimiento data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put("userid", data.getUser_id());
            values.put("ruteid", data.getRute_id());
            values.put("fecha", data.getFecha());
            values.put("latitud", data.getLatitud());
            values.put("longitud", data.getLongitud());

            db.insert("seguimiento", null, values);

            Log.d("seguimiento", data.toString());
            db.close();
        }catch (SQLiteConstraintException e){
            Log.d("data", "failure to insert word,", e);
            return false;
        }

        return true;
    }

    //Eliminamos las rutas del usuario.
    public boolean deleteSeguimiento(int usuarioid, int idrute){
        SQLiteDatabase db = this.getWritableDatabase();
        int p = db.delete("seguimiento", "userid = ? AND ruteid = ?", new String[]{String.valueOf(usuarioid), String.valueOf(idrute)});
        db.close();
        return p > 0;
    }

}

