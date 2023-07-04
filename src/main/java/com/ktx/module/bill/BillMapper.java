package com.ktx.module.bill;

import org.springframework.stereotype.Component;

import com.ktx.module.room.Room;

@Component
public class BillMapper {

	public BillDTO toDto(Bill bill) {
		return new BillDTO()
				.setCode(bill.getCode())
				.setMonth(String.valueOf(bill.getMonth()))
				.setYear(String.valueOf(bill.getYear()))
				.setStatus(bill.getStatus())
				.setTotal(String.valueOf(bill.getTotal()));
	}
	
	public Bill toEntity(BillDTO dto) {
		Room room = new Room();
		room.setCode(dto.getRoomCode());
		return new Bill()
				.setCode(dto.getMonth() + dto.getYear() + room.getCode())
				.setMonth(Integer.parseInt(dto.getMonth()))
				.setYear(Integer.parseInt(dto.getYear()))
				.setStatus(dto.getStatus())
				.setTotal(Integer.parseInt(dto.getTotal()))
				.setRoom(room);
	}
}
