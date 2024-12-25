package com.roudjava.Pet;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoginView extends JFrame {
    //新建各组件
    JLabel nameLabel =new JLabel("宠物领养系统",JLabel.CENTER);
    SpringLayout springLayout=new SpringLayout();
    JPanel centerPanel=new JPanel(springLayout);
    JLabel userNameLabel=new JLabel("用户名");
    JTextField userText=new JTextField();
    JLabel pwdLabel=new JLabel("密码：");
    JPasswordField pwdField=new JPasswordField();
    JButton loginBtn=new JButton("登录");
    JButton resetBtn=new JButton("重置");
    public LoginView(){
        //设置容器
        Container contentPane=getContentPane();
        //设置各组件字体
        nameLabel.setFont(new Font("华文行楷",Font.PLAIN,40));
        Font centerFont=new Font("楷体",Font.PLAIN,20);
        userNameLabel.setFont(centerFont);
        pwdLabel.setFont(centerFont);
        loginBtn.setFont(centerFont);
        resetBtn.setFont(centerFont);
        //设置文本框大小
        nameLabel.setPreferredSize(new Dimension(0,80));
        userText.setPreferredSize(new Dimension(200,30));
        pwdField.setPreferredSize(new Dimension(200,30));
        //加入面板
        centerPanel.add(userText);
        centerPanel.add(userText);
        centerPanel.add(pwdLabel);
        centerPanel.add(pwdField);
        centerPanel.add(loginBtn);
        centerPanel.add(resetBtn);
        //加入容器
        contentPane.add(nameLabel,BorderLayout.NORTH);
        contentPane.add(centerPanel,BorderLayout.CENTER);
        //加入图片
        URL resourse = LoginView.class.getClassLoader().getResource("PetAdoptionSystem.png");
        Image image =new ImageIcon(resourse).getImage();
        setIconImage(image);
        //弹簧布局
        Spring titleLabelwidth=Spring.width(userNameLabel);
        Spring titleTxtWidth=Spring.width(userText);
        //
        setTitle("宠物领养系统 - 登录");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // 窗口居中
        setVisible(true);
    }
    public static void main(String[] args){
        new LoginView();
    }
}
