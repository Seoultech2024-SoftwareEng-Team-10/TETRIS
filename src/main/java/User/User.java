package User;

public class User {
    private String loginId;
    private String password;
    private String nickname;
    public User(String loginId, String password, String nickname){
        this.loginId  = loginId;
        this.password = password;
        this.nickname = nickname;
    }
    public String getLoginId(){
        return this.loginId;
    }
    public String getNickname(){
        return this.nickname;
    }
    public String getPassword(){
        return this.password;
    }
}
