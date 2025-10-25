package emobility.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import emobility.vehicles.*;

/**
 * Displays a table with vehicle's faults.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class FaultsDisplay extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a {@code FaultsDisplay} panel with a table showing vehicle's faults.
	 * @param vehicleFaultsMap map of vehicles and their corresponding faults
	 * @param cardLayout {@code CardLayout} used to switch between different views
	 * @param mainPanel main panel that contains the {@code CardLayout}
	 */
	public FaultsDisplay(Map<Vehicle, List<Fault>> vehicleFaultsMap, CardLayout cardLayout, JPanel mainPanel){
		setLayout(new BorderLayout());
		setBackground(new Color(232,227,223));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
		
		String columnNames[] = {"Vehicle Type","Vehicle ID","Fault Time","Fault Description"};
		DefaultTableModel model = new DefaultTableModel(columnNames,0);
		
		for(Map.Entry<Vehicle, List<Fault>> entry : vehicleFaultsMap.entrySet()){
			Vehicle vehicle = entry.getKey();
			List<Fault> faults = entry.getValue();
			
			for(Fault fault : faults){
				Object rowData[] = {vehicle.getType(),vehicle.getID(),sdf.format(fault.getDateTime()),fault.getDescription()};
				
				model.addRow(rowData);
			}
		}
		
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getViewport().setBackground(new Color(232,227,223));
		
		JButton backButton = CustomButton.createStyledButton("Back to MAIN MENU",300,30);
		backButton.addActionListener(e -> cardLayout.show(mainPanel,"MainMenu"));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(232,227,223));
		backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainDisplay"));
		buttonPanel.add(backButton);
		
		add(scrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
}
