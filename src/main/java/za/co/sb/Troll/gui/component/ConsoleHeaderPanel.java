package za.co.sb.Troll.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

@SuppressWarnings("serial")
public class ConsoleHeaderPanel extends JPanel 
{
	private JLabel titleLabel;

	public ConsoleHeaderPanel() 
	{
		setBorder(new MatteBorder(3, 3, 0, 3, new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		
		JButton btnExport = new JButton("Export");
		btnExport.setEnabled(false);
		panel.add(btnExport);
		
		JButton btnPerformance = new JButton("Performance");
		btnPerformance.setEnabled(false);
		panel.add(btnPerformance);
		
		JPanel titlePanel = new JPanel();
		add(titlePanel, BorderLayout.CENTER);
		
		titleLabel = new JLabel("All Flows");
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		titlePanel.add(titleLabel);
		
		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.EAST);
	}
	
	public void setTitleLabelText(String title)
	{
		titleLabel.setText(title);
	}
}
