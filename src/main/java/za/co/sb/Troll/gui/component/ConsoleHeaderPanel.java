package za.co.sb.Troll.gui.component;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Font;

@SuppressWarnings("serial")
public class ConsoleHeaderPanel extends JPanel {

	public ConsoleHeaderPanel() 
	{
		setBorder(new MatteBorder(3, 3, 0, 3, new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		
		JButton btnExport = new JButton("Export");
		panel.add(btnExport);
		
		JButton btnPerformance = new JButton("Performance");
		panel.add(btnPerformance);
		
		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.CENTER);
		
		JLabel lbLabel = new JLabel("Country");
		lbLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		panel_1.add(lbLabel);
		
		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.EAST);
	}
}
