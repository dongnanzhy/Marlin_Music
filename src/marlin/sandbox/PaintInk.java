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
  // recognize Shape
  public static String recognized = "";
  // prototype list
  public static Shape.Prototype.List pList = new Shape.Prototype.List();

  public void paintComponent(Graphics g){
    G.fillBackground(g, Color.WHITE);
    inkList.show(g);
    // 注意：不止show了inkList，还show了正在画的BUFFER
    g.setColor(Color.RED); Ink.BUFFER.show(g);
    // 显示Buffer size
    g.drawString("points: "+Ink.BUFFER.n, 600,30);
    // 显示recognize结果
    g.drawString(recognized, 700, 40);
    // 显示prototype list
    pList.show(g);
    // 显示最后两个Ink norm后的距离
    if(inkList.size()>1){
      int last = inkList.size()-1;
      int dist = inkList.get(last).norm.dist(inkList.get(last-1).norm);
      g.setColor(dist>UC.noMatchDist?Color.RED:Color.BLACK); // black for same red for different
      g.drawString("Dist: "+dist, 600, 60);
    }
  }

  //注意：这里的逻辑。BUFFER是一个class的static变量
  // 每当按下鼠标，BUFFER首先置空，然后更新BUFFER，往points里add point
  // 每当松开鼠标，用当前BUFFER new 一个Ink()实例
  public void mousePressed(MouseEvent me){Ink.BUFFER.dn(me.getX(),me.getY()); repaint();}
  public void mouseDragged(MouseEvent me){Ink.BUFFER.drag(me.getX(),me.getY()); repaint();}
  public void mouseReleased(MouseEvent me) {
    Ink ink = new Ink();
    // Recognize shape from DB
    Shape s = Shape.recognize(ink);
    recognized = "Recog: " + ((s != null)?s.name : "UN-RECOGNIZED");

    inkList.add(ink);

    Shape.Prototype proto;
    if(pList.bestDist(ink.norm) < UC.noMatchDist){ // we found a match so blend
      proto = Shape.Prototype.List.bestMatch;
      proto.blend(ink.norm);
    }else{  // new Prototype
      proto = new Shape.Prototype();
      pList.add(proto);
    }
    ink.norm = proto;

    repaint();
  }
}