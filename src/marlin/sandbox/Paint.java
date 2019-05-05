package marlin.sandbox;

import marlin.graphicsLib.G;
import marlin.graphicsLib.Window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


// Paint lines
public class Paint extends Window {
  public static int clicks = 0;
  // Path is one line
  public static Path daPath = new Path();
  // Pic is multiple lines
  public static Pic daPic = new Pic();

  public Paint() {
    super("Paint", 1000, 800);
  }

  public void mousePressed(MouseEvent me) {
    clicks++;
    daPath = new Path();
    daPath.add(me.getPoint());
    daPic.add(daPath);
    repaint(); // remember to repaint
  }

  @Override
  public void mouseDragged(MouseEvent me){
    daPath.add(me.getPoint());
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    // White Background
    g.setColor(Color.WHITE); g.fillRect(0,0,3000,3000);

    // set color
    //note: 当resize window的时候，color会更新因为OS会不断调用这个方法。
    // 因此这种写法对GC不友好，可以建一个static array存一些color，然后从来里面选，而不是每次new一个新的color (Flyweight模式)
    Color c = G.rndColor();
    g.setColor(c);

    // show text
    int x = 400, y = 200;
    String msg = "Clicks = " + clicks;
    g.drawString(msg,x,y);
    // local variable fm is information about the current font.
    FontMetrics fm = g.getFontMetrics();
    // the ascent is how far above the baseline the font extends, descent is how far below
    // so the entire height of the font will be a+d
    int a = fm.getAscent(), d = fm.getDescent();
    // width
    int w = fm.stringWidth(msg);

    // so now we know enough to draw the box.
    g.drawRect(x,y-a,w,a+d); // note: move y from baseline UP the page by the ascent

    // draw lines based on mouse click event
    daPic.draw(g);
  }

  // note: 这里用static定义nested class，
  // 这样从Paint内部调用的时候可以直接用Path, 外部调用需要Paint.Path
  public static class Path extends ArrayList<Point> {
    public void draw(Graphics g) {
      for(int i = 1; i<size(); i++) {
        Point p = get(i - 1), n = get(i); // the previous and the next point
        g.drawLine(p.x, p.y, n.x, n.y);
      }
    }
  }

  // Pic = multiple lines
  public static class Pic extends ArrayList<Path> {
    public void draw(Graphics g) {
      for(Path p : this) {
        p.draw(g);
      }
    }
  }
}
