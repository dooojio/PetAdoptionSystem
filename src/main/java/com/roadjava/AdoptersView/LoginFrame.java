package com.roadjava.AdoptersView;




import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;

import static java.sql.DriverManager.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private SystemTray systemTray;
    private TrayIcon trayIcon;
    private UserLogin currentUser; // 新增字段用于记录当前登录用户

    public LoginFrame() {
        setTitle("宠物领养系统 - 登录");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 设置窗口的背景色
        getContentPane().setBackground(new Color(232, 240, 254));

        // 设置主面板的布局
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(232, 240, 254));

        // 顶部图标和标题面板
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(232, 240, 254));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // 加载图标
        try {
            URL resourse = LoginFrame.class.getClassLoader().getResource("PetAdoptionSystem.png");
            ImageIcon image = new ImageIcon(resourse);
            Image img = image.getImage();
            Image scaledImg = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            image = new ImageIcon(scaledImg);
            JLabel logoLabel = new JLabel(image);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            headerPanel.add(logoLabel);
        } catch (Exception e) {
            System.out.println("图标加载失败");
        }

        JLabel titleLabel = new JLabel("宠物领养系统");
        titleLabel.setFont(new Font("华文行楷", Font.BOLD, 30));  // 更改字体
        titleLabel.setForeground(new Color(85, 130, 245));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        mainPanel.add(headerPanel);

        // 添加间隔
        mainPanel.add(Box.createVerticalStrut(20));

        // 用户名和密码输入面板
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(232, 240, 254));

        // 网格布局位置控制
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // 控制间距
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameLabel.setForeground(new Color(85, 130, 245));
        inputPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        inputPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(85, 130, 245));
        inputPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        inputPanel.add(passwordField, gbc);

        mainPanel.add(inputPanel);

        // 登录和注册按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(232, 240, 254));

        loginButton = new JButton("登录");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        loginButton.setBackground(new Color(85, 130, 245));
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(150, 40));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(85, 130, 245), 2, true));
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        registerButton = new JButton("注册");
        registerButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        registerButton.setBackground(new Color(255, 140, 0));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(150, 40));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(255, 140, 0), 2, true));
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        mainPanel.add(buttonPanel);

        // 为了美观添加适当间隔
        mainPanel.add(Box.createVerticalStrut(30));

        // 设置主面板
        add(mainPanel);

        // 登录按钮监听器
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // 创建一个UserLogin实例
                currentUser = new UserLogin(username, password);

                // 验证登录信息
                if (validateLogin(currentUser)) {
                    currentUser.setLoggedIn(true); // 登录成功，设置登录状态
                    JOptionPane.showMessageDialog(LoginFrame.this, "登录成功！");

                    // 启动主界面或其他窗体，并传递当前用户信息
                    new PetListPage(currentUser);
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码错误！");
                }
            }
        });

        // 注册按钮监听器
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterFrame().setVisible(true);  // 打开注册窗口
                setVisible(false);  // 隐藏登录窗口
            }
        });

    }

    private boolean validateLogin(UserLogin user) {
        boolean isValid = false;

        // MySQL 数据库连接信息
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载数据库连接器
        } catch (ClassNotFoundException e) {
            System.out.println("" + e);
        }

        String url = "jdbc:mysql://localhost:3306/pet_adoption_system?user=root&password=swy4apig,&useSSL=false&serverTimezone=GMT";
        try (Connection connection = getConnection(url)) {

            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    isValid = true;  // 用户名和密码匹配
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据库连接失败！");
        }

        return isValid;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}
