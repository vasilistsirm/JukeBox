package gr.hua.dit.oop2;


import javax.swing.*;

public class Main {

  public static void main(String[] args){
    Player pl = new Player();
    pl.setSize(600,600);
    pl.setVisible(true);
    pl.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//if press exit, close frame

  }
}