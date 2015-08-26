package edu.trident.hilliard.assignment3;

/**
 * MaintenanceListener accepts MAINTAIN_TAXI or TAXI_MAINTANED and interacts
 * with user interfaces through their showMaintenance method.
 * @author Noah Hilliard
 *
 */
public interface MaintenanceListener
{
	final int MAINTAIN_TAXI = 1;
	final int TAXI_MAINTAINED = 2;
	
	public void showMaintenance(int maintEvent);
}
