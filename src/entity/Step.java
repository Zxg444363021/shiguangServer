package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Step {
    private long id;
    private long userid;
    private int currentNumber;
    private int todayStep;
    private Date date;
    private String uuid;

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
    @Column(name = "current_number", nullable = false)
    public int getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }

    @Basic
    @Column(name = "today_step", nullable = false)
    public int getTodayStep() {
        return todayStep;
    }

    public void setTodayStep(int todayStep) {
        this.todayStep = todayStep;
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
    @Column(name = "uuid", nullable = true, length = 45)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Step step = (Step) o;

        if (id != step.id) return false;
        if (userid != step.userid) return false;
        if (currentNumber != step.currentNumber) return false;
        if (todayStep != step.todayStep) return false;
        if (date != null ? !date.equals(step.date) : step.date != null) return false;
        if (uuid != null ? !uuid.equals(step.uuid) : step.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (userid ^ (userid >>> 32));
        result = 31 * result + currentNumber;
        result = 31 * result + todayStep;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        return result;
    }
}
