package edu.trident.hilliard.assignment3;

/**
 * RentedTaxi extends {@link Taxi} with operating costs such as servicing
 * and gas.  Tracks miles driven since last service.
 * @author Noah Hilliard
 *
 */
public class RentedTaxi extends Taxi
{
	protected double serviceMileage = 0.0;
	protected double operatingCost = 0.0;
	protected boolean maintenanceNeeded = false;
	protected boolean adminOverride = false;
	protected MaintenanceListener listeners[] = new MaintenanceListener[2];
	protected int numListeners = 0;
	
	/**
	 * Constructor that accepts a gasAmount and limits it to the GAS_CAPACITY
	 * of the Taxi.
	 * @param gasAmount
	 */
	public RentedTaxi(double gasAmount)
	{
		super();
		if(gasAmount <= GAS_CAPACITY)
		{
			
			currentGas = gasAmount;
		}
		else
		{
			currentGas = GAS_CAPACITY;
		}
		
	}
	/**
	 * Provides the miles driven since the last time the taxi was serviced.
	 * @return serviceMileage
	 */
	public double getServiceMileage()
	{
		return serviceMileage;
	}
	
	/**
	 * Provides the accrued operating cost.
	 * @return operatingCost
	 */
	public double getOperatingCost()
	{
		return operatingCost;
	}
	
	/**
	 * Provides true or false based on boolean adminOverride.
	 * @return Whether the administrator has permitted an extra trip.
	 */
	public boolean getAdminOverride()
	{
		return adminOverride;
	}
	
	/**
	 * Provides true or false based on boolean maintenanceNeeded
	 * @return Whether taxi needs maintenance to record more trips
	 */
	public boolean getMaintenanceNeeded()
	{
		return maintenanceNeeded;
	}
	
	/**
	 * adminOverride setter to allow a trip regardless of maintenance status.
	 * @param adminStatus
	 */
	public void setAdminOverride(boolean adminStatus)
	{
		adminOverride = adminStatus;
	}
	
	/**
	 * maintenanceNeeded setter to determine if recordTrip can be used.
	 * @param maintStatus
	 */
	public void setMaintenanceNeeded(boolean maintStatus)
	{
		maintenanceNeeded = maintStatus;
	}
	
	/**
	 * Services the taxi, resets the serviceMileage to 0.0 and adds 25 to the operating cost.
	 */
	public void serviceTaxi()
	{
		serviceMileage = 0.0;
		operatingCost += 25;
		if(maintenanceNeeded)
		{
			for (int i = 0; i < numListeners; i++)
				listeners[i].showMaintenance(MaintenanceListener.TAXI_MAINTAINED);
			setMaintenanceNeeded(false);
		}
			
	}

	/**
	 * Adds the cost of gas added to operating costs, if the value entered exceeds the amount
	 * that can be put into the tank, the amount is reduced to only fill the tank.
	 * @param gallons
	 * @param price
	 */
	public void addGasCost(double gallons, double price)
	{
		if(currentGas + gallons > GAS_CAPACITY)
		{
			gallons = GAS_CAPACITY - currentGas;
		}
		operatingCost += (gallons * price);
	}
	
	/**
	 * Resets the taxi by calling {@link Taxi#resetTaxi() resetTaxi} from super and setting
	 * operatingCost to 0.0
	 */
	public void resetRentedTaxi()
	{
		operatingCost = 0.0;
		resetTaxi();
	}
	
	/**
	 * Adds listeners to an array so they can be notified of events.
	 * @param listener
	 */
	public void addListener(MaintenanceListener listener)
	{
		this.listeners[numListeners] = listener;
		numListeners++;
	}
	
	/**
	 * Checks for maintenance needed or admin override and updates 
	 * serviceMileage with tripMiles and calls {@link Taxi#addTrip(double)} from super.
	 * If serviceMileage is 500 or higher it will notify all listeners and set the
	 * maintenanceNeeded boolean to true.
	 * @returns tripRecorded boolean to verify that there was a valid trip
	 */
	public boolean addRentedTrip(double tripMiles)
	{
		boolean tripRecorded = false;
		
		if(!maintenanceNeeded || adminOverride)
		{
			if(milesAvailable() >= tripMiles) 
			{
				addTrip(tripMiles);
				serviceMileage += tripMiles;
				tripRecorded = true;
				
				if(adminOverride)
				{
					setAdminOverride(false);
				}
				
				if(serviceMileage >= 500)
				{
					for (int i = 0; i < numListeners; i++)
						listeners[i].showMaintenance(MaintenanceListener.MAINTAIN_TAXI);
					setMaintenanceNeeded(true);
				}
			}
		}
		
		return tripRecorded;
	}
}
