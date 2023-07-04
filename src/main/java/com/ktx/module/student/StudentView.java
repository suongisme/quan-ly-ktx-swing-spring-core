package com.ktx.module.student;

import javax.swing.*;

import com.ktx.core.component.LazyJPanel;
import com.ktx.core.utils.NotifierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.Color;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import javax.swing.border.LineBorder;
import java.awt.event.*;
import java.util.Optional;

@Component
public class StudentView extends LazyJPanel {
    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnUpdate;
    private JButton btnDelete;
    private int currentSelectedRow;

    @Autowired
    private StudentService studentService;
    @Autowired
    private NotifierUtils notifier;
    @Autowired
    private StudentForm studentForm;

    public StudentView() {
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

        JLabel lblNewLabel = new JLabel("Quản lý sinh viên");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel.setBounds(10, 11, 198, 25);
        palForm.add(lblNewLabel);

        txtSearch = new JTextField();
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ENTER) return;
				handleSearchPressEnter();
            }
        });
        txtSearch.setBounds(382, 52, 174, 20);
        palForm.add(txtSearch);
        txtSearch.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Nhập mã hoặc tên SV");
        lblNewLabel_1.setBounds(382, 27, 174, 14);
        palForm.add(lblNewLabel_1);

        JButton btnCreate = new JButton("Thêm mới");
        btnCreate.addActionListener(e -> studentForm.visible(new StudentDTO(), this::fillDataToTable));
        btnCreate.setBounds(10, 47, 89, 25);
        palForm.add(btnCreate);

        btnUpdate = new JButton("Chỉnh sửa");
        btnUpdate.setBounds(105, 47, 89, 25);
		btnUpdate.addActionListener(e -> this.handleUpdate());
        palForm.add(btnUpdate);

        btnDelete = new JButton("Xóa");
        btnDelete.setBounds(202, 47, 89, 25);
		btnDelete.addActionListener(e -> this.handleDelete());
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
                new Object[][]{},
                new String[]{
                        "M\u00E3 SV",
                        "T\u00EAn SV",
                        "S\u0110T",
                        "\u0110\u1ECBa ch\u1EC9",
                        "Ng\u00E0y Sinh",
                        "Ph\u00F2ng"
                }
        ) {
            boolean[] columnEditables = new boolean[]{
                    false, false, true, true, true, true
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        };
        table.setModel(this.model);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClickTable(e);
            }
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

    private void handleClickTable(MouseEvent e) {

        int selectedRow = this.table.getSelectedRow();
        if (selectedRow == this.currentSelectedRow && e.getClickCount() == 2) {
            this.removeSelectedRow();
            return;
        }
        this.selectRow(selectedRow);
    }

    public void handleDelete() {
        int yesOrNo = this.notifier.confirmNotInput("Xác nhận", "Bạn có muốn xóa sinh viên này?");
        if (yesOrNo == JOptionPane.NO_OPTION) return;
        try {
            Object roomCode = this.table.getValueAt(this.currentSelectedRow, 0);
            this.studentService.deleteStudent(Optional.ofNullable(roomCode).map(Object::toString).orElse(null));
            this.notifier.success("Thành công", "Xóa sinh viên thành công");
            this.fillDataToTable("");
        } catch (Exception e) {
            this.notifier.error("Lỗi", e.getMessage());
        }
    }

    public void handleUpdate() {
		Object roomCode = this.table.getValueAt(this.currentSelectedRow, 0);
		StudentDTO studentDTO = this.studentService.findById(Optional.ofNullable(roomCode).map(Object::toString).orElse(null));
		this.studentForm.visible(studentDTO, this::fillDataToTable);
    }

    private void selectRow(int selectedRow) {
        this.btnUpdate.setEnabled(true);
        this.btnDelete.setEnabled(true);
        this.currentSelectedRow = selectedRow;
    }

    private void removeSelectedRow() {
        if (this.table.getSelectedRowCount() > 0) {
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
        this.studentService.findAll(keyword)
                .stream()
                .map(st -> new Object[]{st.getStudentId(), st.getName(), st.getPhone(), st.getAddress(), st.getDob(), st.getRoomCode()})
                .forEach(this.model::addRow);
    }
}
