package marlin.reaction;

import marlin.I;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Layer extends ArrayList<I.Show> implements I.Show{
  public String name;

  // 注意：这两个static变量的顺序不能改变，否则初始化Layer的时候byName还没能创建
  public static HashMap<String, Layer> byName = new HashMap<>();
  public static Layer ALL = new Layer("ALL");

  public Layer(String name){
    this.name = name;
    if(!name.equals("ALL")){ALL.add(this);}
    byName.put(name, this);
  }

  // clear out all layers in preperation for undo.
  public static void nuke(){
    for(I.Show lay : ALL){((Layer)lay).clear();}
  }

  public void show(Graphics g){for(I.Show item : this){item.show(g);}}
}