package za.co.sb.Troll.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import za.co.sb.Troll.gui.component.export.ExportDialog;

@SuppressWarnings("serial")
public class ConsoleHeaderPanel extends JPanel implements ActionListener
{
	private static final String EXPORT_ACTION_COMMAND = "EXPORT";
	
	private TrollConsoleFrame trollConsoleFrame;
	
	private JButton btnExport; 
	private JLabel titleLabel;

	public ConsoleHeaderPanel(TrollConsoleFrame trollConsoleFrame) 
	{
		super();
		
		this.trollConsoleFrame = trollConsoleFrame;
		
		setBorder(new MatteBorder(3, 3, 0, 3, new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.WEST);
		
		btnExport = new JButton("Export");
		btnExport.setEnabled(false);
		btnExport.setActionCommand(EXPORT_ACTION_COMMAND);
		btnExport.addActionListener(this);
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
	
	public void setBtnExportEnabled(boolean enabled)
	{
		btnExport.setEnabled(enabled);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		
		if (actionCommand.equals(EXPORT_ACTION_COMMAND))
		{
			ExportDialog dialog = new ExportDialog(trollConsoleFrame.getTransactionViewPanel().getSelectedTransactionViewItemDto());
			
        	try 
        	{
        		dialog.setVisible(true);
        	}
			catch (Exception ex) 
    		{
				// TODO
				ex.printStackTrace();
    		}
		}
	}
}
