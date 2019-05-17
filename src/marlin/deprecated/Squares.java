package marlin.deprecated;
import marlin.graphicsLib.G;
import marlin.graphicsLib.Window;
import marlin.I;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/*
Used in warm up, deprecated!
注意：核心思想
1. Static Nested Class 来 access
2. 通过implements interface来实现函数，传递不同的实例。
   进而通过不同实例来实现判断，而不是用if...else...statement
 */

public class Squares extends Window implements ActionListener {
  /*
  Variables
   */
  public static I.Area curArea;
  public static Square.List LIST = new Square.List();
  public static Square daSquare;
  // Used for dragging offset.
  private static G.V mouseDelta = new G.V(0, 0);

  //注意：BACKGROUND 是一个 anonymous subclass
  // 他虽然是一个Square object，但是重写了pressed 和 dragged 函数，用来应对resize(rubberband)
  public static Square BACKGROUND = new Square(0,0) // this will be our back square BUT FIRST some overrides
  {
    // Override two functions for rubberband(resize)
    public void pressed(int x, int y){daSquare = new Square(x, y); LIST.add(daSquare);}
    public void dragged(int x, int y){daSquare.resize(x,y);}
  };

  // Initializing that special first BACKGROUND Square and put it FIRST on LIST!
  static{BACKGROUND.c = Color.WHITE; BACKGROUND.size.set(3000,3000);
  LIST.add(BACKGROUND);}

  // For Animation
  public static Timer timer;

  /*
  Functions
   */
  // construct function
  public Squares() {
    super("Squares", 1000, 800);
    timer = new Timer(30,this);
    timer.setInitialDelay(5000); // 5 seconds before the timer starts
    timer.start(); // start the timer
  }

  protected void paintComponent(Graphics g){
    LIST.draw(g);
  }

  // drag or create rectangle according to whether mouse hit existed one.
  public void mousePressed(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    // 注意：should always succeed because of BACKGROUND
    curArea = LIST.hit(x,y);
    curArea.dn(x,y);
    repaint(); // don't forget to repaint when you change something.
  }

  // drag mouse to either reshape or move
  public void mouseDragged(MouseEvent me) {
    curArea.drag(me.getX(), me.getY());
    repaint();
  }

  // Override ActionListener function
  public void actionPerformed(ActionEvent ae){repaint();}

  /*
  Nested classes
  Square object and
  List of squares, to keep track of multiple squares
   */
  public static class Square extends G.VS implements I.Area {
    public Color c = G.rndColor();

    // velocity
    public G.V dv = new G.V(G.rnd(20)-10, G.rnd(20)-10);
    //public G.V dv = new G.V(0,0); // stop the motion!

    public Square(int x, int y){super(x,y,100,100);}
    // Draw Square
    public void draw(Graphics g){fill(g, c); moveAndBounce();}

    // rubberband
    public void resize(int x, int y) {
      if(x > loc.x && y > loc.y) {
        size.set(x - loc.x, y - loc.y);
      }
    }
    // drag rectangle to move
    public void move(int x, int y) {loc.set(x, y);}
    // move with bounce for Animation
    public void moveAndBounce(){
      loc.add(dv);
      if(lox() < 0 && dv.x <0){dv.x = - dv.x;}
      if(hix() > 1000 && dv.x >0){dv.x = - dv.x;}
      if(loy() < 0 && dv.y <0){dv.y = - dv.y;}
      if(hiy() > 800 && dv.y >0){dv.y = - dv.y;}
    }

    //overide interface functions for dragging
    // Squares already implement hit() from parent(G.VS).
    // calculate drag offset
    public void dn(int x, int y) {
      mouseDelta.set(loc.x - x, loc.y - y);
      dv.set(0,0); // stop moving once clicked
    }
    public void drag(int x, int y){loc.set(mouseDelta.x + x, mouseDelta.y + y);}
    public void up(int x, int y){}

    /*
     Nested class storing all Squares
     */
    public static class List extends ArrayList<Square>{
      public void draw(Graphics g){for(Square s : this){s.draw(g);}}
      public void addNew(int x, int y){add(new Square(x,y));}
      // see if we hit one of the Squares
      public Square hit(int x, int y) {
        Square res = null;
        for(Square s : this) {
          if (s.hit(x, y)) {
            res = s;
          }
        }
        return res;
      }
    }

  }
}
