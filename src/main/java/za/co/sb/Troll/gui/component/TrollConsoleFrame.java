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
	
	private JPanel viewPanel;
	private JPanel headerPanel;
	private JPanel footerPanel;
	
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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		viewPanel = new ConsoleViewPanel();
		transactionViewPanel = new TransactionViewPanel();
		transactionToolsPanel = new TransactionToolsPanel(transactionViewPanel);
		
		viewPanel.add(transactionToolsPanel, BorderLayout.NORTH);
		viewPanel.add(transactionViewPanel, BorderLayout.CENTER);
		
		contentPane.add(viewPanel, BorderLayout.CENTER);
		
		headerPanel = new ConsoleHeaderPanel();
		contentPane.add(headerPanel, BorderLayout.NORTH);
		
		footerPanel = new ConsoleFooterPanel();
		contentPane.add(footerPanel, BorderLayout.SOUTH);
		
		setContentPane(contentPane);
	}
}
