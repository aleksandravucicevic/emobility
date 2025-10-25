package emobility.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Displays a grid map representing positions vehicles can have during rental simulation and controls for the simulation.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class MapDisplay extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/** The size of the grid (number of rows and columns). */
	private static final int GRID_SIZE = 20;
	
	/** The matrix representing the grid panels for each cell in the grid. */
	private JPanel[][] gridPanels;
	
	/** A map that stores the current position of each vehicle on the grid, keyed by vehicle's ID. */
	private Map<String, Point> vehiclePositions;
	
	/** A map that stores a list of vehicles' IDs at each position on the grid. */
	private Map<Point, List<String>> vehiclesPerPosition;
	
	/** The button used to return to the main display. */
	private JButton backButton;
	
	/** The main display for controlling the application. */
	private MainDisplay mainDisplay;
	
	/**
	 * Constructs a {@code MapDisplay} panel for visualizing vehicle positions on a grid.
	 * @param cardLayout {@code CardLayout} used to switch between different views
	 * @param mainPanel main panel that contains {@code CardLayout}
	 * @param mainDisplay main display of the application
	 */
	public MapDisplay(CardLayout cardLayout, JPanel mainPanel, MainDisplay mainDisplay){
		this.mainDisplay = mainDisplay;
		
		setLayout(new BorderLayout());
		JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE,GRID_SIZE));
		this.gridPanels = new JPanel[GRID_SIZE][GRID_SIZE];
		this.vehiclePositions = new HashMap<>();
		this.vehiclesPerPosition = new HashMap<>();
		
		for(int col = 0; col < GRID_SIZE; col++){
			for(int row = 0; row < GRID_SIZE; row++){
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
				JScrollPane scrollPane = new JScrollPane(panel);
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				scrollPane.setBorder(BorderFactory.createEmptyBorder());
				panel.setBorder(BorderFactory.createLineBorder(new Color(82,43,5)));
				panel.setBackground(getCellColor(row,col));
				this.gridPanels[row][col] = panel;
				gridPanel.add(scrollPane);
			}
		}
		
		add(gridPanel, BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.setBackground(new Color(232,227,223));
		
		JButton startSimulationButton = CustomButton.createStyledButton("Start Simulation",300,30);
		startSimulationButton.addActionListener(e -> startSimulation());
		
		backButton = CustomButton.createStyledButton("Back to MAIN DISPLAY",300,30);
		backButton.addActionListener(e -> cardLayout.show(mainPanel,"MainDisplay"));
		
		controlPanel.add(startSimulationButton);
		controlPanel.add(Box.createRigidArea(new Dimension(0,10)));
		controlPanel.add(backButton);
		
		add(controlPanel, BorderLayout.SOUTH);
	}
	
	/** Starts the simulation and disables the button for returning to the main display. */
	private void startSimulation(){
		backButton.setEnabled(false);
		mainDisplay.startSimulation();
	}
	
	/** Marks the simulation as finished and re-enables the button for returning to the main display. */
	public void simulationFinished(){
		SwingUtilities.invokeLater(() -> {
	        clearMapAfterSimulation();
	        backButton.setEnabled(true);
	    });
	}
	
	/**
	 * Gets the color of a specific grid cell based on its position in the grid.
	 * @param row row index of the cell
	 * @param col column index of the cell
	 * @return the color of the cell
	 */
	private Color getCellColor(int row, int col){
		if(isInWideArea(row,col)){
			return new Color(122,86,58);
		} else{
			return new Color(181,144,116);
		}
	}
	
	/**
	 * Determines if a specific cell is in the wide area of the grid.
	 * @param row row index of the cell
	 * @param col column index of the cell
	 * @return {@code true} if the cell is in the wide area of the grid, otherwise {@code false}
	 */
	private boolean isInWideArea(Integer row, Integer col){
		return ((int)row < 5 || (int)row > 14 || (int)col < 5 || (int)col > 14);
	}
	
	/**
	 * Removes a vehicle from a specific panel in the grid.
	 * @param vehicleID vehicle's ID
	 * @param position vehicle's position on the grid
	 */
	public void removeVehicleFromPanel(String vehicleID, Point position){
		JPanel panel = gridPanels[position.x][position.y];
		
		List<String> vehiclesAtPosition = vehiclesPerPosition.get(position);
		if(vehiclesAtPosition != null){
			vehiclesAtPosition.remove(vehicleID);
			panel.removeAll();
			
			for(String vehicle : vehiclesAtPosition){
				JLabel label = new JLabel(vehicle);
				panel.add(label);
			}
			
			if(vehiclesAtPosition.isEmpty()){
				vehiclesPerPosition.remove(position);
				panel.setBackground(getCellColor(position.x,position.y));
			}
		
			panel.revalidate();
			panel.repaint();
		}
	}
	
	/**
	 * Adds vehicle to a specific panel in the grid.
	 * @param vehicleID vehicle's ID
	 * @param batteryLevel vehicle's current battery level
	 * @param position vehicle's position on the grid
	 */
	private void addVehicleToPanel(String vehicleID, Integer batteryLevel, Point position){
		JPanel panel = gridPanels[position.x][position.y];
		panel.setBackground(new Color(194,179,167));
		
		vehiclesPerPosition.putIfAbsent(position, new ArrayList<>());
		
		List<String> vehiclesList = vehiclesPerPosition.get(position);
		String vehicleInfo = vehicleID + " (" + batteryLevel + "%)";
		
		if(!vehiclesList.contains(vehicleInfo)){
			vehiclesList.add(vehicleInfo);
		}
		
		panel.removeAll();
			
		for(String vehicle : vehiclesList){
			JLabel label = new JLabel(vehicle);
			label.setFont(new Font("Arial",Font.PLAIN,10));
			panel.add(label);
		}
		
		panel.revalidate();
		panel.repaint();
	}
	
	/**
	 * Updates the position of a vehicle on the grid.
	 * @param vehicleID vehicle's ID
	 * @param x x-coordinate of the vehicle's new position
	 * @param y y-coordinate of the vehicle's new position
	 * @param batteryLevel vehicle's current battery level
	 */
	public void updateVehiclePosition(String vehicleID, Integer x, Integer y, Integer batteryLevel){
		Point newPosition = new Point(x,y);
		if(vehiclePositions.containsKey(vehicleID)){
			Point oldPosition = vehiclePositions.get(vehicleID);
			resetPositionColor(oldPosition);
		}
		
		vehiclePositions.put(vehicleID, newPosition);
		addVehicleToPanel(vehicleID,batteryLevel,newPosition);
	}
	
	/** Clears the map after the simulation is finished. */
	public void clearMapAfterSimulation(){
		for(int row = 0; row < GRID_SIZE; row++){
            for(int col = 0; col < GRID_SIZE; col++){
                JPanel panel = gridPanels[row][col];
                panel.removeAll();
                panel.setBackground(getCellColor(row, col));
                panel.revalidate();
                panel.repaint();
            }
        }
	}
	
	/**
	 * Marks a specific position as faulty.
	 * @param vehicleID vehicle's ID
	 * @param position position to mark
	 * @param batteryLevel vehicle's current battery level
	 */
	public void markPositionAsFaulty(String vehicleID, Point position, Integer batteryLevel){
		JPanel panel = gridPanels[position.x][position.y];
		panel.setBackground(new Color(89,66,58));
		panel.removeAll();
		
		JLabel label = new JLabel(vehicleID + " (" + batteryLevel + "%)");
		label.setFont(new Font("Arial",Font.PLAIN,10));
		label.setForeground(Color.WHITE);
		panel.add(label);
		
		panel.revalidate();
		panel.repaint();
	}
	
	/**
	 * Marks a specific position as finished.
	 * @param vehicleID vehicle's ID
	 * @param position position to mark
	 * @param batteryLevel vehicle's current battery level
	 */
	public void markPositionAsFinished(String vehicleID, Point position, Integer batteryLevel){
		JPanel panel = gridPanels[position.x][position.y];
		panel.setBackground(new Color(74,36,13));
		panel.removeAll();
		
		JLabel label = new JLabel(vehicleID + " (" + batteryLevel + "%)");
		label.setFont(new Font("Arial",Font.PLAIN,10));
		label.setForeground(Color.WHITE);
		panel.add(label);
		
		panel.revalidate();
		panel.repaint();
	}
	
	/**
	 * Resets the color of a specific position in the grid to its default color.
	 * @param position position to reset
	 */
	public void resetPositionColor(Point position){
		JPanel panel = gridPanels[position.x][position.y];
		panel.setBackground(getCellColor(position.x,position.y));
		panel.removeAll();
		vehiclesPerPosition.remove(position);
		panel.revalidate();
		panel.repaint();
	}
}
