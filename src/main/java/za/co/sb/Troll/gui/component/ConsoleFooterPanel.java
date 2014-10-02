package za.co.sb.Troll.gui.component;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

@SuppressWarnings("serial")
public class ConsoleFooterPanel extends JPanel {

	public ConsoleFooterPanel() 
	{
		setBorder(new MatteBorder(0, 3, 3, 3, (Color) new Color(0, 0, 0)));
		
		JLabel lbLabel = new JLabel("TODO FOOTER");
		add(lbLabel);
	}
}
