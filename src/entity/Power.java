package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Power {
    private long id;
    private long userid;
    private int power;
    private int powerStolen;
    private Date date;
    private int canSteal;
    private String uuid;
    private Integer powerType;
    private Integer hasGain;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
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
    @Column(name = "power_stolen", nullable = false)
    public int getPowerStolen() {
        return powerStolen;
    }

    public void setPowerStolen(int powerStolen) {
        this.powerStolen = powerStolen;
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
    @Column(name = "can_steal", nullable = false)
    public int getCanSteal() {
        return canSteal;
    }

    public void setCanSteal(int canSteal) {
        this.canSteal = canSteal;
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
    @Column(name = "power_type", nullable = true)
    public Integer getPowerType() {
        return powerType;
    }

    public void setPowerType(Integer powerType) {
        this.powerType = powerType;
    }

    @Basic
    @Column(name = "has_gain", nullable = true)
    public Integer getHasGain() {
        return hasGain;
    }

    public void setHasGain(Integer hasGain) {
        this.hasGain = hasGain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Power power1 = (Power) o;

        if (id != power1.id) return false;
        if (userid != power1.userid) return false;
        if (power != power1.power) return false;
        if (powerStolen != power1.powerStolen) return false;
        if (canSteal != power1.canSteal) return false;
        if (date != null ? !date.equals(power1.date) : power1.date != null) return false;
        if (uuid != null ? !uuid.equals(power1.uuid) : power1.uuid != null) return false;
        if (powerType != null ? !powerType.equals(power1.powerType) : power1.powerType != null) return false;
        if (hasGain != null ? !hasGain.equals(power1.hasGain) : power1.hasGain != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userid ^ (userid >>> 32));
        result = 31 * result + power;
        result = 31 * result + powerStolen;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + canSteal;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (powerType != null ? powerType.hashCode() : 0);
        result = 31 * result + (hasGain != null ? hasGain.hashCode() : 0);
        return result;
    }
}
