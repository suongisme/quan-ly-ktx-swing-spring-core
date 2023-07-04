package com.ktx.module.electronic;

import com.ktx.module.room.Room;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

import com.ktx.core.constant.PaymentEnum;

@Setter
@Getter
@Accessors(chain = true)
@Entity
@Table(name = "electronic")
public class Electronic {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "used_num")
    private Integer usedNum;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentEnum status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_code", referencedColumnName = "code")
    private Room room;
}
