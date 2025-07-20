package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for handling student registration form submissions in SkillSync application
 */
@WebServlet("/StudentRegistrationServlet")
public class StudentRegistrationServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect to the registration form
        response.sendRedirect("student_registration.html");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Extract form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String department = request.getParameter("department");
        String semesterStr = request.getParameter("semester");
        String cvLink = request.getParameter("cvLink");
        String skills = request.getParameter("skills");
        String[] interestAreas = request.getParameterValues("interestAreas");
        
        // TODO: Validate input data
        
        // TODO: Create Student object and save to database
        
        // TODO: For now, just print the received data (for testing)
        System.out.println("Student Registration Data:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Department: " + department);
        System.out.println("Semester: " + semesterStr);
        System.out.println("CV Link: " + cvLink);
        System.out.println("Skills: " + skills);
        if (interestAreas != null) {
            System.out.println("Interest Areas:");
            for (String interest : interestAreas) {
                System.out.println("  - " + interest);
            }
        }
        
        // TODO: Redirect to success page or dashboard
        response.sendRedirect("index.html?registration=success");
    }
}