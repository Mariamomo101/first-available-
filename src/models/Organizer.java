package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Organizer class representing an organizer user in the SkillSync application
 * Extends the abstract User class and adds organizer-specific functionality
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public class Organizer extends User {
    
    // Private fields specific to Organizer
    private String organizationName;
    private List<String> requiredSkills;
    
    /**
     * Default constructor
     * Initializes the required skills list to prevent null pointer exceptions
     */
    public Organizer() {
        super();
        this.requiredSkills = new ArrayList<>();
    }
    
    /**
     * Parameterized constructor with basic user information
     * 
     * @param name The name of the organizer
     * @param email The email address of the organizer
     * @param department The department of the organizer
     * @param semester The semester of the organizer
     */
    public Organizer(String name, String email, String department, int semester) {
        super(name, email, department, semester);
        this.requiredSkills = new ArrayList<>();
    }
    
    /**
     * Complete parameterized constructor
     * 
     * @param name The name of the organizer
     * @param email The email address of the organizer
     * @param department The department of the organizer
     * @param semester The semester of the organizer
     * @param organizationName The name of the organization
     * @param requiredSkills The list of skills required for opportunities
     */
    public Organizer(String name, String email, String department, int semester,
                     String organizationName, List<String> requiredSkills) {
        super(name, email, department, semester);
        this.organizationName = organizationName;
        this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>();
    }
    
    // Getter and Setter methods for organizer-specific fields
    
    /**
     * Gets the organization name
     * 
     * @return The organizer's organization name
     */
    public String getOrganizationName() {
        return organizationName;
    }
    
    /**
     * Sets the organization name
     * 
     * @param organizationName The organization name to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    
    /**
     * Gets the list of required skills
     * 
     * @return A copy of the required skills list to maintain encapsulation
     */
    public List<String> getRequiredSkills() {
        return new ArrayList<>(requiredSkills);
    }
    
    /**
     * Sets the required skills list
     * 
     * @param requiredSkills The required skills list to set
     */
    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>();
    }
    
    /**
     * Adds a required skill to the organizer's required skills list
     * 
     * @param skill The skill to add
     */
    public void addRequiredSkill(String skill) {
        if (skill != null && !skill.trim().isEmpty() && !this.requiredSkills.contains(skill)) {
            this.requiredSkills.add(skill);
        }
    }
    
    /**
     * Removes a required skill from the organizer's required skills list
     * 
     * @param skill The skill to remove
     * @return true if the skill was removed, false otherwise
     */
    public boolean removeRequiredSkill(String skill) {
        return this.requiredSkills.remove(skill);
    }
    
    /**
     * Implementation of the abstract displayProfile method
     * Displays complete organizer profile information
     */
    @Override
    public void displayProfile() {
        System.out.println("=== ORGANIZER PROFILE ===");
        
        // Display basic user information
        displayBasicInfo();
        
        // Display organizer-specific information
        System.out.println("\n--- Organizer Specific Information ---");
        
        System.out.println("Organization: " + (organizationName != null ? organizationName : "Not specified"));
        
        System.out.println("Required Skills for Opportunities: ");
        if (requiredSkills.isEmpty()) {
            System.out.println("  No specific skills required");
        } else {
            for (int i = 0; i < requiredSkills.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + requiredSkills.get(i));
            }
        }
        
        System.out.println("==========================");
    }
    
    /**
     * Checks if a specific skill is required by this organizer
     * 
     * @param skill The skill to check for
     * @return true if the skill is required, false otherwise
     */
    public boolean requiresSkill(String skill) {
        return requiredSkills.contains(skill);
    }
    
    /**
     * Checks if a student matches the required skills for this organizer's opportunities
     * 
     * @param student The student to check
     * @return true if the student has at least one of the required skills, false otherwise
     */
    public boolean isStudentEligible(Student student) {
        if (requiredSkills.isEmpty()) {
            return true; // If no specific skills are required, all students are eligible
        }
        
        List<String> studentSkills = student.getSkills();
        for (String requiredSkill : requiredSkills) {
            if (studentSkills.contains(requiredSkill)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets the number of matching skills between the organizer's requirements and a student
     * 
     * @param student The student to check
     * @return The number of matching skills
     */
    public int getMatchingSkillsCount(Student student) {
        List<String> studentSkills = student.getSkills();
        int matchCount = 0;
        
        for (String requiredSkill : requiredSkills) {
            if (studentSkills.contains(requiredSkill)) {
                matchCount++;
            }
        }
        
        return matchCount;
    }
    
    /**
     * Gets the matching skills between the organizer's requirements and a student
     * 
     * @param student The student to check
     * @return List of matching skills
     */
    public List<String> getMatchingSkills(Student student) {
        List<String> studentSkills = student.getSkills();
        List<String> matchingSkills = new ArrayList<>();
        
        for (String requiredSkill : requiredSkills) {
            if (studentSkills.contains(requiredSkill)) {
                matchingSkills.add(requiredSkill);
            }
        }
        
        return matchingSkills;
    }
    
    /**
     * Override toString method for better object representation
     * 
     * @return String representation of the Organizer object
     */
    @Override
    public String toString() {
        return "Organizer{" +
                "name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", department='" + getDepartment() + '\'' +
                ", semester=" + getSemester() +
                ", organizationName='" + organizationName + '\'' +
                ", requiredSkills=" + requiredSkills +
                '}';
    }
}