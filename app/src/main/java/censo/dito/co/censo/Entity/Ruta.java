package censo.dito.co.censo.Entity;

import java.util.List;

public class Ruta {
    private int _idRuta;
    private String _nombre;
    private String _fechaInicio;
    private String _fechaFin;
    private String _estadoRuta;
    private List<Censo> _censos;
    private List<MapPoint> _mapaRegion;

    public int get_idRuta() {
        return _idRuta;
    }

    public void set_idRuta(int _idRuta) {
        this._idRuta = _idRuta;
    }

    public String get_nombre() {
        return _nombre;
    }

    public void set_nombre(String _nombre) {
        this._nombre = _nombre;
    }

    public String get_fechaInicio() {
        return _fechaInicio;
    }

    public void set_fechaInicio(String _fechaInicio) {
        this._fechaInicio = _fechaInicio;
    }

    public String get_fechaFin() {
        return _fechaFin;
    }

    public void set_fechaFin(String _fechaFin) {
        this._fechaFin = _fechaFin;
    }

    public String get_estadoRuta()
    {
        switch (_estadoRuta){
            case "Finalizada":
                return "Ruta Finalizada";
            case "EnEjecucion":
                return "En Ejecución";
            case "NoIniciada":
                return "No Iniciada";
        }

        return _estadoRuta;
    }

    public String get_enumEstadoRuta()
    {
        return _estadoRuta;
    }

    public void set_estadoRuta(String _estadoRuta) {
        this._estadoRuta = _estadoRuta;
    }

    public List<Censo> get_censos() {
        return _censos;
    }

    public void set_censos(List<Censo> _censos) {
        this._censos = _censos;
    }

    public List<MapPoint> get_mapaRegion() {
        return _mapaRegion;
    }

    public void set_mapaRegion(List<MapPoint> _mapaRegion) {
        this._mapaRegion = _mapaRegion;
    }
}