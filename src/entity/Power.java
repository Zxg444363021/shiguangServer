package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

/**
 * Created by Administrator on 2017/5/28.
 */
@Entity
public class Power {
    private long userid;
    private int power;
    private int power1Today;
    private int power2Today;
    private int power1Stolen;
    private int power2Stolen;
    private Date date;
    private int power1Yesterday;
    private int power2Yesterday;
    private int canPower1Steal;
    private int canPower2Steal;

    public Power(){}
    public Power(long userid, int power1Yesterday, int power2Yesterday, int canPower1Steal, int canPower2Steal) {
        this.userid = userid;
        this.power1Yesterday = power1Yesterday;
        this.power2Yesterday = power2Yesterday;
        this.canPower1Steal = canPower1Steal;
        this.canPower2Steal = canPower2Steal;
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
    @Column(name = "power", nullable = false)
    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
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
    @Column(name = "date", nullable = false)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Power power1 = (Power) o;

        if (userid != power1.userid) return false;
        if (power != power1.power) return false;
        if (power1Today != power1.power1Today) return false;
        if (power2Today != power1.power2Today) return false;
        if (power1Stolen != power1.power1Stolen) return false;
        if (power2Stolen != power1.power2Stolen) return false;
        if (power1Yesterday != power1.power1Yesterday) return false;
        if (power2Yesterday != power1.power2Yesterday) return false;
        if (date != null ? !date.equals(power1.date) : power1.date != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userid ^ (userid >>> 32));
        result = 31 * result + power;
        result = 31 * result + power1Today;
        result = 31 * result + power2Today;
        result = 31 * result + power1Stolen;
        result = 31 * result + power2Stolen;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + power1Yesterday;
        result = 31 * result + power2Yesterday;
        return result;
    }

    @Basic
    @Column(name = "can_power1_steal", nullable = false)
    public int getCanPower1Steal() {
        return canPower1Steal;
    }

    public void setCanPower1Steal(int canPower1Steal) {
        this.canPower1Steal = canPower1Steal;
    }

    @Basic
    @Column(name = "can_power2_steal", nullable = false)
    public int getCanPower2Steal() {
        return canPower2Steal;
    }

    public void setCanPower2Steal(int canPower2Steal) {
        this.canPower2Steal = canPower2Steal;
    }
}
