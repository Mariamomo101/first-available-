package utils;

import models.Student;
import models.Organizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Mock database utility class for SkillSync application
 * Provides in-memory storage simulation using ArrayList
 * This will be replaced with actual JDBC MySQL implementation later
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public class DatabaseMock {
    
    // Thread-safe static collections for data storage
    private static final List<Student> students = Collections.synchronizedList(new ArrayList<>());
    private static final List<Organizer> organizers = Collections.synchronizedList(new ArrayList<>());
    
    // Shortlisted students for organizer invitations
    private static final List<Student> shortlistedStudents = Collections.synchronizedList(new ArrayList<>());
    
    // Auto-increment counters for IDs (simulating database auto-increment)
    private static final AtomicInteger studentIdCounter = new AtomicInteger(1);
    private static final AtomicInteger organizerIdCounter = new AtomicInteger(1);
    
    // Private constructor to prevent instantiation (utility class)
    private DatabaseMock() {
        throw new UnsupportedOperationException("DatabaseMock is a utility class and cannot be instantiated");
    }
    
    /**
     * Adds a new student to the mock database
     * 
     * @param student The student object to add
     * @return The ID assigned to the student (simulating database auto-increment)
     * @throws IllegalArgumentException if student is null or email already exists
     */
    public static synchronized int addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        
        // Check for duplicate email (simulating unique constraint)
        if (isStudentEmailExists(student.getEmail())) {
            throw new IllegalArgumentException("Student with email " + student.getEmail() + " already exists");
        }
        
        // Add student to the list
        students.add(student);
        int assignedId = studentIdCounter.getAndIncrement();
        
        // Log the addition
        System.out.println("🎓 STUDENT ADDED TO DATABASE:");
        System.out.println("   ID: " + assignedId);
        System.out.println("   Name: " + student.getName());
        System.out.println("   Email: " + student.getEmail());
        System.out.println("   Total Students: " + students.size());
        
        return assignedId;
    }
    
    /**
     * Adds a new organizer to the mock database
     * 
     * @param organizer The organizer object to add
     * @return The ID assigned to the organizer (simulating database auto-increment)
     * @throws IllegalArgumentException if organizer is null or email already exists
     */
    public static synchronized int addOrganizer(Organizer organizer) {
        if (organizer == null) {
            throw new IllegalArgumentException("Organizer cannot be null");
        }
        
        // Check for duplicate email (simulating unique constraint)
        if (isOrganizerEmailExists(organizer.getEmail())) {
            throw new IllegalArgumentException("Organizer with email " + organizer.getEmail() + " already exists");
        }
        
        // Add organizer to the list
        organizers.add(organizer);
        int assignedId = organizerIdCounter.getAndIncrement();
        
        // Log the addition
        System.out.println("🏢 ORGANIZER ADDED TO DATABASE:");
        System.out.println("   ID: " + assignedId);
        System.out.println("   Name: " + organizer.getName());
        System.out.println("   Email: " + organizer.getEmail());
        System.out.println("   Organization: " + organizer.getOrganizationName());
        System.out.println("   Total Organizers: " + organizers.size());
        
        return assignedId;
    }
    
    /**
     * Retrieves all students from the mock database
     * 
     * @return A defensive copy of the students list
     */
    public static List<Student> getAllStudents() {
        synchronized (students) {
            return new ArrayList<>(students);
        }
    }
    
    /**
     * Retrieves all organizers from the mock database
     * 
     * @return A defensive copy of the organizers list
     */
    public static List<Organizer> getAllOrganizers() {
        synchronized (organizers) {
            return new ArrayList<>(organizers);
        }
    }
    
    /**
     * Finds a student by email address
     * 
     * @param email The email address to search for
     * @return The student object if found, null otherwise
     */
    public static Student findStudentByEmail(String email) {
        if (email == null) {
            return null;
        }
        
        synchronized (students) {
            return students.stream()
                    .filter(student -> email.equalsIgnoreCase(student.getEmail()))
                    .findFirst()
                    .orElse(null);
        }
    }
    
    /**
     * Finds an organizer by email address
     * 
     * @param email The email address to search for
     * @return The organizer object if found, null otherwise
     */
    public static Organizer findOrganizerByEmail(String email) {
        if (email == null) {
            return null;
        }
        
        synchronized (organizers) {
            return organizers.stream()
                    .filter(organizer -> email.equalsIgnoreCase(organizer.getEmail()))
                    .findFirst()
                    .orElse(null);
        }
    }
    
    /**
     * Finds students by department
     * 
     * @param department The department to search for
     * @return List of students in the specified department
     */
    public static List<Student> findStudentsByDepartment(String department) {
        if (department == null) {
            return new ArrayList<>();
        }
        
        synchronized (students) {
            return students.stream()
                    .filter(student -> department.equalsIgnoreCase(student.getDepartment()))
                    .collect(Collectors.toList());
        }
    }
    
    /**
     * Finds students by skill
     * 
     * @param skill The skill to search for
     * @return List of students who have the specified skill
     */
    public static List<Student> findStudentsBySkill(String skill) {
        if (skill == null) {
            return new ArrayList<>();
        }
        
        synchronized (students) {
            return students.stream()
                    .filter(student -> student.hasSkill(skill))
                    .collect(Collectors.toList());
        }
    }
    
    /**
     * Finds organizers by organization name
     * 
     * @param organizationName The organization name to search for
     * @return List of organizers from the specified organization
     */
    public static List<Organizer> findOrganizersByOrganization(String organizationName) {
        if (organizationName == null) {
            return new ArrayList<>();
        }
        
        synchronized (organizers) {
            return organizers.stream()
                    .filter(organizer -> organizationName.equalsIgnoreCase(organizer.getOrganizationName()))
                    .collect(Collectors.toList());
        }
    }
    
    /**
     * Gets the total count of students
     * 
     * @return The number of students in the database
     */
    public static int getStudentCount() {
        return students.size();
    }
    
    /**
     * Gets the total count of organizers
     * 
     * @return The number of organizers in the database
     */
    public static int getOrganizerCount() {
        return organizers.size();
    }
    
    /**
     * Clears all data from the mock database (useful for testing)
     */
    public static synchronized void clearAll() {
        students.clear();
        organizers.clear();
        studentIdCounter.set(1);
        organizerIdCounter.set(1);
        System.out.println("🗑️ DATABASE CLEARED: All data removed from mock database");
    }
    
    /**
     * Gets database statistics
     * 
     * @return A formatted string with database statistics
     */
    public static String getDatabaseStats() {
        return String.format("📊 DATABASE STATS: Students: %d, Organizers: %d, Total: %d", 
                           getStudentCount(), getOrganizerCount(), 
                           getStudentCount() + getOrganizerCount());
    }
    
    /**
     * Displays all data in the mock database (for debugging)
     */
    public static void displayAllData() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📦 MOCK DATABASE CONTENTS");
        System.out.println("=".repeat(50));
        
        System.out.println("\n👨‍🎓 STUDENTS (" + students.size() + "):");
        if (students.isEmpty()) {
            System.out.println("   No students registered");
        } else {
            synchronized (students) {
                for (int i = 0; i < students.size(); i++) {
                    System.out.println("   " + (i + 1) + ". " + students.get(i).getName() + 
                                     " (" + students.get(i).getEmail() + ")");
                }
            }
        }
        
        System.out.println("\n🏢 ORGANIZERS (" + organizers.size() + "):");
        if (organizers.isEmpty()) {
            System.out.println("   No organizers registered");
        } else {
            synchronized (organizers) {
                for (int i = 0; i < organizers.size(); i++) {
                    System.out.println("   " + (i + 1) + ". " + organizers.get(i).getName() + 
                                     " (" + organizers.get(i).getEmail() + ") - " + 
                                     organizers.get(i).getOrganizationName());
                }
            }
        }
        
        System.out.println("\n" + getDatabaseStats());
        System.out.println("=".repeat(50));
    }
    
    // Private helper methods
    
    /**
     * Checks if a student with the given email already exists
     * 
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    private static boolean isStudentEmailExists(String email) {
        return findStudentByEmail(email) != null;
    }
    
    /**
     * Checks if an organizer with the given email already exists
     * 
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    private static boolean isOrganizerEmailExists(String email) {
        return findOrganizerByEmail(email) != null;
    }
    
    // ============ SHORTLISTING METHODS ============
    
    /**
     * Adds a student to the shortlist for organizer invitations
     * 
     * @param student The student to shortlist
     * @return true if successfully added, false if already shortlisted
     * @throws IllegalArgumentException if student is null
     */
    public static synchronized boolean addToShortlist(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        
        // Check if student is already shortlisted
        if (isStudentShortlisted(student.getEmail())) {
            return false; // Already shortlisted
        }
        
        shortlistedStudents.add(student);
        System.out.println("✅ STUDENT SHORTLISTED:");
        System.out.println("   Name: " + student.getName());
        System.out.println("   Email: " + student.getEmail());
        System.out.println("   Total Shortlisted: " + shortlistedStudents.size());
        
        return true;
    }
    
    /**
     * Adds a student to shortlist by email (finds student first)
     * 
     * @param email The email of the student to shortlist
     * @return true if successfully added, false if student not found or already shortlisted
     */
    public static synchronized boolean addToShortlistByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        Student student = findStudentByEmail(email.trim());
        if (student == null) {
            return false; // Student not found
        }
        
        return addToShortlist(student);
    }
    
    /**
     * Retrieves all shortlisted students
     * 
     * @return A defensive copy of the shortlisted students list
     */
    public static List<Student> getShortlistedStudents() {
        synchronized (shortlistedStudents) {
            return new ArrayList<>(shortlistedStudents);
        }
    }
    
    /**
     * Checks if a student is already shortlisted
     * 
     * @param email The email of the student to check
     * @return true if student is shortlisted, false otherwise
     */
    public static boolean isStudentShortlisted(String email) {
        if (email == null) {
            return false;
        }
        
        synchronized (shortlistedStudents) {
            return shortlistedStudents.stream()
                    .anyMatch(student -> student.getEmail().equalsIgnoreCase(email.trim()));
        }
    }
    
    /**
     * Removes a student from the shortlist
     * 
     * @param email The email of the student to remove
     * @return true if student was removed, false if not found
     */
    public static synchronized boolean removeFromShortlist(String email) {
        if (email == null) {
            return false;
        }
        
        synchronized (shortlistedStudents) {
            boolean removed = shortlistedStudents.removeIf(student -> 
                    student.getEmail().equalsIgnoreCase(email.trim()));
            
            if (removed) {
                System.out.println("🗑️ STUDENT REMOVED FROM SHORTLIST:");
                System.out.println("   Email: " + email);
                System.out.println("   Remaining Shortlisted: " + shortlistedStudents.size());
            }
            
            return removed;
        }
    }
    
    /**
     * Gets the count of shortlisted students
     * 
     * @return Number of shortlisted students
     */
    public static int getShortlistedStudentCount() {
        return shortlistedStudents.size();
    }
    
    /**
     * Clears all shortlisted students (for testing purposes)
     */
    public static synchronized void clearShortlist() {
        shortlistedStudents.clear();
        System.out.println("🗑️ SHORTLIST CLEARED: All shortlisted students removed");
    }
    
    /**
     * Enhanced clearAll method to include shortlist
     */
    public static synchronized void clearAllData() {
        clearAll();
        clearShortlist();
    }
}