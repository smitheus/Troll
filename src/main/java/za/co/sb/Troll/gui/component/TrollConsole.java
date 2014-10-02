package za.co.sb.Troll.gui.component;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import za.co.sb.Troll.Troll;

@SuppressWarnings("serial")
public class TrollConsole extends JFrame
{

	private JPanel contentPane;

	public static void main(String[] args) throws Exception 
	{
		Troll.loadDbProperties();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrollConsole frame = new TrollConsole();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public TrollConsole() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		contentPane.add(new TransactionView());
		
		setContentPane(contentPane);
	}
}
