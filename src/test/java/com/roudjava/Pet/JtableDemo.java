package com.roudjava.Pet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Vector;

public class JtableDemo extends JFrame{
    public JtableDemo()
    {
        Vector<String> columns=new Vector<>();
        columns.addElement("编号");
        columns.addElement("姓名");
        columns.addElement("学号");
        //
        Vector<Vector<Object>> data=new Vector<>();

        Vector<Object> rowVector1=new Vector<>();
        rowVector1.addElement("1");
        rowVector1.addElement("张三");
        rowVector1.addElement("3220707010");

        Vector<Object> rowVector2=new Vector<>();
        rowVector2.addElement("2");
        rowVector2.addElement("李四");
        rowVector2.addElement("3220707011");

        data.addElement(rowVector1);
        data.addElement(rowVector2);

        DefaultTableModel tableModel=new DefaultTableModel(data,columns);

        JTable jTable=new JTable(tableModel);

        //设置表头
        JTableHeader tableHeader=jTable.getTableHeader();
        tableHeader.setFont(new Font(null,Font.BOLD,16));
        tableHeader.setForeground(Color.RED);

        //设置表格体
        jTable.setFont(new Font(null,Font.BOLD,16));
        jTable.setForeground(Color.black);
        jTable.setGridColor(Color.BLACK);
        jTable.setRowHeight(30);
        Container contentPane=getContentPane();

        //jtable放在jpanel上默认不展示列头，需要特殊设置，放在JScrollPane上面，默认显示
        JScrollPane jScrollPane=new JScrollPane(jTable);
        contentPane.add(jScrollPane);

        setSize(600,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}
