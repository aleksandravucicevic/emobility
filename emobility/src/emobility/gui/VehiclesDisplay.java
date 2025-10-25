package emobility.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import emobility.vehicles.*;

/**
 * Displays tables with information about cars, electric bicycles, and electric scooters.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class VehiclesDisplay extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a {@code VehiclesDisplay} panel with tables showing all available vehicles.
	 * @param cars list of cars to display
	 * @param bicycles list of bicycles to display
	 * @param scooters list of scooters to display
	 * @param cardLayout {@code CardLayout} used to switch between different views
	 * @param mainPanel main panel that contains the {@code CardLayout}
	 */
	public VehiclesDisplay(List<Car> cars, List<ElectricBicycle> bicycles, List<ElectricScooter> scooters, CardLayout cardLayout, JPanel mainPanel){
		setLayout(new BorderLayout());
		setBackground(new Color(232,227,223));
		
		JPanel tablesPanel = new JPanel(new GridLayout(3,1));
		tablesPanel.setBackground(new Color(232,227,223));
		
		JTable carsTable = createCarsTable(cars);
		JTable bicyclesTable = createBicyclesTable(bicycles);
		JTable scootersTable = createScootersTable(scooters);
		
		JScrollPane carsScrollPane = new JScrollPane(carsTable);
		carsScrollPane.getViewport().setBackground(new Color(232,227,223));
		JScrollPane bicyclesScrollPane = new JScrollPane(bicyclesTable);
		bicyclesScrollPane.getViewport().setBackground(new Color(232,227,223));
		JScrollPane scootersScrollPane = new JScrollPane(scootersTable);
		scootersScrollPane.getViewport().setBackground(new Color(232,227,223));
		
		JPanel carsPanel = new JPanel(new BorderLayout());
		carsPanel.setBorder(BorderFactory.createTitledBorder("Cars"));
		carsPanel.add(carsScrollPane,BorderLayout.CENTER);
		carsPanel.setBackground(new Color(232,227,223));
		
		JPanel bicyclesPanel = new JPanel(new BorderLayout());
		bicyclesPanel.setBorder(BorderFactory.createTitledBorder("Electric Bicycles"));
		bicyclesPanel.add(bicyclesScrollPane,BorderLayout.CENTER);
		bicyclesPanel.setBackground(new Color(232,227,223));
		
		JPanel scootersPanel = new JPanel(new BorderLayout());
		scootersPanel.setBorder(BorderFactory.createTitledBorder("Electric Scooters"));
		scootersPanel.add(scootersScrollPane,BorderLayout.CENTER);
		scootersPanel.setBackground(new Color(232,227,223));
		
		tablesPanel.add(carsPanel);
		tablesPanel.add(bicyclesPanel);
		tablesPanel.add(scootersPanel);
		
		JButton backButton = CustomButton.createStyledButton("Back to MAIN MENU",300,30);
		backButton.addActionListener(e -> cardLayout.show(mainPanel,"MainMenu"));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(232,227,223));
		backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainDisplay"));
		buttonPanel.add(backButton);
		
		add(tablesPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Creates a JTable to display car data.
	 * @param cars list of cars to display
	 * @return {@code JTable} containing car data
	 */
	private JTable createCarsTable(List<Car> cars){
		String columnNames[] = {"ID","Model","Manufcturer","Battery Level","Purchase Price","Purchase Date","Description","More Seats"};
		DefaultTableModel model = new DefaultTableModel(columnNames,0);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
		
		for(Car car : cars){
			Object rowData[] = {car.getID(),car.getModel(),car.getManufacturer(),car.getBatteryLevel(),String.format("%.2f",car.getPurchasePrice()),sdf.format(car.getPurchaseDate()),car.getDescription(),car.getMoreSeats()};
			model.addRow(rowData);
		}
		
		return new JTable(model);
	}
	
	/**
	 * Creates a JTable to display bicycle data.
	 * @param bicycles list of bicycles to display
	 * @return {@code JTable} containing bicycle data
	 */
	private JTable createBicyclesTable(List<ElectricBicycle> bicycles){
		String columnNames[] = {"ID","Model","Manufcturer","BatteryLevel","Purchase Price","Autonomy"};
		DefaultTableModel model = new DefaultTableModel(columnNames,0);
		
		for(ElectricBicycle bicycle : bicycles){
			Object rowData[] = {bicycle.getID(),bicycle.getModel(),bicycle.getManufacturer(),bicycle.getBatteryLevel(),String.format("%.2f",bicycle.getPurchasePrice()),bicycle.getAutonomy()};
			model.addRow(rowData);
		}
		
		return new JTable(model);
	}
	
	/**
	 * Creates a JTable to display scooter data.
	 * @param scooters list of scooters to display
	 * @return {@code JTable} containing scooter data
	 */
	private JTable createScootersTable(List<ElectricScooter> scooters){
		String columnNames[] = {"ID","Model","Manufcturer","BatteryLevel","Purchase Price","Maximum Speed"};
		DefaultTableModel model = new DefaultTableModel(columnNames,0);
		
		for(ElectricScooter scooter : scooters){
			Object rowData[] = {scooter.getID(),scooter.getModel(),scooter.getManufacturer(),scooter.getBatteryLevel(),String.format("%.2f",scooter.getPurchasePrice()),scooter.getMaxSpeed()};
			model.addRow(rowData);
		}
		
		return new JTable(model);
	}
}
