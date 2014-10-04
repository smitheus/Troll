package za.co.sb.Troll.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class ConsoleViewPanel extends JPanel 
{
	public ConsoleViewPanel() 
	{
		setBorder(new LineBorder(new Color(0, 0, 0), 3));
		setLayout(new BorderLayout(0, 0));
	}
}
