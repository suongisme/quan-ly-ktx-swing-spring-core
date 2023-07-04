package com.ktx.module.electronic;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ktx.core.utils.ValidatorUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElectronicService {

	private final ElectronicRepository electronicRepository;
	private final ElectronicMapper electronicMapper;
	private final ValidatorUtils validator;
	
	public void saveElectronic(ElectronicDTO dto) {
		this.validator.validate(dto);
		Electronic entity = this.electronicMapper.toEntity(dto);
		this.electronicRepository.save(entity);
	}

	public void delete(String orElse) {
		this.electronicRepository.deleteById(orElse);
		
	}

	public ElectronicDTO findByCode(String orElse) {
		return this.electronicRepository.findById(orElse)
				.map(x -> {
					ElectronicDTO dto = this.electronicMapper.toDto(x);
					dto.setRoomCode(x.getRoom().getCode());
					return dto;
				})
				.orElseThrow(() -> new IllegalArgumentException("Khong tim thay"));
	}

	public List<Electronic> findAll(String keyword) {
		return this.electronicRepository.findAllAndFetch(keyword);
	}

}
