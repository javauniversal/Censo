package censo.dito.co.censo.Entity;

/**
 * Created by IngenieroGerman on 12/10/2015.
 */

public class CensuPointDataRequest {

    private int UserId;
    private int RouteId;
    private String CompanyCode;
    private String Data;
    private TracePoint Coordenadas;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getRouteId() {
        return RouteId;
    }

    public void setRouteId(int routeId) {
        RouteId = routeId;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public TracePoint getCoordenadas() {
        return Coordenadas;
    }

    public void setCoordenadas(TracePoint coordenadas) {
        Coordenadas = coordenadas;
    }
}
