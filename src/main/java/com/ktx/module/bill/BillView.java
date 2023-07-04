package com.ktx.module.bill;

import javax.swing.JPanel;

import com.ktx.core.component.LazyJPanel;
import com.ktx.core.utils.NotifierUtils;
import com.ktx.module.electronic.ElectronicDTO;
import com.ktx.module.student.StudentForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.awt.event.ActionEvent;

@Component
public class BillView extends LazyJPanel {
	private JTextField txtSearch;
	private JTable table;
	private int currentSelectedRow;
	private JButton btnDelete;
	private JButton btnUpdate;
	private DefaultTableModel model;
	
	@Autowired private NotifierUtils notifier;
	@Autowired private BillService billService;
	@Autowired private BillForm billForm;
	
	public BillView() {
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
		
		JLabel lblNewLabel = new JLabel("Quản lý hóa đơn phòng");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(10, 11, 268, 25);
		palForm.add(lblNewLabel);
		
		txtSearch = new JTextField();
		txtSearch.setBounds(382, 52, 174, 20);
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER != e.getKeyCode()) return;
				handleSearchPressEnter();
			}
		});
		palForm.add(txtSearch);
		txtSearch.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Nhập mã");
		lblNewLabel_1.setBounds(382, 27, 174, 14);
		palForm.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Thêm mới");
		btnNewButton.addActionListener(e -> this.billForm.visible(new BillDTO(), this::fillDataToTable));
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
					"T\u1ED5ng ti\u1EC1n", 
					"Tr\u1EA1ng th\u00E1i"
				}
			) {
				boolean[] columnEditables = new boolean[] {
					false, true, false, true, true, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			};
			
			table.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					handleClickedRowTable(e);
				}
			});
		table.setModel(this.model);
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
			this.billService.delete(Optional.ofNullable(roomCode).map(Object::toString).orElse(null));
			this.notifier.success("Thành công", "Xóa HĐ thành công");
			this.fillDataToTable("");
		} catch (Exception e) {
			this.notifier.error("Lỗi",e.getMessage());
		}
	}
	
	private void handleUpdate() {
		Object roomCode = this.table.getValueAt(this.currentSelectedRow, 0);
		BillDTO bill = this.billService.findByCode(Optional.ofNullable(roomCode).map(Object::toString).orElse(null));
		this.billForm.visible(bill, this::fillDataToTable);
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
		this.billService.findAll(keyword).stream()
			.map(room -> new Object[] { room.getCode(), room.getRoom().getName(), room.getMonth(), room.getYear(),room.getTotal(), room.getStatus().getValue()})
			.forEach(this.model::addRow);
	}
}
