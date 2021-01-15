/*This program allows the user to draw rectangles on a coordinate plane and displays information about the rectangles and 
fills in the intersection. The top left corner is recorded as the point where it is on the screen, but the toString displays the value that it
would have on the coordinate plane.*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Rectangle extends JPanel{
  //1.
  private int left;
  private int bottom;
  private int width;
  private int height;
  
  private int x1,y1,x2,y2;		//starting and ending points of the rectangles 
  private static boolean click = false;		//determines if the user is clicking	
  private static int rCount = 0;		//counts the number of rectangles drawn
  private static int sx1[] = new int[2], sy1[] = new int[2], sx2[] = new int[2], sy2[] = new int[2];	//saving coordinates of the two rectangles
  
  //2.
  public Rectangle() {
    left = 0;
    bottom = 0;
    width = 0;
    height = 0;
    
    x1 = y1 = x2 = y2 = 0; 
	MyMouseListener l = new MyMouseListener();  //this is so that the user can draw their own rectangles by dragging and releasing their mouse
	addMouseListener(l);
	addMouseMotionListener(l);
  }
  
  //3.
  public Rectangle(int l, int b, int w, int h) {
    left = l;
    bottom = b;
    width = w;
    height = h;
    if (w < 0) {
      width = 0;
    }
    if (h < 0) {
      height = 0;
    }
  }
  
  //4.
  public void set(int l, int b, int w, int h) {
    this.left = l;
    this.bottom = b;
    this.width = w;
    this.height = h;
    if (w < 0) {
      this.width = 0;
    }
    if (h < 0) {
      this.height = 0;
    }
  }
  
  //5.
  public String toString () {
    String s = "base: (" + (this.left - 310) + "," + (270 - this.bottom) + ") w:" + this.width + " h:" + this.height;
    //the (left - 310) and (270 - bottom) is so that the displayed information of the coordinates will be relative to the x and y axis that I created
    return s;
  }
  
  //6.
  public int area () {
    int a = 0;
    a = this.width * this.height;
    return a;
  }
  
  //7.
  public int perimeter () {
    int p = 0;
    if (this.width == 0) {
      p = this.height;
    }
    else if (this.height == 0) {
      p = this.width;
    }
    else {
      p = 2 * (this.width + this.height);
    }
    return p;
  }
  
  //8.
  public static Rectangle intersection(Rectangle a, Rectangle b) {
    Rectangle c = new Rectangle();
    int X1 = a.left + a.width;
    int X2 = b.left + b.width;
    int Y1 = a.bottom + a.height;
    int Y2 = b.bottom + b.height;
    int xLeft = Math.max(a.left, b.left);
    int xRight = Math.min(X1, X2);
    int yBottom = Math.max(a.bottom, b.bottom);
    int yTop = Math.min(Y1, Y2);
    if (xRight >= xLeft && yTop >= yBottom) {
      c.left = xLeft;
      c.width = xRight - xLeft;
      c.bottom = yBottom;
      c.height = yTop - yBottom;
      return c;
    }
    else {
      return c;
    }
    
  }
  
  //9.
  public static int totalPerimeter(Rectangle a, Rectangle b) {
    int p = 0;
    Rectangle c = intersection(a,b);
    boolean isLine = false;
	if (a.height != 0 && a.width != 0 && b.height != 0 && b.width != 0) {	//checks if the rectangles are lines 
		isLine = true;
	}
	if (c.height == 0 && isLine || c.width == 0 && isLine) {		
		p = a.perimeter() + b.perimeter() - (c.perimeter()*2);
	}
	else {
		p = a.perimeter() + b.perimeter() - c.perimeter();	
	}
	return p;
  }
  
  //10.
  public boolean contains(Rectangle a) {
    boolean b = false;
    if (this.left == a.left && this.bottom == a.bottom) {
      if ((this.left + this.width >= a.left + a.width) && (this.bottom + this.height >= a.bottom + a.height)) {
        b = true;
      }
    }
    return b;
  }
  
  //Total area of the two rectangles
  public static int totalArea(Rectangle a, Rectangle b) {
		int tA = a.area() + b.area() - intersection(a,b).area();
	return tA;
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setSize(580, 600);
    frame.setContentPane(new Rectangle());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
  
  public class MyMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) 	{
			click = true;
			x1 = e.getX();		//sets the starting point for the rectangle that is drawn 
			y1 = e.getY();
			if (rCount == 2) {	//if there are two rectangles 
				for (int i = 0; i < sx1.length; i++)	{
					sx1[i] = 0;		//the coordinates of the starting and ending points for each rectangle will be reset 
					sy1[i] = 0;				
					sx2[i] = 0;
					sy2[i] = 0;
				}
				rCount = 0;	
			}
		}
		
		public void mouseDragged(MouseEvent e) {
			x2 = e.getX();		//sets ending point for rectangle while user is dragging their mouse
			y2 = e.getY();
			repaint();
		}
		
		public void mouseReleased(MouseEvent e)	{
			click = false;
			x2 = e.getX();		//final ending point for rectangle is when the user releases mouse
			y2 = e.getY();
			repaint();
			
			sx1[rCount] = x1;	//sets and saves starting and ending points of the rectangle created 
			sy1[rCount] = y1;
			sx2[rCount] = x2;
			sy2[rCount] = y2;
			rCount++;			//after a rectangle is drawn, the counter goes up
		}
	}

	public void drawRectangle(Graphics g, int x,int y,int xi,int yi) {
		int px = Math.min(x, xi);		
	    int py = Math.min(y, yi);	
	    /*py is used to draw the rectangle while pb 
	    is used to store the information of the base of the rectangle since it starts drawing the rectangle from the top and not the bottom*/
	    int pb = Math.max(y, yi);
	    int pw = Math.abs(x-xi);		
	    int ph = Math.abs(y-yi);		
	    Rectangle r = new Rectangle(px, pb, pw, ph);
	    g.drawRect(px, py, pw, ph);		//draw rectangle 
	    if(rCount == 0 && click) {
	    	g.drawString("Rectangle 1: " + r.toString(), 10, 20);		//prints out rectangles
	    }
	    if(rCount == 1 && click) {
	    	g.drawString("Rectangle 2: " + r.toString(), 10, 40);
	    }
	}
	
	public void fillRectangle(Graphics g,Rectangle r) {
		g.setColor(Color.pink);
		g.fillRect(r.left,r.bottom,r.width, r.height);		
	}
	
	public void paint(Graphics g)	{
		super.paint(g);
		g.setColor(Color.black);
		g.drawLine(60, 270, 560, 270);		//draws the x axis
		g.drawLine(310, 20, 310, 520);		//draws the y axis
		g.drawString("-250", 45, 285);
		g.drawString("250", 550, 285);
		g.drawString("250", 285, 25);
		g.drawString("-250", 277, 525);
		drawRectangle(g, x1, y1, x2, y2);	//draws the rectangle 
		
		Rectangle r1 = new Rectangle();		//create two rectangles and display the two rectangles on the screen
		Rectangle r2 = new Rectangle();
		r1.set(Math.min(sx1[0], sx2[0]), Math.min(sy1[0], sy2[0]), Math.abs(sx1[0]-sx2[0]), Math.abs(sy1[0]-sy2[0]));
		g.drawRect(r1.left, r1.bottom, r1.width, r1.height);
		r2.set(Math.min(sx1[1], sx2[1]), Math.min(sy1[1], sy2[1]), Math.abs(sx1[1]-sx2[1]), Math.abs(sy1[1]-sy2[1]));
		g.drawRect(r2.left, r2.bottom, r2.width, r2.height);
		
		//prints out information about the rectangles 
		if(rCount == 1) {
			r1.set(r1.left, Math.max(sy1[0], sy2[0]), r1.width, r1.height);
			g.drawString("Rectangle 1: " + r1.toString(),10,20);
		}
		if(rCount == 2)	{		
			fillRectangle(g, intersection(r1, r2)); 	//colours intersection pink
			g.setColor(Color.black);
			r1.set(r1.left, Math.max(sy1[0], sy2[0]), r1.width, r1.height); //bottom is a max since the top left corner is zero, and as you go down the "y" value increases
			r2.set(r2.left, Math.max(sy1[1], sy2[1]), r2.width, r2.height);
			g.drawString("Rectangle 1: " + r1.toString(), 10, 20);
			g.drawString("Rectangle 2: " + r2.toString(), 10, 40);
			r1.set(r1.left, Math.min(sy1[0], sy2[0]), r1.width, r1.height); 
			r2.set(r2.left, Math.min(sy1[1], sy2[1]), r2.width, r2.height);
			Rectangle c = intersection(r1, r2);
			c.set(c.left, (c.bottom + c.height), c.width, c.height);  //same thing as previous comment
			g.drawString("Intersection: " + c.toString(), 10, 60);
			g.drawString("Total perimeter: " + totalPerimeter(r1, r2), 10, 80);
			g.drawString("Total area: " + totalArea(r1, r2), 10, 100);
		}
	}
 }
