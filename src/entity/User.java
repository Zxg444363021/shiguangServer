package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    private long userid;
    private String phone;
    private String password;
    private String name;
    private String icon;
    private String wxid;
    private String wxname;
    private int power;
    private int tomatoN;
    private int power1Today;
    private int power2Today;
    private int power1Yesterday;
    private int power2Yesterday;
    private int power1Stolen;
    private int power2Stolen;
    private int power1CanSteal;
    private int power2CanSteal;
    private String uuid;
    private String token;
    private Integer canBeStolen;
    private Integer canSteal;

    public User(){}

    public User(long userid, String name, String icon, int power, int tomatoN, String uuid) {
        this.userid = userid;
        this.name = name;
        this.icon = icon;
        this.power = power;
        this.tomatoN = tomatoN;
        this.uuid = uuid;
    }

    @Id
    @Column(name = "userid", nullable = false)
    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "phone", nullable = true, length = 13)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 45)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "icon", nullable = true, length = 80)
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "wxid", nullable = true, length = 45)
    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    @Basic
    @Column(name = "wxname", nullable = true, length = 45)
    public String getWxname() {
        return wxname;
    }

    public void setWxname(String wxname) {
        this.wxname = wxname;
    }

    @Basic
    @Column(name = "power", nullable = false)
    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Basic
    @Column(name = "tomato_n", nullable = false)
    public int getTomatoN() {
        return tomatoN;
    }

    public void setTomatoN(int tomatoN) {
        this.tomatoN = tomatoN;
    }

    @Basic
    @Column(name = "power1_today", nullable = false)
    public int getPower1Today() {
        return power1Today;
    }

    public void setPower1Today(int power1Today) {
        this.power1Today = power1Today;
    }

    @Basic
    @Column(name = "power2_today", nullable = false)
    public int getPower2Today() {
        return power2Today;
    }

    public void setPower2Today(int power2Today) {
        this.power2Today = power2Today;
    }

    @Basic
    @Column(name = "power1_yesterday", nullable = false)
    public int getPower1Yesterday() {
        return power1Yesterday;
    }

    public void setPower1Yesterday(int power1Yesterday) {
        this.power1Yesterday = power1Yesterday;
    }

    @Basic
    @Column(name = "power2_yesterday", nullable = false)
    public int getPower2Yesterday() {
        return power2Yesterday;
    }

    public void setPower2Yesterday(int power2Yesterday) {
        this.power2Yesterday = power2Yesterday;
    }

    @Basic
    @Column(name = "power1_stolen", nullable = false)
    public int getPower1Stolen() {
        return power1Stolen;
    }

    public void setPower1Stolen(int power1Stolen) {
        this.power1Stolen = power1Stolen;
    }

    @Basic
    @Column(name = "power2_stolen", nullable = false)
    public int getPower2Stolen() {
        return power2Stolen;
    }

    public void setPower2Stolen(int power2Stolen) {
        this.power2Stolen = power2Stolen;
    }

    @Basic
    @Column(name = "power1_can_steal", nullable = false)
    public int getPower1CanSteal() {
        return power1CanSteal;
    }

    public void setPower1CanSteal(int power1CanSteal) {
        this.power1CanSteal = power1CanSteal;
    }

    @Basic
    @Column(name = "power2_can_steal", nullable = false)
    public int getPower2CanSteal() {
        return power2CanSteal;
    }

    public void setPower2CanSteal(int power2CanSteal) {
        this.power2CanSteal = power2CanSteal;
    }

    @Basic
    @Column(name = "uuid", nullable = true, length = 45)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "token", nullable = true, length = 45)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "can_be_stolen", nullable = true)
    public Integer getCanBeStolen() {
        return canBeStolen;
    }

    public void setCanBeStolen(Integer canBeStolen) {
        this.canBeStolen = canBeStolen;
    }

    @Basic
    @Column(name = "can_steal", nullable = true)
    public Integer getCanSteal() {
        return canSteal;
    }

    public void setCanSteal(Integer canSteal) {
        this.canSteal = canSteal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userid != user.userid) return false;
        if (power != user.power) return false;
        if (tomatoN != user.tomatoN) return false;
        if (power1Today != user.power1Today) return false;
        if (power2Today != user.power2Today) return false;
        if (power1Yesterday != user.power1Yesterday) return false;
        if (power2Yesterday != user.power2Yesterday) return false;
        if (power1Stolen != user.power1Stolen) return false;
        if (power2Stolen != user.power2Stolen) return false;
        if (power1CanSteal != user.power1CanSteal) return false;
        if (power2CanSteal != user.power2CanSteal) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (icon != null ? !icon.equals(user.icon) : user.icon != null) return false;
        if (wxid != null ? !wxid.equals(user.wxid) : user.wxid != null) return false;
        if (wxname != null ? !wxname.equals(user.wxname) : user.wxname != null) return false;
        if (uuid != null ? !uuid.equals(user.uuid) : user.uuid != null) return false;
        if (token != null ? !token.equals(user.token) : user.token != null) return false;
        if (canBeStolen != null ? !canBeStolen.equals(user.canBeStolen) : user.canBeStolen != null) return false;
        if (canSteal != null ? !canSteal.equals(user.canSteal) : user.canSteal != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userid ^ (userid >>> 32));
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (wxid != null ? wxid.hashCode() : 0);
        result = 31 * result + (wxname != null ? wxname.hashCode() : 0);
        result = 31 * result + power;
        result = 31 * result + tomatoN;
        result = 31 * result + power1Today;
        result = 31 * result + power2Today;
        result = 31 * result + power1Yesterday;
        result = 31 * result + power2Yesterday;
        result = 31 * result + power1Stolen;
        result = 31 * result + power2Stolen;
        result = 31 * result + power1CanSteal;
        result = 31 * result + power2CanSteal;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (canBeStolen != null ? canBeStolen.hashCode() : 0);
        result = 31 * result + (canSteal != null ? canSteal.hashCode() : 0);
        return result;
    }
}
