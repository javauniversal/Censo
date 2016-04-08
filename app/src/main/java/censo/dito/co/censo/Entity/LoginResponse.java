package censo.dito.co.censo.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("User")
    private User User;

    @SerializedName("IsAuthenticated")
    private boolean IsAuthenticated;

    @SerializedName("AuthenticatedErrorMsg")
    private String AuthenticatedErrorMsg;

    @SerializedName("Routes")
    private List<Route> Routes;


    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }

    public boolean isAuthenticated() {
        return IsAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        IsAuthenticated = isAuthenticated;
    }

    public String getAuthenticatedErrorMsg() {
        return AuthenticatedErrorMsg;
    }

    public void setAuthenticatedErrorMsg(String authenticatedErrorMsg) {
        AuthenticatedErrorMsg = authenticatedErrorMsg;
    }

    public List<Route> getRoutes() {
        return Routes;
    }

    public void setRoutes(List<Route> routes) {
        Routes = routes;
    }


}
