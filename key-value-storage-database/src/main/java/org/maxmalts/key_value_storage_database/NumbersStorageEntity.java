package org.maxmalts.key_value_storage_database;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "numbers_storage", schema = "numbers_storage", catalog = "1c-microservices")
public class NumbersStorageEntity {
    @Basic
    @Id
    @Column(name = "key", nullable = false)
    private int key;
    @Basic
    @Column(name = "value", nullable = false, precision = 0)
    private double value;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
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
        NumbersStorageEntity that = (NumbersStorageEntity) o;
        return key == that.key && Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
