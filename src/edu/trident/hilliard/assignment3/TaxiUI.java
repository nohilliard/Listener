package edu.trident.hilliard.assignment3;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

/**
 * TaxiUI creates and populates a JFrame with 3 JTextFields for GasInput, Input, and Output,
 * a combo box and button to provide functionality of recording trips and adding gas,
 * reporting gas, distance.
 * @author Noah Hilliard
 *
 */
public class TaxiUI implements ActionListener, ItemListener, MaintenanceListener
{

	JFrame frame;
	JButton btnOk;
	JComboBox<String> cboSelect;
	JTextField txtInput;
	JTextField txtGasInput;
	JTextField txtOutput;
	JLabel lblMaintenance;
	RentedTaxi cabOne;
	
	/**
	 * Constructor accepts a RentedTaxi object when instantiated.
	 * @param cab
	 */
	public TaxiUI(RentedTaxi cab)
	{
		init();
		cabOne = cab;
				
	}
	
	public TaxiUI()
	{
		
	}
	
	/**
	 * Initializes the frame, provides a layout, places the combo box, button, and textfields.  
	 * Adds ActionListener to each button, orients and makes all text legible.
	 */
	private void init()
	{
		frame = new JFrame("Acme Taxi Tracker Driver");
		btnOk = new JButton("OK");
		txtInput = new JTextField(10);
		txtGasInput = new JTextField(10);
		txtOutput = new JTextField(30);
		lblMaintenance = new JLabel("");
		String[] taxiOptions = {"Record Trip", "Add Gas", "Service", "Gas Report", "Miles Remaining", "Miles Since Service"};
		cboSelect = new JComboBox<String>(taxiOptions);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		
		frame.add(txtInput);
		frame.add(txtGasInput);
		frame.add(cboSelect);
		frame.add(btnOk);
		frame.add(txtOutput);
		frame.add(lblMaintenance);
		
		lblMaintenance.setText("Maintenance Required");
		txtGasInput.setEnabled(false);
		txtOutput.setEnabled(false);
		txtOutput.setDisabledTextColor(Color.BLACK);
		
		btnOk.addActionListener(this);
		cboSelect.addItemListener(this);
		
		frame.setVisible(true);
		frame.pack();
		lblMaintenance.setVisible(false);
	}

	/**
	 * Calls methods from RentedTaxi object passed by MainClass to populate the 
	 * UI with data based on the item selected in the combo box.
	 */
	public void actionPerformed(ActionEvent e)
	{
		DecimalFormat dollar = new DecimalFormat("0.##");
		DecimalFormat reading = new DecimalFormat("0.####");
		boolean tripRecorded = false;
		if(e.getSource() == btnOk)
		{
			try {
				//Check for Record Trip selection and txtInput not null
				if(cboSelect.getSelectedItem() == "Record Trip" && !txtInput.getText().equals(""))
				{
					double temp = 0.0;
					if(Double.parseDouble(txtInput.getText()) >= 0)
					{
						temp = cabOne.calculateFare(Double.parseDouble(txtInput.getText()));
						
						if(temp == 0.0)
						{
							txtOutput.setText("Not enough gas.");
						}
						else
						{
							//Ask professor if addRentedTrip can return success, would be better than tripRecorded boolean
							tripRecorded = cabOne.addRentedTrip(Double.parseDouble(txtInput.getText()));
							if(tripRecorded)
								txtOutput.setText("The trip cost was $" + String.valueOf(dollar.format(temp)) + ".");
							else
							{
								txtOutput.setText("The cab requires maintenance.");
							}
						}
					}
					else
					{
						txtOutput.setText("Please enter a valid number of miles.");
					}
				}
				//Check for Add Gas selection and txtInput/txtGasInput not null
				else if (cboSelect.getSelectedItem() == "Add Gas" && !txtInput.getText().equals("") && !txtGasInput.getText().equals(""))
				{
					double numGallons = Double.parseDouble(txtInput.getText());
					double gasPrice = Double.parseDouble(txtGasInput.getText());
					
					if(numGallons >= 0 && gasPrice >= 0)
					{
						cabOne.addGasCost(numGallons,gasPrice);
						cabOne.addGas(numGallons);
						txtOutput.setText("Gas added, there are now " + String.valueOf(reading.format(cabOne.getCurrentGas())) + " gallons.");
					}
					else
					{
						txtOutput.setText("Please enter a valid number of gallons.");
					}
				}
				else if (cboSelect.getSelectedItem() == "Service")
				{
					cabOne.serviceTaxi();
					txtOutput.setText("The taxi has been serviced.");
					lblMaintenance.setText("Maintenance Not Required");
				}
				else if (cboSelect.getSelectedItem() == "Gas Report")
				{
					txtOutput.setText("There are currently " + String.valueOf(reading.format(cabOne.getCurrentGas())) + " gallons of gas.");
				}
				else if (cboSelect.getSelectedItem() == "Miles Remaining")
				{
					txtOutput.setText("The taxi can drive " + String.valueOf(reading.format(cabOne.milesAvailable())) + " miles on its current gas.");
				}
				else if (cboSelect.getSelectedItem() == "Miles Driven")
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
				else if(cboSelect.getSelectedItem() == "Reset")
				{
					cabOne.resetRentedTaxi();
					txtOutput.setText("The taxi has been reset.");
				}
				//Request a valid input if the criteria for Record Trip or Add Gas are not met
				else
				{
					txtOutput.setText("Please input a valid value.");
				}
			} 
			//Request valid input if we encounter a conversion exception
			catch (NumberFormatException err)
			{
				txtOutput.setText("Please input a valid value.");
			}
		}
	}

	/**
	 * Enables the gallon cost input field when Add Gas is selected
	 * If anything else is selected it checks if gallon cost input field
	 * is enabled and disables it.
	 */
	public void itemStateChanged(ItemEvent event)
	{
		if(event.getItem().equals("Add Gas"))
		{
			txtGasInput.setEnabled(true);
		}
		else if(txtGasInput.isEnabled())
		{
			txtGasInput.setEnabled(false);
		}
		
	}

	/**
	 * Listens for Maintenance events, notifies the driver of maintenance state.
	 */
	public void showMaintenance(int maintEvent)
	{
		if (maintEvent == MaintenanceListener.MAINTAIN_TAXI)
		{
			lblMaintenance.setVisible(true);
		}
		else if(maintEvent == MaintenanceListener.TAXI_MAINTAINED)
		{
			lblMaintenance.setVisible(false);
		}
			
	}
}
