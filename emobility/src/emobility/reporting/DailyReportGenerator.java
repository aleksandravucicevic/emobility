package emobility.reporting;

import java.util.*;

/**
 * Generates daily reports for vehicle rentals. 
 * This class performs calculations for income, discounts, promotional discounts, 
 * and costs associated with each day.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class DailyReportGenerator extends ReportGenerator{
	/** A map of bills grouped by date. */
	private static Map<Date, List<Bill>> billsByDate = BillParser.getBillsByDate();
	
	/** A map storing total income by date. */
	private Map<Date, Double> totalIncome;
	
	/** A map storing total discounts by date. */
	private Map<Date, Double> totalDiscount;
	
	/** A map storing total promotional discounts by date. */
	private Map<Date, Double> totalPromo;
	
	/** A map storing total income made in the narrow area by date. */
	private Map<Date, Double> totalNarrowAreaIncome;
	
	/** A map storing total income made in the wide area by date. */
	private Map<Date, Double> totalWideAreaIncome;
	
	/** A map storing total maintenance costs by date. */
	private Map<Date, Double> totalMaintenanceCost;
	
	/** A map storing total repair costs by date. */
	private Map<Date, Double> totalRepairCost;
	
	/**
	 * Constructs a {@code DailyReportGenerator} object and initializes report type as "daily". 
	 * <p>Performs all necessary calculations.</p>
	 */
	public DailyReportGenerator(){
		super();
		this.reportType = "daily";
		totalIncome = new TreeMap<>();
		totalDiscount = new TreeMap<>();
		totalPromo = new TreeMap<>();
		totalNarrowAreaIncome = new TreeMap<>();
		totalWideAreaIncome = new TreeMap<>();
		totalMaintenanceCost = new TreeMap<>();
		totalRepairCost = new TreeMap<>();
		
		doAllCalculations();
	}
	
	/** Calculates the total income for each day by summing the total prices from all bills on that day. */
	@Override
	protected void calculateTotalIncome(){
		for(Map.Entry<Date, List<Bill>> entry : billsByDate.entrySet()){
			Date rentalDate = entry.getKey();
			List<Bill> bills = entry.getValue();
			Double totalInc = 0.0;
			
			for(Bill bill : bills){
				totalInc += bill.getTotalPrice();
			}
			
			totalIncome.put(rentalDate,totalInc);
		}
	}
	
	/** Calculates the total discount amount for each day by summing the discount prices from all bills on that day. */
	@Override
	protected void calculateTotalDiscount(){
		for(Map.Entry<Date, List<Bill>> entry : billsByDate.entrySet()){
			Date rentalDate = entry.getKey();
			List<Bill> bills = entry.getValue();
			Double totalDisc = 0.0;
			
			for(Bill bill : bills){
				totalDisc += bill.getDiscountPrice();
			}
			
			totalDiscount.put(rentalDate,totalDisc);
		}
	}
	
	/** Calculates the total promotional discount amount for each day by summing the promotional discount prices from all bills on that day. */
	@Override
	protected void calculateTotalPromo(){
		for(Map.Entry<Date, List<Bill>> entry : billsByDate.entrySet()){
			Date rentalDate = entry.getKey();
			List<Bill> bills = entry.getValue();
			Double totalPro = 0.0;
			
			for(Bill bill : bills){
				totalPro += bill.getPromoPrice();
			}
			
			totalPromo.put(rentalDate,totalPro);
		}
	}
	
	/** Calculates the total income from rentals in narrow and wide area for each day. */
	@Override
	protected void calculateNarrowAndWideAreaIncome(){
		for(Map.Entry<Date, List<Bill>> entry : billsByDate.entrySet()){
			Date rentalDate = entry.getKey();
			List<Bill> bills = entry.getValue();
			Double totalNarrowArea = 0.0;
			Double totalWideArea = 0.0;
			
			for(Bill bill : bills){
				String area = bill.getArea().toLowerCase();
				if(area.equals("narrow")){
					totalNarrowArea += bill.getTotalPrice();
				} else if(area.equals("wide")){
					totalWideArea += bill.getTotalPrice();
				} else{
					System.out.println("Undefined rental area.");
					continue;
				}
			}
			
			totalNarrowAreaIncome.put(rentalDate,totalNarrowArea);
			totalWideAreaIncome.put(rentalDate, totalWideArea);
		}
	}
	
	/** Calculates the total maintenance cost for each day by applying the maintenance coefficient to the total income for the day. */
	@Override
	protected void calculateTotalMaintenanceCost(){
		for(Map.Entry<Date, List<Bill>> entry : billsByDate.entrySet()){
			Date rentalDate = entry.getKey();
			List<Bill> bills = entry.getValue();
			Double totalMain = 0.0;
			
			for(Bill bill : bills){
				totalMain += bill.getTotalPrice();
			}
			
			totalMaintenanceCost.put(rentalDate,totalMain  * maintenanceCoefficient);
		}
	}
	
	/** Calculates the total repair cost for each day by applying repair coefficients to the purchase prices of faulty vehicles. */
	@Override
	protected void calculateTotalRepairCost(){
		for(Map.Entry<Date, List<Bill>> entry : billsByDate.entrySet()){
			Date rentalDate = entry.getKey();
			List<Bill> bills = entry.getValue();
			Double totalRep = 0.0;
			
			for(Bill bill : bills){
				if(bill.isFault()){
					String vehicleType = bill.getVehicleType();
					switch(vehicleType.toLowerCase()){
						case "car":
							totalRep += carRepairCoefficient * bill.getVehiclePurchasePrice();
							break;
							
						case "bicycle":
							totalRep += bicycleRepairCoefficient * bill.getVehiclePurchasePrice();
							break;
							
						case "scooter":
							totalRep += scooterRepairCoefficient * bill.getVehiclePurchasePrice();
							break;
							
						default:
							System.out.println("Detected bill for undefined vehicle type: " + vehicleType);
							continue;
					}
				}
			}
			
			totalRepairCost.put(rentalDate,totalRep);
		}
	}
	
	/** Performs all necessary calculations for the daily report. */
	@Override
	protected void doAllCalculations(){
		calculateTotalIncome();
		calculateTotalDiscount();
		calculateTotalPromo();
		calculateNarrowAndWideAreaIncome();
		calculateTotalMaintenanceCost();
		calculateTotalRepairCost();
	}
	
	/**
	 * Gets the total income by date.
	 * @return map that has date as the key, and total income for the date as the value
	 */
	public Map<Date, Double> getTotalIncome(){
		return totalIncome;
	}
	
	/**
	 * Gets the total discounts by date.
	 * @return map that has date as the key, and total discount for the day as the value
	 */
	public Map<Date, Double> getTotalDiscount(){
		return totalDiscount;
	}
	
	/**
	 * Gets the total promotional discounts by date.
	 * @return map that has date as the key, and total promotional discount for the day as value
	 */
	public Map<Date, Double> getTotalPromo(){
		return totalPromo;
	}
	
	/**
	 * Gets the total income made in the narrow area by date.
	 * @return map that has date as the key, and total income from the narrow area for the day as value
	 */
	public Map<Date, Double> getTotalNarrowAreaIncome(){
		return totalNarrowAreaIncome;
	}
	
	/**
	 * Gets the total income made in the wide area by date.
	 * @return map that has date as the key, and total income from the wide area for the day as value
	 */
	public Map<Date, Double> getTotalWideAreaIncome(){
		return totalWideAreaIncome;
	}
	
	/**
	 * Gets the total maintenance cost by date.
	 * @return map that has date as the key, and total maintenance cost for the day as value
	 */
	public Map<Date, Double> getTotalMaintenanceCost(){
		return totalMaintenanceCost;
	}
	
	/**
	 * Gets the total repair cost by date.
	 * @return map that has date as the key, and total repair cost for date as value
	 */
	public Map<Date, Double> getTotalRepairCost(){
		return totalRepairCost;
	}
}
