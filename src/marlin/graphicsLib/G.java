package marlin.graphicsLib;

import java.awt.*;
import java.util.Collection;
import java.util.Random;

public class G{
  /*
  All helper functions
   */

  // get random color
  public static Random RND = new Random();
  public static int rnd(int max){return RND.nextInt(max);}
  public static Color rndColor(){return new Color(rnd(256),rnd(256),rnd(256)); }

  // Set Background, reuse VS class
  public static final VS BACKGROUND_RECT = new VS(0,0,3000,3000);
  public static void fillBackground(Graphics g, Color c) {
    g.setColor(Color.WHITE);
    BACKGROUND_RECT.fill(g, c);
  }

  // Vector, can represent point or size
  public static class V{
    public int x,y;
    public V(int x, int y){this.set(x,y);}
    public V(V v){this.set(v.x, v.y);}
    public void set(int x, int y){this.x = x; this.y = y;}
    public void add(V v){x += v.x; y += v.y;}
  }

  // Rectangles
  public static class VS{
    public V loc, size;

    public VS(int x, int y, int w, int h) {
      loc = new V(x,y);
      size = new V(w,h);
    }

    public int lox(){return loc.x;}
    public int hix(){return loc.x + size.x;}
    public int midx(){return (loc.x + loc.x + size.x)/2;}
    public int loy(){return loc.y;}
    public int hiy(){return loc.y + size.y;}
    public int midy(){return (loc.y + loc.y + size.y)/2;}

    public void fill(Graphics g, Color c) {
      g.setColor(c);
      g.fillRect(loc.x, loc.y, size.x, size.y);
    }
    public boolean hit(int x, int y) {
      return loc.x<=x && loc.y <=y && x<=(loc.x+size.x) && y<=(loc.y+size.y);
    }

  }

  // Polyline
  public static class PL {
    // we keep an array of points
    public V[] points;

    public PL(int count){
      points = new V[count]; // allocate the array
      for(int i = 0; i < count; i++) { points[i] = new V(0, 0); } // populate it with V objects
    }
    public int size(){return points.length;}
    public void drawN(Graphics g, int n){  // used to draw an initial portion of the full array
      for(int i = 1; i < n; i++) {
        g.drawLine(points[i - 1].x, points[i - 1].y, points[i].x, points[i].y);
      }
    }
    public void draw(Graphics g){drawN(g, points.length);} // draws the whole array.
  }
}