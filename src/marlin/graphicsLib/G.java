package marlin.graphicsLib;

import java.awt.*;
import java.io.Serializable;
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

  // draw circle around point
  public static void drawCircle(Graphics g, int x, int y, int r) {
    g.drawOval(x-r,y-r,r+r,r+r);
  }

  // ----Vector-----, can represent point or size
  public static class V implements Serializable{
    public int x,y;
    public V(int x, int y){this.set(x,y);}
    public V(V v){this.set(v.x, v.y);}
    public void set(int x, int y){this.x = x; this.y = y;}
    public void set(V v){this.x = v.x; this.y = v.y;}
    public void add(V v){x += v.x; y += v.y;}

    // For scaling Ink
    public static Transform T = new Transform(); // the single one that V will use most often
    public int tx(){return x * T.newScale/ T.oldScale + T.dx;}
    public int ty(){return y * T.newScale/ T.oldScale + T.dy;}
    public void setT(){set(this.tx(), this.ty());}
    // --isomorphic scaling--
    public static class Transform{
      public Transform(){}
      private int dx=0, dy=0, oldScale=1, newScale=1; // the single scale multiplier is newScal/oldScale
      private void setScale(int oldW, int oldH, int newW, int newH){
        oldScale = (oldW>oldH)?oldW:oldH; newScale = (newW>newH)?newW:newH;
      }
      private int Trans(int oldX, int oldW, int newX, int newW){ // assumes that scale has already been set
        return (-oldX - oldW/2)*newScale/oldScale + (newX + newW/2);
      }
      public void set(VS from, VS to){
        setScale(from.size.x, from.size.y, to.size.x, to.size.y);
        dx = Trans(from.loc.x, from.size.x, to.loc.x, to.size.x);
        dy = Trans(from.loc.y, from.size.y, to.loc.y, to.size.y);
      }
      public void set(BBox from, VS to){
        setScale(from.h.size(), from.v.size(), to.size.x, to.size.y);
        dx = Trans(from.h.lo, from.h.size(), to.loc.x, to.size.x);
        dy = Trans(from.v.lo, from.v.size(), to.loc.y, to.size.y);
      }
    }

    // for smooth shape, average of points
    public void blend(V v, int k){set((k*x + v.x)/(k+1), (k*y + v.y)/(k+1));}
  }

  // ----Rectangles----
  public static class VS implements Serializable{
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

  // ----Polyline----
  public static class PL implements Serializable {
    // we keep an array of points
    public V[] points;

    // initiate PL with number of points
    public PL(int count){
      points = new V[count]; // allocate the array
      for(int i = 0; i < count; i++) { points[i] = new V(0, 0); } // populate it with V objects
    }
    public int size(){return points.length;}

    // draw lines connecting points
    public void drawN(Graphics g, int n){
      for(int i = 1; i < n; i++) {
        g.drawLine(points[i - 1].x, points[i - 1].y, points[i].x, points[i].y);
      }
      // temporally add dots
      drawNDots(g,n);
    }
    // draw dots on each point
    public void drawNDots(Graphics g, int n){
      g.setColor(Color.GREEN);
      for(int i=0; i<n; i++){drawCircle(g, points[i].x, points[i].y, 4);}
    }
    public void draw(Graphics g){drawN(g, points.length);} // draws the whole array.

    //  transform all the Vs in a sinlge polyline
    public void transform(){for(int i = 0; i<points.length; i++){points[i].setT();}}
  }

  // ----low-high pairs---- Two ints indicating range from lo to hi
  public static class LoHi{
    public int lo, hi;
    public LoHi(int min, int max){lo = min; hi = max;}
    public void set(int v){lo = v; hi = v;} // first value into the set
    public void add(int v){if(v<lo){lo=v;} if(v>hi){hi=v;}} // move bounds if necessary
    public int size(){return (hi-lo) > 0 ? hi-lo : 1;}
    // return v w.r.t LoHi
    public int constrain(int v){
      if(v<lo){return lo;}
      else return (v<hi)?v:hi;}
  }

  // ----Bounding Box----
  public static class BBox{
    // horizontal and vertical ranges.
    LoHi h, v;
    public BBox(){h = new LoHi(0,0); v = new LoHi(0,0);}
    public void set(int x, int y){h.set(x); v.set(y);} // sets it to a single point
    public void add(int x, int y){h.add(x); v.add(y);}
    public void add(V v){add(v.x, v.y);}
    public VS getNewVS(){return new VS(h.lo, v.lo, h.hi-h.lo, v.hi-v.lo);}
    public void draw(Graphics g){g.drawRect(h.lo, v.lo, h.hi-h.lo, v.hi-v.lo);}
  }
}