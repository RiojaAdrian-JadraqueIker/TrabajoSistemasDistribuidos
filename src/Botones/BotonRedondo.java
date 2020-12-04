package Botones;

import java.awt.*;

import javax.swing.*;


public class BotonRedondo extends JButton 
{
	public BotonRedondo(String label) 
	{
		super(label);
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(size.width, size.height);
		setPreferredSize(size);
		setContentAreaFilled(false);
	}


	protected void paintComponent(Graphics g) 
	{
		if (getModel().isArmed()) 
		{
			g.setColor(Color.lightGray);
		} else {
			g.setColor(getBackground());
		}
		g.fillOval(0, 0, getSize().width-1, getSize().height-1);
		super.paintComponent(g);
	}
	
	protected void paintBorder(Graphics g) 
	{
		g.setColor(getForeground());
		g.drawOval(0, 0, getSize().width-1, getSize().height-1);
	}

	public static void main(String[] args) {
		//Create a button with the label "Jackpot".
		JButton button = new BotonRedondo("Jackpot");
		button.setBackground(Color.WHITE);

		//Create a frame in which to show the button.
		JFrame frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().add(button);
		frame.getContentPane().setLayout(new FlowLayout());
		frame.setSize(150, 150);
		frame.setVisible(true);
	}
}

