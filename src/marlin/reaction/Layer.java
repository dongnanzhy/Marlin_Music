package marlin.reaction;

import marlin.I;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/*
Layer代表一个可以show出自己的list of object
Layer用来hold Mass objects
  e.g. SimpleReaction.java中两个Layer：Back和Fore，后面的Box(extends Mass)都是加在了Back后面
另外：ALL包含了所有的Layer
 */
public class Layer extends ArrayList<I.Show> implements I.Show{
  public String name;

  // 注意：这两个变量的顺序不能改变，否则初始化Layer的时候byName还没能创建
  // 注意：这两个是static变量，记录程序创建的全部Layer
  public static HashMap<String, Layer> byName = new HashMap<>();
  public static Layer ALL = new Layer("ALL");

  public Layer(String name){
    this.name = name;
    if(!name.equals("ALL")){ALL.add(this);}
    byName.put(name, this);
  }

  // clear out all layers in preparation for undo.
  public static void nuke() {
    // 注意：这里并没有清空ALL，而是把ALL里的每个Layer清空(layer是list，list每个元素设为null)，ALL的长度是不变的
    for(I.Show lay : ALL){((Layer)lay).clear();}
  }

  public void show(Graphics g){for(I.Show item : this){item.show(g);}}
}