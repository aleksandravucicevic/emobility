package emobility.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import emobility.vehicles.*;
import emobility.rental.*;

/**
 * Simulates the movement of the vehicle during the rental period, generates the bill once the rental is done, 
 * and handles charging process between rentals.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class RentSimulator implements Runnable{
	/** Manages the rental process and billing. */
	private RentalManagement rentalManagement;
	
	/** The rented vehicle. */
	private Vehicle vehicle;
	
	/** The date and time of the next rental (if there is any). */
	private Date nextRentalDate;
	
	/** The display used to show the vehicle's real-time position. */
	private MapDisplay mapDisplay;
	
	/**
	 * Constructs a {@code RentSimulator} object with the specified rental management, next rental date, and map display.
	 * @param rentalManagement {@link RentalManagement} object handling the current rental
	 * @param nextRentalDate date and time of the next rental, if there is any
	 * @param mapDisplay {@link MapDisplay} used to show the vehicle's movement
	 */
	public RentSimulator(RentalManagement rentalManagement, Date nextRentalDate, MapDisplay mapDisplay){
		this.rentalManagement = rentalManagement;
		this.vehicle = VehicleParser.getVehicle(rentalManagement.getID());
		this.nextRentalDate = nextRentalDate;
		this.mapDisplay = mapDisplay;
	}
	
	/** Runs the simulation of the vehicle's movement, generates the bill once the rental is done, and charges the vehicle. */
	@Override
	public void run(){
		simulateMovement();
		
		rentalManagement.generateBill();
		System.out.println("\nBill generated for vehicle " + vehicle.getID());
		
		if(nextRentalDate != null){
			rentalManagement.chargeVehicleUntilNextRental(vehicle);
		} else {
			vehicle.setBatteryLevel(100);
		}
	}
	
	/** Simulates the movement of the vehicle from the start location to the goal location. Updates the map display to show the vehicle's current position. */
	private void simulateMovement(){
		String startCoordinates[] = rentalManagement.getStartLocation().split(",");
		String goalCoordinates[] = rentalManagement.getGoalLocation().split(",");
		
		Integer startX = Integer.parseInt(startCoordinates[0]);
		Integer startY = Integer.parseInt(startCoordinates[1]);
		
		Integer goalX = Integer.parseInt(goalCoordinates[0]);
		Integer goalY = Integer.parseInt(goalCoordinates[1]);
		
		Point previousPosition = new Point(startX, startY);
		
		System.out.println("Vehicle " + vehicle.getID() + " starting from: (" + startX + "," + startY + ") heading to (" + goalX + "," + goalY + ")");
		
		// move the vehicle horizontally (x-axis)
		for(Integer x = startX; x != (goalX + (goalX > startX ? 1 : -1)); x += (goalX > startX ? 1 : -1)){
			Integer numOfSteps = Math.abs(goalX - startX) + Math.abs(goalY - startY);
			Integer batteryLevel = getBatteryLevel(startX,startY,x,startY,numOfSteps);
			vehicle.setBatteryLevel(batteryLevel);
			if(batteryLevel < 15){
				final Integer finalX = x;
				try{
	                SwingUtilities.invokeAndWait(() -> {
	                	mapDisplay.resetPositionColor(previousPosition);
	                	mapDisplay.markPositionAsFinished(vehicle.getID(), new Point(finalX, startY), batteryLevel);
	                	mapDisplay.removeVehicleFromPanel(vehicle.getID(), new Point(finalX, startY));
	                });
	            } catch (Exception e){
	                e.printStackTrace();
	            }
				System.out.println("Vehicle " + rentalManagement.getID() + " removed from simulation because of low battery level (" + batteryLevel +"%)");
				return;
			}
			// for validation purposes
			System.out.println("Vehicle " + rentalManagement.getID() + " moving to (" + x + "," + startY + "), battery level: " + batteryLevel +"%");
			
			final Integer finalX = x;
			try{
	            SwingUtilities.invokeAndWait(() -> {
	                mapDisplay.resetPositionColor(previousPosition);
	                mapDisplay.updateVehiclePosition(vehicle.getID(), finalX, startY, batteryLevel);
	            });
	        } catch (Exception e){
	            e.printStackTrace();
	        }
			
	        previousPosition.setLocation(finalX, startY);
			
			Long totalDuration = rentalManagement.getDuration();
			Long durationPerStep = getDurationPerStep(totalDuration,numOfSteps);
			
			try{
				Thread.sleep(durationPerStep);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		// check if fault appeared
		if(hasFaultDuringRental(vehicle)){
			try{
	            SwingUtilities.invokeAndWait(() -> {
	            	mapDisplay.resetPositionColor(previousPosition);
	            	mapDisplay.markPositionAsFaulty(vehicle.getID(), new Point(goalX, startY), vehicle.getBatteryLevel());
	            	mapDisplay.removeVehicleFromPanel(vehicle.getID(), new Point(goalX, startY));
	            	
	            });
	        } catch (Exception e){
	            e.printStackTrace();
	        }
	        System.out.println("Vehicle " + rentalManagement.getID() + " encountered a fault (" + rentalManagement.getRegisteredFault().getDescription() + ")");
	        return;
	    }
		
		// move the vehicle vertically (y-axis)
		for(Integer y = startY; y != (goalY + (goalY > startY ? 1 : -1)); y += (goalY > startY ? 1 : -1)){
			Integer numOfSteps = Math.abs(goalX - startX) + Math.abs(goalY - startY);
			Integer batteryLevel = getBatteryLevel(startX,startY,goalX,y,numOfSteps);
			vehicle.setBatteryLevel(batteryLevel);
			if(batteryLevel < 15){
				final Integer finalY = y;
				try{
	                SwingUtilities.invokeAndWait(() -> {
	                	mapDisplay.resetPositionColor(previousPosition);
	                	mapDisplay.markPositionAsFaulty(vehicle.getID(), new Point(goalX, finalY), batteryLevel);
	                	mapDisplay.removeVehicleFromPanel(vehicle.getID(), new Point(goalX, finalY));
	                });
	            } catch (Exception e){
	                e.printStackTrace();
	            }
				System.out.println("Vehicle " + rentalManagement.getID() + " removed from simulation because of low battery level (" + batteryLevel +"%)");
				return;
			}
			// for validation purposes
			System.out.println("Vehicle " + rentalManagement.getID() + " moving to (" + goalX + "," + y + "), battery level: " + batteryLevel +"%");
			
			final Integer finalY = y;
			try{
	            SwingUtilities.invokeAndWait(() -> {
	                mapDisplay.resetPositionColor(previousPosition);
	                mapDisplay.updateVehiclePosition(vehicle.getID(), goalX, finalY, batteryLevel);
	            });
	        } catch (Exception e){
	            e.printStackTrace();
	        }
			
	        previousPosition.setLocation(goalX, finalY);
			
			if(y.equals(goalY)){
				try{
	                SwingUtilities.invokeAndWait(() -> {
	                	mapDisplay.resetPositionColor(previousPosition);
	                	mapDisplay.markPositionAsFinished(vehicle.getID(), new Point(goalX, goalY), vehicle.getBatteryLevel());
	                	mapDisplay.removeVehicleFromPanel(vehicle.getID(), new Point(goalX, goalY));
	                });
	            } catch (Exception e){
	                e.printStackTrace();
	            }
			}
			
			Long totalDuration = rentalManagement.getDuration();
			Long durationPerStep = getDurationPerStep(totalDuration,numOfSteps);
			
			try{
				Thread.sleep(durationPerStep);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		try{
	        SwingUtilities.invokeAndWait(() -> mapDisplay.clearMapAfterSimulation());
	    } catch (Exception e){
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Calculates the duration of each step of the vehicle's movement, taking into account maximum speed of scooters.
	 * @param totalDuration total duration of the rental
	 * @param numOfSteps total number of steps from start to goal location
	 * @return duration per step in milliseconds
	 */
	private Long getDurationPerStep(Long totalDuration, Integer numOfSteps){
		Long durationPerStep = (totalDuration * 1000) / numOfSteps;
		
		if(vehicle.getType().equalsIgnoreCase("scooter")){
			ElectricScooter scooter = (ElectricScooter) vehicle;
			Long maxSpeed = Long.valueOf(scooter.getMaxSpeed());
			Long maxSpeedPerStep = (maxSpeed * 1000) / numOfSteps;
			return Math.min(durationPerStep, maxSpeedPerStep);
		}
		
		return durationPerStep;
	}
	
	/**
	 * Determines if a fault occurred during the rental of the vehicle.
	 * @param vehicle rented vehicle
	 * @return {@code true} if fault occurred, {@code false} otherwise 
	 */
	private Boolean hasFaultDuringRental(Vehicle vehicle){
		Fault registeredFault = rentalManagement.getRegisteredFault();
		if(registeredFault != null){
			return true;
		}
	    return false;
	}
	
	/**
	 * Calculates the vehicle's current battery level, taking into account autonomy of bicycles.
	 * @param startX starting x-coordinate
	 * @param startY starting y-coordinate
	 * @param currentX current x-coordinate
	 * @param currentY current y-coordinate
	 * @param numOfSteps total number of steps in the rental
	 * @return vehicle's battery level on the specified location
	 */
	private Integer getBatteryLevel(Integer startX, Integer startY, Integer currentX, Integer currentY, Integer numOfSteps){
		Integer currentNumOfSteps = Math.abs(currentX - startX) + Math.abs(currentY - startY);
		Integer minBatteryLevel = 5;
		
		if(vehicle.getType().equalsIgnoreCase("bicycle")){
			ElectricBicycle bicycle = (ElectricBicycle) vehicle;
			Integer possibleSteps = bicycle.getAutonomy();
			
			if(currentNumOfSteps >= possibleSteps){
				return minBatteryLevel;
			}
		}
		
		return Math.max(minBatteryLevel, 100 - (currentNumOfSteps * 33 / numOfSteps));
	}
}