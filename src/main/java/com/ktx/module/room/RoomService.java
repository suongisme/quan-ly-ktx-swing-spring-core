package com.ktx.module.room;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ktx.core.utils.ValidatorUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {

	private final RoomRepository roomRepository;
	private final ValidatorUtils validator;
	private final RoomMapper roomMaper;

	public List<Room> findAll(String keyword) {
		log.info("find all rooms");
		return this.roomRepository.findAllAndFetchStudents(keyword);
	}

	public RoomDTO findByCode(String roomCode) {
		if (!StringUtils.hasText(roomCode)) {
			throw new IllegalArgumentException("Room Code không hợp lệ");
		}
		return this.roomRepository.findById(roomCode)
				.map(this.roomMaper::toDTO)
				.orElseThrow(() -> new IllegalArgumentException("Không tìm thấy: " + roomCode));
	}

	@Transactional
	public void delete(String roomCode) {
		if (!StringUtils.hasLength(roomCode)) {
			throw new IllegalArgumentException("Room code không hợp lệ");
		}
		this.roomRepository.deleteById(roomCode);
	}

	public void saveRoom(RoomDTO room) {
		this.validator.validate(room);
		Room entity = this.roomMaper.toEntity(room);
		this.roomRepository.save(entity);
	}
}
