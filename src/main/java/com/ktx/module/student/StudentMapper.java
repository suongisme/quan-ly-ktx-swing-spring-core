package com.ktx.module.student;

import com.ktx.core.utils.DateUtils;
import com.ktx.module.room.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    private final DateUtils dateUtils;

    public StudentDTO toDTO(Student student) {
        return new StudentDTO()
                .setStudentId(student.getStudentId())
                .setName(student.getName())
                .setDob(this.dateUtils.dateToString(student.getDob()))
                .setPhone(student.getPhone())
                .setAddress(student.getAddress());
    }

    public Student toEntity(StudentDTO studentDTO) {
        Room room = new Room();
        room.setCode(studentDTO.getRoomCode());
        return new Student()
                .setStudentId(studentDTO.getStudentId())
                .setName(studentDTO.getName())
                .setAddress(studentDTO.getAddress())
                .setDob(this.dateUtils.stringToDate(studentDTO.getDob()))
                .setPhone(studentDTO.getPhone())
                .setRoom(room);
    }

}
