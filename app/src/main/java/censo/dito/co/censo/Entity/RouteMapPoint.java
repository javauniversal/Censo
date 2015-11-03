package censo.dito.co.censo.Entity;

import com.google.gson.annotations.SerializedName;

public class RouteMapPoint extends Point {

    @SerializedName("Order")
    private int Order;

    public int getOrder() {
        return Order;
    }

    public void setOrder(int order) {
        Order = order;
    }
}
