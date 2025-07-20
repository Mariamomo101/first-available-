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
            
            // Create Student object using constructor
            Student student = new Student(name.trim(), email.trim(), department.trim(), semester,
                                        skillsList, cvLink != null ? cvLink.trim() : null, 
                                        interestAreasList);
            
            // Log registration for debugging/monitoring
            logStudentRegistration(student);
            
            // TODO: Save student to database
            // Example: studentDAO.save(student);
            
            // Send success response
            sendSuccessResponse(out, student);
            
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
     * Sends a success response to the user with registration confirmation
     * 
     * @param out PrintWriter for response output
     * @param student The registered student object
     */
    private void sendSuccessResponse(PrintWriter out, Student student) {
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
        out.println("                <h2>🎉 Registration Successful!</h2>");
        out.println("                <p class='form-description'>Welcome to SkillSync, " + student.getName() + "!</p>");
        
        out.println("                <div style='background-color: #d4edda; border: 1px solid #c3e6cb; border-radius: 5px; padding: 2rem; margin: 2rem 0;'>");
        out.println("                    <h3 style='color: #155724; margin-bottom: 1rem;'>Registration Details:</h3>");
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
}