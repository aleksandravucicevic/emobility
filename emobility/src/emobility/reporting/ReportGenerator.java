package emobility.reporting;

import emobility.utility.*;

/**
 * Abstract base class for generating reports related to vehicle rentals.
 * This class provides methods to access various repair and maintenance coefficients 
 * and defines abstract methods for calculating different financial metrics.
 */
public abstract class ReportGenerator{
	/** The type of the report (e.g. "daily*, "summary"). */
	protected String reportType;
	
	/** Path to the properties file containing configuration data. */
	protected static String configFilePath = "src/emobility/utility/config.properties";
	
	/** Properties reader needed for reading the configuration data. */
	protected static PropertiesReader config = new PropertiesReader(configFilePath);
	
	/** File path for repair properties file. */
	private static String pricingPropertiesFilePath = config.getProperty("REPAIR_PROPERTIES_FILE_PATH");
	
	/** Pricing reader needed to access the pricing information from a properties file. */
	protected static PricingReader pr = new PricingReader(pricingPropertiesFilePath);
	
	/** Coefficient for car repairs. */
	protected static Double carRepairCoefficient = pr.getDoubleProperty("CAR_REPAIR_COEFFICIENT");
	
	/** Coefficient for bicycle repairs. */
	protected static Double bicycleRepairCoefficient = pr.getDoubleProperty("BICYCLE_REPAIR_COEFFICIENT");
	
	/** Coefficient for scooter repairs. */
	protected static Double scooterRepairCoefficient = pr.getDoubleProperty("SCOOTER_REPAIR_COEFFICIENT");
	
	/** Coefficient for maintenance costs. */
	protected static Double maintenanceCoefficient = pr.getDoubleProperty("MAINTENANCE_COEFFICIENT");
	
	/** Coefficient for general expenses. */
	protected static Double expenseCoefficient = pr.getDoubleProperty("EXPENSE_COEFFICIENT");
	
	/** Coefficient for taxes. */
	protected static Double taxCoefficient = pr.getDoubleProperty("TAX_COEFFICIENT");

	/** Constructor which initializes the report type as "general*. */
	public ReportGenerator(){
		this.reportType = "general";
	}
	
	/**
	 * Gets the type of the report.
	 * @return report's type
	 */
	public String getReportType(){
		return reportType;
	}
	
	/**
	 * Gets the car repair coefficient.
	 * @return car's repair coefficient
	 */
	public static Double getCarRepairCoefficient(){
		return carRepairCoefficient;
	}
	
	/**
	 * Gets the bicycle repair coefficient.
	 * @return bicycle's repair coefficient
	 */
	public static Double getBicycleRepairCoefficient(){
		return bicycleRepairCoefficient;
	}
	
	/**
	 * Gets the scooter repair coefficient.
	 * @return scooter's repair coefficient
	 */
	public static Double getScooterRepairCoefficient(){
		return scooterRepairCoefficient;
	}
	 
	/** Abstract method to calculate total income. Must be implemented by subclasses. */
	protected abstract void calculateTotalIncome();
	/** Abstract method to calculate total discount. Must be implemented by subclasses. */
	protected abstract void calculateTotalDiscount();
	/** Abstract method to calculate total promotional discount. Must be implemented by subclasses. */
	protected abstract void calculateTotalPromo();
	/** Abstract method to calculate total income made in each area. Must be implemented by subclasses. */
	protected abstract void calculateNarrowAndWideAreaIncome();
	/** Abstract method to calculate total maintenance costs. Must be implemented by subclasses. */
	protected abstract void calculateTotalMaintenanceCost();
	/** Abstract method to calculate total repair costs. Must be implemented by subclasses. */
	protected abstract void calculateTotalRepairCost();
	/** Abstract method to perform all calculations. Must be implemented by subclasses. */
	protected abstract void doAllCalculations();
}
