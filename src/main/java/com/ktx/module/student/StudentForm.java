package com.ktx.module.student;

import com.ktx.core.component.NonNullTextField;
import com.ktx.core.utils.NotifierUtils;
import com.ktx.module.room.Room;
import com.ktx.module.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.SwingConstants;

@Component
@Scope(value = "prototype")
public class StudentForm extends JDialog {

	private JLabel lblTitle;
	private JTextField txtStudentCode;
	private JTextField txtStudentName;
	private JTextField txtPhone;
	private JTextField txtAddress;
	private JTextField txtDob;
	private JComboBox<Room> comboBox;

	private Runnable callback;

	@Autowired private StudentService studentService;
	@Autowired private NotifierUtils notifierUtils;
	@Autowired private RoomService roomService;

	public StudentForm() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 328);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Mã SV");
			lblNewLabel.setBounds(10, 52, 46, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			txtStudentCode = new NonNullTextField();
			txtStudentCode.setBounds(10, 73, 197, 29);
			contentPanel.add(txtStudentCode);
			txtStudentCode.setColumns(10);
		}
		{
			JLabel lblTnSv = new JLabel("Tên SV");
			lblTnSv.setBounds(227, 52, 46, 14);
			contentPanel.add(lblTnSv);
		}
		{
			txtStudentName = new NonNullTextField();
			txtStudentName.setColumns(10);
			txtStudentName.setBounds(227, 73, 197, 29);
			contentPanel.add(txtStudentName);
		}
		{
			JLabel lblSt = new JLabel("SĐT");
			lblSt.setBounds(10, 121, 46, 14);
			contentPanel.add(lblSt);
		}
		{
			txtPhone = new NonNullTextField();
			txtPhone.setColumns(10);
			txtPhone.setBounds(10, 142, 197, 29);
			contentPanel.add(txtPhone);
		}
		{
			JLabel lblaCh = new JLabel("Địa chỉ");
			lblaCh.setBounds(227, 121, 46, 14);
			contentPanel.add(lblaCh);
		}
		{
			txtAddress = new NonNullTextField();
			txtAddress.setColumns(10);
			txtAddress.setBounds(227, 142, 197, 29);
			contentPanel.add(txtAddress);
		}
		{
			JLabel lblNgySinh = new JLabel("Ngày sinh");
			lblNgySinh.setBounds(10, 182, 197, 14);
			contentPanel.add(lblNgySinh);
		}
		{
			txtDob = new NonNullTextField();
			txtDob.setColumns(10);
			txtDob.setBounds(10, 203, 197, 29);
			contentPanel.add(txtDob);
		}
		{
			JLabel lblPhng = new JLabel("Phòng");
			lblPhng.setBounds(227, 182, 197, 14);
			contentPanel.add(lblPhng);
		}

		comboBox = new JComboBox<>();
		comboBox.setBounds(227, 203, 197, 29);
		contentPanel.add(comboBox);

		lblTitle = new JLabel("Thêm mới sinh viên");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTitle.setBounds(10, 11, 414, 30);
		contentPanel.add(lblTitle);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnSave = new JButton("Lưu");
				btnSave.setActionCommand("SAVE");
				buttonPane.add(btnSave);
				btnSave.addActionListener(x -> this.handleSave());
				getRootPane().setDefaultButton(btnSave);
			}
			{
				JButton btnCancel = new JButton("Hủy");
				btnCancel.setActionCommand("CANCEL");
				btnCancel.addActionListener(x -> this.handleCancel());
				buttonPane.add(btnCancel);
			}
		}
	}

	public void visible(StudentDTO studentDTO, Runnable okCallback) {
		this.setTitle("Thêm mới sinh viên");
		this.lblTitle.setText("Thêm mới sinh viên");
		this.txtStudentCode.setEnabled(true);
		this.callback = okCallback;
		this.fillSelectBox();
		this.fillToForm(studentDTO);
		this.setVisible(true);
	}

	private void fillSelectBox() {
		this.comboBox.removeAllItems();
		this.roomService.findAll("")
				.forEach(this.comboBox::addItem);
		this.comboBox.setSelectedIndex(0);
	}

	private StudentDTO formToDto() {
		Room selectedItem = (Room) this.comboBox.getSelectedItem();
		assert selectedItem != null;
		return new StudentDTO()
				.setStudentId(this.txtStudentCode.getText())
				.setName(this.txtStudentName.getText())
				.setAddress(this.txtAddress.getText())
				.setPhone(this.txtPhone.getText())
				.setDob(this.txtDob.getText())
				.setRoomCode(selectedItem.getCode());
	}

	private void fillToForm(StudentDTO studentDTO) {
		if (StringUtils.hasText(studentDTO.getStudentId())) {
			this.setTitle("Cập nhật sinh viên");
			this.lblTitle.setText("Cập nhật sinh viên");
			this.txtStudentCode.setEnabled(false);
		}
		this.txtStudentCode.setText(studentDTO.getStudentId());
		this.txtStudentName.setText(studentDTO.getName());
		this.txtAddress.setText(studentDTO.getAddress());
		this.txtPhone.setText(studentDTO.getPhone());
		this.txtDob.setText(studentDTO.getDob());
		if (StringUtils.hasText(studentDTO.getRoomCode())) {
			this.comboBox.setSelectedItem(studentDTO.getRoomCode());
		}
	}

	private void clearForm() {
		this.fillToForm(new StudentDTO());
	}

	private void handleSave() {
		try {
			StudentDTO studentDTO = this.formToDto();
			this.studentService.saveStudent(studentDTO);
			this.notifierUtils.success("Thành công", "Lưu sinh viên thành công");
			this.dispose();
			this.callback.run();
		} catch (Exception ex) {
			this.notifierUtils.error("Lỗi", ex.getMessage());
		}
	}

	private void handleCancel() {
		this.clearForm();
		this.dispose();
	}
}
