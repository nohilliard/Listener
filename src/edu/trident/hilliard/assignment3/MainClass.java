package edu.trident.hilliard.assignment3;

import javax.swing.JOptionPane;

/**
 * MainClass creates an instance of {@link RentedTaxi} with numGallons of gas
 * , adds {@link MaintenanceListener MaintenanceListeners} to the taxi
 * and passes the taxi to {@link TaxiUI} and {@link AdminUI}.
 * 
 * @author Noah Hilliard
 */
public class MainClass
{

	public static void main(String[] args) 
	{
		double numGallons = 0.0;
		boolean inputValid = false;
		
		//Loops to acquire valid input to start the RentedTaxi
		while (inputValid == false)
		{
			try
			{
				numGallons = Double.parseDouble(JOptionPane.showInputDialog("Enter amount of gas in Taxi: "));
				if(numGallons >= 0.0)
				{
					inputValid = true;
				}
			} catch (NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
		
		RentedTaxi cabOne = new RentedTaxi(numGallons);
		
		TaxiUI cabOneWindow = new TaxiUI(cabOne);
		AdminUI adminWindow = new AdminUI(cabOne);
		
		cabOne.addListener(adminWindow);
		cabOne.addListener(cabOneWindow);
	}

}
