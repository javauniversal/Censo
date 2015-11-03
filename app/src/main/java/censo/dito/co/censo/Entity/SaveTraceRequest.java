package censo.dito.co.censo.Entity;

import java.util.List;


public class SaveTraceRequest {

    private int UserId;
    private int RouteId;
    private List<TracePoint> Points;

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

    public List<TracePoint> getPoints() {
        return Points;
    }

    public void setPoints(List<TracePoint> points) {
        Points = points;
    }

}
