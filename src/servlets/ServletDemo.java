package servlets;

import models.Student;
import models.Organizer;
import utils.DatabaseMock;
import java.util.Arrays;
import java.util.List;

/**
 * Demo class to simulate servlet functionality with DatabaseMock
 * Demonstrates how the servlets would process form data and store in mock database
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public class ServletDemo {
    
    /**
     * Main method to run servlet demonstrations
     */
    public static void main(String[] args) {
        System.out.println("=== SkillSync Servlet + DatabaseMock Demo ===\n");
        
        // Clear database to start fresh
        DatabaseMock.clearAll();
        
        // Demo student registration
        demonstrateStudentRegistration();
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Demo organizer request
        demonstrateOrganizerRequest();
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Demo duplicate email handling
        demonstrateDuplicateEmailHandling();
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Demo database queries
        demonstrateDatabaseQueries();
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Display final database state
        DatabaseMock.displayAllData();
    }
    
    /**
     * Demonstrates student registration servlet functionality with database storage
     */
    private static void demonstrateStudentRegistration() {
        System.out.println("DEMONSTRATING STUDENT REGISTRATION WITH DATABASE STORAGE");
        System.out.println("=" + "=".repeat(52));
        
        // Test multiple students
        String[][] studentData = {
            {"Alice Johnson", "alice.johnson@university.edu", "Computer Science", "6", 
             "https://linkedin.com/in/alicejohnson", 
             "Java Programming, Web Development, Database Design, Problem Solving", 
             "Programming,Design"},
            {"Bob Smith", "bob.smith@university.edu", "Data Science", "4", 
             "https://github.com/bobsmith", 
             "Python, Machine Learning, Data Analysis, Statistics", 
             "Programming"},
            {"Carol Martinez", "carol.martinez@university.edu", "Engineering", "8", 
             "", 
             "Project Management, Leadership, Communication", 
             "Public Speaking,Hosting"}
        };
        
        for (String[] data : studentData) {
            try {
                // Parse data
                String name = data[0];
                String email = data[1];
                String department = data[2];
                int semester = Integer.parseInt(data[3]);
                String cvLink = data[4].isEmpty() ? null : data[4];
                String skillsStr = data[5];
                String[] interestAreasArray = data[6].split(",");
                
                // Process skills
                List<String> skillsList = Arrays.asList(skillsStr.split(","));
                skillsList.replaceAll(String::trim);
                
                // Process interest areas
                List<String> interestAreasList = Arrays.asList(interestAreasArray);
                
                // Create Student object (simulating servlet logic)
                Student student = new Student(name.trim(), email.trim(), department.trim(), semester,
                                            skillsList, cvLink, interestAreasList);
                
                // Save to database (simulating servlet database call)
                int studentId = DatabaseMock.addStudent(student);
                
                System.out.println("✅ " + name + " registered successfully with ID: " + studentId);
                
            } catch (Exception e) {
                System.err.println("❌ ERROR registering student: " + e.getMessage());
            }
        }
        
        System.out.println("\n" + DatabaseMock.getDatabaseStats());
    }
    
    /**
     * Demonstrates organizer request servlet functionality with database storage
     */
    private static void demonstrateOrganizerRequest() {
        System.out.println("DEMONSTRATING ORGANIZER REQUEST WITH DATABASE STORAGE");
        System.out.println("=" + "=".repeat(48));
        
        // Test multiple organizers
        String[][] organizerData = {
            {"Dr. Sarah Wilson", "sarah.wilson@techcorp.com", "Engineering", 
             "TechCorp Solutions", "Java Programming, Team Work, Communication, Leadership"},
            {"Prof. Michael Chen", "m.chen@datalab.org", "Mathematics", 
             "DataLab Research", "Python, Data Analysis, Statistics, Research"},
            {"Jennifer Adams", "j.adams@startupx.com", "Business", 
             "StartupX Incubator", "Entrepreneurship, Business Development, Networking"}
        };
        
        for (String[] data : organizerData) {
            try {
                // Parse data
                String name = data[0];
                String email = data[1];
                String department = data[2];
                String organizationName = data[3];
                String requiredSkillsStr = data[4];
                
                // Process required skills
                List<String> requiredSkillsList = Arrays.asList(requiredSkillsStr.split(","));
                requiredSkillsList.replaceAll(String::trim);
                
                // Create Organizer object (simulating servlet logic)
                Organizer organizer = new Organizer(name.trim(), email.trim(), department.trim(), 0,
                                                  organizationName.trim(), requiredSkillsList);
                
                // Save to database (simulating servlet database call)
                int organizerId = DatabaseMock.addOrganizer(organizer);
                
                System.out.println("✅ " + name + " registered successfully with ID: " + organizerId);
                
            } catch (Exception e) {
                System.err.println("❌ ERROR registering organizer: " + e.getMessage());
            }
        }
        
        System.out.println("\n" + DatabaseMock.getDatabaseStats());
    }
    
    /**
     * Demonstrates duplicate email handling
     */
    private static void demonstrateDuplicateEmailHandling() {
        System.out.println("DEMONSTRATING DUPLICATE EMAIL HANDLING");
        System.out.println("=" + "=".repeat(37));
        
        // Try to register a student with duplicate email
        try {
            Student duplicateStudent = new Student("Alice Duplicate", "alice.johnson@university.edu", 
                                                 "Physics", 3, Arrays.asList("Physics"), null, Arrays.asList());
            DatabaseMock.addStudent(duplicateStudent);
            System.out.println("❌ ERROR: Should have prevented duplicate email!");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ DUPLICATE EMAIL PREVENTED: " + e.getMessage());
        }
        
        // Try to register an organizer with duplicate email
        try {
            Organizer duplicateOrganizer = new Organizer("Sarah Duplicate", "sarah.wilson@techcorp.com", 
                                                        "Marketing", 0, "Other Corp", Arrays.asList("Marketing"));
            DatabaseMock.addOrganizer(duplicateOrganizer);
            System.out.println("❌ ERROR: Should have prevented duplicate email!");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ DUPLICATE EMAIL PREVENTED: " + e.getMessage());
        }
    }
    
    /**
     * Demonstrates database query functionality
     */
    private static void demonstrateDatabaseQueries() {
        System.out.println("DEMONSTRATING DATABASE QUERY FUNCTIONALITY");
        System.out.println("=" + "=".repeat(41));
        
        // Find student by email
        Student foundStudent = DatabaseMock.findStudentByEmail("alice.johnson@university.edu");
        if (foundStudent != null) {
            System.out.println("✅ Found student by email: " + foundStudent.getName());
        }
        
        // Find organizer by email
        Organizer foundOrganizer = DatabaseMock.findOrganizerByEmail("sarah.wilson@techcorp.com");
        if (foundOrganizer != null) {
            System.out.println("✅ Found organizer by email: " + foundOrganizer.getName());
        }
        
        // Find students by department
        List<Student> csStudents = DatabaseMock.findStudentsByDepartment("Computer Science");
        System.out.println("✅ Students in Computer Science: " + csStudents.size());
        
        // Find students by skill
        List<Student> javaStudents = DatabaseMock.findStudentsBySkill("Java Programming");
        System.out.println("✅ Students with Java Programming skill: " + javaStudents.size());
        
        // Find organizers by organization
        List<Organizer> techCorpOrganizers = DatabaseMock.findOrganizersByOrganization("TechCorp Solutions");
        System.out.println("✅ Organizers from TechCorp Solutions: " + techCorpOrganizers.size());
        
        // Get all data
        List<Student> allStudents = DatabaseMock.getAllStudents();
        List<Organizer> allOrganizers = DatabaseMock.getAllOrganizers();
        System.out.println("✅ Total students: " + allStudents.size());
        System.out.println("✅ Total organizers: " + allOrganizers.size());
        
        // Test student-organizer matching
        if (!allStudents.isEmpty() && !allOrganizers.isEmpty()) {
            Student student = allStudents.get(0);
            Organizer organizer = allOrganizers.get(0);
            
            System.out.println("\n🔍 TESTING STUDENT-ORGANIZER MATCHING:");
            System.out.println("Student: " + student.getName());
            System.out.println("Organizer: " + organizer.getName());
            System.out.println("Is student eligible? " + organizer.isStudentEligible(student));
            System.out.println("Matching skills: " + organizer.getMatchingSkills(student));
        }
    }
}