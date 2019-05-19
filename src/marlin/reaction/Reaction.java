package marlin.reaction;

import marlin.sandbox.Shape;
import marlin.I;
import marlin.UC;

import java.util.ArrayList;
import java.util.HashMap;

/*
注意：abstract class。这意味着Reaction只用来register和remove Reaction
     相应的，Reaction implements I.React，但最重要的两个函数bid(),act()标志了React的特性，不会在这里定义，只会在子类定义
注意：Reaction在两处存在
     1. Reaction List owned by a Mass
     2. byShape Map，其实是一个marketplace。所有Gesture通过在marketplace中找到shape，然后通过shape bid出best Reaction.
 */
public abstract class Reaction implements I.React{
  public Shape shape;

  private static Map byShape = new Map();
  // used by Undo to restart everything.
  public static List initialReactions = new List();

  // 通过shape的名字来初始化当前Reaction，"SW-SW", "DOT", "N-N", "S-S"
  public Reaction(String shapeName){
    shape = Shape.DB.get(shapeName);
    if(shape == null){System.out.println("WTF? - Shape.DB don't know about: "+shapeName);}
  }

  /*
  通过Gesture(一个shape)来在marketplace里找到Reaction List
  然后由List里的Reaction去bid，找到最优Reaction
  注意: bid()方法是由具体子类负责实现的
  注意：这个方法在Gesture中被调用，用来找到最优的Reaction，然后执行该Reaction所对应的act()
 */
  public static Reaction best(Gesture g){return byShape.getList(g.shape).loBid(g);}

  /*
   enable()和disable()用来控制Reaction是否在Map(marketplace)中
   */
  public void enable() {
    List list = byShape.getList(shape);
    if(!list.contains(this)){list.add(this);}
  }
  public void disable() {
    List list = byShape.getList(shape);
    list.remove(this);
  }

  // used to reset for UNDO
  public static void nuke(){
    // throw away all the reactions.
    byShape = new Map();
    // 保证初始的Reaction(e.g. SimpleReaction中定义的"SW-SW")依然被加到了byShape Map里
    initialReactions.enable();
  }

  // --- List of Reactions class ---
  public static class List extends ArrayList<Reaction> {
    /*
    注意：以下三个方法---List wrapper来负责插入删除
         1. byShape Map(marketplace)中对应enable或disable
         2. Mass，即该List中插入或删除
     */
    public void addReaction(Reaction r){add(r); r.enable();}
    public void removeReaction(Reaction r){ remove(r); r.disable();}
    public void clearAll(){for(Reaction r : this){r.disable();} this.clear();}

    // enables entire list
    public void enable(){for(Reaction r : this){r.enable();}}

    /*
      通过Gesture找到该List里最优的Reaction
      can return null - list is Empty or no one wants to bid.
      注意，bid()方法是由具体Reaction子类负责实现的
     */
    public Reaction loBid(Gesture g){
      Reaction res = null;
      int bestSoFar = UC.noBid;
      for(Reaction r : this){
        int b = r.bid(g);
        if(b < bestSoFar){bestSoFar = b; res = r;}
      }
      return res;
    }
  }

  // --- Shape to Reactions class ---
  public static class Map extends HashMap<Shape, List> {

    // 注意：这里如果Map里没有该Shape，新建List后返回
    public List getList(Shape s){
      List res = get(s);
      if(res == null){res = new List(); put(s,res);}
      return res;
    }
  }
}