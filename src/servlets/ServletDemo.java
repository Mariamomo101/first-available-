package servlets;

import models.Student;
import models.Organizer;
import java.util.Arrays;
import java.util.List;

/**
 * Demo class to simulate servlet functionality without requiring servlet container
 * Demonstrates how the servlets would process form data and create model objects
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public class ServletDemo {
    
    /**
     * Main method to run servlet demonstrations
     */
    public static void main(String[] args) {
        System.out.println("=== SkillSync Servlet Demo ===\n");
        
        // Demo student registration
        demonstrateStudentRegistration();
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Demo organizer request
        demonstrateOrganizerRequest();
    }
    
    /**
     * Demonstrates student registration servlet functionality
     */
    private static void demonstrateStudentRegistration() {
        System.out.println("DEMONSTRATING STUDENT REGISTRATION SERVLET");
        System.out.println("=" + "=".repeat(40));
        
        // Simulate form data (normally from request.getParameter())
        String name = "Alice Johnson";
        String email = "alice.johnson@university.edu";
        String department = "Computer Science";
        String semesterStr = "6";
        String cvLink = "https://linkedin.com/in/alicejohnson";
        String skillsStr = "Java Programming, Web Development, Database Design, Problem Solving";
        String[] interestAreasArray = {"Programming", "Design"};
        
        try {
            // Parse semester
            int semester = Integer.parseInt(semesterStr);
            
            // Process skills
            List<String> skillsList = Arrays.asList(skillsStr.split(","));
            // Trim whitespace
            skillsList.replaceAll(String::trim);
            
            // Process interest areas
            List<String> interestAreasList = Arrays.asList(interestAreasArray);
            
            // Create Student object (simulating servlet logic)
            Student student = new Student(name.trim(), email.trim(), department.trim(), semester,
                                        skillsList, cvLink.trim(), interestAreasList);
            
            // Log the registration (simulating servlet logging)
            System.out.println("Student registration processed successfully!");
            System.out.println("Generated Student Object:");
            student.displayProfile();
            
            System.out.println("\n✅ SUCCESS: Student would be saved to database");
            System.out.println("✅ SUCCESS: Confirmation email would be sent to " + student.getEmail());
            System.out.println("✅ SUCCESS: User would see success page");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates organizer request servlet functionality
     */
    private static void demonstrateOrganizerRequest() {
        System.out.println("DEMONSTRATING ORGANIZER REQUEST SERVLET");
        System.out.println("=" + "=".repeat(41));
        
        // Simulate form data (normally from request.getParameter())
        String name = "Dr. Sarah Wilson";
        String email = "sarah.wilson@techcorp.com";
        String department = "Engineering";
        String organizationName = "TechCorp Solutions";
        String requiredSkillsStr = "Java Programming, Team Work, Communication, Leadership";
        String preferredSemester = "5-6";
        String organizationType = "corporate";
        String[] eventTypesArray = {"Workshops", "Seminars", "Internships"};
        String eventDescription = "We plan to organize technical workshops and internship programs for advanced students.";
        String experience = "10+ years in software development and team management.";
        String termsAgreement = "agreed";
        
        try {
            // Validate required fields
            if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                department == null || department.trim().isEmpty() ||
                organizationName == null || organizationName.trim().isEmpty() ||
                requiredSkillsStr == null || requiredSkillsStr.trim().isEmpty() ||
                !termsAgreement.equals("agreed")) {
                
                throw new IllegalArgumentException("Required fields missing or terms not agreed");
            }
            
            // Process required skills
            List<String> requiredSkillsList = Arrays.asList(requiredSkillsStr.split(","));
            // Trim whitespace
            requiredSkillsList.replaceAll(String::trim);
            
            // Create Organizer object (simulating servlet logic)
            Organizer organizer = new Organizer(name.trim(), email.trim(), department.trim(), 0,
                                              organizationName.trim(), requiredSkillsList);
            
            // Additional data processing
            List<String> eventTypesList = Arrays.asList(eventTypesArray);
            
            // Log the request (simulating servlet logging)
            System.out.println("Organizer request processed successfully!");
            System.out.println("Generated Organizer Object:");
            organizer.displayProfile();
            
            System.out.println("\nAdditional Request Information:");
            System.out.println("Preferred Semester: " + preferredSemester);
            System.out.println("Organization Type: " + organizationType);
            System.out.println("Event Types: " + eventTypesList);
            System.out.println("Event Description: " + eventDescription);
            System.out.println("Experience: " + experience);
            
            System.out.println("\n✅ SUCCESS: Organizer request would be saved to database");
            System.out.println("✅ SUCCESS: Admin notification would be sent");
            System.out.println("✅ SUCCESS: Confirmation email would be sent to " + organizer.getEmail());
            System.out.println("✅ SUCCESS: User would see success page");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR: " + e.getMessage());
        }
    }
}