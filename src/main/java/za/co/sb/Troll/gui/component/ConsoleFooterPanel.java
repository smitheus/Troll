package za.co.sb.Troll.gui.component;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.MatteBorder;

import za.co.sb.Troll.dao.DbStatusDao;
import za.co.sb.Troll.gui.util.ImageUtils;

@SuppressWarnings("serial")
public class ConsoleFooterPanel extends JPanel 
{
	private DbStatusPanel dbStatusPanel;
	
	public ConsoleFooterPanel() 
	{
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		setBorder(new MatteBorder(0, 3, 3, 3, (Color) new Color(0, 0, 0)));
		
		dbStatusPanel = new DbStatusPanel();
		add(dbStatusPanel);
	}
}

@SuppressWarnings("serial")
class DbStatusPanel extends JPanel
{
	public static final String DB_ONLINE_TEXT = "DB Online";
	public static final String DB_OFFLINE_TEXT = "DB Offline";
	
	private static final Image ONLINE_IMAGE = Toolkit.getDefaultToolkit().getImage(ImageUtils.class.getClassLoader().getResource(ImageUtils.ONLINE_IMAGE));
	private static final Image OFFLINE_IMAGE = Toolkit.getDefaultToolkit().getImage(ImageUtils.class.getClassLoader().getResource(ImageUtils.OFFLINE_IMAGE));

	public DbStatusPanel() 
	{
		super();
		
		final JLabel dbStatusLabel = new JLabel(DB_ONLINE_TEXT);
		final JLabel dbStatusIconLabel = new JLabel("", new ImageIcon(ONLINE_IMAGE), JLabel.RIGHT);
		
		this.add(dbStatusLabel);
		this.add(dbStatusIconLabel);
		
		SwingWorker<Void, Void> dbStatusWorker = new SwingWorker<Void, Void>() 
				{
					@Override
					protected Void doInBackground() throws Exception 
					{
						DbStatusDao dbStatusDao = new DbStatusDao();

						while (true)
						{
							Thread.sleep(1000);
							
							try
							{
								dbStatusDao.checkDbStatus();
								
								dbStatusLabel.setText(DB_ONLINE_TEXT);
								dbStatusLabel.setForeground(new Color(106, 168, 79));
								dbStatusIconLabel.setIcon(new ImageIcon(OFFLINE_IMAGE));
							}
							catch (SQLException sqle)
							{
								dbStatusLabel.setText(DB_OFFLINE_TEXT);
								dbStatusLabel.setForeground(new Color(204, 0, 0));
								dbStatusIconLabel.setIcon(new ImageIcon(OFFLINE_IMAGE));
								dbStatusIconLabel.setToolTipText(sqle.getMessage());
							}
						}
					}
				};

		dbStatusWorker.execute();
	}
}
