package models;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * Student class representing a student user in the SkillSync application
 * Extends the abstract User class and adds student-specific functionality
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public class Student extends User {
    
    // Private fields specific to Student
    private List<String> skills;
    private String cvLink;
    private List<String> interestAreas;
    private LocalDateTime lastUpdated;
    
    /**
     * Default constructor
     * Initializes the lists to prevent null pointer exceptions
     */
    public Student() {
        super();
        this.skills = new ArrayList<>();
        this.interestAreas = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
    
    /**
     * Parameterized constructor with basic user information
     * 
     * @param name The name of the student
     * @param email The email address of the student
     * @param department The department of the student
     * @param semester The semester of the student
     */
    public Student(String name, String email, String department, int semester) {
        super(name, email, department, semester);
        this.skills = new ArrayList<>();
        this.interestAreas = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
    
    /**
     * Complete parameterized constructor
     * 
     * @param name The name of the student
     * @param email The email address of the student
     * @param department The department of the student
     * @param semester The semester of the student
     * @param skills The list of skills the student possesses
     * @param cvLink Link to the student's CV/resume
     * @param interestAreas The list of areas the student is interested in
     */
    public Student(String name, String email, String department, int semester,
                   List<String> skills, String cvLink, List<String> interestAreas) {
        super(name, email, department, semester);
        this.skills = skills != null ? new ArrayList<>(skills) : new ArrayList<>();
        this.cvLink = cvLink;
        this.interestAreas = interestAreas != null ? new ArrayList<>(interestAreas) : new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getter and Setter methods for student-specific fields
    
    /**
     * Gets the list of skills
     * 
     * @return A copy of the skills list to maintain encapsulation
     */
    public List<String> getSkills() {
        return new ArrayList<>(skills);
    }
    
    /**
     * Sets the skills list
     * 
     * @param skills The skills list to set
     */
    public void setSkills(List<String> skills) {
        this.skills = skills != null ? new ArrayList<>(skills) : new ArrayList<>();
    }
    
    /**
     * Adds a skill to the student's skill set
     * 
     * @param skill The skill to add
     */
    public void addSkill(String skill) {
        if (skill != null && !skill.trim().isEmpty() && !this.skills.contains(skill)) {
            this.skills.add(skill);
        }
    }
    
    /**
     * Removes a skill from the student's skill set
     * 
     * @param skill The skill to remove
     * @return true if the skill was removed, false otherwise
     */
    public boolean removeSkill(String skill) {
        return this.skills.remove(skill);
    }
    
    /**
     * Gets the CV link
     * 
     * @return The student's CV link
     */
    public String getCvLink() {
        return cvLink;
    }
    
    /**
     * Sets the CV link
     * 
     * @param cvLink The CV link to set
     */
    public void setCvLink(String cvLink) {
        this.cvLink = cvLink;
    }
    
    /**
     * Gets the list of interest areas
     * 
     * @return A copy of the interest areas list to maintain encapsulation
     */
    public List<String> getInterestAreas() {
        return new ArrayList<>(interestAreas);
    }
    
    /**
     * Sets the interest areas list
     * 
     * @param interestAreas The interest areas list to set
     */
    public void setInterestAreas(List<String> interestAreas) {
        this.interestAreas = interestAreas != null ? new ArrayList<>(interestAreas) : new ArrayList<>();
    }
    
    /**
     * Adds an interest area to the student's interest list
     * 
     * @param interestArea The interest area to add
     */
    public void addInterestArea(String interestArea) {
        if (interestArea != null && !interestArea.trim().isEmpty() && !this.interestAreas.contains(interestArea)) {
            this.interestAreas.add(interestArea);
        }
    }
    
    /**
     * Removes an interest area from the student's interest list
     * 
     * @param interestArea The interest area to remove
     * @return true if the interest area was removed, false otherwise
     */
    public boolean removeInterestArea(String interestArea) {
        return this.interestAreas.remove(interestArea);
    }
    
    /**
     * Implementation of the abstract displayProfile method
     * Displays complete student profile information
     */
    @Override
    public void displayProfile() {
        System.out.println("=== STUDENT PROFILE ===");
        
        // Display basic user information
        displayBasicInfo();
        
        // Display student-specific information
        System.out.println("\n--- Student Specific Information ---");
        
        System.out.println("Skills: ");
        if (skills.isEmpty()) {
            System.out.println("  No skills listed");
        } else {
            for (int i = 0; i < skills.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + skills.get(i));
            }
        }
        
        System.out.println("CV Link: " + (cvLink != null ? cvLink : "Not provided"));
        
        System.out.println("Interest Areas: ");
        if (interestAreas.isEmpty()) {
            System.out.println("  No interest areas specified");
        } else {
            for (int i = 0; i < interestAreas.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + interestAreas.get(i));
            }
        }
        
        System.out.println("Last Updated: " + (lastUpdated != null ? lastUpdated.toString() : "Unknown"));
        
        System.out.println("========================");
    }
    
    /**
     * Checks if the student has a specific skill
     * 
     * @param skill The skill to check for
     * @return true if the student has the skill, false otherwise
     */
    public boolean hasSkill(String skill) {
        return skills.contains(skill);
    }
    
    /**
     * Checks if the student is interested in a specific area
     * 
     * @param area The area to check for
     * @return true if the student is interested in the area, false otherwise
     */
    public boolean isInterestedIn(String area) {
        return interestAreas.contains(area);
    }
    
    /**
     * Gets the last updated timestamp
     * 
     * @return The LocalDateTime when the student profile was last updated
     */
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    /**
     * Sets the last updated timestamp
     * 
     * @param lastUpdated The LocalDateTime to set
     */
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    /**
     * Updates the student profile and sets the lastUpdated timestamp to now
     * 
     * @param semester The new semester
     * @param skills The updated skills list
     * @param cvLink The updated CV link
     * @param interestAreas The updated interest areas
     */
    public void updateProfile(int semester, List<String> skills, String cvLink, List<String> interestAreas) {
        setSemester(semester);
        setSkills(skills);
        setCvLink(cvLink);
        setInterestAreas(interestAreas);
        this.lastUpdated = LocalDateTime.now();
    }
    
    /**
     * Updates the lastUpdated timestamp to current time
     */
    public void touchLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    /**
     * Override toString method for better object representation
     * 
     * @return String representation of the Student object
     */
    @Override
    public String toString() {
        return "Student{" +
                "name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", department='" + getDepartment() + '\'' +
                ", semester=" + getSemester() +
                ", skills=" + skills +
                ", cvLink='" + cvLink + '\'' +
                ", interestAreas=" + interestAreas +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}