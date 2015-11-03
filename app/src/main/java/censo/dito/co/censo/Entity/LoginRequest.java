package censo.dito.co.censo.Entity;


import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("User")
    private String user;

    @SerializedName("Password")
    private String password;

    @SerializedName("CiaId")
    private String ciaId;

    public String getUser() { return user; }

    public void setUser(String user) { this.user = user; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getCiaId() { return ciaId; }

    public void setCiaId(String ciaId) { this.ciaId = ciaId; }

}
