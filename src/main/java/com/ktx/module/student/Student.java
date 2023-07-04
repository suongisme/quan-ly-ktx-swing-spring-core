package com.ktx.module.student;

import com.ktx.module.room.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "student_id", unique = true)
    private String studentId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Temporal(TemporalType.DATE)
    @Column(name = "dob")
    private Date dob;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "room_code", referencedColumnName = "code")
    private Room room;
}
