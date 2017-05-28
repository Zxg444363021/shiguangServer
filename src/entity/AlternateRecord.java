package entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/5/28.
 */
@Entity
@Table(name = "alternate_record", schema = "shiguang", catalog = "")
public class AlternateRecord {
    private long id;
    private int type;
    private int power;
    private long user1Id;
    private long user2Id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlternateRecord that = (AlternateRecord) o;

        if (id != that.id) return false;
        if (type != that.type) return false;
        if (power != that.power) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + type;
        result = 31 * result + power;
        return result;
    }

    @Basic
    @Column(name = "user1id", nullable = false)
    public long getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(long user1Id) {
        this.user1Id = user1Id;
    }

    @Basic
    @Column(name = "user2id", nullable = false)
    public long getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(long user2Id) {
        this.user2Id = user2Id;
    }

    @Basic
    @Column(name = "time", nullable = false)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;

    }
}
