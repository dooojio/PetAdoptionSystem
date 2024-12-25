package com.roadjava.AdoptersView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddPetPage extends JFrame {

    private JComboBox<String> typeCombo;  // 宠物类别选择框
    private JComboBox<String> breedCombo;  // 宠物品种选择框
    private JComboBox<String> genderCombo;  // 宠物性别选择框
    private JComboBox<String> adoptCombo;  // 是否领养选择框

    private JTextField nameField;  // 宠物名称输入框
    private JTextField ageField;  // 宠物年龄输入框

    public AddPetPage() {
        setTitle("添加宠物");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // 设置组件之间的间隔

        // 设置窗体的宽度和高度
        setPreferredSize(new Dimension(500, 500));
        setLocationRelativeTo(null);  // 窗口居中显示

        // 设置字体大小
        Font labelFont = new Font("微软雅黑", Font.PLAIN, 16);
        Font inputFont = new Font("微软雅黑", Font.PLAIN, 14);

        // 初始化组件
        JLabel nameLabel = new JLabel("宠物名称:");
        nameLabel.setFont(labelFont);
        nameField = new JTextField(20);
        nameField.setFont(inputFont);

        JLabel breedLabel = new JLabel("品种:");
        breedLabel.setFont(labelFont);
        breedCombo = new JComboBox<>();
        breedCombo.setFont(inputFont);

        JLabel ageLabel = new JLabel("年龄:");
        ageLabel.setFont(labelFont);
        ageField = new JTextField(20);
        ageField.setFont(inputFont);

        JLabel typeLabel = new JLabel("宠物类别:");
        typeLabel.setFont(labelFont);
        typeCombo = new JComboBox<>();
        typeCombo.setFont(inputFont);

        JLabel genderLabel = new JLabel("性别:");
        genderLabel.setFont(labelFont);
        genderCombo = new JComboBox<>(new String[] {"雄", "雌"});
        genderCombo.setFont(inputFont);

        JLabel adoptLabel = new JLabel("是否可领养:");
        adoptLabel.setFont(labelFont);
        adoptCombo = new JComboBox<>(new String[] {"是", "否"});
        adoptCombo.setFont(inputFont);

        // 获取宠物类别列表并填充到 typeCombo
        List<String> petTypes = getAllPetTypes();
        for (String type : petTypes) {
            typeCombo.addItem(type);
        }

        // 根据宠物类别更新宠物品种
        typeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = typeCombo.getSelectedIndex() + 1;  // 获取选中的类别ID
                List<String> breeds = getBreedsByType(selectedIndex);
                breedCombo.removeAllItems();
                for (String breed : breeds) {
                    breedCombo.addItem(breed);
                }
            }
        });

        // 添加提交按钮
        JButton addButton = new JButton("添加宠物");
        addButton.setFont(inputFont);
        addButton.setPreferredSize(new Dimension(150, 40));  // 调整按钮大小
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String breed = (String) breedCombo.getSelectedItem();
                int age = Integer.parseInt(ageField.getText());
                String gender = (String) genderCombo.getSelectedItem();
                boolean availableForAdoption = adoptCombo.getSelectedItem().equals("是");

                // 获取宠物品种对应的 breed_id
                int breedId = getBreedId(breed);

                // 添加宠物到数据库
                addPet(name, breedId, age, gender, availableForAdoption);
            }
        });

        // 设置网格布局
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(typeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(breedLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(breedCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(ageLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(ageField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        add(genderLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        add(genderCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        add(adoptLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        add(adoptCombo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(addButton, gbc);

        pack();  // 自动调整窗体大小以适应内容
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);  // 窗口居中显示
        setVisible(true);
    }

    // 获取所有宠物类别
    private List<String> getAllPetTypes() {
        List<String> petTypes = new ArrayList<>();
        try (Connection conn = DBConnection.connect()) {
            String sql = "SELECT * FROM pet_types";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    petTypes.add(rs.getString("type_name"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return petTypes;
    }

    // 根据宠物类别获取对应的宠物种类
    private List<String> getBreedsByType(int typeId) {
        List<String> breeds = new ArrayList<>();
        try (Connection conn = DBConnection.connect()) {
            String sql = "SELECT breed_name FROM pet_breeds WHERE type_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, typeId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    breeds.add(rs.getString("breed_name"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return breeds;
    }

    // 根据宠物种类获取 breed_id
    private int getBreedId(String breedName) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "SELECT breed_id FROM pet_breeds WHERE breed_name = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, breedName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("breed_id");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // 如果没有找到匹配的种类，返回-1
    }

    // 添加宠物到数据库
    private void addPet(String name, int breedId, int age, String gender, boolean availableForAdoption) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "INSERT INTO pets (name, breed_id, age, gender, available_for_adoption) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setInt(2, breedId);
                ps.setInt(3, age);
                ps.setString(4, gender);  // 性别值为雄或雌
                ps.setBoolean(5, availableForAdoption);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "宠物添加成功！");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "添加宠物失败！");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddPetPage());
    }
}
