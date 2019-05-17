package marlin;

import java.awt.*;

// UC = Universal Constants
// In a class US, typically define things with rather long names - user friendly.
public class UC {
  public static final int mainWindowWidth = 1000;
  public static final int mainWindowHeight = 800;

  public static final int  defaultBufferSize = 1000;
  public static final int  inkBufferMax = 1500;
  // subsample size
  public static final int normSampleSize = 25;
  public static final Color inkColor = Color.BLUE;
  // max size for norm
  public static final int normCoordMax = 300;
  // threshold for a match of norm
  public static final int noMatchDist = 300000;
  // threshold for a match of DOT
  public static final int dotThreshold = 20;
  // file path to store Shapes DB
  public static final String fullPathToShapeDB = "shapes.db";

  public static Color mainWindowBackgroundColor = Color.WHITE;
  public static Color dialogBackgroundColor = new Color(0,0,10); // light blue
}
