package censo.dito.co.censo.Entity;


public class ShippingForm {

    private int userId;
    private TracePoint tracePoint;
    private String data;
    private String company_code;
    private int routeId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public TracePoint getTracePoint() {
        return tracePoint;
    }

    public void setTracePoint(TracePoint tracePoint) {
        this.tracePoint = tracePoint;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
}
