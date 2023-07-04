package com.ktx.module.student;

import com.ktx.core.utils.ValidatorUtils;
import com.ktx.module.room.Room;
import com.ktx.module.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final ValidatorUtils validator;
    private final RoomRepository roomRepository;

    public List<StudentDTO> findAll(String keyword) {
        return this.studentRepository.findAllAndFetchRoom(keyword)
                .stream()
                .map(x -> {
                    StudentDTO dto = this.studentMapper.toDTO(x);
                    dto.setRoomCode(x.getRoom().getCode());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public StudentDTO findById(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("ID không hợp lệ");
        }
        return this.studentRepository.findById(id)
                .map(this.studentMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Student ID không tồn taại: " + id));
    }

    @Transactional
    public void saveStudent(StudentDTO studentDTO) {
        this.validator.validate(studentDTO);
        Room room = this.roomRepository.findById(studentDTO.getRoomCode())
                .orElseThrow(() -> new IllegalArgumentException("Mã phòng không tồn tại"));
        Student entity = this.studentMapper.toEntity(studentDTO);
        entity.setRoom(room);
        this.studentRepository.save(entity);
    }

    public void deleteStudent(String studentId) {
        this.studentRepository.deleteById(studentId);
    }
}
