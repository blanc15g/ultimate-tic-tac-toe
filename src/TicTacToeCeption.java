import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class TicTacToeCeption extends JFrame implements Runnable{

	private Container c;
	int whoseMove;
	private MiniGrid[][] MainGrid;
	private int[][] states;
	private int lastButton[];
	private boolean undoAble;
	private int numTurns;
	private int wholeGrid; //stores how many moves have passed until the wholeGrid was open
	static TicTacToeCeption t = new TicTacToeCeption();
	
	public TicTacToeCeption(){
		whoseMove = 1;
		wholeGrid = 1;
		undoAble = false;
		lastButton = new int[4];
		states = new int[3][3];
		MainGrid = new MiniGrid[3][3];
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				MainGrid[i][j] = new MiniGrid(this,i,j);
			}
		}
	}

	public void makeMenus()
	{JMenuBar ticMenu = new JMenuBar();
    setJMenuBar(ticMenu);
    JMenu ticFile = new JMenu("File");
    ticMenu.add(ticFile);
    JMenuItem ticFileNewGame = new JMenuItem("New Game");
    ticFile.add(ticFileNewGame);
    ticFileNewGame.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e)
    	{
    		TicTacToeCeption newGame = new TicTacToeCeption();
    		javax.swing.SwingUtilities.invokeLater(newGame);
    		t.setVisible(false);
    		t.dispose();
    	}
    });
    JMenuItem ticHelp = new JMenuItem("Help");
    ticFile.add(ticHelp);
    ticHelp.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent e)
    	{
    		JFrame helpFrame = new JFrame("HELP");
    		String help = new String("This game is essentially a large tic tac toe game wherein tic tac toe games exist in each individual corner and the box chosen determines the next player's area/corner to choose. Essentially what happens is Player X chooses any space on the grid and then Player O continues with the box chosen. The location relative to each minigrid chosen by each player determines what grid the next player's move will be sent to. For example if Player x chooses the middle left grid space in the middle square of the Main Grid, Player O will be sent to the Middle Left Main Grid to make their move and so on. Created by Guy Blanc and Adithya Iyengar. v1.0");
    		JTextArea helpArea = new JTextArea(help);
    		helpArea.setLineWrap(true);
    		helpArea.setWrapStyleWord(true);
    		helpArea.setEditable(false);
    		helpFrame.getContentPane().add(helpArea);
    		helpFrame.setSize(500,500);
    		helpFrame.setVisible(true);
    	}
    });
    JMenuItem ticFileQuit = new JMenuItem("Quit");
    ticFile.add(ticFileQuit);
    ticFileQuit.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent e)
    	{
    		System.exit(0);
    	}
    });
    JButton ticUndo = new JButton("Undo");
    ticMenu.add(ticUndo);
    ticUndo.addActionListener(new ActionListener(){
    	public void actionPerformed(ActionEvent e)
    	{
    		if (undoAble)
    		{
    			getPlayer();
    			undoAble = false;
    			MainGrid[lastButton[0]][lastButton[1]].resetState(lastButton[2], lastButton[3]);
    			if (wholeGrid == 2)
    			{
    				enableAll();
    				wholeGrid = 1;
    			}
    			else
    				nextMove(lastButton[0],lastButton[1]);
    		}
    	}
    });
	}
	
	public int getPlayer(){
		whoseMove = -1*whoseMove;
		return (-1*whoseMove);
	}
	
	@Override
	public void run() {
		c = getContentPane();
	    c.setLayout(new GridLayout(3,3));
		makeMenus();
	    for(int i = 0; i < 3; i++){
	    	for (int j=0; j < 3; j++){
	    	    MainGrid[i][j].setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 4));
	    		c.add(MainGrid[i][j]);
	    	}
	    }
	    setTitle("TicTacToeCeption");
	    setSize(800,800);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setResizable(false);
	    setVisible(true);  
	}
	
	public void nextMove(int a, int b)
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				MainGrid[i][j].disable();
			}
		}
		MainGrid[a][b].enable();
		wholeGrid++;
		numTurns++;
		if (numTurns == 81){
			if (checkWin() == 0)
				JOptionPane.showMessageDialog(null, "DRAW", "GAMEOVER", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void enableAll(){
		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				if (!MainGrid[i][j].getFilled()){
					MainGrid[i][j].enable();
				}
			}
		}
	}
		
	public void gridWon()
	{
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				states[i][j] = MainGrid[i][j].getWin();
			}
		}
		if (checkWin() != 0)
		{
			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 3; j++)
				{
					MainGrid[i][j].disable();
				}
			}
			if (checkWin() == 1)
				JOptionPane.showMessageDialog(null, "X WON", "GAMEOVER", JOptionPane.INFORMATION_MESSAGE);
			else if (checkWin() == -1)
				JOptionPane.showMessageDialog(null, "Y WON", "GAMEOVER", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private int checkWin()
    {
    	//horizontal
    	for (int i = 0; i < 3; i++)
    	{
    		int s = states[i][0] + states[i][1] + states[i][2];
    		if (s == 3) return 1;
    		if (s == -3) return -1;
    	}
    	//vertical
    	for (int i = 0; i < 3; i++)
    	{
    		int s = states[0][i] + states[1][i] + states[2][i];
    		if (s == 3) return 1;
    		if (s == -3) return -1;
    	}
    	//main diagonal
    	int s = states[0][0] + states[1][1] + states[2][2];
    	if (s == 3) return 1;
		if (s == -3) return -1;
		
		//minor diagonal 
		s = states[2][0] + states[1][1] + states[0][2];
		if (s == 3) return 1;
		if (s == -3) return -1;
		return 0;
    }
	
	public void setLastButton(int a, int b, int c, int d)
	{
		lastButton[0] = a;
		lastButton[1] = b;
		lastButton[2] = c;
		lastButton[3] = d;
		undoAble = true;
	}
	
	public static void main(String[] args) {
	    javax.swing.SwingUtilities.invokeLater(t);
	}

}
