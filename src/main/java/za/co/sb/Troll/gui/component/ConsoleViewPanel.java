package za.co.sb.Troll.gui.component;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class ConsoleViewPanel extends JPanel 
{
	public ConsoleViewPanel() 
	{
		setBorder(new LineBorder(new Color(0, 0, 0), 3));
		setLayout(new BorderLayout(0, 0));
	}
}
