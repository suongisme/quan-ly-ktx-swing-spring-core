package com.ktx.module.bill;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ktx.core.component.NonNullTextField;
import com.ktx.core.constant.PaymentEnum;
import com.ktx.core.utils.NotifierUtils;
import com.ktx.module.room.Room;
import com.ktx.module.room.RoomService;

@Component
@Scope(value = "prototype")
public class BillForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCode;
	private JTextField txtMonth;
	private JTextField txtYear;
	private JTextField txtTotal;
	private JComboBox<Room> comboBox;
	private JLabel lblTitle;
	private JRadioButton rdoUnPaid;
	private JRadioButton rdoPaid;
	
	private Runnable okCallBack;
	
	@Autowired private RoomService roomService;
	@Autowired private BillService electronicService;
	@Autowired private NotifierUtils notifier;
	
	

	public BillForm() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 325);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Mã");
			lblNewLabel.setBounds(10, 52, 46, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			txtCode = new NonNullTextField();
			txtCode.setEditable(false);
			txtCode.setEnabled(false);
			txtCode.setText("");
			txtCode.setBounds(10, 73, 197, 29);
			contentPanel.add(txtCode);
			txtCode.setColumns(10);
		}
		{
			JLabel lblTnSv = new JLabel("Tháng");
			lblTnSv.setBounds(227, 52, 46, 14);
			contentPanel.add(lblTnSv);
		}
		{
			txtMonth = new NonNullTextField();
			txtMonth.setText("");
			txtMonth.setColumns(10);
			txtMonth.setBounds(227, 73, 197, 29);
			contentPanel.add(txtMonth);
		}
		{
			JLabel lblSt = new JLabel("Năm");
			lblSt.setBounds(10, 121, 46, 14);
			contentPanel.add(lblSt);
		}
		{
			txtYear = new NonNullTextField();
			txtYear.setText("");
			txtYear.setColumns(10);
			txtYear.setBounds(10, 142, 197, 29);
			contentPanel.add(txtYear);
		}
		{
			JLabel lblaCh = new JLabel("Tổng tiền");
			lblaCh.setBounds(227, 121, 197, 14);
			contentPanel.add(lblaCh);
		}
		{
			txtTotal = new NonNullTextField();
			txtTotal.setText("");
			txtTotal.setColumns(10);
			txtTotal.setBounds(227, 142, 197, 29);
			contentPanel.add(txtTotal);
		}
		{
			JLabel lblPhng = new JLabel("Phòng");
			lblPhng.setBounds(10, 182, 197, 14);
			contentPanel.add(lblPhng);
		}
		
		comboBox = new JComboBox<>();
		comboBox.setBounds(10, 203, 197, 29);
		contentPanel.add(comboBox);
		
		lblTitle = new JLabel("Thêm mới HĐ điện");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblTitle.setBounds(10, 11, 414, 30);
		contentPanel.add(lblTitle);
		ButtonGroup buttonGroup = new ButtonGroup();
		{
			JLabel lblTrngThi = new JLabel("Trạng thái");
			lblTrngThi.setBounds(227, 182, 197, 14);
			contentPanel.add(lblTrngThi);
		}
		{
			rdoUnPaid = new JRadioButton("Chưa TT");
			rdoUnPaid.setSelected(true);
			rdoUnPaid.setBounds(227, 206, 67, 23);
			contentPanel.add(rdoUnPaid);
			buttonGroup.add(rdoUnPaid);
		}
		{
			rdoPaid = new JRadioButton("Đã TT");
			rdoPaid.setBounds(310, 206, 67, 23);
			contentPanel.add(rdoPaid);
			buttonGroup.add(rdoPaid);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(x -> this.save());
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(x -> this.cancel());
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	
	private void fillDataToCbx() {
		this.comboBox.removeAllItems();
		this.roomService.findAll("")
			.forEach(this.comboBox::addItem);
		this.comboBox.setSelectedIndex(0);
	}
	
	public void visible(BillDTO dto, Runnable okCallback) {
		lblTitle.setText("Thêm mới HĐ phòng");
		this.setTitle("TThêm mới HĐ phòng");
		this.okCallBack = okCallback;
		this.fillDataToCbx();
		this.fillToForm(dto);
		super.setVisible(true);
	}
	
	private void fillToForm(BillDTO dto) {
		this.txtCode.setText(dto.getCode());
		this.txtMonth.setText(String.valueOf(dto.getMonth()));
		this.txtYear.setText(String.valueOf(dto.getYear()));
		this.txtTotal.setText(String.valueOf(dto.getTotal()));
		
		PaymentEnum status = dto.getStatus();
		if (PaymentEnum.PAID.equals(status)) {
			this.rdoPaid.setSelected(true);
		}
		
		if (PaymentEnum.UN_PAID.equals(status)) {
			this.rdoUnPaid.setSelected(true);
		}
		
		if (StringUtils.hasLength(dto.getRoomCode())) {
			this.comboBox.setSelectedItem(dto.getRoomCode());
		}
		
		if (StringUtils.hasText(dto.getCode())) {
			this.txtCode.setEnabled(false);
			lblTitle.setText("Cập nhật HĐ phòng");
			this.setTitle("Cập nhật HĐ phòng");
		}
	}
	
	private BillDTO formToDto() {
		Room room = (Room) this.comboBox.getSelectedItem();
		return new BillDTO()
				.setCode(this.txtCode.getText())
				.setMonth(this.txtMonth.getText())
				.setYear(this.txtYear.getText())
				.setTotal(this.txtTotal.getText())
				.setRoomCode(room.getCode())
				.setStatus(this.rdoPaid.isSelected() ? PaymentEnum.PAID : PaymentEnum.UN_PAID);
	}
	
	private void save() {
		try {
			BillDTO dto = this.formToDto();
			this.electronicService.saveElectronic(dto);
			this.notifier.success("Thành công", "Lưu HĐ phòng thành công");
			this.dispose();
			this.okCallBack.run();
		} catch (Exception e) {
			this.notifier.error("Lỗi", e.getMessage());
		}
	}
	
	private void clearForm() {
		this.fillToForm(new BillDTO());
	}
	
	private void cancel() {
		this.clearForm();
		this.dispose();
	}
}
