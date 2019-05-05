package marlin.sandbox;
import marlin.graphicsLib.G;
import marlin.graphicsLib.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class Squares extends Window implements ActionListener {
  public static Square.List daList = new Square.List();

  public static Square daSquare;

  private boolean dragging = false;
  private static G.V mouseDelta = new G.V(0, 0);

  public static Timer timer;
  // we will set this when the mouse is pressed
  public static G.V pressedLoc = new G.V(0,0);

  public Squares() {
    super("Squares", 1000, 800);
    timer = new Timer(30,this);
    timer.setInitialDelay(5000); // I give myself 5 seconds before the timer starts
    timer.start(); // start the timer
  }

  public void actionPerformed(ActionEvent ae){repaint();}

  protected void paintComponent(Graphics g){
    G.fillBackground(g, Color.WHITE);
    daList.draw(g);
  }

  // drag or create rectangle according to whether mouse hit existed one.
  public void mousePressed(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    daSquare = daList.hit(x, y);
    if(daSquare == null) {
      dragging = false;
      //daList.addNew(me.getX(), me.getY());
      daSquare = new Square(x, y);
      daList.add(daSquare);
    } else {
      dragging = true;
      daSquare.dv.set(0,0);  // <-- we add this line to stop the velocity for the clicked square
      pressedLoc.set(x,y);   // also create a static G.V pressedLoc to Store the place where the mouse went down
      mouseDelta.set(daSquare.loc.x - x, daSquare.loc.y - y);
    }
    repaint(); // don't forget to repaint when you change something.
  }

  // drag mouse to either reshape or move
  public void mouseDragged(MouseEvent me) {
    int x = me.getX(), y = me.getY();
    if(dragging) {
      daSquare.move(x + mouseDelta.x, y + mouseDelta.y);
    } else {
      daSquare.resize(x, y);
    }
    repaint();
  }

  /*
  Keep track of multiple squares
   */
  public static class Square extends G.VS{
    public Color c = G.rndColor();
    // velocity
    //public G.V dv = new G.V(G.rnd(20)-10, G.rnd(20)-10);
    public G.V dv = new G.V(0,0); // stop the motion!

    public Square(int x, int y){super(x,y,100,100);}

    public void draw(Graphics g){fill(g, c); moveAndBounce();}

    public void moveAndBounce(){
      loc.add(dv);
      if(lox() < 0 && dv.x <0){dv.x = - dv.x;}
      if(hix() > 1000 && dv.x >0){dv.x = - dv.x;}
      if(loy() < 0 && dv.y <0){dv.y = - dv.y;}
      if(hiy() > 800 && dv.y >0){dv.y = - dv.y;}
    }

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
