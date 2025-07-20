package models;

import java.util.Arrays;
import java.util.List;

/**
 * Test class to demonstrate the functionality of the SkillSync model classes
 * This class shows how to use the User, Student, and Organizer classes
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public class ModelTest {
    
    /**
     * Main method to run the model tests
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== SkillSync Model Classes Test ===\n");
        
        // Test Student class
        testStudentClass();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Test Organizer class
        testOrganizerClass();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // Test interaction between Student and Organizer
        testStudentOrganizerInteraction();
    }
    
    /**
     * Test the Student class functionality
     */
    private static void testStudentClass() {
        System.out.println("TESTING STUDENT CLASS");
        System.out.println("=" + "=".repeat(20));
        
        // Create a student using different constructors
        Student student1 = new Student();
        student1.setName("Alice Johnson");
        student1.setEmail("alice.johnson@university.edu");
        student1.setDepartment("Computer Science");
        student1.setSemester(6);
        student1.setCvLink("https://linkedin.com/in/alicejohnson");
        
        // Add skills and interests
        student1.addSkill("Java Programming");
        student1.addSkill("Web Development");
        student1.addSkill("Database Design");
        student1.addInterestArea("Artificial Intelligence");
        student1.addInterestArea("Software Engineering");
        
        System.out.println("Student 1 (using setters):");
        student1.displayProfile();
        
        System.out.println("\n" + "-".repeat(30) + "\n");
        
        // Create another student using parameterized constructor
        List<String> skills = Arrays.asList("Python", "Machine Learning", "Data Analysis");
        List<String> interests = Arrays.asList("Data Science", "Research", "AI Ethics");
        
        Student student2 = new Student("Bob Smith", "bob.smith@university.edu", 
                                     "Data Science", 4, skills, 
                                     "https://github.com/bobsmith", interests);
        
        System.out.println("Student 2 (using constructor):");
        student2.displayProfile();
        
        // Test utility methods
        System.out.println("\nUtility Methods Test:");
        System.out.println("Student 1 has Java Programming: " + student1.hasSkill("Java Programming"));
        System.out.println("Student 1 interested in AI: " + student1.isInterestedIn("Artificial Intelligence"));
        System.out.println("Student 2 has Web Development: " + student2.hasSkill("Web Development"));
    }
    
    /**
     * Test the Organizer class functionality
     */
    private static void testOrganizerClass() {
        System.out.println("TESTING ORGANIZER CLASS");
        System.out.println("=" + "=".repeat(22));
        
        // Create an organizer
        Organizer organizer1 = new Organizer();
        organizer1.setName("Dr. Sarah Wilson");
        organizer1.setEmail("sarah.wilson@techcorp.com");
        organizer1.setDepartment("Engineering");
        organizer1.setSemester(0); // Organizers might not have semesters
        organizer1.setOrganizationName("TechCorp Solutions");
        
        // Add required skills
        organizer1.addRequiredSkill("Java Programming");
        organizer1.addRequiredSkill("Problem Solving");
        organizer1.addRequiredSkill("Team Work");
        
        System.out.println("Organizer 1:");
        organizer1.displayProfile();
        
        System.out.println("\n" + "-".repeat(30) + "\n");
        
        // Create another organizer using constructor
        List<String> requiredSkills = Arrays.asList("Python", "Data Analysis", "Statistics");
        
        Organizer organizer2 = new Organizer("Prof. Michael Chen", "m.chen@datalab.org",
                                            "Mathematics", 0, "DataLab Research",
                                            requiredSkills);
        
        System.out.println("Organizer 2:");
        organizer2.displayProfile();
        
        // Test utility methods
        System.out.println("\nUtility Methods Test:");
        System.out.println("Organizer 1 requires Java: " + organizer1.requiresSkill("Java Programming"));
        System.out.println("Organizer 2 requires Web Dev: " + organizer2.requiresSkill("Web Development"));
    }
    
    /**
     * Test interaction between Student and Organizer classes
     */
    private static void testStudentOrganizerInteraction() {
        System.out.println("TESTING STUDENT-ORGANIZER INTERACTION");
        System.out.println("=" + "=".repeat(35));
        
        // Create test data
        List<String> studentSkills = Arrays.asList("Java Programming", "Python", "Web Development");
        Student student = new Student("Emma Davis", "emma.davis@university.edu",
                                    "Computer Science", 5, studentSkills,
                                    "https://portfolio.emmadavis.com", 
                                    Arrays.asList("Software Development", "Startups"));
        
        List<String> organizerRequirements = Arrays.asList("Java Programming", "Team Work", "Communication");
        Organizer organizer = new Organizer("John Martinez", "j.martinez@startupx.com",
                                          "Business", 0, "StartupX Incubator",
                                          organizerRequirements);
        
        System.out.println("Student Profile:");
        student.displayProfile();
        
        System.out.println("\nOrganizer Profile:");
        organizer.displayProfile();
        
        // Test matching functionality
        System.out.println("\nMATCHING ANALYSIS:");
        System.out.println("-".repeat(20));
        
        boolean isEligible = organizer.isStudentEligible(student);
        System.out.println("Is student eligible for organizer's opportunities? " + isEligible);
        
        int matchingCount = organizer.getMatchingSkillsCount(student);
        System.out.println("Number of matching skills: " + matchingCount);
        
        List<String> matchingSkills = organizer.getMatchingSkills(student);
        System.out.println("Matching skills: " + matchingSkills);
        
        // Test with a non-matching student
        Student nonMatchingStudent = new Student("Tom Wilson", "tom.wilson@university.edu",
                                               "Art", 3, Arrays.asList("Drawing", "Painting"),
                                               null, Arrays.asList("Fine Arts"));
        
        System.out.println("\nTesting with non-matching student:");
        System.out.println("Is non-matching student eligible? " + organizer.isStudentEligible(nonMatchingStudent));
        System.out.println("Matching skills count: " + organizer.getMatchingSkillsCount(nonMatchingStudent));
    }
}