package com.roadjava.AdoptersView;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PetListPage extends JFrame {
    private JTable petTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton prevButton, nextButton;
    private JLabel pageLabel;
    private int currentPage = 1;
    private final int pageSize = 10;
    private UserLogin user;

    public PetListPage(UserLogin user) {
        this.user = user;
        setTitle("宠物列表");
        setLayout(new BorderLayout());

        // 搜索区域
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("搜索");
        searchPanel.add(new JLabel("欢迎！"+user.getUsername()));
        searchPanel.add(new JLabel("宠物名称:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // 表格区域
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{"ID", "名称", "类别", "品种", "年龄", "性别", "是否可领养", "操作"});
        petTable = new JTable(tableModel);
        petTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(petTable);

        // 分页区域
        JPanel paginationPanel = new JPanel();
        prevButton = new JButton("上一页");
        nextButton = new JButton("下一页");
        pageLabel = new JLabel("页码: " + currentPage);
        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);

        // 添加组件到布局中
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);

        // 设置按钮点击事件
        searchButton.addActionListener(e -> searchPets());
        prevButton.addActionListener(e -> loadPage(currentPage - 1));
        nextButton.addActionListener(e -> loadPage(currentPage + 1));

        // 初始加载第一页
        loadPage(currentPage);

        // 设置窗口大小并显示
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // 加载宠物数据，支持分页
    private void loadPage(int page) {
        if (page < 1) return;  // 防止页码小于1
        currentPage = page;
        pageLabel.setText("页码: " + currentPage);

        List<Pet> pets = fetchPets(currentPage);
        updateTable(pets);

        // 控制分页按钮的启用状态
        prevButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(pets.size() == pageSize);  // 如果当前页数据量达到最大值，则启用下一页按钮
    }

    // 根据当前页码获取宠物数据
    private List<Pet> fetchPets(int page) {
        List<Pet> pets = new ArrayList<>();
        String query = "SELECT * FROM pets AS P\n" +
                "JOIN pet_breeds AS B ON B.breed_id=P.breed_id\n" +
                "JOIN pet_types AS T ON T.type_id=B.type_id LIMIT ?, ?";

        try (Connection conn = DBConnection.connect()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, (page - 1) * pageSize);
            ps.setInt(2, pageSize);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pets.add(new Pet(
                        rs.getInt("pet_id"),
                        rs.getString("name"),
                        rs.getString("type_name"),
                        rs.getString("breed_name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getBoolean("available_for_adoption")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pets;
    }

    // 更新表格内容
    private void updateTable(List<Pet> pets) {
        tableModel.setRowCount(0);  // 清空表格内容
        for (Pet pet : pets) {
            Object[] row = {
                    pet.getId(), pet.getName(), pet.getType(),
                    pet.getBreed(), pet.getAge(), pet.getGender(),
                    pet.isAvailableForAdoption() ? "是" : "否",
                    "查看详情", "领养此宠物"  // 添加操作按钮列
            };
            tableModel.addRow(row);
        }

        // 设置按钮列的操作
        petTable.getColumn("操作").setCellRenderer(new ButtonRenderer());
        petTable.getColumn("操作").setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    // 搜索宠物
    private void searchPets() {
        String searchQuery = searchField.getText().trim();
        if (!searchQuery.isEmpty()) {
            List<Pet> pets = searchPetsByName(searchQuery);
            updateTable(pets);
        } else {
            loadPage(1);  // 搜索框为空时，加载第一页数据
        }
    }

    // 根据名称搜索宠物
    private List<Pet> searchPetsByName(String name) {
        List<Pet> pets = new ArrayList<>();
        String query = "SELECT * FROM pets AS P\n" +
                "JOIN pet_breeds AS B ON B.breed_id=P.breed_id\n" +
                "JOIN pet_types AS T ON T.type_id=B.type_id WHERE name LIKE ? LIMIT ?, ?";

        try (Connection conn = DBConnection.connect()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + name + "%");
            ps.setInt(2, (currentPage - 1) * pageSize);
            ps.setInt(3, pageSize);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                pets.add(new Pet(
                        rs.getInt("pet_id"),
                        rs.getString("name"),
                        rs.getString("type_name"),
                        rs.getString("breed_name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getBoolean("available_for_adoption")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return pets;
    }

    // 按钮渲染器类
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value == "查看详情") {
                setText("查看详情");
            } else if (value == "领养此宠物") {
                setText("领养此宠物");
            }
            return this;
        }
    }

    // 按钮编辑器类
    class ButtonEditor extends DefaultCellEditor {
        private String label;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == "查看详情") ? "查看详情" : "领养此宠物";
            JButton button=new JButton();
            button.setText(label);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }

  /*  public static void main(String[] args) {
        // Example of running the PetListPage
        UserLogin user = new UserLogin();  // Assume UserLogin is initialized here
        SwingUtilities.invokeLater(() -> new PetListPage(user));
    }*/
}
