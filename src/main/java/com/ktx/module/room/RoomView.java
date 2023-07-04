package com.ktx.module.room;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ktx.core.component.LazyJPanel;
import com.ktx.core.utils.NotifierUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@Component
public class RoomView extends LazyJPanel {
	private JTextField txtSearch;
	private JTable table;
	private DefaultTableModel model;
	private JButton btnUpdate;
	private JButton btnDelete;
	private int currentSelectedRow;
	
	// denpendencies
	@Autowired private RoomService roomService;
	@Autowired private RoomForm roomForm;
	@Autowired private NotifierUtils notifier;
	
	public RoomView() {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBackground(new Color(255, 255, 255));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBounds(0, 0, 570, 453);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerSize(0);
		splitPane.setEnabled(false);
		splitPane.setResizeWeight(0.2);
		add(splitPane);

		JPanel palForm = new JPanel();
		palForm.setBackground(new Color(255, 255, 255));
		splitPane.add(palForm);
		palForm.setLayout(null);

		JLabel lblNewLabel = new JLabel("Quản lý phòng");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(10, 11, 198, 25);
		palForm.add(lblNewLabel);

		txtSearch = new JTextField();
		txtSearch.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER != e.getKeyCode()) return;
				handleSearchPressEnter();
			}
		});
		txtSearch.setBounds(382, 49, 174, 20);
		palForm.add(txtSearch);
		txtSearch.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Nhập mã hoặc tên phòng");
		lblNewLabel_1.setBounds(382, 20, 174, 14);
		palForm.add(lblNewLabel_1);

		JButton btnNewButton = new JButton("Thêm mới");
		btnNewButton.addActionListener(e -> {
			roomForm.visible(new RoomDTO(), this::fillDataToTable);
		});
		btnNewButton.setBounds(10, 47, 89, 25);
		palForm.add(btnNewButton);

		btnUpdate = new JButton("Chỉnh sửa");
		btnUpdate.setEnabled(false);
		btnUpdate.setBounds(105, 47, 89, 25);
		btnUpdate.addActionListener(e -> this.handleUpdateRoom());
		palForm.add(btnUpdate);

		btnDelete = new JButton("Xóa");
		btnDelete.setEnabled(false);
		btnDelete.setBounds(202, 47, 89, 25);
		btnDelete.addActionListener(e -> this.handleDeleteRoom());
		palForm.add(btnDelete);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		splitPane.add(panel_1);
		panel_1.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 0, 546, 348);
		panel_1.add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleClickedRowTable(e);
			}
		});
		table.setRowHeight(40);
		this.model = new DefaultTableModel(
				new Object[][] {},
				new String[] { 
						"M\u00E3 Ph\u00F2ng", 
						"T\u00EAn Ph\u00F2ng",
						"S\u1ED1 L\u01B0\u1EE3ng SV t\u1ED1i \u0111a", 
						"S\u1ED1 L\u01B0\u1EE3ng SV hi\u1EC7n t\u1EA1i",
						"Gi\u00E1/Th\u00E1ng" 
		}) {
			boolean[] columnEditables = new boolean[] { false, false, false, false, false };
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		table.setModel(this.model);
		table.getColumnModel().getColumn(2).setPreferredWidth(110);
		table.getColumnModel().getColumn(3).setPreferredWidth(123);
		scrollPane.setViewportView(table);
	}
	
	@Override
	protected void load() {
		this.fillDataToTable();
	}
	
	private void handleSearchPressEnter() {
		String keyword = this.txtSearch.getText();
		this.fillDataToTable(keyword);
	}
	
	private void handleClickedRowTable(MouseEvent e) {
		int selectedRow = this.table.getSelectedRow();
		if (selectedRow == this.currentSelectedRow && e.getClickCount() == 2) {
			this.removeSelectedRow();
			return;
		}
		this.selectedRow(selectedRow);
	}
	
	private void handleDeleteRoom() {
		int yesOrNo = this.notifier.confirmNotInput("Xác nhận", "Bạn có muốn xóa phòng này?");
		if (yesOrNo == JOptionPane.NO_OPTION) return;
		try {
			Object roomCode = this.table.getValueAt(this.currentSelectedRow, 0);
			this.roomService.delete(Optional.ofNullable(roomCode).map(Object::toString).orElse(null));
			this.notifier.success("Thành công", "Xóa phòng thành công");
			this.fillDataToTable("");
		} catch (Exception e) {
			this.notifier.error("Lỗi",e.getMessage());
		}
	}
	
	private void handleUpdateRoom() {
		Object roomCode = this.table.getValueAt(this.currentSelectedRow, 0);
		RoomDTO room = this.roomService.findByCode(Optional.ofNullable(roomCode).map(Object::toString).orElse(null));
		this.roomForm.visible(room, this::fillDataToTable);
	}
	
	private void selectedRow(int selected) {
		this.currentSelectedRow = selected;
		this.btnUpdate.setEnabled(true);
		this.btnDelete.setEnabled(true);
	}
	
	private void removeSelectedRow() {
		if (this.table.getSelectedColumnCount() > 0) {
			this.table.removeRowSelectionInterval(this.currentSelectedRow, this.currentSelectedRow);	
		}
		this.btnUpdate.setEnabled(false);
		this.btnDelete.setEnabled(false);
		this.currentSelectedRow = -1;
	}
	
	private void fillDataToTable() {
		this.fillDataToTable("");
	}
	
	private void fillDataToTable(String keyword) {
		this.removeSelectedRow();
		this.model.setRowCount(0);
		this.roomService.findAll(keyword).stream()
			.map(room -> new Object[] { room.getCode(), room.getName(), room.getMaxPeople(), room.getStudents().size(), room.getPrice()})
			.forEach(this.model::addRow);
	}
}
