package cc.chengheng;

public class EntityUser {

    private int id;
    private String pwd;

    public EntityUser() {
    }

    public EntityUser(int id, String pwd) {
        this.id = id;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
