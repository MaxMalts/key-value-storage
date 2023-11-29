package org.maxmalts.key_value_storage_database;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "strings_storage", schema = "strings_storage", catalog = "1c-microservices")
public class StringsStorageEntity {
    @Basic
    @Id
    @Column(name = "key", nullable = false, length = -1)
    private String key;
    @Basic
    @Column(name = "value", nullable = false, length = -1)
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StringsStorageEntity that = (StringsStorageEntity) o;
        return Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
