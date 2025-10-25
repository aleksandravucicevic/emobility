package emobility.gui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.text.*;
import emobility.reporting.*;

/**
 * Displays business results, including summary reports, daily reports, and loss analysis.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class BusinessResultsDisplay extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/** The daily reports generator for calculating daily financial data. */
	private DailyReportGenerator dailyReports;
	
	/** The summary report generator for calculating overall financial data. */
	private SummaryReportGenerator summaryReport;
	
	/** The loss analysis for determining vehicles with the greatest losses. */
	private LossAnalysis lossAnalysis;
	
	/**
	 * Constructs a {@code BusinessResultsDisplay} panel that contains tabs for summary report, daily reports and loss analysis.
	 * @param dailyReports daily report generator for daily financial data
	 * @param summaryReport summary report generator for summary financial data
	 * @param lossAnalysis loss analysis for vehicles with the greatest losses
	 * @param cardLayout {@code CardLayout} used to switch between different views
	 * @param mainPanel main panel that contains the {@code CardLayout}
	 */
	public BusinessResultsDisplay(DailyReportGenerator dailyReports, SummaryReportGenerator summaryReport, LossAnalysis lossAnalysis, CardLayout cardLayout, JPanel mainPanel){
		this.dailyReports = dailyReports;
		this.summaryReport = summaryReport;
		this.lossAnalysis = lossAnalysis;
		
		setLayout(new BorderLayout());
		setBackground(new Color(232,227,223));
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		tabbedPane.addTab("Summary Report", createSummaryReportPanel());
		tabbedPane.addTab("Daily Reports", createDailyReportsPanel());
		tabbedPane.addTab("Loss Analysis", createLossAnalysisPanel());
		tabbedPane.setBackground(new Color(179,156,139));
		
		JButton backButton = CustomButton.createStyledButton("Back to MAIN MENU",300,30);
		backButton.addActionListener(e -> cardLayout.show(mainPanel,"MainMenu"));
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(232,227,223));
		backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainDisplay"));
		buttonPanel.add(backButton);
		
		add(tabbedPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Adds a label to the specified panel with the given text and value.
	 * @param panel panel to which the label is added
	 * @param labelText text for the label
	 * @param value value to display in the label
	 */
	private void addLabelToPanel(JPanel panel, String labelText, Object value){
	    String formattedValue;
	    if(value instanceof Number){
	    	formattedValue = String.format("%.2f", ((Number)value).doubleValue());
	    } else{
	    	formattedValue = value.toString();
	    }
		
		JLabel label = new JLabel(labelText + formattedValue);
	    label.setFont(new Font("Arial",Font.PLAIN,14));
	    panel.add(label);
	    panel.add(Box.createRigidArea(new Dimension(0,5)));
	}
	
	/**
	 * Creates a panel that displays the summary report.
	 * @return {@code JPanel} containing the summary report
	 */
	private JPanel createSummaryReportPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		panel.setBackground(new Color(232,227,223));
		
		addLabelToPanel(panel,"Total Income: ",summaryReport.getTotalIncome());
		addLabelToPanel(panel,"Total Discount: ",summaryReport.getTotalDiscount());
		addLabelToPanel(panel,"Total Promo: ",summaryReport.getTotalPromo());
		addLabelToPanel(panel,"Narrow Area Income: ",summaryReport.getNarrowAreaIncome());
		addLabelToPanel(panel,"Wide Area Income: ",summaryReport.getWideAreaIncome());
		addLabelToPanel(panel,"Total Maintenance Cost: ",summaryReport.getTotalMaintenanceCost());
		addLabelToPanel(panel,"Total Repair Cost: ",summaryReport.getTotalRepairCost());
		addLabelToPanel(panel,"Total Expense Cost: ",summaryReport.getTotalExpenseCost());
		addLabelToPanel(panel,"Total Tax Cost: ",summaryReport.getTotalTaxCost());
		
		return panel;
	}
	
	/**
	 * Creates a panel that displays the daily reports in form of a table.
	 * @return {@code JPanel} containing the daily reports
	 */
	private JPanel createDailyReportsPanel(){
		JPanel panel = new JPanel(new BorderLayout());
		Map<Date, Double> dailyIncomes = dailyReports.getTotalIncome();
		Map<Date, Double> totalDiscount = dailyReports.getTotalDiscount();
		Map<Date, Double> totalPromo = dailyReports.getTotalPromo();
		Map<Date, Double> totalNarrowAreaIncome = dailyReports.getTotalNarrowAreaIncome();
		Map<Date, Double> totalWideAreaIncome = dailyReports.getTotalWideAreaIncome();
		Map<Date, Double> totalMaintenanceCost = dailyReports.getTotalMaintenanceCost();
		Map<Date, Double> totalRepairCost = dailyReports.getTotalRepairCost();
		
		String columnNames[] = {"Date","Total Income","Total Discount","Total Promo","Narrow Area Income","Wide Area Income","Maintenance Cost","Repair Cost"};
		Object data[][] = new Object[dailyIncomes.size()][columnNames.length];
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.");
		
		int i = 0;
		for(Date date : dailyIncomes.keySet()){
			data[i][0] = sdf.format(date);
			data[i][1] = String.format("%.2f",dailyIncomes.get(date));
			data[i][2] = String.format("%.2f",totalDiscount.get(date));
			data[i][3] = String.format("%.2f",totalPromo.get(date));
			data[i][4] = String.format("%.2f",totalNarrowAreaIncome.get(date));
			data[i][5] = String.format("%.2f",totalWideAreaIncome.get(date));
			data[i][6] = String.format("%.2f",totalMaintenanceCost.get(date));
			data[i][7] = String.format("%.2f",totalRepairCost.get(date));
			
			i++;
		}
		
		JTable table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getViewport().setBackground(new Color(232,227,223));
		panel.add(scrollPane,BorderLayout.CENTER);
		return panel;
	}
	
	/**
	 * Creates a panel that displays the loss analysis report.
	 * @return {@code JTable} containing the loss analysis report
	 */
	private JPanel createLossAnalysisPanel(){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		panel.setBackground(new Color(232,227,223));
		
		if(lossAnalysis.getCarWithGreatestLoss() != null){
			addLabelToPanel(panel,"Car with the greatest loss: ",lossAnalysis.getCarWithGreatestLoss().vehicleToString());
			addLabelToPanel(panel,"  loss the car produced: ",lossAnalysis.getMaxCarLoss());
		} else{
			addLabelToPanel(panel, "Car with the greatest loss: ","No data available");
		}
		
		if(lossAnalysis.getBicycleWithGreatestLoss() != null){
			addLabelToPanel(panel,"Bicycle with the greatest loss: ",lossAnalysis.getBicycleWithGreatestLoss().vehicleToString());
			addLabelToPanel(panel,"  loss the bicycle produced: ",lossAnalysis.getMaxBicycleLoss());
		} else{
			addLabelToPanel(panel,"Bicycle with the greatest loss: ","No data available");
		}
		
		if(lossAnalysis.getScooterWithGreatestLoss() != null){
			addLabelToPanel(panel,"Scooter with the greatest loss: ",lossAnalysis.getScooterWithGreatestLoss().vehicleToString());
			addLabelToPanel(panel,"  loss the scooter produced: ",lossAnalysis.getMaxScooterLoss());
		} else{
			addLabelToPanel(panel,"Scooter with the greatest loss: ","No data available");
		}
		
		return panel;
	}
}
