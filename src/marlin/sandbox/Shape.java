package marlin.sandbox;

import marlin.UC;
import marlin.I;
import marlin.graphicsLib.G;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

// Shape keeps track of different ways to draw a shape
//  - Basically we take a single new input Norm and
//  - compare it distancewise to all the prototypes in all the shapes that we have and find the best match
public class Shape implements Serializable {
  public Prototype.List prototypes = new Prototype.List();
  public String name;
  public Shape(String name){this.name = name;}

  // --- Serialize Shapes to DB
  public static Database DB = loadShapeDB();
  public static Shape DOT = DB.get("DOT");
  public static Collection<Shape> LIST = DB.values();
  public static String nameList(){
    String res = "";
    if(DB != null){for(String s : DB.keySet()){res += " "+s;}}
    return res;
  }
  public static Database loadShapeDB() {
    Database db = null;
    try{
      System.out.println("attempting DB load..");
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(UC.fullPathToShapeDB));
      db = (Database) ois.readObject();
      System.out.println("Successful load: "+ nameList());
    } catch(Exception e) {
      System.out.println("-FAIL- Load DB\n" + e);
      db = new Database(); // use default empty if load failed
    }
    return db;
  }
  public static void saveShapeDB() {
    try{
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(UC.fullPathToShapeDB));
      oos.writeObject(DB);
      System.out.println("DB Saved: " + nameList());
    } catch(Exception e) {
      System.out.println("-FAIL- Save DB\n" + e);
    }
  }
  public static Shape recognize(Ink ink) {
    if(ink.vs.size.x < UC.dotThreshold && ink.vs.size.y < UC.dotThreshold){return DOT;}
    Shape bestMatch = null;
    int bestSoFar = UC.noMatchDist;
    for(Shape s:LIST){
      int d = s.prototypes.bestDist(ink.norm);
      if(d < bestSoFar){bestMatch = s; bestSoFar = d;}
    }
    return bestMatch;
  }

  // --- Shape.List class ---
  public static class List extends ArrayList<Shape>{
    public static Shape bestMatch;
    public int bestDist(Ink.Norm norm){
      bestMatch = null; int bestSoFar = UC.noMatchDist; // assume no match
      for(Shape s : this){int d = s.prototypes.bestDist(norm); if(d < bestSoFar){bestMatch = s; bestSoFar = d;}}
      return bestSoFar;
    }
  }

  // --- Prototype class
  // 注意：这里Prototype extends Norm, 而Norm extends VS，所以对应VS也要implements Serializable
  public static class Prototype extends Ink.Norm implements Serializable {
    // how many numbers have kept track for smooth
    int nBlend = 1;

    // average all prototypes
    public void blend(Ink.Norm norm) {
      for(int i = 0; i<N; i++){points[i].blend(norm.points[i], nBlend);}
    }

    // -- List of Prototypes --
    public static class List extends ArrayList<Prototype> implements I.Show, Serializable {
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

      // train prototypes
      public void train(Ink.Norm norm){
        if(bestDist(norm) < UC.noMatchDist){ // we found a match so blend
          bestMatch.blend(norm);
        }else{
          add(new Shape.Prototype()); // didn't match so add a new one (from Ink.BUFFER)
        }
      }
    }
  }

  // --- Database class ---
  public static class Database extends HashMap<String, Shape> implements Serializable {
    private Database(){super();
    // make sure DOT exists
    String dot = "DOT"; put(dot, new Shape(dot));}
    private Shape forceShape(String name) {
      if(!DB.containsKey(name)) {
        DB.put(name, new Shape(name));
      }
      return DB.get(name);
    }
    public void train(String name, Ink.Norm norm) {
      if(isLegal(name)) {
        forceShape(name).prototypes.train(norm);
      }
    }
    public static boolean isLegal(String name){return !name.equals(" ") && !name.equals("DOT");}
  }

}
