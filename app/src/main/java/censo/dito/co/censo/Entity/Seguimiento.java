package censo.dito.co.censo.Entity;


public class Seguimiento {

    private int user_id;
    private int rute_id;
    private String fecha;
    private double latitud;
    private double longitud;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRute_id() {
        return rute_id;
    }

    public void setRute_id(int rute_id) {
        this.rute_id = rute_id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
