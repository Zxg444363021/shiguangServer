package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
public class Schedule {
    private long idInServer;
    private long userid;
    private String name;
    private String description;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int dayOfWeek;
    private int status;
    private int type;
    private int isDirty;
    private Timestamp lastModify;
    private int isDelete;

    @Id
    @Column(name = "id_in_server", nullable = false)
    public long getIdInServer() {
        return idInServer;
    }

    public void setIdInServer(long idInServer) {
        this.idInServer = idInServer;
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
    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "description", nullable = false, length = 100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "year", nullable = false)
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Basic
    @Column(name = "month", nullable = false)
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Basic
    @Column(name = "day", nullable = false)
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Basic
    @Column(name = "hour", nullable = false)
    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Basic
    @Column(name = "minute", nullable = false)
    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Basic
    @Column(name = "day_of_week", nullable = false)
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Basic
    @Column(name = "status", nullable = false)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
    @Column(name = "is_dirty", nullable = false)
    public int getIsDirty() {
        return isDirty;
    }

    public void setIsDirty(int isDirty) {
        this.isDirty = isDirty;
    }

    @Basic
    @Column(name = "last_modify", nullable = false)
    public Timestamp getLastModify() {
        return lastModify;
    }

    public void setLastModify(Timestamp lastModify) {
        this.lastModify = lastModify;
    }

    @Basic
    @Column(name = "is_delete", nullable = false)
    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        if (idInServer != schedule.idInServer) return false;
        if (userid != schedule.userid) return false;
        if (year != schedule.year) return false;
        if (month != schedule.month) return false;
        if (day != schedule.day) return false;
        if (hour != schedule.hour) return false;
        if (minute != schedule.minute) return false;
        if (dayOfWeek != schedule.dayOfWeek) return false;
        if (status != schedule.status) return false;
        if (type != schedule.type) return false;
        if (isDirty != schedule.isDirty) return false;
        if (isDelete != schedule.isDelete) return false;
        if (name != null ? !name.equals(schedule.name) : schedule.name != null) return false;
        if (description != null ? !description.equals(schedule.description) : schedule.description != null)
            return false;
        if (lastModify != null ? !lastModify.equals(schedule.lastModify) : schedule.lastModify != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (idInServer ^ (idInServer >>> 32));
        result = 31 * result + (int) (userid ^ (userid >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + year;
        result = 31 * result + month;
        result = 31 * result + day;
        result = 31 * result + hour;
        result = 31 * result + minute;
        result = 31 * result + dayOfWeek;
        result = 31 * result + status;
        result = 31 * result + type;
        result = 31 * result + isDirty;
        result = 31 * result + (lastModify != null ? lastModify.hashCode() : 0);
        result = 31 * result + isDelete;
        return result;
    }
}
