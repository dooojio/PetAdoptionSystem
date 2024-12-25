package com.roadjava.AdoptersView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // MySQL 数据库连接信息

       /* try {
            Class.forName("com.mysql.cj.jdbc.Driver");//加载数据库连接器
        }

        catch(ClassNotFoundException e){
        System.out.println(""+e);
    }*/

    private static String URL="jdbc:mysql://localhost:3306/pet_adoption_system?user=root&password=swy4apig,&useSSL=false&serverTimezone=GMT";
   /* private static final String URL = "jdbc:mysql://localhost:3306/adoption_system";
    private static final String USER = "root";
    private static final String PASSWORD = "password"; // 修改为你的数据库密码*/

    public static Connection connect() {
        try {
            //Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Connection conn = DriverManager.getConnection(URL);

            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

