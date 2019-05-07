package marlin.sandbox;
import marlin.graphicsLib.G;
import marlin.graphicsLib.Window;
import marlin.UC;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PaintInk extends Window{
  public PaintInk(){
    super("Paint Ink", UC.mainWindowWidth, UC.mainWindowHeight);
  }
  public static Ink.List inkList = new Ink.List();
  // static block add one single ink item so we can test showing a list
  static {inkList.add(new Ink());}

  public void paintComponent(Graphics g){
    G.fillBackground(g, Color.WHITE);
    inkList.show(g);
    g.setColor(Color.RED); Ink.BUFFER.show(g);
  }

  //注意：这里的逻辑。BUFFER是一个class的static变量
  // 每当按下鼠标，BUFFER首先置空，然后更新BUFFER，往points里add point
  // 每当松开鼠标，用当前BUFFER new 一个Ink()实例
  public void mousePressed(MouseEvent me){Ink.BUFFER.pressed(me.getX(),me.getY()); repaint();}
  public void mouseDragged(MouseEvent me){Ink.BUFFER.dragged(me.getX(),me.getY()); repaint();}
  public void mouseReleased(MouseEvent me){inkList.add(new Ink()); repaint();}
}