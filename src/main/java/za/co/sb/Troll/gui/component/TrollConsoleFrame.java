package za.co.sb.Troll.gui.component;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import za.co.sb.Troll.Troll;
import za.co.sb.Troll.gui.component.transaction.TransactionToolsPanel;
import za.co.sb.Troll.gui.component.transaction.TransactionViewPanel;

@SuppressWarnings("serial")
public class TrollConsoleFrame extends JFrame
{
	private JPanel contentPane;
	
	private ConsoleViewPanel viewPanel;
	private ConsoleHeaderPanel headerPanel;
	private ConsoleFooterPanel footerPanel;
	
	private TransactionToolsPanel transactionToolsPanel;
	private TransactionViewPanel transactionViewPanel;

	public static void main(String[] args) throws Exception 
	{
		Troll.loadDbProperties();
		
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					TrollConsoleFrame frame = new TrollConsoleFrame();
					frame.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	public TrollConsoleFrame() throws SQLException 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 1200, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// Header panel
		headerPanel = new ConsoleHeaderPanel();
		contentPane.add(headerPanel, BorderLayout.NORTH);
		
		// View panel
		viewPanel = new ConsoleViewPanel();
		transactionViewPanel = new TransactionViewPanel();
		transactionToolsPanel = new TransactionToolsPanel(transactionViewPanel, headerPanel);
		
		viewPanel.add(transactionToolsPanel, BorderLayout.NORTH);
		viewPanel.add(transactionViewPanel, BorderLayout.CENTER);
		
		contentPane.add(viewPanel, BorderLayout.CENTER);
				
		// Footer panel
		footerPanel = new ConsoleFooterPanel();
		contentPane.add(footerPanel, BorderLayout.SOUTH);
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		setContentPane(contentPane);
	}
}
