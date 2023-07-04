package com.ktx.core.component;

import javax.swing.JPanel;

public abstract class LazyJPanel extends JPanel {
	
	protected abstract void load();
	
	@Override
	public void setVisible(boolean isShow) {
		if (isShow) {
			this.load();
		}
		super.setVisible(isShow);
	}
}
