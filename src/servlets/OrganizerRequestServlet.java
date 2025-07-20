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

import models.Organizer;
import utils.DatabaseMock;

/**
 * Servlet for handling organizer request form submissions in SkillSync application
 * Processes POST requests from organizer_request.html form
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
@WebServlet("/OrganizerRequestServlet")
public class OrganizerRequestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect GET requests to the organizer request form
        response.sendRedirect("organizer_request.html");
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
            String preferredSemester = request.getParameter("preferredSemester");
            String organizationName = request.getParameter("organizationName");
            String organizationType = request.getParameter("organizationType");
            String requiredSkillsStr = request.getParameter("requiredSkills");
            String[] eventTypesArray = request.getParameterValues("eventTypes");
            String eventDescription = request.getParameter("eventDescription");
            String experience = request.getParameter("experience");
            String termsAgreement = request.getParameter("termsAgreement");
            
            // Validate required fields
            if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                department == null || department.trim().isEmpty() ||
                organizationName == null || organizationName.trim().isEmpty() ||
                requiredSkillsStr == null || requiredSkillsStr.trim().isEmpty() ||
                termsAgreement == null || !termsAgreement.equals("agreed")) {
                
                sendErrorResponse(out, "All required fields must be filled out and terms must be agreed to.");
                return;
            }
            
            // Process required skills - split by comma and trim whitespace
            List<String> requiredSkillsList = new ArrayList<>();
            if (requiredSkillsStr != null && !requiredSkillsStr.trim().isEmpty()) {
                String[] skillsArray = requiredSkillsStr.split(",");
                for (String skill : skillsArray) {
                    String trimmedSkill = skill.trim();
                    if (!trimmedSkill.isEmpty()) {
                        requiredSkillsList.add(trimmedSkill);
                    }
                }
            }
            
            // Create Organizer object using constructor
            // Note: Using semester as 0 for organizers as they typically don't have semesters
            Organizer organizer = new Organizer(name.trim(), email.trim(), department.trim(), 0,
                                              organizationName.trim(), requiredSkillsList);
            
            // Store additional information (these would typically be saved to database)
            String organizerPreferredSemester = preferredSemester != null ? preferredSemester : "Any Semester";
            String organizerType = organizationType != null ? organizationType : "Not specified";
            List<String> eventTypesList = eventTypesArray != null ? Arrays.asList(eventTypesArray) : new ArrayList<>();
            String organizerEventDescription = eventDescription != null ? eventDescription.trim() : "";
            String organizerExperience = experience != null ? experience.trim() : "";
            
            // Save organizer to mock database
            try {
                int organizerId = DatabaseMock.addOrganizer(organizer);
                System.out.println("✅ Organizer successfully registered with ID: " + organizerId);
                
                // Log organizer request for debugging/monitoring
                logOrganizerRequest(organizer, organizerPreferredSemester, organizerType, 
                                  eventTypesList, organizerEventDescription, organizerExperience);
                
                // Display current database stats
                System.out.println(DatabaseMock.getDatabaseStats());
                
                // TODO: Save additional fields like organizationType, eventTypes, etc. to separate tables
                // TODO: Send notification email to admin about new organizer request
                // TODO: Send confirmation email to organizer
                
                // Send success response with organizer ID
                sendSuccessResponse(out, organizer, organizerId, organizerPreferredSemester, organizerType, 
                                  eventTypesList, organizerEventDescription, organizerExperience);
                
            } catch (IllegalArgumentException e) {
                // Handle duplicate email or other validation errors
                sendErrorResponse(out, "Request failed: " + e.getMessage());
                return;
            }
            
        } catch (Exception e) {
            // Handle any unexpected errors
            System.err.println("Error processing organizer request: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(out, "An error occurred while processing your request. Please try again.");
        } finally {
            out.close();
        }
    }
    
    /**
     * Sends a success response to the user with request confirmation
     * 
     * @param out PrintWriter for response output
     * @param organizer The organizer object
     * @param organizerId The assigned organizer ID from database
     * @param preferredSemester Preferred target semester
     * @param organizationType Type of organization
     * @param eventTypes List of event types
     * @param eventDescription Event description
     * @param experience Organizer experience
     */
    private void sendSuccessResponse(PrintWriter out, Organizer organizer, int organizerId, String preferredSemester,
                                   String organizationType, List<String> eventTypes, 
                                   String eventDescription, String experience) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Request Submitted - SkillSync</title>");
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
        out.println("                <h2>✅ Request Submitted Successfully!</h2>");
        out.println("                <p class='form-description'>Thank you for your interest in organizing events, " + organizer.getName() + "!</p>");
        
        out.println("                <div style='background-color: #d1ecf1; border: 1px solid #bee5eb; border-radius: 5px; padding: 2rem; margin: 2rem 0;'>");
        out.println("                    <h3 style='color: #0c5460; margin-bottom: 1rem;'>Request Details:</h3>");
        out.println("                    <p><strong>Organizer ID:</strong> #" + organizerId + "</p>");
        out.println("                    <p><strong>Name:</strong> " + organizer.getName() + "</p>");
        out.println("                    <p><strong>Email:</strong> " + organizer.getEmail() + "</p>");
        out.println("                    <p><strong>Department:</strong> " + organizer.getDepartment() + "</p>");
        out.println("                    <p><strong>Organization:</strong> " + organizer.getOrganizationName() + "</p>");
        
        if (organizationType != null && !organizationType.isEmpty() && !organizationType.equals("Not specified")) {
            out.println("                    <p><strong>Organization Type:</strong> " + organizationType + "</p>");
        }
        
        out.println("                    <p><strong>Preferred Target Semester:</strong> " + preferredSemester + "</p>");
        
        if (!organizer.getRequiredSkills().isEmpty()) {
            out.println("                    <p><strong>Required Skills:</strong> " + String.join(", ", organizer.getRequiredSkills()) + "</p>");
        }
        
        if (!eventTypes.isEmpty()) {
            out.println("                    <p><strong>Event Types:</strong> " + String.join(", ", eventTypes) + "</p>");
        }
        
        if (eventDescription != null && !eventDescription.isEmpty()) {
            out.println("                    <p><strong>Event Description:</strong></p>");
            out.println("                    <p style='margin-left: 1rem; font-style: italic;'>" + eventDescription + "</p>");
        }
        
        if (experience != null && !experience.isEmpty()) {
            out.println("                    <p><strong>Experience:</strong></p>");
            out.println("                    <p style='margin-left: 1rem; font-style: italic;'>" + experience + "</p>");
        }
        out.println("                </div>");
        
        out.println("                <div style='background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 5px; padding: 1.5rem; margin: 2rem 0;'>");
        out.println("                    <h3 style='color: #856404; margin-bottom: 1rem;'>What's Next?</h3>");
        out.println("                    <ul style='color: #856404; margin-left: 1.5rem;'>");
        out.println("                        <li>Our team will review your application within 2-3 business days</li>");
        out.println("                        <li>You will receive an email notification about the status of your request</li>");
        out.println("                        <li>If approved, you'll get access to the organizer dashboard</li>");
        out.println("                        <li>You can then start creating amazing opportunities for students!</li>");
        out.println("                    </ul>");
        out.println("                </div>");
        
        out.println("                <div class='form-actions'>");
        out.println("                    <a href='index.html' class='btn btn-primary'>Go to Homepage</a>");
        out.println("                    <a href='organizer_request.html' class='btn btn-secondary'>Submit Another Request</a>");
        out.println("                </div>");
        
        out.println("                <div class='form-footer'>");
        out.println("                    <p>A confirmation email has been sent to " + organizer.getEmail() + "</p>");
        out.println("                    <p>Questions? Contact us at <a href='mailto:support@skillsync.com'>support@skillsync.com</a></p>");
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
        out.println("    <title>Request Error - SkillSync</title>");
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
        out.println("                <h2>❌ Request Error</h2>");
        out.println("                <div style='background-color: #f8d7da; border: 1px solid #f5c6cb; border-radius: 5px; padding: 2rem; margin: 2rem 0;'>");
        out.println("                    <h3 style='color: #721c24; margin-bottom: 1rem;'>Error:</h3>");
        out.println("                    <p style='color: #721c24;'>" + errorMessage + "</p>");
        out.println("                </div>");
        out.println("                <div class='form-actions'>");
        out.println("                    <a href='organizer_request.html' class='btn btn-primary'>Back to Request Form</a>");
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
     * Logs organizer request for monitoring and debugging
     * 
     * @param organizer The organizer object
     * @param preferredSemester Preferred target semester
     * @param organizationType Type of organization
     * @param eventTypes List of event types
     * @param eventDescription Event description
     * @param experience Organizer experience
     */
    private void logOrganizerRequest(Organizer organizer, String preferredSemester, String organizationType,
                                   List<String> eventTypes, String eventDescription, String experience) {
        System.out.println("=== NEW ORGANIZER REQUEST ===");
        System.out.println("Timestamp: " + new java.util.Date());
        organizer.displayProfile();
        System.out.println("Additional Information:");
        System.out.println("Preferred Semester: " + preferredSemester);
        System.out.println("Organization Type: " + organizationType);
        System.out.println("Event Types: " + eventTypes);
        System.out.println("Event Description: " + eventDescription);
        System.out.println("Experience: " + experience);
        System.out.println("==============================");
    }
}