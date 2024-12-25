package com.roadjava.AdoptersView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.*;

public class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterFrame() {
        setTitle("宠物领养系统 - 注册");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 窗口居中

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
            URL resourse = RegisterFrame.class.getClassLoader().getResource("PetAdoptionSystem.png");
            ImageIcon image =new ImageIcon(resourse);
            // 调整图标大小
            Image img = image.getImage(); // 获取原始图像
            Image scaledImg = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH); // 调整图标大小为50x50
            image = new ImageIcon(scaledImg); // 使用调整大小后的图像创建新的ImageIcon
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

        // 用户名标签和输入框
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

        // 密码标签和输入框
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

        // 确认密码标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel confirmPasswordLabel = new JLabel("确认密码:");
        confirmPasswordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        confirmPasswordLabel.setForeground(new Color(85, 130, 245));
        inputPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        inputPanel.add(confirmPasswordField, gbc);

        mainPanel.add(inputPanel);

        // 注册和返回按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(232, 240, 254));

        registerButton = new JButton("注册");
        registerButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        registerButton.setBackground(new Color(85, 130, 245));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(150, 40));
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(85, 130, 245), 2, true));
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backButton = new JButton("返回");
        backButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        backButton.setBackground(new Color(255, 140, 0));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(255, 140, 0), 2, true));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel);

        // 为了美观添加适当间隔
        mainPanel.add(Box.createVerticalStrut(30));

        // 设置主面板
        add(mainPanel);

        // 注册按钮监听器
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "两次密码输入不一致！");
                    return;
                }

                // 保存用户信息到数据库
                if (registerUser(username, password)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "注册成功！");
                    new LoginFrame().setVisible(true);  // 跳转到登录页面
                    setVisible(false);  // 关闭注册页面
                } else {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "注册失败，用户名可能已被占用！");
                }
            }
        });

        // 返回按钮监听器
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);  // 返回登录窗口
                setVisible(false);  // 隐藏注册窗口
            }
        });
    }

    private boolean registerUser(String username, String password) {
        boolean isRegistered = false;

        // MySQL 数据库连接信息
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//加载数据库连接器
        }
        catch(ClassNotFoundException e){
            System.out.println(""+e);
        }

        String url="jdbc:mysql://localhost:3306/pet_adoption_system?user=root&password=swy4apig,&useSSL=false"+"&serverTimezone=GMT";

        try (Connection connection = DriverManager.getConnection(url)) {
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, username);

                ResultSet resultSet = checkStatement.executeQuery();
                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "用户名已存在！");
                } else {
                    // 用户名不存在，执行注册操作
                    String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, username);
                        insertStatement.setString(2, password);

                        int rowsAffected = insertStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            isRegistered = true;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "数据库连接失败！");
        }

        return isRegistered;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterFrame().setVisible(true);
            }
        });
    }
}
