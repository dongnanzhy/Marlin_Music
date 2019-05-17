package marlin.sandbox;

import marlin.UC;
import marlin.graphicsLib.G;
import marlin.graphicsLib.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ShapeTrainer extends Window {
  public static Shape.Prototype.List pList = new Shape.Prototype.List();

  public ShapeTrainer() {
    super("Shape Trainer", UC.mainWindowWidth, UC.mainWindowHeight);
  }

  public static String UNKNOWN = " <- this name is currently Unknown.";
  public static String ILLEGAL = " <-this name is NOT a legal Shape name.";
  public static String KNOWN = " <-this is a known shape.";
  public static String curName = "";
  public static String curState = ILLEGAL;

  // set state and get prototype list from DB
  public void setState() {
    curState = (Shape.Database.isLegal(curName)) ? UNKNOWN : ILLEGAL;
    if(curState.equals(UNKNOWN)){
      if(Shape.DB.containsKey(curName)){
        curState = KNOWN;
        pList = Shape.DB.get(curName).prototypes;
      }else{
        pList = null;
      }
    }
  }

  public void paintComponent(Graphics g) {
    G.fillBackground(g, Color.WHITE);
    g.setColor(Color.BLACK);
    g.drawString(curName, 600, 30);
    g.drawString(curState, 700, 30);
    g.setColor(Color.RED);
    // show 了正在画的buffer
    Ink.BUFFER.show(g);
    // 如果pList存在，画出正在train的prototype
    if(pList != null){pList.show(g);}
  }

  public void keyTyped(KeyEvent e) {
    char c = e.getKeyChar();
    System.out.println("Typed: " + c);
    // 换行符结尾表示当前输入结束，确认Shape name
    if(c == 0x0D || c == 0x0A) { // x0D & x0A are ascii CR & LF
      Shape.saveShapeDB();
      curName = "";
    } else {
      curName = curName + c;
    }
    setState();
    repaint();
  }

  //注意：这里的逻辑。BUFFER是一个class的static变量
  // 每当按下鼠标，BUFFER首先置空，然后更新BUFFER，往points里add point
  // 每当松开鼠标，用当前BUFFER new 一个Ink()实例
  public void mousePressed(MouseEvent me){Ink.BUFFER.dn(me.getX(),me.getY()); repaint();}
  public void mouseDragged(MouseEvent me){Ink.BUFFER.drag(me.getX(),me.getY()); repaint();}
  public void mouseReleased(MouseEvent me) {
    Ink ink = new Ink();
    Shape.DB.train(curName, ink.norm); // this is safe because legal name testing is done in Database
    setState(); // possibly convert previously UNKNOWN to KNOWN
    repaint();
  }

}