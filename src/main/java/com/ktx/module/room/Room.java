package com.ktx.module.room;

import com.ktx.module.student.Student;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Accessors(chain = true)
@Entity
@Table(name = "room")
public class Room {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "max_people")
    private Integer maxPeople;
    
    @Column(name = "price")
    private Long price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    private Set<Student> students;

    @Override
    public String toString() {
        return this.code + " - " + this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Room)) {
            return false;
        }

        Room that = (Room) obj;
        return Objects.equals(that.getCode(), this.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.code);
    }
}
