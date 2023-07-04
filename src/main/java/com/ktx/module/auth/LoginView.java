package com.ktx.module.auth;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.ktx.core.utils.NotifierUtils;
import com.ktx.core.utils.SystemUtils;
import com.ktx.core.view.HomeView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LoginView extends JFrame {

	private JTextField txtUsername;
	private JPasswordField txtPassword;

	// dependencies
	@Autowired private AccountService accountService;
	@Autowired private NotifierUtils notifier;
	@Autowired private HomeView homeView;

	public LoginView(SystemUtils systemUtils) {
		setTitle("Đăng nhập");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 350, 304);
		setLocationRelativeTo(null);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitleLogin = new JLabel("Đăng Nhập");
		lblTitleLogin.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblTitleLogin.setToolTipText("Đăng Nhập");
		lblTitleLogin.setBounds(101, 11, 139, 31);
		contentPane.add(lblTitleLogin);

		JLabel lblUsername = new JLabel("Tên đăng nhập");
		lblUsername.setToolTipText("Tên đăng nhập");
		lblUsername.setBounds(11, 51, 88, 14);
		contentPane.add(lblUsername);

		txtUsername = new JTextField();
		txtUsername.setBounds(11, 76, 314, 31);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);

		JLabel lblPassword = new JLabel("Mật khẩu");
		lblPassword.setToolTipText("Mật khẩu");
		lblPassword.setBounds(11, 118, 65, 14);
		contentPane.add(lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setEchoChar('*');
		txtPassword.setBounds(11, 143, 279, 31);
		contentPane.add(txtPassword);

		JButton btnLogin = new JButton("Đăng nhập");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
		btnLogin.setToolTipText("Đăng nhập");
		btnLogin.setBounds(10, 197, 315, 40);
		contentPane.add(btnLogin);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showOrHidePassword();
			}
		});
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBorder(new LineBorder(new Color(192, 192, 192)));
		lblNewLabel.setIcon(new ImageIcon(systemUtils.getSysImage("eye.png")));
		lblNewLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblNewLabel.setBounds(288, 143, 37, 31);
		contentPane.add(lblNewLabel);
	}

	private void showOrHidePassword() {
		if (this.txtPassword.getEchoChar() != '*') {
			this.txtPassword.setEchoChar('*');
			return;
		}
		this.txtPassword.setEchoChar('\u0000');
	}

	private void login() {
		try {
			LoginDTO loginDTO = LoginDTO.builder()
					.username(this.txtUsername.getText())
					.password(new String(this.txtPassword.getPassword()))
					.build();
			this.accountService.login(loginDTO);
			this.notifier.success("Thành công", "Đăng nhập thành công");
			this.homeView.setVisible(true);
			this.dispose();
		} catch (Exception ex) {
			this.notifier.error("Lỗi", ex.getMessage());
		}
	}
}
