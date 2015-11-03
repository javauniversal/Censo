package censo.dito.co.censo.Entity;

public class Censo {
    private int _idCenso;
    private int _idRuta;
    private String _tiempocenso;
    private double _latitud;
    private double _longitud;
    private String _censoData;

    //Ejemplo Censo Data
    //Ejem: “<d><El Tx=“Zona" Val=“1-Sur”><El Tx=“Barrio" Val=“Envigado"></d>"

    public int get_idCenso() {
        return _idCenso;
    }

    public void set_idCenso(int _idCenso) {
        this._idCenso = _idCenso;
    }

    public int get_idRuta() {
        return _idRuta;
    }

    public void set_idRuta(int _idRuta) {
        this._idRuta = _idRuta;
    }

    public String get_tiempocenso() {
        return _tiempocenso;
    }

    public void set_tiempocenso(String _tiempocenso) {
        this._tiempocenso = _tiempocenso;
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

    public String get_censoData() {
        return _censoData;
    }

    public void set_censoData(String _censoData) {
        this._censoData = _censoData;
    }

}
