package censo.dito.co.censo.Entity;

import com.google.gson.annotations.SerializedName;

public class Point {

    @SerializedName("Latitude")
    double Latitude;

    @SerializedName("Longitude")
    double Longitude;

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
