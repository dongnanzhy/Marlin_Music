package marlin.reaction;

import marlin.I;
import marlin.UC;
import marlin.graphicsLib.G;
import marlin.graphicsLib.Window;
import marlin.sandbox.Ink;

import java.awt.*;
import java.awt.event.MouseEvent;

/*
extends Window代表是main用来调用运行的

 */
public class SimpleReaction extends Window {

  /*
    static 全局新建两个Layer
    后面的Box extends Mass, 都会被加在"BACK"这个ayer
   */
  static{new Layer("BACK"); new Layer("FORE");}

  public SimpleReaction(){
    super("Simple Reaction", UC.mainWindowWidth, UC.mainWindowHeight);
    /*
     Anonymous class to implement function from interface
     注意："SW-SW"这个Reaction被加到initialReactions里面。所以在UNDO操作，Reaction 清空后也存在
     */
    Reaction.initialReactions.addReaction(new Reaction("SW-SW"){
      public int bid(Gesture g) {
        // always win bid
        return 0;
      }
      // 这个Gesture的作用是create一个Box(注意Box实现了show，显示一个矩形)
      public void act(Gesture g){new Box(g.vs);}
    });
  }
  public void paintComponent(Graphics g){
    G.fillBackground(g, Color.WHITE);
    g.setColor(Color.BLUE);
    Ink.BUFFER.show(g);
    Layer.ALL.show(g);
  }
  /*
    通过鼠标记录Ink，识别Gesture，通过bid()找到best Reaction，进而通过act()进行操作
   */
  public void mousePressed(MouseEvent me){Gesture.AREA.dn(me.getX(),me.getY()); repaint();}
  public void mouseDragged(MouseEvent me){Gesture.AREA.drag(me.getX(),me.getY()); repaint();}
  public void mouseReleased(MouseEvent me){Gesture.AREA.up(me.getX(),me.getY()); repaint();}

  // --- Box class ---
  public static class Box extends Mass{
    public G.VS vs;
    public Color c = G.rndColor();
    public Box(G.VS vs){
      super("BACK");
      this.vs = vs;

      // delete
      addReaction(new Reaction("S-S"){
        public int bid(Gesture g){
          // get the x,y from the Gesture
          int x = g.vs.midx(), y = g.vs.loy();
          // "Box.this" is one specific Box for a Reaction
          if(Box.this.vs.hit(x,y)) {
            // 表示该线段x坐标与Box顶部中点x坐标间的距离, 通过这种方式即便图形有重叠，也能找到最优的结果
            return Math.abs(x-Box.this.vs.midx());
          } else {
            return UC.noBid;
          }
        }
        public void act(Gesture g){Box.this.delete();}
      });

      // change color
      addReaction(new Reaction("DOT"){
        public int bid(Gesture g){
          int x = g.vs.midx(), y = g.vs.loy();
          if(Box.this.vs.hit(x,y)){return Math.abs(x - Box.this.vs.midx());} else {return UC.noBid;}
        }
        public void act(Gesture g){Box.this.c = G.rndColor();}
      });
    }
    public void show(Graphics g){vs.fill(g,c);}
  }
}