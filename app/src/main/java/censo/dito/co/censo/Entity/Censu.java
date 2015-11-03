package censo.dito.co.censo.Entity;


import com.google.gson.annotations.SerializedName;

public class Censu extends Point {

    @SerializedName("Id")
    private int Id;

    @SerializedName("RouteId")
    private int RouteId;

    @SerializedName("DescriptionLine1")
    private String DescriptionLine1;

    @SerializedName("DescriptionLine2")
    private String DescriptionLine2;

    @SerializedName("Data")
    private String Data;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getRouteId() {
        return RouteId;
    }

    public void setRouteId(int routeId) {
        RouteId = routeId;
    }

    public String getDescriptionLine1() {
        return DescriptionLine1;
    }

    public void setDescriptionLine1(String descriptionLine1) {
        DescriptionLine1 = descriptionLine1;
    }

    public String getDescriptionLine2() {
        return DescriptionLine2;
    }

    public void setDescriptionLine2(String descriptionLine2) {
        DescriptionLine2 = descriptionLine2;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

}
