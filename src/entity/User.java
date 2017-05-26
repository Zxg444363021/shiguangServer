package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Administrator on 2017/5/26.
 */
@Entity
public class User {
    private long userid;
    private String phone;
    private String email;
    private String password;
    private String name;
    private String icon;
    private Integer power;
    private String wxid;
    private String qqid;
    private String sinaid;
    private String wxname;
    private String qqname;
    private String sinaname;
    private Integer tomatoN;
    private Integer powerToday;

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
    @Column(name = "email", nullable = true, length = 45)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    @Column(name = "power", nullable = true)
    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
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
    @Column(name = "qqid", nullable = true, length = 45)
    public String getQqid() {
        return qqid;
    }

    public void setQqid(String qqid) {
        this.qqid = qqid;
    }

    @Basic
    @Column(name = "sinaid", nullable = true, length = 45)
    public String getSinaid() {
        return sinaid;
    }

    public void setSinaid(String sinaid) {
        this.sinaid = sinaid;
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
    @Column(name = "qqname", nullable = true, length = 45)
    public String getQqname() {
        return qqname;
    }

    public void setQqname(String qqname) {
        this.qqname = qqname;
    }

    @Basic
    @Column(name = "sinaname", nullable = true, length = 45)
    public String getSinaname() {
        return sinaname;
    }

    public void setSinaname(String sinaname) {
        this.sinaname = sinaname;
    }

    @Basic
    @Column(name = "tomato_n", nullable = true)
    public Integer getTomatoN() {
        return tomatoN;
    }

    public void setTomatoN(Integer tomatoN) {
        this.tomatoN = tomatoN;
    }

    @Basic
    @Column(name = "power_today", nullable = true)
    public Integer getPowerToday() {
        return powerToday;
    }

    public void setPowerToday(Integer powerToday) {
        this.powerToday = powerToday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userid != user.userid) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (icon != null ? !icon.equals(user.icon) : user.icon != null) return false;
        if (power != null ? !power.equals(user.power) : user.power != null) return false;
        if (wxid != null ? !wxid.equals(user.wxid) : user.wxid != null) return false;
        if (qqid != null ? !qqid.equals(user.qqid) : user.qqid != null) return false;
        if (sinaid != null ? !sinaid.equals(user.sinaid) : user.sinaid != null) return false;
        if (wxname != null ? !wxname.equals(user.wxname) : user.wxname != null) return false;
        if (qqname != null ? !qqname.equals(user.qqname) : user.qqname != null) return false;
        if (sinaname != null ? !sinaname.equals(user.sinaname) : user.sinaname != null) return false;
        if (tomatoN != null ? !tomatoN.equals(user.tomatoN) : user.tomatoN != null) return false;
        if (powerToday != null ? !powerToday.equals(user.powerToday) : user.powerToday != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userid ^ (userid >>> 32));
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (power != null ? power.hashCode() : 0);
        result = 31 * result + (wxid != null ? wxid.hashCode() : 0);
        result = 31 * result + (qqid != null ? qqid.hashCode() : 0);
        result = 31 * result + (sinaid != null ? sinaid.hashCode() : 0);
        result = 31 * result + (wxname != null ? wxname.hashCode() : 0);
        result = 31 * result + (qqname != null ? qqname.hashCode() : 0);
        result = 31 * result + (sinaname != null ? sinaname.hashCode() : 0);
        result = 31 * result + (tomatoN != null ? tomatoN.hashCode() : 0);
        result = 31 * result + (powerToday != null ? powerToday.hashCode() : 0);
        return result;
    }
}
