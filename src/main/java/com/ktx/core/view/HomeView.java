package com.ktx.core.view;

import com.ktx.core.utils.SystemUtils;
import com.ktx.module.auth.AccountService;
import com.ktx.module.auth.LoginView;
import com.ktx.module.bill.BillView;
import com.ktx.module.electronic.ElectronicView;
import com.ktx.module.room.RoomView;
import com.ktx.module.student.StudentView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class HomeView extends JFrame {

	private final CardLayout cardLayout = new CardLayout(0, 0);

	private JPanel palNavbar;

	// dependencies
	private final ApplicationContext context;
	private final StudentView studentView;
	private final RoomView roomView;
	private final ElectronicView electronicView;
	private final BillView billView;
	private final DefaultView defaultView;
	private final SystemUtils systemUtils;
	private final AccountService accountService;

	@Autowired private LoginView loginView;

	@PostConstruct
	private void init() {
		setTitle("Trang chủ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 785, 508);
		setLocationRelativeTo(null);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(0, 1, 0, 10));
		setContentPane(contentPane);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setResizeWeight(0.1);
		splitPane.setEnabled(false);
		splitPane.setDividerSize(0);

		JPanel panel = this.createOutletRouter();
		this.palNavbar = this.createNavbar();

		JButton btnHome = this.createButton("Trang chủ", "home.png");
		btnHome.setBounds(0, 0, 140, 45);
		btnHome.addActionListener(e -> cardLayout.show(panel, defaultView.getClass().getName()));
		this.addGridBagConstraints(0, btnHome);

		JButton btnStudent = this.createButton("Sinh viên", "student.png");
		btnStudent.setBounds(0, 47, 140, 45);
		btnStudent.addActionListener(e -> cardLayout.show(panel, studentView.getClass().getName()));
		this.addGridBagConstraints(1, btnStudent);

		JButton btnRoom = this.createButton("Phòng", "room.png");
		btnRoom.setBounds(0, 92, 140, 45);
		btnRoom.addActionListener(e -> cardLayout.show(panel, roomView.getClass().getName()));
		this.addGridBagConstraints(2, btnRoom);

		JButton btnElectronic = this.createButton("Điện, nước", "water.png");
		btnElectronic.setBounds(0, 140, 140, 45);
		btnElectronic.addActionListener(e -> cardLayout.show(panel, electronicView.getClass().getName()));
		this.addGridBagConstraints(3, btnElectronic);

		JButton btnBill = this.createButton("Hóa đơn", "bill.png");
		btnBill.setBounds(0, 186, 140, 45);
		btnBill.addActionListener(e -> cardLayout.show(panel, billView.getClass().getName()));
		this.addGridBagConstraints(4, btnBill);

		JButton btnLogout = this.createButton("Đăng xuất", null);
		btnLogout.setBackground(new Color(220, 53, 69));
		btnLogout.addActionListener(e -> {
			accountService.logout();
			loginView.setVisible(true);
			setVisible(false);
		});
		this.addGridBagConstraints(5, btnLogout);

		splitPane.add(this.palNavbar);
		splitPane.add(panel);
		contentPane.add(splitPane);
		this.cardLayout.show(panel, this.defaultView.getClass().getName());
	}

	private JPanel createOutletRouter() {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(142, 0, 588, 453);
		panel.setLayout(cardLayout);
		this.appendToJPanel(panel);
		return panel;
	}

	private JPanel createNavbar() {
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 5, 136, 436);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] {80};
		gbl_panel_1.rowHeights = new int[] {60, 60, 60, 60, 60, 60};
		gbl_panel_1.columnWeights = new double[]{80};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		panel_1.setLayout(gbl_panel_1);
		return panel_1;
	}

	private JButton createButton(String text, String iconName) {
		JButton button = new JButton(text);
		button.setHorizontalAlignment(SwingConstants.LEFT);
		if (Objects.nonNull(iconName)) {
			button.setIcon(new ImageIcon(systemUtils.getSysImage(iconName)));
		}
		button.setForeground(new Color(255, 255, 255));
		button.setBackground(new Color(40, 167, 69));
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		return button;
	}

	private void addGridBagConstraints(int gridY, JButton button) {
		GridBagConstraints gridBagConstraints = this.createGridBagConstraints(gridY);
		this.palNavbar.add(button, gridBagConstraints);
	}

	private GridBagConstraints createGridBagConstraints(int gridY) {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = new Insets(0, 0, 5, 0);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = gridY;
		return gridBagConstraints;
	}

	private void appendToJPanel(JPanel panel) {
		Map<String, JPanel> beans = this.context.getBeansOfType(JPanel.class);
		beans.values().forEach(view -> {
			panel.add(view);
			cardLayout.addLayoutComponent(view, view.getClass().getName());
		});
		
	}
}
