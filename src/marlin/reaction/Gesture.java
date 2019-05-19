package marlin.reaction;

import marlin.I;

import marlin.graphicsLib.G;
import marlin.sandbox.Ink;
import marlin.sandbox.Shape;

import java.util.ArrayList;

/*
Gesture是Ink的另一种输出形式，他通过Ink(画出的图形)来识别Shape
Shape 和 vs 是他的variable
Reaction可以通过bid Gesture来进行后续操作: doGesture()
 */
public class Gesture{
  public Shape shape;
  public G.VS vs;

  // private的全局list，used in undo
  private static List UNDO = new List();

  // private 构造函数，factory mode
  private Gesture(Shape shape, G.VS vs){this.shape = shape; this.vs = vs;}

  // 通过norm后的Ink来recognize Shape来建一个Gesture
  public static Gesture getNew(Ink ink){
    Shape s = Shape.recognize(ink);
    return (s==null) ? null : new Gesture(s,ink.vs);
  }


  /*
  注意：这两个function都是执行Gesture
       undoableGesture()在执行的同时加到UNDO list里面，以供后面undo操作
       doGesture()用在真正执行undo操作时，只是执行Gesture并不会另外加入UNDO list
   */
  private void doGesture(){
    Reaction r = Reaction.best(this);
    if(r != null){ r.act(this);}
  }
  private void undoableGesture(){
    Reaction r = Reaction.best(this);
    if(r != null){ UNDO.add(this); r.act(this);}
  }

  // undo function
  public static void undo(){
    if(UNDO.size() > 0){
      // remove last element
      UNDO.remove(UNDO.size()-1);
      // eliminates all the masses
      Layer.nuke();
      // clears byShape then reloads initial reactions
      Reaction.nuke();
      // 重新执行除最后一个操作外的所有操作
      UNDO.redo();
    }
  }

  // --- List of Gesture (used in undo) ---
  public static class List extends ArrayList<Gesture>{
    private void redo(){ for(Gesture g : this){g.doGesture();}}
  };

  /* ----  A Gesture Area (anonymous class) ------
    注意：这个定义与PaintInk中的定义一样，也是显示出鼠标stroke，bbox和点
         唯一不同的是在up()里加入了一些对Gesture的操作
   */
  public static I.Area AREA = new I.Area(){
    public boolean hit(int x, int y){ return true; }
    public void dn(int x, int y){Ink.BUFFER.dn(x,y);}
    public void drag(int x, int y){Ink.BUFFER.drag(x,y);}
    public void up(int x, int y){
      Ink.BUFFER.add(x,y);
      Ink ink = new Ink();
      // 通过识别Ink来新建一个Gesture(可能无法识别Ink进而无法新建Gesture[unrecognized]),无法新建则什么都不操作
      Gesture g = Gesture.getNew(ink);
      // 彻底清除BUFFER
      Ink.BUFFER.clear();
      if(g != null){
        if(g.shape.name.equals("N-N")){ // hardwired UNDO
          undo();
        }else {
          g.undoableGesture();
        }
      }
    }
  } ;

}