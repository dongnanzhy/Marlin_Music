package marlin.sandbox;

import marlin.UC;
import marlin.I;
import marlin.graphicsLib.G;

import java.awt.*;
import java.util.ArrayList;

// Shape keeps track of different ways to draw a shape
//  - Basically we take a single new input Norm and
//  - compare it distancewise to all the prototypes in all the shapes that we have and find the best match
public class Shape {
  public Prototype.List prototypes = new Prototype.List();
  public String name;
  public Shape(String name){this.name = name;}

  // --- Prototype class
  public static class Prototype extends Ink.Norm{
    // how many numbers have kept track for smooth
    int nBlend;

    // -- List of Prototypes --
    public static class List extends ArrayList<Prototype> implements I.Show {
      // for showing
      private static int m = 10, w = 60; private static G.VS showbox = new G.VS(m,m,w,w);
      public void show(Graphics g){ // draw a list of boxes across top of screen
        g.setColor(Color.ORANGE);
        for(int i = 0; i<size(); i++){
          Prototype p = get(i); int x = m + i*(m+w);
          showbox.loc.set(x, m); // march the showbox across the top of the screen
          p.drawAt(g, showbox);
          g.drawString(""+p.nBlend,x,20);
        }
      }

      // this is set as a side effect when running bestDist
      public static Prototype bestMatch;
      public int bestDist(Ink.Norm norm){
        bestMatch = null;
        int bestSoFar = UC.noMatchDist; // assume no match
        for(Prototype p : this) {
          int d = p.dist(norm);
          if(d < bestSoFar){
            bestMatch = p;
            bestSoFar = d;
          }
        }
        return bestSoFar;
      }
    }
    // average all prototypes
    public void blend(Ink.Norm norm) {
      for(int i = 0; i<N; i++){points[i].blend(norm.points[i], nBlend);}
    }
  }

}
