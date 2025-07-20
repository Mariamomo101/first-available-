package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Student;
import utils.DatabaseMock;
import utils.SkillRecommender;

/**
 * Servlet for handling student registration form submissions in SkillSync application
 * Processes POST requests from student_registration.html form
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
@WebServlet("/StudentRegistrationServlet")
public class StudentRegistrationServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect GET requests to the registration form
        response.sendRedirect("student_registration.html");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Set response content type
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Check which action was requested
            String action = request.getParameter("action");
            if (action == null) action = "register"; // Default to register for backward compatibility
            
            // Extract form parameters
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String department = request.getParameter("department");
            String semesterStr = request.getParameter("semester");
            String cvLink = request.getParameter("cvLink");
            String skillsStr = request.getParameter("skills");
            String[] interestAreasArray = request.getParameterValues("interestAreas");
            
            // Validate required fields
            if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                department == null || department.trim().isEmpty() ||
                semesterStr == null || semesterStr.trim().isEmpty() ||
                skillsStr == null || skillsStr.trim().isEmpty()) {
                
                sendErrorResponse(out, "All required fields must be filled out.");
                return;
            }
            
            // Parse semester to integer
            int semester;
            try {
                semester = Integer.parseInt(semesterStr);
                if (semester < 1 || semester > 10) {
                    throw new NumberFormatException("Semester must be between 1 and 10");
                }
            } catch (NumberFormatException e) {
                sendErrorResponse(out, "Invalid semester value. Please select a valid semester.");
                return;
            }
            
            // Process skills - split by comma and trim whitespace
            List<String> skillsList = new ArrayList<>();
            if (skillsStr != null && !skillsStr.trim().isEmpty()) {
                String[] skillsArray = skillsStr.split(",");
                for (String skill : skillsArray) {
                    String trimmedSkill = skill.trim();
                    if (!trimmedSkill.isEmpty()) {
                        skillsList.add(trimmedSkill);
                    }
                }
            }
            
            // Process interest areas
            List<String> interestAreasList = new ArrayList<>();
            if (interestAreasArray != null) {
                interestAreasList = Arrays.asList(interestAreasArray);
            }
            
            // Handle registration or update based on action
            if ("update".equals(action)) {
                // Update existing student profile
                handleProfileUpdate(out, name.trim(), email.trim(), department.trim(), semester,
                                  skillsList, cvLink != null ? cvLink.trim() : null, interestAreasList);
            } else {
                // Register new student
                handleStudentRegistration(out, name.trim(), email.trim(), department.trim(), semester,
                                        skillsList, cvLink != null ? cvLink.trim() : null, interestAreasList);
            }
            
        } catch (Exception e) {
            // Handle any unexpected errors
            System.err.println("Error processing student registration: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(out, "An error occurred while processing your registration. Please try again.");
        } finally {
            out.close();
        }
    }
    
    /**
     * Handles new student registration
     */
    private void handleStudentRegistration(PrintWriter out, String name, String email, String department, 
                                         int semester, List<String> skills, String cvLink, List<String> interestAreas) {
        try {
            // Create Student object using constructor
            Student student = new Student(name, email, department, semester, skills, cvLink, interestAreas);
            
            // Save student to mock database
            int studentId = DatabaseMock.addStudent(student);
            System.out.println("✅ Student successfully registered with ID: " + studentId);
            
            // Generate skill recommendations using AI engine
            List<String> recommendedSkills = SkillRecommender.recommendSkillsAdvanced(
                interestAreas, skills, semester);
            List<String> complementarySkills = SkillRecommender.recommendComplementarySkills(skills);
            
            // Log registration for debugging/monitoring
            logStudentRegistration(student);
            
            // Log skill recommendations
            logSkillRecommendations(student, recommendedSkills, complementarySkills);
            
            // Display current database stats
            System.out.println(DatabaseMock.getDatabaseStats());
            
            // Send success response with student ID and recommendations
            sendSuccessResponseWithRecommendations(out, student, studentId, "registration", 
                                                 recommendedSkills, complementarySkills);
            
        } catch (IllegalArgumentException e) {
            // Handle duplicate email or other validation errors
            sendErrorResponse(out, "Registration failed: " + e.getMessage());
        }
    }
    
    /**
     * Handles updating existing student profile
     */
    private void handleProfileUpdate(PrintWriter out, String name, String email, String department, 
                                   int semester, List<String> skills, String cvLink, List<String> interestAreas) {
        try {
            // Check if student exists
            Student existingStudent = DatabaseMock.findStudentByEmail(email);
            
            if (existingStudent != null) {
                // Update existing student profile
                existingStudent.setName(name);
                existingStudent.setDepartment(department);
                existingStudent.updateProfile(semester, skills, cvLink, interestAreas);
                
                System.out.println("✅ Student profile updated successfully: " + email);
                
                // Generate updated skill recommendations
                List<String> recommendedSkills = SkillRecommender.recommendSkillsAdvanced(
                    interestAreas, skills, semester);
                List<String> complementarySkills = SkillRecommender.recommendComplementarySkills(skills);
                
                // Log update for debugging/monitoring
                logStudentUpdate(existingStudent);
                
                // Log skill recommendations
                logSkillRecommendations(existingStudent, recommendedSkills, complementarySkills);
                
                // Send success response with recommendations
                sendSuccessResponseWithRecommendations(out, existingStudent, existingStudent.getEmail().hashCode(), 
                                                     "update", recommendedSkills, complementarySkills);
                
            } else {
                // Student doesn't exist, suggest registration instead
                sendErrorResponse(out, "No student found with email " + email + ". Please register as a new student first.");
            }
            
        } catch (Exception e) {
            sendErrorResponse(out, "Profile update failed: " + e.getMessage());
        }
    }
    
    /**
     * Sends a success response to the user with registration confirmation (backwards compatibility)
     * 
     * @param out PrintWriter for response output
     * @param student The registered student object
     * @param studentId The assigned student ID from database
     */
    private void sendSuccessResponse(PrintWriter out, Student student, int studentId) {
        sendSuccessResponse(out, student, studentId, "registration");
    }
    
    /**
     * Sends a success response with AI-powered skill recommendations
     * 
     * @param out PrintWriter for response output
     * @param student The registered/updated student object
     * @param studentId The assigned student ID from database
     * @param actionType The type of action performed (registration or update)
     * @param recommendedSkills List of recommended skills from AI engine
     * @param complementarySkills List of complementary skills
     */
        private void sendSuccessResponseWithRecommendations(PrintWriter out, Student student, int studentId, 
                                                       String actionType, List<String> recommendedSkills, 
                                                       List<String> complementarySkills) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        String pageTitle = "update".equals(actionType) ? "Profile Update Success - SkillSync" : "Registration Success - SkillSync";
        out.println("    <title>" + pageTitle + "</title>");
        out.println("    <link rel='stylesheet' href='css/style.css'>");
        out.println("    <style>");
        out.println("        .recommendation-section { background-color: #e8f4fd; border: 1px solid #bee5eb; border-radius: 8px; padding: 1.5rem; margin: 1.5rem 0; }");
        out.println("        .recommendation-section h3 { color: #0c5460; margin-bottom: 1rem; }");
        out.println("        .skill-list { display: flex; flex-wrap: wrap; gap: 0.5rem; margin-top: 0.5rem; }");
        out.println("        .skill-tag { background-color: #3498db; color: white; padding: 0.3rem 0.8rem; border-radius: 15px; font-size: 0.9rem; }");
        out.println("        .complementary-skill-tag { background-color: #27ae60; color: white; padding: 0.3rem 0.8rem; border-radius: 15px; font-size: 0.9rem; }");
        out.println("        .ai-badge { background-color: #9b59b6; color: white; padding: 0.2rem 0.5rem; border-radius: 10px; font-size: 0.8rem; font-weight: bold; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        
        // Header
        out.println("    <header>");
        out.println("        <nav>");
        out.println("            <div class='logo'>");
        out.println("                <h1>SkillSync</h1>");
        out.println("            </div>");
        out.println("            <ul class='nav-links'>");
        out.println("                <li><a href='index.html'>Home</a></li>");
        out.println("                <li><a href='student_registration.html'>Student Registration</a></li>");
        out.println("                <li><a href='organizer_request.html'>Organizer Request</a></li>");
        out.println("                <li><a href='login'>Login</a></li>");
        out.println("            </ul>");
        out.println("        </nav>");
        out.println("    </header>");
        
        // Main content
        out.println("    <main>");
        out.println("        <div class='form-container'>");
        out.println("            <section class='registration-form'>");
        
        String title = "update".equals(actionType) ? "🎉 Profile Updated Successfully!" : "🎉 Registration Successful!";
        String subtitle = "update".equals(actionType) ? 
            "Your profile has been updated, " + student.getName() + "!" : 
            "Welcome to SkillSync, " + student.getName() + "!";
        String detailsHeader = "update".equals(actionType) ? "Updated Profile Details:" : "Registration Details:";
        
        out.println("                <h2>" + title + "</h2>");
        out.println("                <p class='form-description'>" + subtitle + "</p>");

        // Profile details section
        out.println("                <div style='background-color: #d4edda; border: 1px solid #c3e6cb; border-radius: 5px; padding: 2rem; margin: 2rem 0;'>");
        out.println("                    <h3 style='color: #155724; margin-bottom: 1rem;'>" + detailsHeader + "</h3>");
        out.println("                    <p><strong>Student ID:</strong> #" + studentId + "</p>");
        out.println("                    <p><strong>Name:</strong> " + student.getName() + "</p>");
        out.println("                    <p><strong>Email:</strong> " + student.getEmail() + "</p>");
        out.println("                    <p><strong>Department:</strong> " + student.getDepartment() + "</p>");
        out.println("                    <p><strong>Semester:</strong> " + student.getSemester() + "</p>");
        
        if (!student.getSkills().isEmpty()) {
            out.println("                    <p><strong>Skills:</strong> " + String.join(", ", student.getSkills()) + "</p>");
        }
        
        if (!student.getInterestAreas().isEmpty()) {
            out.println("                    <p><strong>Interest Areas:</strong> " + String.join(", ", student.getInterestAreas()) + "</p>");
        }
        
        if (student.getCvLink() != null && !student.getCvLink().isEmpty()) {
            out.println("                    <p><strong>CV Link:</strong> <a href='" + student.getCvLink() + "' target='_blank'>" + student.getCvLink() + "</a></p>");
        }
        
        if (student.getLastUpdated() != null) {
            out.println("                    <p><strong>Last Updated:</strong> " + student.getLastUpdated().toString() + "</p>");
        }
        out.println("                </div>");
        
        // AI Skill Recommendations Section
        if (!recommendedSkills.isEmpty() || !complementarySkills.isEmpty()) {
            out.println("                <div class='recommendation-section'>");
            out.println("                    <h3>🤖 AI-Powered Skill Recommendations <span class='ai-badge'>AI</span></h3>");
            out.println("                    <p>Based on your interests and current skills, our AI engine suggests these skills to enhance your profile:</p>");
            
            if (!recommendedSkills.isEmpty()) {
                out.println("                    <h4>📚 Recommended Skills for Your Interests:</h4>");
                out.println("                    <div class='skill-list'>");
                for (String skill : recommendedSkills) {
                    out.println("                        <span class='skill-tag'>" + skill + "</span>");
                }
                out.println("                    </div>");
            }
            
            if (!complementarySkills.isEmpty()) {
                out.println("                    <h4>🔗 Complementary Skills:</h4>");
                out.println("                    <p style='font-size: 0.9rem; color: #666; margin-bottom: 0.5rem;'>Skills that work well with your existing abilities:</p>");
                out.println("                    <div class='skill-list'>");
                for (String skill : complementarySkills) {
                    out.println("                        <span class='complementary-skill-tag'>" + skill + "</span>");
                }
                out.println("                    </div>");
            }
            
            out.println("                    <p style='margin-top: 1rem; font-size: 0.9rem; color: #555;'>");
            out.println("                        💡 <strong>Pro Tip:</strong> Consider adding these skills to your profile to increase your chances of being selected for events and opportunities!");
            out.println("                    </p>");
            out.println("                </div>");
        }
        
        // Action buttons
        out.println("                <div class='form-actions'>");
        out.println("                    <a href='index.html' class='btn btn-primary'>Go to Homepage</a>");
        if ("update".equals(actionType)) {
            out.println("                    <a href='student_registration.html' class='btn btn-secondary'>Update Profile Again</a>");
        } else {
            out.println("                    <a href='student_registration.html' class='btn btn-secondary'>Register Another Student</a>");
        }
        out.println("                    <a href='filter_students.html' class='btn btn-success'>Browse Events</a>");
        out.println("                </div>");
        
        out.println("                <div class='form-footer'>");
        if ("update".equals(actionType)) {
            out.println("                    <p>Your profile has been successfully updated!</p>");
            out.println("                    <p>Organizers will now see your latest information and skills.</p>");
        } else {
            out.println("                    <p>You will receive a confirmation email shortly at " + student.getEmail() + "</p>");
            out.println("                    <p>Check your inbox and start exploring opportunities!</p>");
        }
        out.println("                </div>");
        out.println("            </section>");
        out.println("        </div>");
        out.println("    </main>");
        
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Sends a success response to the user with registration confirmation
     * 
     * @param out PrintWriter for response output
     * @param student The registered student object
     * @param studentId The assigned student ID from database
     * @param actionType The type of action performed (registration or update)
     */
    private void sendSuccessResponse(PrintWriter out, Student student, int studentId, String actionType) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Registration Success - SkillSync</title>");
        out.println("    <link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        
        // Header
        out.println("    <header>");
        out.println("        <nav>");
        out.println("            <div class='logo'>");
        out.println("                <h1>SkillSync</h1>");
        out.println("            </div>");
        out.println("            <ul class='nav-links'>");
        out.println("                <li><a href='index.html'>Home</a></li>");
        out.println("                <li><a href='student_registration.html'>Student Registration</a></li>");
        out.println("                <li><a href='organizer_request.html'>Organizer Request</a></li>");
        out.println("                <li><a href='login'>Login</a></li>");
        out.println("            </ul>");
        out.println("        </nav>");
        out.println("    </header>");
        
        // Main content
        out.println("    <main>");
        out.println("        <div class='form-container'>");
        out.println("            <section class='registration-form'>");
                        String title = "update".equals(actionType) ? "🎉 Profile Updated Successfully!" : "🎉 Registration Successful!";
                String subtitle = "update".equals(actionType) ? 
                    "Your profile has been updated, " + student.getName() + "!" : 
                    "Welcome to SkillSync, " + student.getName() + "!";
                String detailsHeader = "update".equals(actionType) ? "Updated Profile Details:" : "Registration Details:";
                
                out.println("                <h2>" + title + "</h2>");
                out.println("                <p class='form-description'>" + subtitle + "</p>");
        
        out.println("                <div style='background-color: #d4edda; border: 1px solid #c3e6cb; border-radius: 5px; padding: 2rem; margin: 2rem 0;'>");
        out.println("                    <h3 style='color: #155724; margin-bottom: 1rem;'>" + detailsHeader + "</h3>");
        out.println("                    <p><strong>Student ID:</strong> #" + studentId + "</p>");
        out.println("                    <p><strong>Name:</strong> " + student.getName() + "</p>");
        out.println("                    <p><strong>Email:</strong> " + student.getEmail() + "</p>");
        out.println("                    <p><strong>Department:</strong> " + student.getDepartment() + "</p>");
        out.println("                    <p><strong>Semester:</strong> " + student.getSemester() + "</p>");
        
        if (!student.getSkills().isEmpty()) {
            out.println("                    <p><strong>Skills:</strong> " + String.join(", ", student.getSkills()) + "</p>");
        }
        
        if (!student.getInterestAreas().isEmpty()) {
            out.println("                    <p><strong>Interest Areas:</strong> " + String.join(", ", student.getInterestAreas()) + "</p>");
        }
        
        if (student.getCvLink() != null && !student.getCvLink().isEmpty()) {
            out.println("                    <p><strong>CV Link:</strong> <a href='" + student.getCvLink() + "' target='_blank'>" + student.getCvLink() + "</a></p>");
        }
        
        if (student.getLastUpdated() != null) {
            out.println("                    <p><strong>Last Updated:</strong> " + student.getLastUpdated().toString() + "</p>");
        }
        out.println("                </div>");
        
        out.println("                <div class='form-actions'>");
        out.println("                    <a href='index.html' class='btn btn-primary'>Go to Homepage</a>");
        out.println("                    <a href='student_registration.html' class='btn btn-secondary'>Register Another Student</a>");
        out.println("                </div>");
        
        out.println("                <div class='form-footer'>");
        out.println("                    <p>You will receive a confirmation email shortly at " + student.getEmail() + "</p>");
        out.println("                    <p>Check your inbox and start exploring opportunities!</p>");
        out.println("                </div>");
        out.println("            </section>");
        out.println("        </div>");
        out.println("    </main>");
        
        // Footer
        out.println("    <footer>");
        out.println("        <p>&copy; 2024 SkillSync. All rights reserved.</p>");
        out.println("    </footer>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Sends an error response to the user
     * 
     * @param out PrintWriter for response output
     * @param errorMessage The error message to display
     */
    private void sendErrorResponse(PrintWriter out, String errorMessage) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Registration Error - SkillSync</title>");
        out.println("    <link rel='stylesheet' href='css/style.css'>");
        out.println("</head>");
        out.println("<body>");
        
        // Header
        out.println("    <header>");
        out.println("        <nav>");
        out.println("            <div class='logo'>");
        out.println("                <h1>SkillSync</h1>");
        out.println("            </div>");
        out.println("            <ul class='nav-links'>");
        out.println("                <li><a href='index.html'>Home</a></li>");
        out.println("                <li><a href='student_registration.html'>Student Registration</a></li>");
        out.println("                <li><a href='organizer_request.html'>Organizer Request</a></li>");
        out.println("                <li><a href='login'>Login</a></li>");
        out.println("            </ul>");
        out.println("        </nav>");
        out.println("    </header>");
        
        // Main content
        out.println("    <main>");
        out.println("        <div class='form-container'>");
        out.println("            <section class='registration-form'>");
        out.println("                <h2>❌ Registration Error</h2>");
        out.println("                <div style='background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 5px; padding: 2rem; margin: 2rem 0;'>");
        out.println("                    <h3 style='color: #721c24; margin-bottom: 1rem;'>Error:</h3>");
        out.println("                    <p style='color: #721c24;'>" + errorMessage + "</p>");
        out.println("                </div>");
        out.println("                <div class='form-actions'>");
        out.println("                    <a href='student_registration.html' class='btn btn-primary'>Back to Registration</a>");
        out.println("                    <a href='index.html' class='btn btn-secondary'>Go to Homepage</a>");
        out.println("                </div>");
        out.println("            </section>");
        out.println("        </div>");
        out.println("    </main>");
        
        // Footer
        out.println("    <footer>");
        out.println("        <p>&copy; 2024 SkillSync. All rights reserved.</p>");
        out.println("    </footer>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Logs student registration for monitoring and debugging
     * 
     * @param student The registered student
     */
    private void logStudentRegistration(Student student) {
        System.out.println("=== NEW STUDENT REGISTRATION ===");
        System.out.println("Timestamp: " + new java.util.Date());
        student.displayProfile();
        System.out.println("================================");
    }
    
    /**
     * Logs student profile update for monitoring and debugging
     * 
     * @param student The updated student
     */
    private void logStudentUpdate(Student student) {
        System.out.println("=== STUDENT PROFILE UPDATE ===");
        System.out.println("Timestamp: " + new java.util.Date());
        student.displayProfile();
        System.out.println("==============================");
    }
    
    /**
     * Logs AI-generated skill recommendations for monitoring and analysis
     * 
     * @param student The student who received recommendations
     * @param recommendedSkills List of recommended skills
     * @param complementarySkills List of complementary skills
     */
    private void logSkillRecommendations(Student student, List<String> recommendedSkills, List<String> complementarySkills) {
        System.out.println("=== AI SKILL RECOMMENDATIONS ===");
        System.out.println("Student: " + student.getName() + " (" + student.getEmail() + ")");
        System.out.println("Timestamp: " + new java.util.Date());
        System.out.println("Interest Areas: " + student.getInterestAreas());
        System.out.println("Current Skills: " + student.getSkills());
        System.out.println("Recommended Skills: " + recommendedSkills);
        System.out.println("Complementary Skills: " + complementarySkills);
        System.out.println("Total Recommendations: " + (recommendedSkills.size() + complementarySkills.size()));
        
        // Generate and log detailed recommendation report
        String report = SkillRecommender.generateRecommendationReport(
            student.getInterestAreas(), student.getSkills(), recommendedSkills);
        System.out.println(report);
        System.out.println("=================================");
    }
}