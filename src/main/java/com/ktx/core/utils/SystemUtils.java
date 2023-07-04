package com.ktx.core.utils;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Optional;

@Component
public class SystemUtils {

	public String getSysImage(String imageName) {
		return Optional.ofNullable(SystemUtils.class.getResource("/sys_icon/" + imageName))
				.map(URL::getFile)
				.orElse("");
	}
}
