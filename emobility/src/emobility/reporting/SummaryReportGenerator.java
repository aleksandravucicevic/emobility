package emobility.reporting;

import java.util.List;

/**
 * Generates a summary report for vehicle rentals. 
 * This class calculates the total income, costs, and other important financial data over all available data.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class SummaryReportGenerator extends ReportGenerator{
	/** A list of all bills. */
	private static List<Bill> allBills = BillParser.getBills();
	
	/** Total income from all bills. */
	private Double totalIncome;
	
	/** Total discounts from all bills. */
	private Double totalDiscount;
	
	/** Total promotional discounts from all bills. */
	private Double totalPromo;
	
	/** Total income made in the narrow area. */
	private Double narrowAreaIncome;
	
	/** Total income made in the wide area. */
	private Double wideAreaIncome;
	
	/** Total maintenance costs. */
	private Double totalMaintenanceCost;
	
	/** Total repair costs. */
	private Double totalRepairCost;
	
	/** Total expense costs. */
	private Double totalExpenseCost;
	
	/** Total tax costs. */
	private Double totalTaxCost;
	
	/**
	 * Constructs a {@code SummaryReportGenerator} and initializes report type as "summary". 
	 * <p>Performs all necessary calculations.</p>
	 */
	public SummaryReportGenerator(){
		super();
		this.reportType = "summary";
		doAllCalculations();
	}
	
	/** Calculates the total income across all rentals by summing the total prices from all bills. */
	@Override
	protected void calculateTotalIncome(){
		totalIncome = 0.0;
		for(Bill bill : allBills){
			totalIncome += bill.getTotalPrice();
		}
	}
	
	/** Calculates the total discount amount across all rentals by summing the discount prices from all bills. */
	@Override
	protected void calculateTotalDiscount(){
		totalDiscount = 0.0;
		for(Bill bill : allBills){
			totalDiscount += bill.getDiscountPrice();
		}
	}
	
	/** Calculates the total promotional discount amount across all rentals by summing the promotional discount prices from all bills. */
	@Override
	protected void calculateTotalPromo(){
		totalPromo = 0.0;
		for(Bill bill : allBills){
			totalPromo += bill.getPromoPrice();
		}
	}
	
	/** Calculates the income from rentals in narrow and wide area across all rentals. */
	@Override
	protected void calculateNarrowAndWideAreaIncome(){
		narrowAreaIncome = 0.0;
		wideAreaIncome = 0.0;
		for(Bill bill : allBills){
			String area = bill.getArea().toLowerCase();
			if(area.equals("narrow")){
				narrowAreaIncome += bill.getTotalPrice();
			} else if(area.equals("wide")){
				wideAreaIncome += bill.getTotalPrice();
			} else{
				System.out.println("Undefined rental area.");
				continue;
			}
		}
	}
	
	/** Calculates the total maintenance cost across all rentals by applying the maintenance coefficient to the total income. */
	@Override
	protected void calculateTotalMaintenanceCost(){
		totalMaintenanceCost = totalIncome * maintenanceCoefficient;
	}
	
	/** Calculates the total repair cost across all rentals by applying repair coefficients to the purchase prices of faulty vehicles. */
	@Override
	protected void calculateTotalRepairCost(){
		totalRepairCost = 0.0;
		for(Bill bill : allBills){
			if(bill.isFault()){
				String vehicleType = bill.getVehicleType();
				switch(vehicleType.toLowerCase()){
					case "car":
						totalRepairCost += carRepairCoefficient * bill.getVehiclePurchasePrice();
						break;
						
					case "bicycle":
						totalRepairCost += bicycleRepairCoefficient * bill.getVehiclePurchasePrice();
						break;
						
					case "scooter":
						totalRepairCost += scooterRepairCoefficient * bill.getVehiclePurchasePrice();
						break;
						
					default:
						System.out.println("Detected bill for undefined vehicle type: " + vehicleType);
						continue;
				}
			}
		}
	}
	
	/** Calculates the total expense cost based on the total income. */
	protected void calculateTotalExpenseCost(){
		totalExpenseCost = totalIncome * expenseCoefficient;
	}
	
	/** Calculates the total tax cost. */
	protected void calculateTotalTaxCost(){
		totalTaxCost = Math.abs(totalIncome - totalMaintenanceCost - totalRepairCost - totalExpenseCost) * taxCoefficient;
	}
	
	/** Performs all necessary calculations for the summary report. */
	@Override
	protected void doAllCalculations(){
		calculateTotalIncome();
		calculateTotalDiscount();
		calculateTotalPromo();
		calculateNarrowAndWideAreaIncome();
		calculateTotalMaintenanceCost();
		calculateTotalRepairCost();
		calculateTotalExpenseCost();
		calculateTotalTaxCost();
	}
	
	/**
	 * Gets the total income.
	 * @return total income
	 */
	public Double getTotalIncome(){
		return totalIncome;
	}
	
	/**
	 * Gets the total discount.
	 * @return total discount
	 */
	public Double getTotalDiscount(){
		return totalDiscount;
	}
	
	/**
	 * Gets the total promotional discount.
	 * @return total promotional discount
	 */
	public Double getTotalPromo(){
		return totalPromo;
	}
	
	/**
	 * Gets the total income made in the narrow area.
	 * @return narrow area's income
	 */
	public Double getNarrowAreaIncome(){
		return narrowAreaIncome;
	}
	
	/**
	 * Gets the total income made in the wide area.
	 * @return wide area's income
	 */
	public Double getWideAreaIncome(){
		return wideAreaIncome;
	}
	
	/**
	 * Gets the total maintenance costs.
	 * @return total maintenance costs
	 */
	public Double getTotalMaintenanceCost(){
		return totalMaintenanceCost;
	}
	
	/**
	 * Gets the total repair costs.
	 * @return total repair costs
	 */
	public Double getTotalRepairCost(){
		return totalRepairCost;
	}
	
	/**
	 * Gets the total expense costs.
	 * @return expense costs
	 */
	public Double getTotalExpenseCost(){
		return totalExpenseCost;
	}
	
	/**
	 * Gets the total tax costs.
	 * @return total tax costs
	 */
	public Double getTotalTaxCost(){
		return totalTaxCost;
	}
}
