package com.ktx.module.room;

import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

@Component
public class RoomMapper {

	public RoomDTO toDTO(Room room) {
		return new RoomDTO()
				.setCode(room.getCode())
				.setName(room.getName())
				.setPrice(String.valueOf(room.getPrice()))
				.setMaxPeople(String.valueOf(room.getMaxPeople()));
	}
	
	public Room toEntity(RoomDTO roomDto) {
		return new Room()
				.setCode(roomDto.getCode())
				.setName(roomDto.getName())
				.setPrice(NumberUtils.parseNumber(roomDto.getPrice(), Long.class))
				.setMaxPeople(NumberUtils.parseNumber(roomDto.getMaxPeople(), Integer.class));
	}
	
}
