package hw1;
/**
 * A class representation of a removable and rechargeable camera battery
 * 
 * @author Ian Nelson
 *
 */
public class CameraBattery {
	
	
	/**
	 * number of external charger settings between 0 inclusive and 4 exclusive 
	 */
	public static final int NUM_CHARGER_SETTINGS = 4;

	/**
	 * constant used in calculating charge rate of external charger
	 */
	public static final double CHARGE_RATE = 2.0;
	
	/**
	 * default power consumption at the start of the simulation
	 */
	public static final double DEFAULT_CAMERA_POWER_CONSUMPTION = 1.0;
	
	
	
	
	/**
	 * "state" variable to determine if battery is in the camera or not, can only be 1 or 0
	 */
	private int inCamera;
			
	/**
	 * "state" variable to determine if battery is in the external charger or not, can only be 1 or 0
	 */
	private int inExternal;
	
	/**
	 * tracks the charger setting between 0 inclusive and NUM_CHARGER SETTINGS exclusive
	 */
	private int chargerSetting;
	
	/**
	 * tracks the current battery charge in miliamp-hours (mAh)
	 */
	private double batteryCharge;
	
	/**
	 * holds the max battery capacity in miliamp-hours (mAh)
	 */
	private double batteryCapacity;
	
	/**
	 * sums whenever drain() is called until reset by resetBatteryMonitor()
	 */
	private double totalDrain;
	
	/**
	 * tracks the camera's power consumption, defaults to DEFAULT_CAMERA_POWER_CONSUMPTION
	 */
	private double cameraPowerConsumption;
	
	
	
	
	/**
	 * Constructs a new battery simulation disconnected from the camera and the external charger
	 * 
	 * @param batterySartingCharge - initial battery charge
	 * @param batteryCapacity - max battery capacity
	 */
	public CameraBattery(double batteryStartingCharge, double batteryCapacity) {
		inCamera = 0;
		inExternal = 0;
		chargerSetting = 0;
		batteryCharge = Math.min(batteryStartingCharge, batteryCapacity);
		this.batteryCapacity = batteryCapacity;
		totalDrain = 0;
		cameraPowerConsumption = DEFAULT_CAMERA_POWER_CONSUMPTION;
	}
	
	/**
	 * indicates the user has pressed the setting button one time on the external charger.
	 * The charge setting increments by one or if already at the maximum setting wraps around to setting 0. 
	 */
	public void buttonPress() { // mutator
		chargerSetting += 1;
		chargerSetting %= NUM_CHARGER_SETTINGS;
	}
	
	/**
	 * Charges the battery when connected to the camera for a given amount of time at a constant rate,
	 * Charge cannot exceed the battery capacity.
	 * 
	 * @param minutes - given time to charge in minutes
	 * @return the actual amount the battery charged
	 */
	public double cameraCharge(double minutes) {
		double charge = Math.min(batteryCapacity - batteryCharge, minutes * CHARGE_RATE) * inCamera;
		batteryCharge += charge;
		return charge;
	}
	
	/**
	 * Charges the battery connected to the external charger, if connected, for a given number of minutes.
	 * the formula is minutes * CHARGE_RATE * charger setting
	 * 
	 * @param minutes - given time to charge in minutes
	 * @return the actual amount charged
	 */
	public double externalCharge(double minutes) { //mutator
		double charge = Math.min(batteryCapacity - batteryCharge, minutes * CHARGE_RATE * chargerSetting) * inExternal;
		batteryCharge += charge;
		return charge;
	}
	
	/**
	 * Drains the battery connected to the camera, if connected, for given number of minutes.
	 * The drain cannot exceed the the current battery charge
	 * 
	 * @param minutes - given time to charge in minutes
	 * @return the actual amount drained
	 */
	public double drain(double minutes) { //mutator
		double batteryDrain = Math.min(batteryCharge, minutes * cameraPowerConsumption) * inCamera;
		batteryCharge -= batteryDrain;
		totalDrain += batteryDrain;
		return batteryDrain;
	}
	
	/**
	 * Reset the battery monitoring system by setting the total battery drain count back to 0. 
	 */
	public void resetBatteryMonitor() {
		totalDrain = 0;
	}
	
	/**
	 * Get the battery's capacity. 
	 * 
	 * @return the battery's capacity
	 */
	public double getBatteryCapacity() {
		return batteryCapacity;
	}
	
	/**
	 * Get the battery's current charge. 
	 * 
	 * @return the battery's current charge
	 */
	public double getBatteryCharge() {
		return batteryCharge;
	}
	
	/**
	 * Get the current charge of the camera's battery. 
	 * 
	 * @return the battery's charge if it's connected to the camera, inCamera = 1
	 */
	public double getCameraCharge() { 
		return batteryCharge * inCamera;
	}
	
	/**
	 * Get the current power consumption of the camera 
	 * 
	 * @return the current power Consumption
	 */
	public double getCameraPowerConsumption() { 
		return cameraPowerConsumption;
	}

	/**
	 *  Get the external charger setting
	 *  
	 * @return the current charger setting
	 */
	public int getChargerSetting() { 
		return chargerSetting;
	}
	
	/**
	 * Get the total amount of power drained from the battery since the last time the battery monitor was started or reset. 
	 * 
	 * @return total drain since last reset
	 */
	public double getTotalDrain() {
		return totalDrain;
	}
	
	/**
	 * Move the battery to the external charger. 
	 * Updates the variables inCamera and inExternal
	 */
	public void moveBatteryExternal() {
		inExternal = 1;
		inCamera = 0;
	}
	
	/**
	 * Move the battery to the camera.
	 * Updates the variables inCamera and inExternal
	 */
	public void moveBatteryCamera() {
		inCamera = 1;
		inExternal = 0;
	}
	
	/**
	 * Remove the battery from either the camera or external charger.
	 * Updates the variables inCamera and inExternal
	 */
	public void removeBattery() {
		inCamera = 0;
		inExternal = 0;
	}
	
	/**
	 * Set the power consumption of the camera.
	 * @param cameraPowerConsumption - the given new power consumption 
	 */
	public void setCameraPowerConsumption(double cameraPowerConsumption) {
		this.cameraPowerConsumption = cameraPowerConsumption;
	}
}
