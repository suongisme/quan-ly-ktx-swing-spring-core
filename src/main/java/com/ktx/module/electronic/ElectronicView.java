package com.ktx.module.electronic;

import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ktx.core.component.LazyJPanel;
import com.ktx.core.utils.NotifierUtils;
import com.ktx.module.room.RoomDTO;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.awt.event.ActionEvent;

@Component
public class ElectronicView extends LazyJPanel {
	private JTextField txtSearch;
	private JTable table;
	private JButton btnDelete;
	private JButton btnUpdate;
	
	private DefaultTableModel model;
	private int currentSelectedRow;
	
	@Autowired private ElectronicService electronicService;
	@Autowired private ElectronicForm electronicForm;
	@Autowired private NotifierUtils notifier;
	
	
	public ElectronicView() {
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
		
		JLabel lblNewLabel = new JLabel("Quản lý điện nước");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(10, 11, 198, 25);
		palForm.add(lblNewLabel);
		
		txtSearch = new JTextField();
		txtSearch.setBounds(382, 52, 174, 20);
		palForm.add(txtSearch);
		txtSearch.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Nhập mã");
		lblNewLabel_1.setBounds(382, 27, 174, 14);
		palForm.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Thêm mới");
		btnNewButton.addActionListener(e -> electronicForm.visible(new ElectronicDTO(), this::fillDataToTable));
		btnNewButton.setBounds(10, 47, 89, 25);
		palForm.add(btnNewButton);
		
		btnUpdate = new JButton("Chỉnh sửa");
		btnUpdate.setBounds(105, 47, 89, 25);
		btnUpdate.addActionListener(x -> this.handleUpdate());
		palForm.add(btnUpdate);
		
		btnDelete = new JButton("Xóa");
		btnDelete.setBounds(202, 47, 89, 25);
		btnDelete.addActionListener(x -> this.handleDelete());
		palForm.add(btnDelete);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(255, 255, 255));
		splitPane.add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 0, 546, 348);
		panel_1.add(scrollPane);
		
		table = new JTable();
		table.setRowHeight(40);
		this.model = new DefaultTableModel(
				new Object[][] { },
				new String[] {
					"M\u00E3", 
					"Ph\u00F2ng", 
					"Th\u00E1ng", 
					"N\u0103m", 
					"\u0110i\u1EC7n ti\u00EAu th\u1EE5", 
					"Tr\u1EA1ng th\u00E1i"
				}
			) {
				boolean[] columnEditables = new boolean[] {
					false, false, false, false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
		table.setModel(this.model);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				handleClickedRowTable(e);
			};
		});
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
	
	private void handleDelete() {
		int yesOrNo = this.notifier.confirmNotInput("Xác nhận", "Bạn có muốn xóa HĐ này?");
		if (yesOrNo == JOptionPane.NO_OPTION) return;
		try {
			Object roomCode = this.table.getValueAt(this.currentSelectedRow, 0);
			this.electronicService.delete(Optional.ofNullable(roomCode).map(Object::toString).orElse(null));
			this.notifier.success("Thành công", "Xóa HĐ thành công");
			this.fillDataToTable("");
		} catch (Exception e) {
			this.notifier.error("Lỗi",e.getMessage());
		}
	}
	
	private void handleUpdate() {
		Object roomCode = this.table.getValueAt(this.currentSelectedRow, 0);
		ElectronicDTO room = this.electronicService.findByCode(Optional.ofNullable(roomCode).map(Object::toString).orElse(null));
		this.electronicForm.visible(room, this::fillDataToTable);
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
		this.electronicService.findAll(keyword).stream()
			.map(room -> new Object[] { room.getCode(), room.getRoom().getName(), room.getMonth(), room.getYear(),room.getUsedNum(), room.getStatus().getValue()})
			.forEach(this.model::addRow);
	}
}
