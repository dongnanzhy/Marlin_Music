package marlin.sandbox;

import marlin.I;
import marlin.graphicsLib.G;
import marlin.UC;

import java.awt.*;
import java.util.ArrayList;

public class Ink implements I.Show{
  public Norm norm;
  public G.VS vs;
  // buffer at drawing
  public static Buffer BUFFER = new Buffer();


  public Ink() {
    // norm automatically loads from BUFFER
    norm = new Norm();
    // vs is bbox where the ink was on the screen
    vs = BUFFER.bbox.getNewVS();
  }

  public void show(Graphics g) {
    g.setColor(UC.inkColor);
    norm.drawAt(g, vs);
  }


  // ---- List of Ink ----
  public static class List extends ArrayList<Ink> implements I.Show{
    public void show(Graphics g){for(Ink ink : this){ink.show(g);}}
  }

  // ---- Ink Buffer ----
  public static class Buffer extends G.PL implements I.Show, I.Area{
    public static final int MAX = UC.inkBufferMax; // maximum size of buffer
    public int n; // how many points are actually in the buffer.
    public G.BBox bbox = new G.BBox();

    // create the PL with MAX points
    // 注意：private construct function is almost always an indication of a Singlton
    private Buffer(){super(MAX);}

    // adds new point to buffer if buffer size allowed
    public void add(int x, int y) {
      if(n<MAX){points[n].set(x,y); n++;}
      bbox.add(x, y);
    }
    public void clear(){n = 0;}

    // implement I.Show using function defined in G.PL
    // Only draw existing points
    public void show(Graphics g){
      this.drawN(g, n);
      bbox.draw(g); // to test
    }
    // implement I.Area
    public boolean hit(int x, int y){return true;} // any point COULD go into ink
    // add first point
    public void pressed(int x, int y) {
      clear();
      bbox.set(x, y);
      add(x,y);
    }
    public void dragged(int x, int y){add(x,y);} // add each point as it comes in
    public void released(int x, int y){}

    // sub-sample K from n points in Buffer
    public G.PL subSample(int k){
      G.PL res = new G.PL(k);
      for(int i = 0; i<k; i++){res.points[i].set(this.points[i*(n-1)/(k-1)]);}
      return res;
    }
  }

  // --- Norm ---
  public static class Norm extends G.PL{
    public static final int N = UC.normSampleSize, MAX = UC.normCoordMax;
    public static final G.VS CS = new G.VS(0,0,MAX,MAX); // the coordinate box for Transforms
    public Norm(){
      super(N);
      // subsample K from n points
      G.PL temp = BUFFER.subSample(N);
      // transform --> scale and move to specific position
      G.V.T.set(BUFFER.bbox, CS);
      temp.transform();
      for(int i = 0; i<N; i++){this.points[i].set(temp.points[i]);}
    }

    // Norms only draw at some place with a VS
    public void drawAt(Graphics g, G.VS vs){ // expands Norm to fit in vs
      G.V.T.set(CS, vs); // prepare to move from normalized CS to vs
      for(int i = 1; i<N; i++){
        g.drawLine(points[i-1].tx(), points[i-1].ty(), points[i].tx(), points[i].ty());
      }
    }

    // compare distances between two norms
    public int dist(Norm n){
      int res = 0;
      for(int i = 0; i<N; i++){
        int dx = points[i].x - n.points[i].x, dy = points[i].y - n.points[i].y;
        res += dx*dx + dy*dy;
      }
      return res;
    }
  }

}