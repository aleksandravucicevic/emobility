package emobility.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import emobility.vehicles.*;
import emobility.rental.*;
import emobility.reporting.*;
import emobility.utility.*;

/**
 * Serves as the central hub for the eMobility application, managing the user interface, 
 * and orchestrating the simulation of vehicles' rentals. It initializes the main components of the application, 
 * including the map display, vehicles display, and rental reports.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class MainDisplay{
	/** The main frame. */
	private JFrame frame;
	
	/** The {@code CardLayout} used to switch between different panels in the main panel. */
	private CardLayout cardLayout;
	
	/** The main panel. */
	private JPanel mainPanel;
	
	/** The map display where vehicles' movements are visualized. */
	private MapDisplay mapDisplay;
	
	/** Indicates whether a simulation is currently running. */
	private Boolean simulationRunning = false;
	
	/** The pricing properties file path. */
	private String pricingPropertiesPath;
	
	/** The rental data file path. */
	private String rentalsFilePath;
	
	/** The directory path where the bills are stored. */
	private String billsDirectoryPath;
	
	/** The directory path of loss analysis data. */
	private String lossAnalysisDirectoryPath;
	
	/**
	 * Constructs the {@code MainDisplay} object, initializing the user interface and loading initial data. 
	 * The main components include the map display, vehicles display, and simulation control.
	 */
	public MainDisplay(){
		this.frame = new JFrame("eMobility App");
		this.cardLayout = new CardLayout();
		this.mainPanel = new JPanel(cardLayout);
		
		this.mapDisplay = new MapDisplay(cardLayout, mainPanel, this);
		
		mainPanel.add(new MainMenuPanel(cardLayout, mainPanel),"MainDisplay");
		mainPanel.add(mapDisplay, "MapDisplay");
		
		String configFilePath = "src/emobility/utility/config.properties";
		PropertiesReader config = new PropertiesReader(configFilePath);
		
		String vehiclesFilePath = config.getProperty("VEHICLES_FILE_PATH");
		this.rentalsFilePath = config.getProperty("RENTALS_FILE_PATH");
		this.pricingPropertiesPath = config.getProperty("PRICING_PROPERTIES_FILE_PATH");
		this.billsDirectoryPath = config.getProperty("BILLS_DIRECTORY");
		this.lossAnalysisDirectoryPath = config.getProperty("LOSS_ANALYSIS_DIRECTORY");
		
		VehicleParser.parseVehicleCSV(vehiclesFilePath);
		List<Car> cars = VehicleParser.getCars();
        List<ElectricBicycle> bicycles = VehicleParser.getBicycles();
        List<ElectricScooter> scooters = VehicleParser.getScooters();
        mainPanel.add(new VehiclesDisplay(cars,bicycles,scooters,cardLayout, mainPanel),"VehiclesDisplay");
        
        RentalParser.parseRentalCSV(rentalsFilePath);
        Rental.processRentals();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mainPanel);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(800,800);
		frame.setVisible(true);
	}
	
	/**
	 * Starts the simulation of vehicles' rentals. If simulation is already running, an info message is shown. 
	 * The simulation runs in a separate thread, allowing the user interface to remain responsive.
	 */
	public void startSimulation(){
		if(simulationRunning){
			JOptionPane.showMessageDialog(null, "Simulation is already running!");
            return;
		}
		
		simulationRunning = true;
		cardLayout.show(mainPanel, "MapDisplay");
		
		new Thread(() -> {
            runSimulation(pricingPropertiesPath, billsDirectoryPath);
            Map<Vehicle, List<Fault>> vehicleFaultsMap = Rental.getVehicleFaultsMap();
            initializeFaultsDisplay(vehicleFaultsMap);
            processSimulationResults(billsDirectoryPath, lossAnalysisDirectoryPath);
            simulationRunning = false;
    		SwingUtilities.invokeLater(() -> {
                mapDisplay.simulationFinished(); 
                JOptionPane.showMessageDialog(null, "Simulation finished.");
            });
        }).start();
	}
	
	/**
	 * Runs the simulation of vehicles' rentals, processing rentals grouped by date and time. 
	 * Each rental is simulated in a separate thread.
	 * @param pricingPropertiesPath the pricing properties file path
	 * @param billsDirectoryPath the directory where the bills are stored
	 */
	private void runSimulation(String pricingPropertiesPath, String billsDirectoryPath){
		Map<Date, List<List<Rental>>> groupedRentalsByDateAndTime = RentalManagement.getRentalsGroupedByDateAndTime();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
		SimpleDateFormat rentalDateTimeFormat = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
		
		for(Map.Entry<Date, List<List<Rental>>> entry : groupedRentalsByDateAndTime.entrySet()){
			Date rentalDate = entry.getKey();
			
			System.out.println("\nProcessing rentals for: " + dateFormat.format(rentalDate) + "\n");
            List<List<Rental>> rentalsGroupedByTime = entry.getValue();
            
            for(List<Rental> rentalsAtSameTime : rentalsGroupedByTime){
            	List<Thread> threads = new ArrayList<>();
                            	
            	for(Rental rental : rentalsAtSameTime){
            		RentalManagement rentalManagement = new RentalManagement(pricingPropertiesPath,billsDirectoryPath,rental);
            		System.out.println(" Date/Time: " + rentalDateTimeFormat.format(rental.getRentalDateTime()) + " - vehicle: " + rental.getID() + "\n");
            		            		
            		Date nextRentalDate = getNextRentalDate(rentalsGroupedByTime,rentalManagement);
                    Thread rentalThread = new Thread(new RentSimulator(rentalManagement, nextRentalDate, mapDisplay));
                    rentalThread.start();
                    threads.add(rentalThread);
            	}
                
            	// wait for all threads in the current time group to finish
                for (Thread thread : threads){
                	try {
                		thread.join();
                	} catch(InterruptedException e){
                		e.printStackTrace();
                	}
                }
                
                try{
                    Thread.sleep(5000);
                } catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
	}
	
	/**
	 * Determines the next rental date and time after the current rental.
	 * @param allRentalsAtSameDay list of all rentals happening on the same day
	 * @param currentRental rental currently being processed
	 * @return date and time of the next rental, or {@code null} if there is none
	 */
	private static Date getNextRentalDate(List<List<Rental>> allRentalsAtSameDay, RentalManagement currentRental){
		Date currentRentalDateTime = currentRental.getRentalDateTime();
		Date currentRentalTime = FormatDate.getTimeOnlyCalendar(currentRentalDateTime);
		Date closestNextRentalDateTime = null;
		
		for(List<Rental> rentalsAtSameTime : allRentalsAtSameDay){
			for(Rental rental : rentalsAtSameTime){
				Date rentalDateTime = rental.getRentalDateTime();
				Date rentalTime = FormatDate.getTimeOnlyCalendar(rentalDateTime);
				
				if(FormatDate.isSameDay(currentRentalDateTime,rentalDateTime) && currentRentalTime.before(rentalTime)){
					Date closestNextRentalTime = FormatDate.getTimeOnlyCalendar(closestNextRentalDateTime);
					
					if(closestNextRentalDateTime == null || rentalTime.before(closestNextRentalTime)){
						closestNextRentalDateTime = rentalDateTime;
						return closestNextRentalDateTime;
					}
				}
			}
		}
		
		return closestNextRentalDateTime;
	}
	
	/**
	 * Initializes the display showing vehicles' faults.
	 * @param vehicleFaultsMap map of vehicles and faults associated with them
	 */
	private void initializeFaultsDisplay(Map<Vehicle, List<Fault>> vehicleFaultsMap){
		mainPanel.add(new FaultsDisplay(vehicleFaultsMap,cardLayout, mainPanel),"FaultsDisplay");
		cardLayout.show(mainPanel, "MainDisplay");
	}
	
	/**
	 * Processes the simulation results by generating reports and displaying them.
	 * @param billsDirectoryPath the directory where the bills are stored
	 * @param lossAnalysisDirectoryPath the directory where the loss analysis data is stored
	 */
	private void processSimulationResults(String billsDirectoryPath, String lossAnalysisDirectoryPath){
		BillParser.parseBills(billsDirectoryPath);
		
		DailyReportGenerator dailyReports = new DailyReportGenerator();
		SummaryReportGenerator summaryReport = new SummaryReportGenerator();
		LossAnalysis lossAnalysis = new LossAnalysis(lossAnalysisDirectoryPath);
		lossAnalysis.serializeReport();
		LossAnalysis lossAnalysisDeserialized = lossAnalysis.deserializeReport(lossAnalysisDirectoryPath);
		
		mainPanel.add(new BusinessResultsDisplay(dailyReports,summaryReport,lossAnalysisDeserialized,cardLayout, mainPanel),"BusinessDisplay");
		cardLayout.show(mainPanel, "MainDisplay");
	}
    
	/**
	 * the main method that launches the eMobility application.
	 * @param args command-line arguments (not used)
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainDisplay::new);
    }
}
