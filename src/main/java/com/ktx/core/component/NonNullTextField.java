package com.ktx.core.component;

import java.util.Optional;

import javax.swing.JTextField;

public class NonNullTextField extends JTextField {
	
	@Override
	public void setText(String t) {
		Optional.ofNullable(t)
			.filter(x -> !"null".equalsIgnoreCase(x))
			.ifPresent(super::setText);
	}

}
