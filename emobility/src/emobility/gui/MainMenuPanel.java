package emobility.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Represents the main menu of the application, providing options to view all existing vehicles, city map, 
 * faults that occurred during rentals, and business results.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class MainMenuPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a {@code MainMenuPanel} with buttons for navigating to different views.
	 * @param cardLayout {@code CardLayout} used to switch between different views
	 * @param mainPanel main panel that contains the {@code CardLayout}
	 */
	public MainMenuPanel(CardLayout cardLayout, JPanel mainPanel){
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
		setBackground(new Color(232,227,223));
		
		JButton mapViewButton = CustomButton.createStyledButton("View City Map",200,50);
		mapViewButton.addActionListener(e -> cardLayout.show(mainPanel,"MapDisplay"));
		
		JButton vehiclesViewButton = CustomButton.createStyledButton("View Vehicles",200,50);
		vehiclesViewButton.addActionListener(e -> cardLayout.show(mainPanel,"VehiclesDisplay"));
		
		JButton faultsViewButton = CustomButton.createStyledButton("View Faults",200,50);
		faultsViewButton.addActionListener(e -> cardLayout.show(mainPanel, "FaultsDisplay"));
		
		JButton resultsButton = CustomButton.createStyledButton("View Business Results",200,50);
		resultsButton.addActionListener(e -> cardLayout.show(mainPanel, "BusinessDisplay"));
		
		add(Box.createVerticalGlue());
		add(vehiclesViewButton);
		add(Box.createRigidArea(new Dimension(0,20)));
		add(mapViewButton);
		add(Box.createRigidArea(new Dimension(0,20)));
		add(faultsViewButton);
		add(Box.createRigidArea(new Dimension(0,20)));
		add(resultsButton);
		add(Box.createVerticalGlue());
	}
}
