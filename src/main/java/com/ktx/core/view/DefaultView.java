package com.ktx.core.view;

import javax.swing.JPanel;

import org.springframework.stereotype.Component;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

@Component
public class DefaultView extends JPanel {
	
	public DefaultView() {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBackground(new Color(255, 255, 255));
		setBounds(0, 0, 588, 453);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(1, 1, 586, 452);
		lblNewLabel.setIcon(new ImageIcon("D:\\sp\\KTX\\src\\main\\resources\\sys_icon\\bg.jpg"));
		add(lblNewLabel);

	}
	private static class __Tmp {
		private static void __tmp() {
			  javax.swing.JPanel __wbp_panel = new javax.swing.JPanel();
		}
	}
}
