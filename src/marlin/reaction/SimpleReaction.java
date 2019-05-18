package marlin.reaction;

import marlin.I;
import marlin.UC;
import marlin.graphicsLib.G;
import marlin.graphicsLib.Window;
import marlin.sandbox.Ink;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SimpleReaction extends Window {

  static{new Layer("BACK"); new Layer("FORE");} // create the layers that this class will need.

  public SimpleReaction(){
    super("Simple Reaction", UC.mainWindowWidth, UC.mainWindowHeight);
    // Anonymous class has to implement function from interface
    Reaction.initialReactions.addReaction(new Reaction("SW-SW"){
      public int bid(Gesture g){return 0;}
      public void act(Gesture g){new Box(g.vs);}
    });
  }
  public void paintComponent(Graphics g){
    G.fillBackground(g, Color.WHITE);
    g.setColor(Color.BLUE);
    Ink.BUFFER.show(g);
    Layer.ALL.show(g);
  }
  public void mousePressed(MouseEvent me){Gesture.AREA.dn(me.getX(),me.getY()); repaint();}
  public void mouseDragged(MouseEvent me){Gesture.AREA.drag(me.getX(),me.getY()); repaint();}
  public void mouseReleased(MouseEvent me){Gesture.AREA.up(me.getX(),me.getY()); repaint();}

  // --- Box class ---
  public static class Box extends Mass{
    public G.VS vs;
    public Color c = G.rndColor();
    public Box(G.VS vs){
      super("BACK"); this.vs = vs;

      // delete
      addReaction(new Reaction("S-S"){
        public int bid(Gesture g){
          // get the x,y from the Gesture
          int x = g.vs.midx(), y = g.vs.loy();
          // "Box.this" is one specific Box for a Reaction
          if(Box.this.vs.hit(x,y)) {
            // 表示该线段x坐标与Box顶部中点x坐标间的距离
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