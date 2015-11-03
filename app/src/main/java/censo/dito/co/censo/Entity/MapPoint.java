package censo.dito.co.censo.Entity;

public class MapPoint {
    private int _orden;
    private int _idRuta;
    private double _latitud;
    private double _longitud;

    public int get_orden() {
        return _orden;
    }

    public void set_orden(int _orden) {
        this._orden = _orden;
    }

    public int get_idRuta() {
        return _idRuta;
    }

    public void set_idRuta(int _idRuta) {
        this._idRuta = _idRuta;
    }

    public double get_latitud() {
        return _latitud;
    }

    public void set_latitud(double _latitud) {
        this._latitud = _latitud;
    }

    public double get_longitud() {
        return _longitud;
    }

    public void set_longitud(double _longitud) {
        this._longitud = _longitud;
    }
}
