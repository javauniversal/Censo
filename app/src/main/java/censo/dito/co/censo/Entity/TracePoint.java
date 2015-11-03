package censo.dito.co.censo.Entity;

import com.google.gson.annotations.SerializedName;

public class TracePoint extends Point {

    @SerializedName("DateTime")
    private String DateTime;

    public String getDateTime() { return DateTime; }

    public void setDateTime(String dateTime) { DateTime = dateTime; }

}
