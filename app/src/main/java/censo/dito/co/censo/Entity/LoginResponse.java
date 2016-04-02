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

    @SerializedName("ActiveRoute")
    private Route ActiveRoute;

    @SerializedName("Routes")
    private List<Route> Routes;

    private static LoginResponse loginRequest;

    public static LoginResponse getLoginRequest() {
        return loginRequest;
    }

    public static void setLoginRequest(LoginResponse loginRequest) {
        LoginResponse.loginRequest = loginRequest;
    }

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

    public Route getActiveRoute() {
        return ActiveRoute;
    }

    public void setActiveRoute(Route activeRoute) {
        ActiveRoute = activeRoute;
    }

}
