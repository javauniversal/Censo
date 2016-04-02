package censo.dito.co.censo.Entity;


import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("Id")
    private int Id;

    @SerializedName("UserId")
    private String UserId;

    @SerializedName("FullName")
    private String FullName;

    @SerializedName("PhotoURL")
    private String PhotoURL;

    @SerializedName("ActiveRoute")
    private Route ActiveRoute;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public Route getActiveRoute() { return ActiveRoute; }

    public void setActiveRoute(Route activeRoute) { ActiveRoute = activeRoute; }

}
