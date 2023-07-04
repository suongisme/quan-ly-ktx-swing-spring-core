package com.ktx.module.electronic;

import org.springframework.stereotype.Component;

import com.ktx.module.room.Room;

@Component
public class ElectronicMapper {

	public ElectronicDTO toDto(Electronic electronic) {
		return new ElectronicDTO()
				.setCode(electronic.getCode())
				.setMonth(String.valueOf(electronic.getMonth()))
				.setYear(String.valueOf(electronic.getYear()))
				.setStatus(electronic.getStatus())
				.setUsedNum(String.valueOf(electronic.getUsedNum()));
	}
	
	public Electronic toEntity(ElectronicDTO electronicDTO) {
		Room room = new Room();
		room.setCode(electronicDTO.getRoomCode());
		return new Electronic()
				.setCode(electronicDTO.getMonth()+electronicDTO.getYear()+room.getCode())
				.setMonth(Integer.parseInt(electronicDTO.getMonth()))
				.setYear(Integer.parseInt(electronicDTO.getYear()))
				.setStatus(electronicDTO.getStatus())
				.setRoom(room);
	}
}
