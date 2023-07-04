package com.ktx.module.room;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ktx.core.component.NonNullTextField;
import com.ktx.core.utils.NotifierUtils;

@Component
@Scope(value = "prototype")
public class RoomForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtRoomCode;
	private JTextField txtRoomName;
	private JTextField txtMaxPeople;
	private JTextField txtPrice;
	private JLabel lblTitle;
	
	private Runnable okCallBack;
	
	@Autowired private RoomService roomService;
	@Autowired private NotifierUtils notifier;

	public RoomForm() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 254);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Mã Phòng");
			lblNewLabel.setBounds(10, 52, 197, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			txtRoomCode = new NonNullTextField();
			txtRoomCode.setBounds(10, 73, 197, 29);
			contentPanel.add(txtRoomCode);
			txtRoomCode.setColumns(10);
		}
		{
			JLabel lblTnSv = new JLabel("Tên Phòng");
			lblTnSv.setBounds(227, 52, 197, 14);
			contentPanel.add(lblTnSv);
		}
		{
			txtRoomName = new NonNullTextField();
			txtRoomName.setColumns(10);
			txtRoomName.setBounds(227, 73, 197, 29);
			contentPanel.add(txtRoomName);
		}
		{
			JLabel lblSt = new JLabel("Số Lượng SV tối đa");
			lblSt.setBounds(10, 121, 197, 14);
			contentPanel.add(lblSt);
		}
		{
			txtMaxPeople = new NonNullTextField();
			txtMaxPeople.setColumns(10);
			txtMaxPeople.setBounds(10, 142, 197, 29);
			contentPanel.add(txtMaxPeople);
		}
		
		lblTitle = new JLabel("Thêm mới phòng");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTitle.setBounds(10, 11, 414, 30);
		contentPanel.add(lblTitle);
		{
			JLabel lblGithng = new JLabel("Giá/Tháng");
			lblGithng.setBounds(227, 121, 197, 14);
			contentPanel.add(lblGithng);
		}
		{
			txtPrice = new NonNullTextField();
			txtPrice.setColumns(10);
			txtPrice.setBounds(227, 142, 197, 29);
			contentPanel.add(txtPrice);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(e -> this.save());
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(x -> this.cancel());
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public void visible(RoomDTO room, Runnable okCallback) {
		this.txtRoomCode.setEnabled(true);
		lblTitle.setText("Thêm mới phòng");
		this.setTitle("Thêm mới phòng");
		this.okCallBack = okCallback;
		this.fillToForm(room);
		super.setVisible(true);
	}
	
	private void fillToForm(RoomDTO room) {
		this.txtRoomCode.setText(room.getCode());
		this.txtRoomName.setText(room.getName());
		this.txtPrice.setText(String.valueOf(room.getPrice()));
		this.txtMaxPeople.setText(String.valueOf(room.getMaxPeople()));
		
		if (StringUtils.hasText(room.getCode())) {
			this.txtRoomCode.setEnabled(false);
			lblTitle.setText("Cập nhật phòng: " + room.getCode());
			this.setTitle("Cập nhật phòng");
		}
	}
	
	private RoomDTO formToDto() {
		return new RoomDTO()
				.setCode(this.txtRoomCode.getText())
				.setName(this.txtRoomName.getText())
				.setMaxPeople(this.txtMaxPeople.getText())
				.setPrice(this.txtPrice.getText());
	}
	
	private void save() {
		try {
			RoomDTO room = this.formToDto();
			this.roomService.saveRoom(room);
			this.notifier.success("Thành công", "Tưu phòng thành công");
			this.dispose();
			this.okCallBack.run();
		} catch (Exception e) {
			this.notifier.error("Lỗi", e.getMessage());
		}
	}
	
	private void clearForm() {
		this.fillToForm(new RoomDTO());
	}
	
	private void cancel() {
		this.clearForm();
		this.dispose();
	}
}
