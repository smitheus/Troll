package za.co.sb.Troll.gui.component.datetime;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.border.MatteBorder;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

@SuppressWarnings("serial")
public class DateTimePickerDialog extends JDialog implements ActionListener
{
	private static final String OK_ACTION_COMMAND = "Ok";
	private static final String CANCEL_ACTION_COMMAND = "Cancel";
		
	private final JPanel contentPanel = new JPanel();
	
	private UtilDateModel fromDateModel = new UtilDateModel(new Date());
	private UtilDateModel toDateModel = new UtilDateModel(new Date());
	private SpinnerModel fromTimeModel = new SpinnerDateModel();
	private SpinnerModel toTimeModel = new SpinnerDateModel();
	
	private String lastActionPerformed = CANCEL_ACTION_COMMAND;
	private Date selectedFromDate = new Date();
	private Date selectedToDate = new Date();
	
	/**
	 * Create the dialog.
	 */
	public DateTimePickerDialog() 
	{
		super();
		
		setBounds(100, 100, 350, 220);
		setTitle("Select date and time period...");
		setModal(true);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		contentPanel.setBorder(new MatteBorder(3, 3, 3, 3, Color.BLACK));
		
		// From date picker components
		contentPanel.add(new JLabel("From : "));
		
		JDatePanelImpl fromDatePanel = new JDatePanelImpl(fromDateModel);
		JDatePickerImpl fromDatePicker = new JDatePickerImpl(fromDatePanel);
		
		JSpinner fromTimeSpinner = new JSpinner(fromTimeModel);
		fromTimeSpinner.setEditor(new JSpinner.DateEditor(fromTimeSpinner, "HH:mm:ss"));
				
		JPanel fromPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fromPanel.add(fromDatePicker);
		fromPanel.add(new JLabel(" at "));
		fromPanel.add(fromTimeSpinner);
		
		contentPanel.add(fromPanel);
		
		// To date picker components
		contentPanel.add(new JLabel("To : "));
		
		JDatePanelImpl toDatePanel = new JDatePanelImpl(toDateModel);
		JDatePickerImpl toDatePicker = new JDatePickerImpl(toDatePanel);
		
		JSpinner toTimeSpinner = new JSpinner(toTimeModel);
		toTimeSpinner.setEditor(new JSpinner.DateEditor(toTimeSpinner, "HH:mm:ss"));
		
		JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toPanel.add(toDatePicker);
		toPanel.add(new JLabel(" at "));
		toPanel.add(toTimeSpinner);
		
		contentPanel.add(toPanel);
		
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new MatteBorder(0, 3, 3, 3, Color.BLACK));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		// OK button
		JButton btnOk = new JButton("Ok");
		btnOk.setActionCommand(OK_ACTION_COMMAND);
		btnOk.addActionListener(this);
		buttonPane.add(btnOk);
		getRootPane().setDefaultButton(btnOk);
		
		// Cancel button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand(CANCEL_ACTION_COMMAND);
		btnCancel.addActionListener(this);
		buttonPane.add(btnCancel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		
		if (actionCommand.equals(OK_ACTION_COMMAND))
		{
			// Build FROM Date
			Calendar fromDateCalendar = Calendar.getInstance();
			fromDateCalendar.setTime(fromDateModel.getValue());
			
			Calendar fromTimeCalendar = Calendar.getInstance();
			fromTimeCalendar.setTime((Date) fromTimeModel.getValue());
			
			fromDateCalendar.set(Calendar.HOUR_OF_DAY, fromTimeCalendar.get(Calendar.HOUR_OF_DAY));
			fromDateCalendar.set(Calendar.MINUTE, fromTimeCalendar.get(Calendar.MINUTE));
			fromDateCalendar.set(Calendar.SECOND, fromTimeCalendar.get(Calendar.SECOND));
			fromDateCalendar.set(Calendar.MILLISECOND, 0);
			
			selectedFromDate = fromDateCalendar.getTime();
			
			// Build TO Date
			Calendar toDateCalendar = Calendar.getInstance();
			toDateCalendar.setTime(toDateModel.getValue());
			
			Calendar toTimeCalendar = Calendar.getInstance();
			toTimeCalendar.setTime((Date) toTimeModel.getValue());
			
			toDateCalendar.set(Calendar.HOUR_OF_DAY, toTimeCalendar.get(Calendar.HOUR_OF_DAY));
			toDateCalendar.set(Calendar.MINUTE, toTimeCalendar.get(Calendar.MINUTE));
			toDateCalendar.set(Calendar.SECOND, toTimeCalendar.get(Calendar.SECOND));
			toDateCalendar.set(Calendar.MILLISECOND, 0);
			
			selectedToDate = toDateCalendar.getTime();
			
			System.out.println(selectedFromDate);
			System.out.println(selectedToDate);
			
			lastActionPerformed = OK_ACTION_COMMAND;
		}
		else if (actionCommand.equals(CANCEL_ACTION_COMMAND))
		{
			lastActionPerformed = CANCEL_ACTION_COMMAND;
		}
		
		dispose();
	}
	
	public boolean isCancellAction()
	{
		if (lastActionPerformed.equalsIgnoreCase(CANCEL_ACTION_COMMAND))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Date getSelectedToDate()
	{
		return selectedToDate;
		
	}
	
	public Date getSelectedFromDate()
	{
		return selectedFromDate;
	}
}