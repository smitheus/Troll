package za.co.sb.Troll.gui.component;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.MatteBorder;

import za.co.sb.Troll.dao.TrollStatusDao;
import za.co.sb.Troll.dto.TrollServerStatusDto;
import za.co.sb.Troll.dto.TrollerStatusDto;
import za.co.sb.Troll.gui.util.ImageUtils;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class ConsoleFooterPanel extends JPanel 
{
	private DbStatusPanel dbStatusPanel;
	private TrollerStatusPanel trollerStatusPanel;
	private TrollServerStatusPanel trollServerStatusPanel;
	
	public ConsoleFooterPanel() 
	{
		setBorder(new MatteBorder(0, 3, 3, 3, (Color) new Color(0, 0, 0)));
		setLayout(new BorderLayout(0, 0));
		
		trollerStatusPanel = new TrollerStatusPanel();
		add(trollerStatusPanel, BorderLayout.WEST);
		
		trollServerStatusPanel = new TrollServerStatusPanel();
		add(trollServerStatusPanel);
		
		dbStatusPanel = new DbStatusPanel();
		add(dbStatusPanel, BorderLayout.EAST);
		
		SwingWorker<Void, Void> statusWorker = new SwingWorker<Void, Void>() 
				{
					@Override
					protected Void doInBackground() throws Exception 
					{
						TrollStatusDao dbStatusDao = new TrollStatusDao();

						while (true)
						{
							dbStatusPanel.updateDbStatusPanel(dbStatusDao);
							
							dbStatusDao.checkTrollStatus();
							trollerStatusPanel.updateTrollerStatusPanel(dbStatusDao);
							trollServerStatusPanel.updateTrollerStatusPanel(dbStatusDao);
							
							Thread.sleep(1000);
						}
					}
				};

		statusWorker.execute();
	}
}

@SuppressWarnings("serial")
class DbStatusPanel extends JPanel
{
	private static final Image ONLINE_IMAGE = Toolkit.getDefaultToolkit().getImage(ImageUtils.class.getClassLoader().getResource(ImageUtils.RED_BUTTON_24_IMAGE));
	private static final Image OFFLINE_IMAGE = Toolkit.getDefaultToolkit().getImage(ImageUtils.class.getClassLoader().getResource(ImageUtils.GREEN_BUTTON_24_IMAGE));
	
	private final JPanel contentPanel;
	private final JLabel dbStatusIconLabel;

	public DbStatusPanel() 
	{
		super();
		
		dbStatusIconLabel = new JLabel("", new ImageIcon(ONLINE_IMAGE), JLabel.RIGHT);
		
		contentPanel = new JPanel();
		contentPanel.add(new JLabel("DB : "));
		contentPanel.add(dbStatusIconLabel);
		
		add(contentPanel);
	}
	
	protected void updateDbStatusPanel(TrollStatusDao dbStatusDao)
	{
		try
		{
			dbStatusDao.checkDbStatus();
			
			dbStatusIconLabel.setIcon(new ImageIcon(OFFLINE_IMAGE));
		}
		catch (SQLException sqle)
		{
			dbStatusIconLabel.setIcon(new ImageIcon(OFFLINE_IMAGE));
			dbStatusIconLabel.setToolTipText(sqle.getMessage());
		}
	}
}

@SuppressWarnings("serial")
class TrollerStatusPanel extends JPanel
{
	private static final Image RED_BUTTON_IMAGE = Toolkit.getDefaultToolkit().getImage(ImageUtils.class.getClassLoader().getResource(ImageUtils.RED_BUTTON_24_IMAGE));
	private static final Image ORANGE_BUTTON_IMAGE = Toolkit.getDefaultToolkit().getImage(ImageUtils.class.getClassLoader().getResource(ImageUtils.ORANGE_BUTTON_24_IMAGE));
	private static final Image GREEN_BUTTON_IMAGE = Toolkit.getDefaultToolkit().getImage(ImageUtils.class.getClassLoader().getResource(ImageUtils.GREEN_BUTTON_24_IMAGE));
	
	private final Map<String, TrollerSourceSystemStatusPanel> trollerSourceSystemStatusPanelMap = new LinkedHashMap<String, TrollerSourceSystemStatusPanel>();
	
	public TrollerStatusPanel() 
	{
		super(new FlowLayout(FlowLayout.LEFT));
	}
	
	protected TrollerSourceSystemStatusPanel getTrollerSourceSystemStatusPanel(String sourceSystem)
	{
		return trollerSourceSystemStatusPanelMap.get(sourceSystem);
	}
	
	protected void addTrollerSourceSystemStatusPanel(String sourceSystem, TrollerSourceSystemStatusPanel trollerSourceSystemStatusPanel)
	{
		add(trollerSourceSystemStatusPanel);
		trollerSourceSystemStatusPanelMap.put(sourceSystem, trollerSourceSystemStatusPanel);
	}
	
	protected void updateTrollerStatusPanel(TrollStatusDao dbStatusDao)
	{
		try
		{
			List<TrollerStatusDto> trollerStatusDtoList = dbStatusDao.selectAllTrollerStatusDtos();
			
			for (TrollerStatusDto trollerStatusDto : trollerStatusDtoList)
			{
				TrollerSourceSystemStatusPanel trollerSourceSystemStatusPanel = getTrollerSourceSystemStatusPanel(trollerStatusDto.getSourceSystem());
				
				if (trollerSourceSystemStatusPanel == null) 
				{
					trollerSourceSystemStatusPanel = new TrollerSourceSystemStatusPanel(trollerStatusDto.getSourceSystem());
					addTrollerSourceSystemStatusPanel(trollerStatusDto.getSourceSystem(), trollerSourceSystemStatusPanel);
				}
				
				JLabel trollerStatusLabel = trollerSourceSystemStatusPanel.getTrollerStatusLabel(trollerStatusDto.getInstanceNo());
				
				if (trollerStatusLabel == null) 
				{
					trollerStatusLabel = new JLabel(String.valueOf(trollerStatusDto.getInstanceNo()));
					trollerStatusLabel.setHorizontalTextPosition(SwingConstants.CENTER);
					
					trollerSourceSystemStatusPanel.addTrollerStatusLabel(trollerStatusDto.getInstanceNo(), trollerStatusLabel);
				}
				
				switch (trollerStatusDto.getShortStatus())
				{
					case G :
						trollerStatusLabel.setIcon(new ImageIcon(GREEN_BUTTON_IMAGE));
						trollerStatusLabel.setForeground(Color.BLACK);
						break;
					case A :
						trollerStatusLabel.setIcon(new ImageIcon(ORANGE_BUTTON_IMAGE));
						trollerStatusLabel.setForeground(Color.BLACK);
						break;
					case R :
					default :
						trollerStatusLabel.setIcon(new ImageIcon(RED_BUTTON_IMAGE));
						trollerStatusLabel.setForeground(Color.WHITE);
				}
			}
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}
}

@SuppressWarnings("serial")
class TrollerSourceSystemStatusPanel extends JPanel
{
	private final Map<Integer, JLabel> trollerStatusLabelMap = new LinkedHashMap<Integer, JLabel>();
	
	public TrollerSourceSystemStatusPanel(String title) 
	{
		super(new FlowLayout(FlowLayout.RIGHT));
		
		JLabel titleLable = new JLabel(title + " : ");
		add(titleLable);
	}
	
	protected JLabel getTrollerStatusLabel(int instanceNo)
	{
		return trollerStatusLabelMap.get(instanceNo);
	}
	
	protected void addTrollerStatusLabel(int instanceNo, JLabel trollerStatusLabel)
	{
		add(trollerStatusLabel);
		trollerStatusLabelMap.put(instanceNo, trollerStatusLabel);
	}
}

@SuppressWarnings("serial")
class TrollServerStatusPanel extends JPanel
{
	private static final Image RED_BUTTON_IMAGE = Toolkit.getDefaultToolkit().getImage(ImageUtils.class.getClassLoader().getResource(ImageUtils.RED_BUTTON_24_IMAGE));
	private static final Image ORANGE_BUTTON_IMAGE = Toolkit.getDefaultToolkit().getImage(ImageUtils.class.getClassLoader().getResource(ImageUtils.ORANGE_BUTTON_24_IMAGE));
	private static final Image GREEN_BUTTON_IMAGE = Toolkit.getDefaultToolkit().getImage(ImageUtils.class.getClassLoader().getResource(ImageUtils.GREEN_BUTTON_24_IMAGE));
	
	private final Map<Integer, JLabel> trollServerLabelMap = new LinkedHashMap<Integer, JLabel>();
	private final JPanel contentPanel;
	
	public TrollServerStatusPanel() 
	{
		super(new FlowLayout(FlowLayout.RIGHT));
		
		contentPanel = new JPanel();
		contentPanel.add(new JLabel("Server(s) : "));
		
		add(contentPanel);
	}
	
	protected JLabel getTrollServerStatusLabel(int instanceNo)
	{
		return trollServerLabelMap.get(instanceNo);
	}
	
	protected void addTrollServerStatusLabel(int instanceNo, JLabel trollServerStatusLabel)
	{
		contentPanel.add(trollServerStatusLabel);
		trollServerLabelMap.put(instanceNo, trollServerStatusLabel);
	}
	
	protected void updateTrollerStatusPanel(TrollStatusDao dbStatusDao)
	{
		try
		{
			List<TrollServerStatusDto> trollServerStatusDtoList = dbStatusDao.selectAllTrollServerStatusDtos();
			
			for (TrollServerStatusDto trollServerStatusDto : trollServerStatusDtoList)
			{
				JLabel trollServerStatusLabel = getTrollServerStatusLabel(trollServerStatusDto.getInstanceNo());
				
				if (trollServerStatusLabel == null) 
				{
					trollServerStatusLabel = new JLabel(String.valueOf(trollServerStatusDto.getInstanceNo()));
					trollServerStatusLabel.setHorizontalTextPosition(SwingConstants.CENTER);
					
					addTrollServerStatusLabel(trollServerStatusDto.getInstanceNo(), trollServerStatusLabel);
				}
				
				switch (trollServerStatusDto.getShortStatus())
				{
					case G :
						trollServerStatusLabel.setIcon(new ImageIcon(GREEN_BUTTON_IMAGE));
						trollServerStatusLabel.setForeground(Color.BLACK);
					case A :
						trollServerStatusLabel.setIcon(new ImageIcon(ORANGE_BUTTON_IMAGE));
						trollServerStatusLabel.setForeground(Color.BLACK);
					case R :
					default :
						trollServerStatusLabel.setIcon(new ImageIcon(RED_BUTTON_IMAGE));
						trollServerStatusLabel.setForeground(Color.WHITE);
				}
			}
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
		}
	}
}


