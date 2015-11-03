package censo.dito.co.censo.Entity;

public class DetalleRuta {
    private int _idDetalleRuta;
    private int _idRuta;
    private String _tiempoSeguimiento;
    private double _latitud;
    private double _longitud;

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

    public int get_idDetalleRuta() {
        return _idDetalleRuta;
    }

    public void set_idDetalleRuta(int _idDetalleRuta) {
        this._idDetalleRuta = _idDetalleRuta;
    }

    public int get_idRuta() {
        return _idRuta;
    }

    public void set_idRuta(int _idRuta) {
        this._idRuta = _idRuta;
    }

    public String get_tiempoSeguimiento() {
        return _tiempoSeguimiento;
    }

    public void set_tiempoSeguimiento(String _tiempoSeguimiento) {
        this._tiempoSeguimiento = _tiempoSeguimiento;
    }
}
