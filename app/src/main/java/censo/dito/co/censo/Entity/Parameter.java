package censo.dito.co.censo.Entity;

import com.google.gson.annotations.SerializedName;

public class Parameter {

    @SerializedName("userId")
    private int userId;

    @SerializedName("routeId")
    private int routeId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
}

