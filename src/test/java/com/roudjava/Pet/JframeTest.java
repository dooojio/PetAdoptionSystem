package com.roudjava.Pet;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class JframeTest {
    public static void main(String[] args){
        JFrame jFrame=new JFrame();

        URL resourse = JframeTest.class.getClassLoader().getResource("PetAdoptionSystem.png");
        Image image =new ImageIcon(resourse).getImage();
        jFrame.setIconImage(image);
        jFrame.setSize(600,400);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setVisible(true);

        //
        JButton jButton=new JButton();
        Container container=jFrame.getContentPane();
        container.add(jButton);


    }
}
