package com.roadjava.AdoptersView;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AdminPetManagePage extends JFrame {

    private JTable petTable;  // 用于展示宠物信息的表格
    private DefaultTableModel tableModel;
    private JTextField searchField;  // 搜索框
    private JButton addButton, editButton, deleteButton;  // 增、改、删按钮

    public AdminPetManagePage() {
        setTitle("宠物管理");
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));  // 设置窗体尺寸
        setLocationRelativeTo(null);  // 居中显示
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 搜索栏
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("搜索宠物:");
        searchField = new JTextField(20);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JButton searchButton = new JButton("搜索");
        searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // 表格模型
        String[] columnNames = {"宠物ID", "宠物名称", "类别", "品种", "年龄", "性别", "是否领养"};
        tableModel = new DefaultTableModel(columnNames, 0);
        petTable = new JTable(tableModel);
        petTable.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        petTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(petTable);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        addButton = new JButton("添加宠物");
        addButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        editButton = new JButton("编辑宠物");
        editButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        deleteButton = new JButton("删除宠物");
        deleteButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // 为按钮添加监听器
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPets(searchField.getText());  // 根据搜索框内容过滤宠物列表
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 打开添加宠物页面
                new AddPetPage();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = petTable.getSelectedRow();
                if (selectedRow != -1) {
                    // 获取选中的宠物ID并打开编辑页面
                    int petId = (int) petTable.getValueAt(selectedRow, 0);
                    new EditPetPage(petId);
                } else {
                    JOptionPane.showMessageDialog(null, "请先选择一个宠物");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = petTable.getSelectedRow();
                if (selectedRow != -1) {
                    int petId = (int) petTable.getValueAt(selectedRow, 0);
                    deletePet(petId);
                } else {
                    JOptionPane.showMessageDialog(null, "请先选择一个宠物");
                }
            }
        });

        // 添加组件到布局
        add(searchPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadPetData();  // 加载宠物数据
        pack();
        setVisible(true);
    }

    // 加载宠物数据
    private void loadPetData() {
        List<Pet> pets = getAllPets();
        tableModel.setRowCount(0);  // 清空表格
        for (Pet pet : pets) {
            tableModel.addRow(new Object[] {
                    pet.getId(),
                    pet.getName(),
                    pet.getType(),
                    pet.getBreed(),
                    pet.getAge(),
                    pet.getGender(),
                    pet.isAvailableForAdoption() ? "是" : "否"
            });
        }
    }

    // 获取所有宠物数据
    private List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();
        try (Connection conn = DBConnection.connect()) {
            String sql = "SELECT * FROM pets AS P\n" +
                    "JOIN pet_breeds AS B ON B.breed_id=P.breed_id\n" +
                    "JOIN pet_types AS T ON T.type_id=B.type_id ";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pet pet = new Pet(
                            rs.getInt("pet_id"),
                            rs.getString("name"),
                            rs.getString("type_name"),
                            rs.getString("breed_name"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getBoolean("available_for_adoption")
                    );
                    pets.add(pet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pets;
    }

    // 根据搜索条件筛选宠物
    private void searchPets(String keyword) {
        List<Pet> pets = searchPetsByName(keyword);
        tableModel.setRowCount(0);
        for (Pet pet : pets) {
            tableModel.addRow(new Object[] {
                    pet.getId(),
                    pet.getName(),
                    pet.getType(),
                    pet.getBreed(),
                    pet.getAge(),
                    pet.getGender(),
                    pet.isAvailableForAdoption() ? "是" : "否"
            });
        }
    }

    // 根据宠物名称搜索
    private List<Pet> searchPetsByName(String keyword) {
        List<Pet> pets = new ArrayList<>();
        try (Connection conn = DBConnection.connect()) {
            String sql = "SELECT * FROM pets WHERE name LIKE ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, "%" + keyword + "%");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Pet pet = new Pet(
                            rs.getInt("pet_id"),
                            rs.getString("name"),
                            rs.getString("type_name"),
                            rs.getString("breed_name"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getBoolean("available_for_adoption")
                    );
                    pets.add(pet);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pets;
    }

    // 删除宠物
    private void deletePet(int petId) {
        int confirm = JOptionPane.showConfirmDialog(this, "确认删除该宠物?", "删除宠物", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.connect()) {
                String sql = "DELETE FROM pets WHERE pet_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, petId);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(this, "宠物删除成功!");
                    loadPetData();  // 重新加载宠物数据
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "删除失败，请稍后再试");
            }
        }
    }

    // 主方法
    public static void main(String[] args) {
        new AdminPetManagePage();
    }
}
