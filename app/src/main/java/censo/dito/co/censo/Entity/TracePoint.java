package censo.dito.co.censo.Entity;

import com.google.gson.annotations.SerializedName;

public class TracePoint extends Point {

    @SerializedName("DateTime")
    private String DateTime;

    private int idRoute;

    public int getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(int idRoute) {
        this.idRoute = idRoute;
    }

    public String getDateTime() { return DateTime; }

    public void setDateTime(String dateTime) { DateTime = dateTime; }

}
