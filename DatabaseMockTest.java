import models.Student;
import models.Organizer;
import utils.DatabaseMock;
import java.util.Arrays;
import java.util.List;

/**
 * Comprehensive test class for DatabaseMock functionality
 * Demonstrates complete integration between models and mock database
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public class DatabaseMockTest {
    
    public static void main(String[] args) {
        System.out.println("🧪 COMPREHENSIVE DATABASE MOCK TEST");
        System.out.println("=" + "=".repeat(38));
        
        // Test 1: Fresh database
        testFreshDatabase();
        
        // Test 2: Student registration simulation
        testStudentRegistrationFlow();
        
        // Test 3: Organizer registration simulation
        testOrganizerRegistrationFlow();
        
        // Test 4: Duplicate prevention
        testDuplicatePrevention();
        
        // Test 5: Advanced queries
        testAdvancedQueries();
        
        // Test 6: Business logic integration
        testBusinessLogic();
        
        // Test 7: Database statistics and management
        testDatabaseManagement();
        
        System.out.println("\n🎉 ALL TESTS COMPLETED SUCCESSFULLY!");
    }
    
    /**
     * Test fresh database state
     */
    private static void testFreshDatabase() {
        System.out.println("\n📝 TEST 1: Fresh Database State");
        System.out.println("-".repeat(30));
        
        DatabaseMock.clearAll();
        
        assert DatabaseMock.getStudentCount() == 0 : "Student count should be 0";
        assert DatabaseMock.getOrganizerCount() == 0 : "Organizer count should be 0";
        assert DatabaseMock.getAllStudents().isEmpty() : "Students list should be empty";
        assert DatabaseMock.getAllOrganizers().isEmpty() : "Organizers list should be empty";
        
        System.out.println("✅ Fresh database test passed");
    }
    
    /**
     * Test student registration workflow
     */
    private static void testStudentRegistrationFlow() {
        System.out.println("\n📝 TEST 2: Student Registration Flow");
        System.out.println("-".repeat(35));
        
        // Create test students
        Student alice = new Student("Alice Johnson", "alice@university.edu", 
                                  "Computer Science", 6, 
                                  Arrays.asList("Java", "Python", "Web Development"),
                                  "https://linkedin.com/alice", 
                                  Arrays.asList("Programming", "Design"));
        
        Student bob = new Student("Bob Smith", "bob@university.edu", 
                                "Data Science", 4, 
                                Arrays.asList("Python", "Machine Learning"),
                                null, 
                                Arrays.asList("Programming"));
        
        // Register students
        int aliceId = DatabaseMock.addStudent(alice);
        int bobId = DatabaseMock.addStudent(bob);
        
        assert aliceId == 1 : "Alice should get ID 1";
        assert bobId == 2 : "Bob should get ID 2";
        assert DatabaseMock.getStudentCount() == 2 : "Should have 2 students";
        
        // Verify retrieval
        Student foundAlice = DatabaseMock.findStudentByEmail("alice@university.edu");
        assert foundAlice != null : "Should find Alice by email";
        assert foundAlice.getName().equals("Alice Johnson") : "Should find correct student";
        
        System.out.println("✅ Student registration flow test passed");
    }
    
    /**
     * Test organizer registration workflow
     */
    private static void testOrganizerRegistrationFlow() {
        System.out.println("\n📝 TEST 3: Organizer Registration Flow");
        System.out.println("-".repeat(36));
        
        // Create test organizers
        Organizer sarah = new Organizer("Dr. Sarah Wilson", "sarah@techcorp.com", 
                                       "Engineering", 0, "TechCorp Solutions",
                                       Arrays.asList("Java", "Leadership", "Team Work"));
        
        Organizer michael = new Organizer("Prof. Michael Chen", "michael@datalab.org", 
                                         "Mathematics", 0, "DataLab Research",
                                         Arrays.asList("Python", "Research", "Statistics"));
        
        // Register organizers
        int sarahId = DatabaseMock.addOrganizer(sarah);
        int michaelId = DatabaseMock.addOrganizer(michael);
        
        assert sarahId == 1 : "Sarah should get ID 1";
        assert michaelId == 2 : "Michael should get ID 2";
        assert DatabaseMock.getOrganizerCount() == 2 : "Should have 2 organizers";
        
        // Verify retrieval
        Organizer foundSarah = DatabaseMock.findOrganizerByEmail("sarah@techcorp.com");
        assert foundSarah != null : "Should find Sarah by email";
        assert foundSarah.getOrganizationName().equals("TechCorp Solutions") : "Should find correct organizer";
        
        System.out.println("✅ Organizer registration flow test passed");
    }
    
    /**
     * Test duplicate email prevention
     */
    private static void testDuplicatePrevention() {
        System.out.println("\n📝 TEST 4: Duplicate Email Prevention");
        System.out.println("-".repeat(34));
        
        // Try to register student with duplicate email
        try {
            Student duplicate = new Student("Alice Duplicate", "alice@university.edu", 
                                          "Physics", 3, Arrays.asList("Physics"), null, Arrays.asList());
            DatabaseMock.addStudent(duplicate);
            assert false : "Should have thrown exception for duplicate email";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("already exists") : "Should mention email already exists";
            System.out.println("✅ Student duplicate email prevention works");
        }
        
        // Try to register organizer with duplicate email
        try {
            Organizer duplicate = new Organizer("Sarah Duplicate", "sarah@techcorp.com", 
                                               "Marketing", 0, "Other Corp", Arrays.asList("Marketing"));
            DatabaseMock.addOrganizer(duplicate);
            assert false : "Should have thrown exception for duplicate email";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("already exists") : "Should mention email already exists";
            System.out.println("✅ Organizer duplicate email prevention works");
        }
        
        System.out.println("✅ Duplicate prevention test passed");
    }
    
    /**
     * Test advanced query functionality
     */
    private static void testAdvancedQueries() {
        System.out.println("\n📝 TEST 5: Advanced Query Functionality");
        System.out.println("-".repeat(36));
        
        // Find students by department
        List<Student> csStudents = DatabaseMock.findStudentsByDepartment("Computer Science");
        assert csStudents.size() == 1 : "Should find 1 CS student";
        assert csStudents.get(0).getName().equals("Alice Johnson") : "Should be Alice";
        
        // Find students by skill
        List<Student> pythonStudents = DatabaseMock.findStudentsBySkill("Python");
        assert pythonStudents.size() == 2 : "Should find 2 Python students";
        
        // Find organizers by organization
        List<Organizer> techCorpOrganizers = DatabaseMock.findOrganizersByOrganization("TechCorp Solutions");
        assert techCorpOrganizers.size() == 1 : "Should find 1 TechCorp organizer";
        
        // Test case-insensitive search
        Student foundStudent = DatabaseMock.findStudentByEmail("ALICE@UNIVERSITY.EDU");
        assert foundStudent != null : "Should find student with case-insensitive email";
        
        System.out.println("✅ Advanced query test passed");
    }
    
    /**
     * Test business logic integration
     */
    private static void testBusinessLogic() {
        System.out.println("\n📝 TEST 6: Business Logic Integration");
        System.out.println("-".repeat(35));
        
        // Get student and organizer for matching test
        Student alice = DatabaseMock.findStudentByEmail("alice@university.edu");
        Organizer sarah = DatabaseMock.findOrganizerByEmail("sarah@techcorp.com");
        
        assert alice != null : "Alice should be found";
        assert sarah != null : "Sarah should be found";
        
        // Test student-organizer matching
        boolean isEligible = sarah.isStudentEligible(alice);
        assert isEligible : "Alice should be eligible for Sarah's opportunities";
        
        List<String> matchingSkills = sarah.getMatchingSkills(alice);
        assert matchingSkills.contains("Java") : "Should match Java skill";
        
        int matchCount = sarah.getMatchingSkillsCount(alice);
        assert matchCount >= 1 : "Should have at least 1 matching skill";
        
        // Test student skill checking
        assert alice.hasSkill("Java") : "Alice should have Java skill";
        assert !alice.hasSkill("C++") : "Alice should not have C++ skill";
        
        // Test student interest checking
        assert alice.isInterestedIn("Programming") : "Alice should be interested in Programming";
        
        System.out.println("✅ Business logic integration test passed");
    }
    
    /**
     * Test database management functionality
     */
    private static void testDatabaseManagement() {
        System.out.println("\n📝 TEST 7: Database Management");
        System.out.println("-".repeat(29));
        
        // Test statistics
        String stats = DatabaseMock.getDatabaseStats();
        assert stats.contains("Students: 2") : "Should show 2 students in stats";
        assert stats.contains("Organizers: 2") : "Should show 2 organizers in stats";
        
        // Test data display (should not throw exceptions)
        DatabaseMock.displayAllData();
        
        // Test defensive copying
        List<Student> students1 = DatabaseMock.getAllStudents();
        List<Student> students2 = DatabaseMock.getAllStudents();
        assert students1 != students2 : "Should return different list instances";
        assert students1.size() == students2.size() : "But with same content";
        
        // Verify clearing doesn't affect existing references
        int originalSize = students1.size();
        DatabaseMock.clearAll();
        assert students1.size() == originalSize : "Original list should not be affected by clear";
        assert DatabaseMock.getStudentCount() == 0 : "Database should be cleared";
        
        System.out.println("✅ Database management test passed");
    }
}