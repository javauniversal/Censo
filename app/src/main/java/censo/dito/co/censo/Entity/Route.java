package censo.dito.co.censo.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {

    @SerializedName("Id")
    private int Id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Description")
    private String Description;

    @SerializedName("Start")
    private String Start;

    @SerializedName("End")
    private String End;

    @SerializedName("State")
    private int State;

    @SerializedName("StrucDataEntry")
    private String StrucDataEntry;

    @SerializedName("Map")
    private List<RouteMapPoint> Map;

    @SerializedName("TrackingDetail")
    private List<TracePoint> TrackingDetail;



    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStart() {
        return Start;
    }

    public void setStart(String start) {
        Start = start;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public List<RouteMapPoint> getMap() {
        return Map;
    }

    public void setMap(List<RouteMapPoint> map) {
        Map = map;
    }

    public List<TracePoint> getTrackingDetail() {
        return TrackingDetail;
    }

    public void setTrackingDetail(List<TracePoint> trackingDetail) {
        TrackingDetail = trackingDetail;
    }

    public String getStrucDataEntry() {
        return StrucDataEntry;
    }

    public void setStrucDataEntry(String strucDataEntry) {
        StrucDataEntry = strucDataEntry;
    }
}
