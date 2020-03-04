import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TracingPaper extends Frame implements MouseListener, MouseMotionListener, WindowListener, ActionListener {
  int x, y, red, green, blue, clickCounter;
  String str = "", clickedButton = "";
  Label coordinates, mouse_activity;
  TextField functionName, colorR, colorG, colorB;
  Checkbox fill;

  ArrayList<Integer> xArr, yArr, xAll, yAll;
  BufferedWriter writer;
  Scanner input;

  public TracingPaper() {
    clickCounter = 0;
    xArr = new ArrayList<Integer>();
    yArr = new ArrayList<Integer>();
    xAll = new ArrayList<Integer>();
    yAll = new ArrayList<Integer>();

    input = new Scanner(System.in);
    // open empty file for writing;
    try {
      writer = new BufferedWriter(new FileWriter("./trace.txt", true)); // Set true for append mode
    } catch (Exception exception) {
      System.out.println("FILE IO EXCEPTION");
    }

    Panel footer = new Panel();
    Panel shapes = new Panel();
    // button setup .---------------------------
    Button oval, rect, arc, line, poly, draw, set, save, clear;
    oval = new Button("oval");
    rect = new Button("rect");
    arc = new Button("arc");
    line = new Button("line");
    poly = new Button("poly");
    draw = new Button("DRAW");
    set = new Button("SET");
    save = new Button("SAVE");
    clear = new Button("CLEAR");

    oval.addActionListener(this);
    rect.addActionListener(this);
    arc.addActionListener(this);
    line.addActionListener(this);
    poly.addActionListener(this);
    draw.addActionListener(this);
    set.addActionListener(this);
    save.addActionListener(this);
    clear.addActionListener(this);

    // button setup ends---------------------------

    functionName = new TextField("Name");
    colorR = new TextField("0");
    colorG = new TextField("0");
    colorB = new TextField("0");
    fill = new Checkbox("Fill", false);

    footer.setPreferredSize(new Dimension(800, 30));
    shapes.setPreferredSize(new Dimension(800, 30));
    shapes.setBackground(Color.LIGHT_GRAY);

    shapes.add(fill);
    shapes.add(oval);
    shapes.add(rect);
    shapes.add(arc);
    shapes.add(line);
    shapes.add(poly);
    shapes.add(new Label("function:"));
    shapes.add(functionName);
    shapes.add(draw);
    shapes.add(new Label("Color RGB:"));
    shapes.add(colorR);
    shapes.add(colorG);
    shapes.add(colorB);
    shapes.add(set);
    shapes.add(save);
    shapes.add(clear);

    coordinates = new Label("X,Y: . . . . .");
    mouse_activity = new Label("Mouse Activity: . . .");

    footer.setLayout(new BorderLayout());
    footer.add(coordinates, BorderLayout.WEST);
    footer.add(mouse_activity, BorderLayout.EAST);
    footer.add(
        new Label(
            "Press Alt+F4 to QUIT ||| Check CONSOLE FOR HINTS ||| OutPut on trace.txt ||| Mouse drag to clear traces"),
        BorderLayout.CENTER);
    footer.setBackground(Color.LIGHT_GRAY);
    this.add(footer, BorderLayout.SOUTH);
    this.add(shapes, BorderLayout.NORTH);

    setUndecorated(true);
    setOpacity(0.660F);

    addMouseListener(this);
    addMouseMotionListener(this);
    addWindowListener(this);

    setSize(900, 600);
    setVisible(true);
  }

  // override ActionListener abstract methods
  public void actionPerformed(ActionEvent e) {
    clickedButton = e.getActionCommand();
    System.out.println("clicked button:" + clickedButton);

    try {
      if (clickedButton == "DRAW") {
        writer.newLine();
        writer.write("void " + functionName.getText() + "(int x, int y ,Graphics g){");
        writer.write("Graphics2D g2D = (Graphics2D) g;");
      } else if (clickedButton == "SET") {
        String color = colorR.getText() + "," + colorG.getText() + "," + colorB.getText();
        writer.newLine();
        writer.write("g.setColor(new Color(" + color + "));");

      } else if (clickedButton == "poly" && !(fill.getState())) {
        StringBuilder xPoints = new StringBuilder("" + xArr);
        StringBuilder yPoints = new StringBuilder("" + yArr);
        xPoints.deleteCharAt(0);
        xPoints.deleteCharAt(xPoints.length() - 1);
        yPoints.deleteCharAt(0);
        yPoints.deleteCharAt(yPoints.length() - 1);
         //Post-fixing +x and +y to each coordinate
         String xValues = xPoints.toString().replace(",", "+x,");
         String yValues = yPoints.toString().replace(",", "+y,");

        writer.newLine();
        writer.write("int xpoints[]={" + xValues + "+x};");
        writer.newLine();
        writer.write("int ypoints[]={" + yValues + "+y};");
        writer.newLine();
        writer.write("int points=" + yArr.size() + ";");
        writer.newLine();
        writer.write("g.drawPolygon(xpoints,ypoints,points);");
      } else if (clickedButton == "poly" && fill.getState()) {
        StringBuilder xPoints = new StringBuilder("" + xArr);
        StringBuilder yPoints = new StringBuilder("" + yArr);
        xPoints.deleteCharAt(0);
        xPoints.deleteCharAt(xPoints.length() - 1);
        yPoints.deleteCharAt(0);
        yPoints.deleteCharAt(yPoints.length() - 1);
        //Prefixing +x and +y to each coordinate
        String xValues = xPoints.toString().replace(",", "+x,");
        String yValues = yPoints.toString().replace(",", "+y,");

        writer.newLine();
        writer.write("int xpoints[]={" + xValues + "+x};");
        writer.newLine();
        writer.write("int ypoints[]={" + yValues + "+y};");
        writer.newLine();
        writer.write("int points=" + yArr.size() + ";");
        writer.newLine();
        writer.write("g.fillPolygon(xpoints,ypoints,points);");
      } else if (clickedButton == "oval" && !(fill.getState())) {

        try {
          Integer xPoint = xArr.get(0);
          Integer yPoint = yArr.get(0);
          int width = Math.abs(xArr.get(1) - xPoint);
          int height = Math.abs(yArr.get(1) - yPoint);
          String values = xPoint + "+x ," + yPoint + "+y ," + width + "," + height;
          writer.newLine();
          writer.write("g.drawOval(" + values + ");");

        } catch (Exception exception) {
          System.out.println("Insufficient value of Oval: " + exception);
        }

      } else if (clickedButton == "oval" && fill.getState()) {

        try {
          Integer xPoint = xArr.get(0);
          Integer yPoint = yArr.get(0);
          int width = Math.abs(xArr.get(1) - xPoint);
          int height = Math.abs(yArr.get(1) - yPoint);
          String values = xPoint + "+x ," + yPoint + "+y ," + width + "," + height;
          writer.newLine();
          writer.write("g.fillOval(" + values + ");");

        } catch (Exception exception) {
          System.out.println("Insufficient value of FillOval: " + exception);
        }
      } else if (clickedButton == "rect" && !(fill.getState())) {
        try {
          Integer xPoint1 = xArr.get(0);
          Integer yPoint1 = yArr.get(0);
          int width = Math.abs(xArr.get(1) - xPoint1);
          int height = Math.abs(yArr.get(1) - yPoint1);

          String values = xPoint1 + "+x," + yPoint1 + "+y," + width + "," + height;
          writer.newLine();
          writer.write("g.drawRect(" + values + ");");

        } catch (Exception exception) {
          System.out.println("Insufficient value of Rectangle: " + exception);
        }
      } else if (clickedButton == "rect" && fill.getState()) {
        try {
          Integer xPoint1 = xArr.get(0);
          Integer yPoint1 = yArr.get(0);
          int width = Math.abs(xArr.get(1) - xPoint1);
          int height = Math.abs(yArr.get(1) - yPoint1);

          String values = xPoint1 + "+x," + yPoint1 + "+y," + width + "," + height;
          writer.newLine();
          writer.write("g.fillRect(" + values + ");");

        } catch (Exception exception) {
          System.out.println("Insufficient value of Fill Rectangle: " + exception);
        }
      } else if (clickedButton == "line") {
        try {
          Integer xPoint1 = xArr.get(0);
          Integer yPoint1 = yArr.get(0);
          Integer xPoint2 = xArr.get(1);
          Integer yPoint2 = yArr.get(1);

          String values = xPoint1 + "+x," + yPoint1 + "+y," + xPoint2 + "+x," + yPoint2 + "+y";
          writer.newLine();
          writer.write("g.drawLine(" + values + ");");

        } catch (Exception exception) {
          System.out.println("Insufficient value of Line: " + exception);
        }
      } else if (clickedButton == "arc" && !(fill.getState())) {
        try {
          Integer xPoint = xArr.get(0);
          Integer yPoint = yArr.get(0);
          int width = Math.abs(xArr.get(1) - xPoint);
          int height = Math.abs(yArr.get(1) - yPoint);

          System.out.println("enter startAngle and arcAngle to draw Arc");
          int startAngle = input.nextInt();
          int arcAngle = input.nextInt();

          String values = xPoint + "+x," + yPoint + "+y," + width + "," + height + "," + startAngle + "," + arcAngle;
          writer.newLine();
          writer.write("g.drawArc(" + values + ");");

        } catch (Exception exception) {
          System.out.println("Insufficient value of arc: " + exception);
        }
      } else if (clickedButton == "arc" && fill.getState()) {
        try {
          Integer xPoint = xArr.get(0);
          Integer yPoint = yArr.get(0);
          int width = Math.abs(xArr.get(1) - xPoint);
          int height = Math.abs(yArr.get(1) - yPoint);

          System.out.println("enter startAngle and arcAngle to draw Arc");
          int startAngle = input.nextInt();
          int arcAngle = input.nextInt();

          String values = xPoint + "+x," + yPoint + "+y," + width + "," + height + "," + startAngle + "," + arcAngle;
          writer.newLine();
          writer.write("g.fillArc(" + values + ");");

        } catch (Exception exception) {
          System.out.println("Insufficient value of FillArc: " + exception);
        }
      }

      if (clickedButton == "SAVE") {
        writer.newLine();
        writer.write("//}//----------- END SESSION -------------");
        writer.close();
        System.out.println("Changes to file have been saved");
        // open empty file for writing;
        try {
          writer = new BufferedWriter(new FileWriter("./trace.txt", true)); // Set true for append mode
        } catch (Exception exception) {
          System.out.println("FILE IO EXCEPTION");
        }
      }
      if (clickedButton == "CLEAR") {
        clickCounter = 0;
        clickedButton = "";
        xAll.addAll(xArr);
        yAll.addAll(yArr);
        xArr = new ArrayList<Integer>();
        yArr = new ArrayList<Integer>();
        System.out.println("xArray ,yArray, clickCounter, clickedButton have been cleared");
      }

    } catch (Exception exception) {
      System.out.println("IOException Occoured:" + exception);
    }
  }

  // override WindowListner seven abstract methods //withoud use
  public void windowClosing(WindowEvent e) {
    try {
      writer.close();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    dispose();
  }
  public void windowDeactivated(WindowEvent e){}
  public void windowActivated(WindowEvent e){}
  public void windowDeiconified(WindowEvent e){}
  public void windowIconified(WindowEvent e){}
  public void windowClosed(WindowEvent e){}
  public void windowOpened(WindowEvent e){}
  
                          // override MouseListener five abstract methods
  public void mousePressed(MouseEvent e)
  {
    x = e.getX();
    y = e.getY();
    str = "Mouse Pressed";
    coordinates.setText( x +","+ y );
    mouse_activity.setText(str);
    repaint();
  }
  public void mouseReleased(MouseEvent e)
  {
    x = e.getX();
    y = e.getY();
    str = "Mouse Released";
    coordinates.setText(x +","+ y);
    mouse_activity.setText(str);
    repaint();
  }
  public void mouseClicked(MouseEvent e)
  {
    clickCounter ++ ;
    //System.out.println(e.getClickCount());
    x = e.getX();
    y = e.getY();
    str = "Mouse Clicked";
    coordinates.setText(x +","+ y);
    mouse_activity.setText(str);
    System.out.println("P"+clickCounter+"("+x+","+y+")"); //fir printing the cordinates

    xArr.add(x); 
    yArr.add(y); 

    repaint();
  }
  public void mouseEntered(MouseEvent e)
  {
    x = e.getX();
    y = e.getY();
    str = "Mouse Entered";
    coordinates.setText(x +","+ y);
    mouse_activity.setText(str);
    repaint();
  }
  public void mouseExited(MouseEvent e)
  {
    System.out.println(clickCounter+":"+xArr+":"+yArr); 
    x = e.getX();
    y = e.getY();
    str = "Mouse Exited";
    coordinates.setText(x +","+ y);
    mouse_activity.setText(str);
    repaint();
  }
                          // override MouseMotionListener two abstract methods
  public void mouseMoved(MouseEvent e)
  {
    x = e.getX();
    y = e.getY();
    str = "Mouse Moved";
    coordinates.setText(x +","+ y);
    mouse_activity.setText(str);
    repaint();
  }
  public void mouseDragged(MouseEvent e)
  {
    x = e.getX();
    y = e.getY();

    xAll = new ArrayList<Integer>();
    yAll = new ArrayList<Integer>();
    System.out.println("All Tracing Point Cleared");

    str = "Mouse dragged";
    coordinates.setText(x +","+ y);
    mouse_activity.setText(str);
    repaint();
  }
  public void paint(Graphics g)
  {
    g.setFont(new Font("Monospaced", Font.BOLD, 15));
    try{
    red=Integer.parseInt(colorR.getText());
    green=Integer.parseInt(colorG.getText());
    blue=Integer.parseInt(colorB.getText());
    g.setColor(new Color(red,green,blue));
    g.fillRect(x+20, y+20, 10, 10);         // gives the bullet
    
    //set stroke widht
    g.setColor(Color.BLACK);
    Graphics2D g2D = (Graphics2D) g;      
    g2D.setStroke(new BasicStroke(20F));  
    g2D.drawLine(20, 50, 50, 50);

    // for creating tracing marks
    g2D.setStroke(new BasicStroke(1F));
    g.setColor(Color.RED);
      for(int i=0; i< xArr.size() ;i++){
        g.drawOval(xArr.get(i)-2,yArr.get(i)-2,3,3);
      }

    if(xAll.size()>0 && yAll.size()>0){
      g.setColor(Color.BLACK);
      for(int i=0; i< xAll.size() ;i++){
        g.drawOval(xAll.get(i)-2,yAll.get(i)-2,3,3);
        if(i<xAll.size()-1)
        g.drawLine(xAll.get(i),yAll.get(i),xAll.get(i+1),yAll.get(i+1));
      }
    }

    //g.drawString(x + "," + y,  x+10, y -10);  // displays the x and y position
    //g.drawString(str, x+10, y+20);            // displays the action performed
    }catch(IllegalArgumentException e){
        System.out.println("Invalid Color WARNING: RGB Ranges should be within 0-255");
    }catch(Exception exception){
       System.out.println("Some Exception:"+exception);
    }

  }

  public static void main(String args[])
  {
    new TracingPaper();
  }
}