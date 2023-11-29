package org.maxmalts.key_value_storage_database;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class StudentsStorageEntityPK implements Serializable {
    @Column(name = "group_id", nullable = false)
    @Id
    private int groupId;
    @Column(name = "name", nullable = false, length = -1)
    @Id
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StudentsStorageEntityPK that = (StudentsStorageEntityPK) o;
        return groupId == that.groupId && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, name);
    }
}
