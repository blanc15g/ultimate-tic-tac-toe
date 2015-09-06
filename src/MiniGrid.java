import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MiniGrid extends JPanel {
    
    private int[][] states;
    private TicTacToeButton[][] buttons;
    private int win;
    private TicTacToeCeption parent;
    private int[] lineStart;
    private int[] lineEnd;
    private int positionX;
    private int positionY;
    boolean filled;

    public MiniGrid(TicTacToeCeption _parent, int x, int y)
    {
    	this.setLayout(new GridLayout(3,3));
        parent = _parent;
        positionX = x;
        positionY = y;
        filled = false;
        states = new int[3][3];
        buttons = new TicTacToeButton[3][3];
        lineStart = new int[2];
        lineEnd = new int[2];
        win = 0;
        
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                buttons[i][j] = new TicTacToeButton(i,j);
                this.add(buttons[i][j]);
                buttons[i][j].addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						TicTacToeButton temp = (TicTacToeButton) e.getSource();
						int move = parent.getPlayer();
						states[temp.getA()][temp.getB()] = move; 
						temp.setState(move);
						parent.nextMove(temp.getA(),temp.getB());
						parent.setLastButton(positionX, positionY, temp.getA(), temp.getB());
						if (win == 0) 
						{
							win = checkWin();
							if (win != 0){
								parent.gridWon();
								repaint();
							}
						}
					}               	
                });
            }
        }   
    }
    
    public void disable()
    {
    	for (int i = 0; i < 3; i++)
    	{
    		for (int j = 0; j < 3; j++)
    		{
    			buttons[i][j].setEnabled(false);
    		}
    	}
    }
    
    public void enable()
    {
    	boolean filledUp = true;
    	for (int i = 0; i < 3; i++)
    	{
    		for (int j = 0; j < 3; j++)
    		{
    			if (states[i][j] == 0) {
    				buttons[i][j].setEnabled(true);
    				filledUp = false;
    			}
    		}
    	}
    	if (filledUp){
    		filled = true;
    		parent.enableAll();
    	}
    }
    
    private int checkWin()
    {
    	//horizontal
    	for (int i = 0; i < 3; i++)
    	{
    		int s = states[i][0] + states[i][1] + states[i][2];
    		if (s == 3) 
    		{
    			lineStart[0] = i;
    			lineStart[1] = 0;
    			lineEnd[0] = i;
    			lineEnd[1] = 2;
    			return 1;
    		}
    		if (s == -3) {
    			lineStart[0] = i;
    			lineStart[1] = 0;
    			lineEnd[0] = i;
    			lineEnd[1] = 2;
    			return -1;
    		}
    	}
    	//vertical
    	for (int i = 0; i < 3; i++)
    	{
    		int s = states[0][i] + states[1][i] + states[2][i];
    		if (s == 3) {
    			lineStart[0] = 0;
    			lineStart[1] = i;
    			lineEnd[0] = 2;
    			lineEnd[1] = i;
    			return 1;
    		}
    		if (s == -3) 
    			{
    			lineStart[0] = 0;
    			lineStart[1] = i;
    			lineEnd[0] = 2;
    			lineEnd[1] = i;
    			return -1;
    			}
    	}
    	//main diagonal
    	int s = states[0][0] + states[1][1] + states[2][2];
    	if (s == 3) {
    		lineStart[0] = 0;
			lineStart[1] = 0;
			lineEnd[0] = 2;
			lineEnd[1] = 2;
			return 1;
    	}
		if (s == -3) {
			lineStart[0] = 0;
			lineStart[1] = 0;
			lineEnd[0] = 2;
			lineEnd[1] = 2;
			return -1;
		}
		
		//minor diagonal 
		s = states[2][0] + states[1][1] + states[0][2];
		if (s == 3) {
			lineStart[0] = 0;
			lineStart[1] = 2;
			lineEnd[0] = 2;
			lineEnd[1] = 0;
			return 1;
		}
		if (s == -3) {
			lineStart[0] = 0;
			lineStart[1] = 2;
			lineEnd[0] = 2;
			lineEnd[1] = 0;
			return -1;
		}
		return 0;
    }    
    
    public void resetState(int a, int b)
    {
    	states[a][b] = 0;
    	buttons[a][b].setText("");
    	buttons[a][b].setBackground(null);
    }
    
    public int getWin() {return win;}
    
    public boolean getFilled() {return filled;}
    
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g; 
    	int width = (int) this.getSize().width/3;
    	int height = (int) this.getSize().height/3;
    	
    	for (int i = 0; i < 3; i++){
    		for (int j = 0; j < 3; j++){
    			int x = buttons[i][j].getLocation().x;
    			int y = buttons[i][j].getLocation().y;
	        	if (states[i][j] == 1){
	        		g2.setColor(Color.YELLOW);
	        		g2.fillRect(x, y, width, height);
	        	}
	        	if (states[i][j] == -1){
	        		g2.setColor(Color.PINK);
	        		g2.fillRect(x, y, width, height);
	        	}
	        	g2.setColor(Color.BLACK);
    		}
    	}
    	
    	if (win != 0){
	        g2.setColor(Color.BLACK);       
	        try{
	        	int offsetX = (int) this.getSize().width/6;
	        	int offsetY = (int) this.getSize().height/6;
	        	Point start = buttons[lineStart[0]][lineStart[1]].getLocation();
	        	Point end = buttons[lineEnd[0]][lineEnd[1]].getLocation();
	            g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));    
	        	g2.drawLine(start.x + offsetX, start.y + offsetY, end.x + offsetX, end.y + offsetY);
	            g2.setStroke(new BasicStroke());       
	        }
	        catch(NullPointerException ex){
	        	System.out.println(ex.getMessage());
	        	ex.printStackTrace();
	        }
    	}
    }
    	
}
