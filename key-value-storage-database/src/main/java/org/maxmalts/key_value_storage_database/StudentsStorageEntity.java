package org.maxmalts.key_value_storage_database;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "students_storage", schema = "students_storage", catalog = "1c-microservices")
@IdClass(StudentsStorageEntityPK.class)
public class StudentsStorageEntity {
    @Id
    @Column(name = "group_id", nullable = false)
    private int groupId;
    @Id
    @Column(name = "name", nullable = false, length = -1)
    private String name;
    @Basic
    @Column(name = "home_town", nullable = true, length = -1)
    private String homeTown;
    @Basic
    @Column(name = "birth_date", nullable = true)
    private Date birthDate;
    @Basic
    @Column(name = "has_dormitory", nullable = true)
    private Boolean hasDormitory;
    @Basic
    @Column(name = "average_score", nullable = true, precision = 0)
    private Double averageScore;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getHasDormitory() {
        return hasDormitory;
    }

    public void setHasDormitory(Boolean hasDormitory) {
        this.hasDormitory = hasDormitory;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StudentsStorageEntity that = (StudentsStorageEntity) o;
        return groupId == that.groupId && Objects.equals(
            name,
            that.name
        ) && Objects.equals(homeTown, that.homeTown) && Objects.equals(
            birthDate,
            that.birthDate
        ) && Objects.equals(hasDormitory, that.hasDormitory) && Objects.equals(
            averageScore,
            that.averageScore
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, name, homeTown, birthDate, hasDormitory, averageScore);
    }
}
