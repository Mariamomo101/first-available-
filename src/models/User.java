package models;

/**
 * Abstract User class representing a generic user in the SkillSync application
 * This serves as the base class for Student and Organizer classes
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public abstract class User {
    
    // Private fields for encapsulation
    private String name;
    private String email;
    private String department;
    private int semester;
    
    /**
     * Default constructor
     */
    public User() {
    }
    
    /**
     * Parameterized constructor
     * 
     * @param name The name of the user
     * @param email The email address of the user
     * @param department The department of the user
     * @param semester The semester of the user
     */
    public User(String name, String email, String department, int semester) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.semester = semester;
    }
    
    // Getter and Setter methods for encapsulation
    
    /**
     * Gets the name of the user
     * 
     * @return The user's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name of the user
     * 
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the email of the user
     * 
     * @return The user's email
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Sets the email of the user
     * 
     * @param email The email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Gets the department of the user
     * 
     * @return The user's department
     */
    public String getDepartment() {
        return department;
    }
    
    /**
     * Sets the department of the user
     * 
     * @param department The department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }
    
    /**
     * Gets the semester of the user
     * 
     * @return The user's semester
     */
    public int getSemester() {
        return semester;
    }
    
    /**
     * Sets the semester of the user
     * 
     * @param semester The semester to set
     */
    public void setSemester(int semester) {
        this.semester = semester;
    }
    
    /**
     * Abstract method to display user profile
     * Must be implemented by concrete subclasses
     */
    public abstract void displayProfile();
    
    /**
     * Common method to display basic user information
     * This can be used by subclasses in their displayProfile implementation
     */
    protected void displayBasicInfo() {
        System.out.println("=== User Profile ===");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Department: " + department);
        System.out.println("Semester: " + semester);
    }
    
    /**
     * Override toString method for better object representation
     * 
     * @return String representation of the User object
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", semester=" + semester +
                '}';
    }
}