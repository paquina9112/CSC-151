import java.util.Scanner;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete Pad Estimating Application
 * Helps Jim's construction business calculate concrete pad project estimates
 * Includes cost calculations, labor estimation, and CSV file storage
 */
public class M5_Project_Paquin_Alexis {
    
    // Cost calculation constants
    private static final double CONCRETE_COST = 150.0;  // Cost per cubic yard of concrete
    private static final double LABOR_COST = 45.0;      // Cost per hour of labor
    private static final String CSV_FILE = "concrete_estimates.csv";  // File for storing estimates
    
    // Variables to store project information
    private String projectName, projectLocation;
    private double padLength, padWidth, slabThickness;
    private int numberOfEmployees;
    private double estimatedLaborHours;
    
    public static void main(String[] args) {
        new M5_Project_Paquin_Alexis().run();
    }
    
    /**
     * Main workflow: collects project data, displays estimate, and offers save/view options
     */
    private void run() {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("CONCRETE PAD ESTIMATING APPLICATION");
        System.out.println("====================================\n");
        
        // Collect project details from user
        projectName = input(sc, "Project Name: ");
        projectLocation = input(sc, "Project Location: ");
        
        // Collect concrete pad dimensions (length, width, thickness)
        padLength = doubleInput(sc, "Pad Length (feet): ");
        padWidth = doubleInput(sc, "Pad Width (feet): ");
        slabThickness = doubleInput(sc, "Slab Thickness (inches): ");
        
        // Collect labor information
        numberOfEmployees = intInput(sc, "Number of Employees: ");
        
        // Calculate work hours: (Length × Width / 100) estimates 100 sq ft per hour, 
        // then multiply by number of employees for total work hours
        estimatedLaborHours = (padLength * padWidth / 100.0) * numberOfEmployees;
        
        // Display the complete estimate
        display();
        
        // Ask if user wants to save to file
        System.out.print("Save to file? (y/n): ");
        if (sc.nextLine().trim().toLowerCase().startsWith("y")) {
            saveCSV();
        }
        
        // Ask if user wants to view previous estimates
        System.out.print("View previous estimates? (y/n): ");
        if (sc.nextLine().trim().toLowerCase().startsWith("y")) {
            showCSV();
        }
        
        sc.close();
    }
    
    /**
     * Gets a string input from user
     * @param sc Scanner for input
     * @param prompt Message to display to user
     * @return User's input as string
     */
    private String input(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }
    
    /**
     * Gets a positive double value from user with validation
     * Reprompts if user enters invalid or negative number
     * @param sc Scanner for input
     * @param prompt Message to display to user
     * @return Valid positive double value
     */
    private double doubleInput(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double val = Double.parseDouble(sc.nextLine().trim());
                if (val > 0) return val;
                System.out.println("Enter a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }
    
    /**
     * Gets a positive integer value from user with validation
     * Reprompts if user enters invalid or negative number
     * @param sc Scanner for input
     * @param prompt Message to display to user
     * @return Valid positive integer value
     */
    private int intInput(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val > 0) return val;
                System.out.println("Enter a positive number.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }
    }
    
    /**
     * Calculates concrete volume in cubic yards
     * Formula: (Length × Width × Thickness in feet) / 27 cubic feet per cubic yard
     * @return Volume of concrete needed in cubic yards
     */
    private double concreteVolume() {
        return padLength * padWidth * (slabThickness / 12.0) / 27.0;
    }
    
    /**
     * Calculates total concrete material cost
     * @return Concrete volume × cost per cubic yard
     */
    private double concreteCost() {
        return concreteVolume() * CONCRETE_COST;
    }
    
    /**
     * Calculates total labor cost
     * @return Labor hours × cost per hour
     */
    private double laborCost() {
        return estimatedLaborHours * LABOR_COST;
    }
    
    /**
     * Calculates total project cost (concrete + labor)
     * @return Sum of concrete cost and labor cost
     */
    private double totalCost() {
        return concreteCost() + laborCost();
    }
    
    /**
     * Displays the estimate report with all project details and costs
     * Shows project info, dimensions, concrete requirements, labor info, and total cost
     */
    private void display() {
        double area = padLength * padWidth;
        
        System.out.println("\nESTIMATE REPORT");
        System.out.println("====================================");
        System.out.println("Project: " + projectName);
        System.out.println("Location: " + projectLocation);
        
        // Display area calculations
        System.out.println("\nAREA:");
        System.out.printf("  Length: %.2f ft, Width: %.2f ft%n", padLength, padWidth);
        System.out.printf("  Total Area: %.2f sq ft%n", area);
        System.out.printf("  Thickness: %.2f in (%.3f ft)%n", slabThickness, slabThickness / 12.0);
        
        // Display concrete requirements and cost
        System.out.println("\nCONCRETE:");
        System.out.printf("  Volume: %.2f cubic yards%n", concreteVolume());
        System.out.printf("  Cost: $%.2f%n", concreteCost());
        
        // Display labor information and cost
        System.out.println("\nLABOR:");
        System.out.printf("  Employees: %d%n", numberOfEmployees);
        System.out.printf("  Work Hours: %.2f%n", estimatedLaborHours);
        System.out.printf("  Manpower Hours: %.2f%n", estimatedLaborHours * numberOfEmployees);
        System.out.printf("  Cost: $%.2f%n", laborCost());
        
        // Display total project cost
        System.out.println("\nTOTAL PROJECT COST: $" + String.format("%.2f", totalCost()));
        System.out.println("====================================\n");
    }
    
    /**
     * Saves the current estimate to a CSV file for record-keeping
     * Creates file with headers on first save, appends data on subsequent saves
     * Stores: timestamp, project info, dimensions, volumes, hours, and costs
     */
    private void saveCSV() {
        try {
            File file = new File(CSV_FILE);
            boolean isNew = !file.exists();  // Check if this is first save
            
            // Open file for appending (not overwriting)
            BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true));
            
            // Add header row if creating new file
            if (isNew) {
                writer.write("Timestamp,Project,Location,Length,Width,Area,Thickness,Volume,Employees,Work Hours,Manpower Hours,Concrete Cost,Labor Cost,Total Cost");
                writer.newLine();
            }
            
            // Prepare values to save
            double area = padLength * padWidth;
            double manpowerHours = estimatedLaborHours * numberOfEmployees;
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            // Write data row to CSV file
            writer.write(String.format("%s,\"%s\",\"%s\",%.2f,%.2f,%.2f,%.2f,%.2f,%d,%.2f,%.2f,%.2f,%.2f,%.2f",
                    timestamp, projectName, projectLocation, padLength, padWidth, area, slabThickness,
                    concreteVolume(), numberOfEmployees, estimatedLaborHours, manpowerHours,
                    concreteCost(), laborCost(), totalCost()));
            writer.newLine();
            writer.close();
            
            System.out.println("Saved to " + CSV_FILE);
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }
    
    /**
     * Displays all previously saved estimates from the CSV file
     * Shows project name, work hours, manpower hours, and total cost for each estimate
     */
    private void showCSV() {
        try {
            File file = new File(CSV_FILE);
            
            // Check if CSV file exists, if not there are no previous estimates
            if (!file.exists()) {
                System.out.println("No estimates found.");
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE));
            String line = reader.readLine(); // Skip header row
            
            // Display header for the table
            System.out.println("\nPREVIOUS ESTIMATES:");
            System.out.println("====================================");
            System.out.printf("%-20s %-15s %-12s %-15s%n", "Project", "Work Hours", "Manpower Hrs", "Total Cost");
            System.out.println("------------------------------------");
            
            // Read and display each estimate from file
            int count = 0;
            while ((line = reader.readLine()) != null) {
                // Split CSV line by comma, but respect quoted values
                String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                
                // Extract relevant fields: project (index 1), work hours (9), manpower hours (10), total cost (13)
                if (parts.length >= 14) {
                    count++;
                    System.out.printf("%-20s %-12s %-15s $%-12s%n", 
                            parts[1].replaceAll("\"", ""), parts[9], parts[10], parts[13]);
                }
            }
            
            reader.close();
            
            // Show summary
            System.out.println("------------------------------------");
            System.out.println("Total: " + count + " estimates\n");
        } catch (IOException e) {
            System.out.println("Error reading: " + e.getMessage());
        }
    }
}

