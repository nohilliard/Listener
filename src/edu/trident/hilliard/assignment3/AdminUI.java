package edu.trident.hilliard.assignment3;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * AdminUI creates and populates a JFrame with a JTextField for Output,
 * a combo box and button to provide functionality of miles driven, miles
 * since service, gross earnings, net earnings, and allows resetting the Taxi object.
 * @author Noah Hilliard
 *
 */
public class AdminUI implements ActionListener, MaintenanceListener
{
	JFrame frame;
	JButton btnOk;
	JButton btnOverride;
	JComboBox<String> cboSelect;
	JTextField txtOutput;
	RentedTaxi cabOne;
	
	public AdminUI(RentedTaxi cab)
	{
		init();
		cabOne = cab;
	}

	/**
	 * Initializes the frame, provides a layout, places the combo box, button, and textfields.  
	 * Adds ActionListener to each button, orients and makes all text legible.
	 */
	private void init()
	{
		frame = new JFrame("Acme Taxi Tracker Admin");
		btnOk = new JButton("OK");
		txtOutput = new JTextField(30);
		btnOverride = new JButton("Override");
		String[] taxiOptions = {"Miles Driven", "Miles Since Service", "Gross Earnings", "Net Earnings", "Reset"};
		cboSelect = new JComboBox<String>(taxiOptions);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		
		frame.add(cboSelect);
		frame.add(btnOk);
		frame.add(txtOutput);
		frame.add(btnOverride);
		
		txtOutput.setEnabled(false);
		txtOutput.setDisabledTextColor(Color.BLACK);
		
		btnOverride.setEnabled(false);
		
		btnOk.addActionListener(this);
		btnOverride.addActionListener(this);
		
		frame.setVisible(true);
		frame.pack();
	}
	
	/**
	 * Calls methods from RentedTaxi object passed by MainClass to populate the 
	 * UI with data based on the item selected in the combo box.
	 */
	public void actionPerformed(ActionEvent e) 
	{
		DecimalFormat dollar = new DecimalFormat("0.##");
		DecimalFormat reading = new DecimalFormat("0.####");
		if(e.getSource() == btnOk)
		{
			if (cboSelect.getSelectedItem() == "Miles Driven")
			{
				txtOutput.setText("The taxi has " + String.valueOf(reading.format(cabOne.getCurrentMileage())) + " miles.");
			}
			else if (cboSelect.getSelectedItem() == "Miles Since Service")
			{
				txtOutput.setText("The taxi has gone " + String.valueOf(reading.format(cabOne.getServiceMileage())) + 
						" miles since the last service.");
			}
			else if (cboSelect.getSelectedItem() == "Gross Earnings")
			{
				txtOutput.setText("$" + String.valueOf(dollar.format(cabOne.getTotalFare())) + " earned gross.");
			}
			else if (cboSelect.getSelectedItem() == "Net Earnings")
			{
				txtOutput.setText("$" + String.valueOf(dollar.format(cabOne.getTotalFare() - cabOne.getOperatingCost())) + 
						" earned after operating costs.");
			}
			//Since there are no inputs in this UI to check, reset can be the default
			else
			{
				cabOne.resetRentedTaxi();
				txtOutput.setText("The taxi has been reset.");
			}
		}
		else if(e.getSource() == btnOverride)
		{
			cabOne.setAdminOverride(true);
			txtOutput.setText("Service requirement overridden.");
		}
	}
	
	/**
	 * Listens for Maintenance events, notifies the administrator of maintenance state.
	 * Enables a button that allows the administrator to override the maintenance requirement for the cab.
	 */
	public void showMaintenance(int maintEvent)
	{
		if (maintEvent == MaintenanceListener.MAINTAIN_TAXI)
		{
			txtOutput.setText("Cab needs maintenance.\n");
			btnOverride.setEnabled(true);
		}
		else if(maintEvent == MaintenanceListener.TAXI_MAINTAINED)
		{
			txtOutput.setText("Cab has been maintained.\n");
			btnOverride.setEnabled(false);
		}
	}
}
