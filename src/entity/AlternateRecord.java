package entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by ZXG on 2017/7/27.
 */
@Entity
@Table(name = "alternate_record", schema = "shiguang", catalog = "")
public class AlternateRecord {
    private long id;
    private long user1id;
    private long user2id;
    private int type;
    private int power;
    private Timestamp time;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user1id", nullable = false)
    public long getUser1id() {
        return user1id;
    }

    public void setUser1id(long user1id) {
        this.user1id = user1id;
    }
    @Basic
    @Column(name = "user2id", nullable = false)
    public long getUser2id() {
        return user2id;
    }

    public void setUser2id(long user2id) {
        this.user2id = user2id;
    }

    @Basic
    @Column(name = "type", nullable = false)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
    @Column(name = "time", nullable = false)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlternateRecord that = (AlternateRecord) o;

        if (id != that.id) return false;
        if (type != that.type) return false;
        if (power != that.power) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + type;
        result = 31 * result + power;
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
