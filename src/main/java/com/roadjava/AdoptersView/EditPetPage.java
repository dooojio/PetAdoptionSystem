package com.roadjava.AdoptersView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditPetPage extends JFrame {

    private JComboBox<String> typeCombo;  // 宠物类别选择框
    private JComboBox<String> breedCombo;  // 宠物品种选择框
    private JComboBox<String> genderCombo;  // 宠物性别选择框
    private JComboBox<String> adoptCombo;  // 是否领养选择框

    private JTextField nameField;  // 宠物名称输入框
    private JTextField ageField;  // 宠物年龄输入框
    private int petId;  // 当前编辑宠物的ID

    public EditPetPage(int petId) {
        this.petId = petId;  // 记录正在编辑的宠物ID
        setTitle("编辑宠物");
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

        // 获取当前宠物信息并填充
        loadPetInfo(petId);

        // 默认选择第一个
        //breedCombo.setSelectedIndex(0);
        // 根据宠物类别更新宠物品种
        typeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = typeCombo.getSelectedIndex() + 1;  // 获取选中的类别ID
                List<String> breeds = getBreedsByType(selectedIndex);

                // 如果没有品种数据，隐藏品种选择框
                if (breeds.isEmpty()) {
                    breedCombo.setVisible(false);  // 隐藏品种选择框
                } else {
                    breedCombo.setVisible(true);  // 显示品种选择框
                    breedCombo.removeAllItems();  // 清空当前品种列表

                    // 填充品种列表
                    for (String breed : breeds) {
                        breedCombo.addItem(breed);
                    }

                    // 默认选中第一个品种
                    breedCombo.setSelectedIndex(0);  // 选择第一个品种
                }
            }
        });


        // 添加提交按钮
        JButton updateButton = new JButton("更新宠物");
        updateButton.setFont(inputFont);
        updateButton.setPreferredSize(new Dimension(150, 40));  // 调整按钮大小
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String breed = (String) breedCombo.getSelectedItem();
                int age = Integer.parseInt(ageField.getText());
                String gender = (String) genderCombo.getSelectedItem();
                boolean availableForAdoption = adoptCombo.getSelectedItem().equals("是");

                // 获取宠物品种对应的 breed_id
                int breedId = getBreedId(breed);

                // 更新宠物信息
                updatePet(petId, name, breedId, age, gender, availableForAdoption);
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
        add(updateButton, gbc);

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

    // 获取并填充宠物信息
    private void loadPetInfo(int petId) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "SELECT * FROM pets WHERE pet_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, petId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    nameField.setText(rs.getString("name"));
                    ageField.setText(String.valueOf(rs.getInt("age")));
                    genderCombo.setSelectedItem(rs.getString("gender"));
                    adoptCombo.setSelectedItem(rs.getBoolean("available_for_adoption") ? "是" : "否");

                    // 设置类别并更新品种
                    String type = getPetType(rs.getInt("type_id"));
                    typeCombo.setSelectedItem(type);
                    int breedId = rs.getInt("breed_id");
                    String breed = getBreedById(breedId);
                    breedCombo.setSelectedItem(breed);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // 获取宠物类别
    private String getPetType(int typeId) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "SELECT type_name FROM pet_types WHERE type_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, typeId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("type_name");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    // 根据 breed_id 获取品种名称
    private String getBreedById(int breedId) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "SELECT breed_name FROM pet_breeds WHERE breed_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, breedId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("breed_name");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    // 更新宠物信息
    private void updatePet(int petId, String name, int breedId, int age, String gender, boolean availableForAdoption) {
        try (Connection conn = DBConnection.connect()) {
            String sql = "UPDATE pets SET name = ?, breed_id = ?, age = ?, gender = ?, available_for_adoption = ? WHERE pet_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setInt(2, breedId);
                ps.setInt(3, age);
                ps.setString(4, gender);
                ps.setBoolean(5, availableForAdoption);
                ps.setInt(6, petId);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "宠物信息更新成功！");
                    dispose();  // 关闭当前编辑页面
                } else {
                    JOptionPane.showMessageDialog(this, "更新宠物信息失败！");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "更新宠物信息失败！");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditPetPage(1));  // 使用一个示例ID（1）启动编辑页面
    }
}
