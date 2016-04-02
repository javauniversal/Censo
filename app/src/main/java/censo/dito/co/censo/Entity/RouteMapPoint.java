package censo.dito.co.censo.Entity;

import com.google.gson.annotations.SerializedName;

public class RouteMapPoint extends Point {

    @SerializedName("Order")
    private int Order;

    private int idRoute;

    public int getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(int idRoute) {
        this.idRoute = idRoute;
    }

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }
}
