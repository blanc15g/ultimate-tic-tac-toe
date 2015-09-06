import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JButton;


public class TicTacToeButton extends JButton{
	
	private int a;
	private int b;
	private int state;
	
	public TicTacToeButton(int _a, int _b)
	{
		a = _a;
		b = _b;
		state = 0;
		this.setOpaque(false);
	}
	
	public int getA() {return a;}
	
	public int getB() {return b;}
	
	public void setState(int _state)
	{
		state = _state;
		setEnabled(false);
		this.setFont(new Font("SERIF", Font.BOLD,40));
		if (state == 1)
		{
			this.setText("X");
		}
		else
		{
			this.setText("O");
		}
		repaint();
		
	}
}
