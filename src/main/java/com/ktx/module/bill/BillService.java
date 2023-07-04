package com.ktx.module.bill;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ktx.core.utils.ValidatorUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillService {

	private final BillRepository billRepository;
	private final BillMapper billMapper;
	private final ValidatorUtils validator;
	
	public void saveElectronic(BillDTO dto) {
		this.validator.validate(dto);
		Bill entity = this.billMapper.toEntity(dto);
		this.billRepository.save(entity);
	}

	public void delete(String orElse) {
		this.billRepository.deleteById(orElse);
		
	}

	public BillDTO findByCode(String orElse) {
		return this.billRepository.findById(orElse)
				.map(x -> {
					BillDTO dto = this.billMapper.toDto(x);
					dto.setRoomCode(x.getRoom().getCode());
					return dto;
				})
				.orElseThrow(() -> new IllegalArgumentException("Khong tim thay"));
	}

	public List<Bill> findAll(String keyword) {
		return this.billRepository.findAllAndFetch(keyword);
	}

}
