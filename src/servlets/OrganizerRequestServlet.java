package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling organizer request form submissions in SkillSync application
 */
@WebServlet("/OrganizerRequestServlet")
public class OrganizerRequestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect to the organizer request form
        response.sendRedirect("organizer_request.html");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Extract form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String department = request.getParameter("department");
        String preferredSemester = request.getParameter("preferredSemester");
        String organizationName = request.getParameter("organizationName");
        String organizationType = request.getParameter("organizationType");
        String requiredSkills = request.getParameter("requiredSkills");
        String[] eventTypes = request.getParameterValues("eventTypes");
        String eventDescription = request.getParameter("eventDescription");
        String experience = request.getParameter("experience");
        String termsAgreement = request.getParameter("termsAgreement");
        
        // TODO: Validate input data
        
        // TODO: Create Organizer object and save to database
        
        // TODO: For now, just print the received data (for testing)
        System.out.println("Organizer Request Data:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Department: " + department);
        System.out.println("Preferred Semester: " + preferredSemester);
        System.out.println("Organization Name: " + organizationName);
        System.out.println("Organization Type: " + organizationType);
        System.out.println("Required Skills: " + requiredSkills);
        
        if (eventTypes != null) {
            System.out.println("Event Types:");
            for (String eventType : eventTypes) {
                System.out.println("  - " + eventType);
            }
        }
        
        System.out.println("Event Description: " + eventDescription);
        System.out.println("Experience: " + experience);
        System.out.println("Terms Agreement: " + termsAgreement);
        
        // TODO: Send confirmation email to organizer
        // TODO: Notify admin about new organizer request
        // TODO: Redirect to success page
        response.sendRedirect("index.html?organizer_request=success");
    }
}