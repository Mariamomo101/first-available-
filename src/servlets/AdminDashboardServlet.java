package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Student;
import models.Organizer;
import utils.DatabaseMock;

/**
 * Admin Dashboard Servlet for SkillSync application
 * Displays all registered students and organizers from the mock database
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
@WebServlet("/admin")
public class AdminDashboardServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Set response content type
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Get all data from mock database
            List<Student> students = DatabaseMock.getAllStudents();
            List<Organizer> organizers = DatabaseMock.getAllOrganizers();
            
            // Generate admin dashboard HTML
            generateAdminDashboard(out, students, organizers);
            
        } catch (Exception e) {
            System.err.println("Error generating admin dashboard: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(out, "Error loading dashboard data: " + e.getMessage());
        } finally {
            out.close();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("clear".equals(action)) {
            // Clear all data
            DatabaseMock.clearAll();
            System.out.println("🗑️ Admin cleared all database data");
            response.sendRedirect("admin");
        } else {
            // Default to GET behavior
            doGet(request, response);
        }
    }
    
    /**
     * Generates the admin dashboard HTML
     * 
     * @param out PrintWriter for response output
     * @param students List of all students
     * @param organizers List of all organizers
     */
    private void generateAdminDashboard(PrintWriter out, List<Student> students, List<Organizer> organizers) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Admin Dashboard - SkillSync</title>");
        out.println("    <link rel='stylesheet' href='css/style.css'>");
        out.println("    <style>");
        out.println("        .admin-container { max-width: 1200px; margin: 2rem auto; padding: 0 2rem; }");
        out.println("        .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1rem; margin-bottom: 2rem; }");
        out.println("        .stat-card { background: white; padding: 1.5rem; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; }");
        out.println("        .stat-number { font-size: 2rem; font-weight: bold; color: #3498db; }");
        out.println("        .data-section { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); margin-bottom: 2rem; }");
        out.println("        .user-card { border: 1px solid #e9ecef; border-radius: 5px; padding: 1rem; margin-bottom: 1rem; }");
        out.println("        .user-header { font-weight: bold; color: #2c3e50; margin-bottom: 0.5rem; }");
        out.println("        .user-details { color: #6c757d; font-size: 0.9rem; }");
        out.println("        .skills-list, .interests-list { display: flex; flex-wrap: wrap; gap: 0.25rem; margin-top: 0.5rem; }");
        out.println("        .skill-tag, .interest-tag { background: #e9ecef; padding: 0.25rem 0.5rem; border-radius: 3px; font-size: 0.8rem; }");
        out.println("        .clear-btn { background: #dc3545; color: white; border: none; padding: 0.5rem 1rem; border-radius: 5px; cursor: pointer; }");
        out.println("        .clear-btn:hover { background: #c82333; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        
        // Header
        out.println("    <header>");
        out.println("        <nav>");
        out.println("            <div class='logo'>");
        out.println("                <h1>SkillSync Admin</h1>");
        out.println("            </div>");
        out.println("            <ul class='nav-links'>");
        out.println("                <li><a href='index.html'>Home</a></li>");
        out.println("                <li><a href='student_registration.html'>Student Registration</a></li>");
        out.println("                <li><a href='organizer_request.html'>Organizer Request</a></li>");
        out.println("                <li><a href='admin'>Admin Dashboard</a></li>");
        out.println("            </ul>");
        out.println("        </nav>");
        out.println("    </header>");
        
        // Main content
        out.println("    <div class='admin-container'>");
        out.println("        <h2>📊 SkillSync Database Dashboard</h2>");
        out.println("        <p>Real-time view of registered students and organizers</p>");
        
        // Statistics
        out.println("        <div class='stats-grid'>");
        out.println("            <div class='stat-card'>");
        out.println("                <div class='stat-number'>" + students.size() + "</div>");
        out.println("                <div>Registered Students</div>");
        out.println("            </div>");
        out.println("            <div class='stat-card'>");
        out.println("                <div class='stat-number'>" + organizers.size() + "</div>");
        out.println("                <div>Registered Organizers</div>");
        out.println("            </div>");
        out.println("            <div class='stat-card'>");
        out.println("                <div class='stat-number'>" + (students.size() + organizers.size()) + "</div>");
        out.println("                <div>Total Users</div>");
        out.println("            </div>");
        out.println("        </div>");
        
        // Database actions
        out.println("        <div class='data-section'>");
        out.println("            <h3>Database Actions</h3>");
        out.println("            <form method='post' style='display: inline;' onsubmit='return confirm(\"Are you sure you want to clear all data?\");'>");
        out.println("                <input type='hidden' name='action' value='clear'>");
        out.println("                <button type='submit' class='clear-btn'>🗑️ Clear All Data</button>");
        out.println("            </form>");
        out.println("            <button onclick='location.reload()' class='btn btn-secondary' style='margin-left: 1rem;'>🔄 Refresh</button>");
        out.println("        </div>");
        
        // Students section
        out.println("        <div class='data-section'>");
        out.println("            <h3>👨‍🎓 Registered Students (" + students.size() + ")</h3>");
        
        if (students.isEmpty()) {
            out.println("            <p>No students registered yet.</p>");
        } else {
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                out.println("            <div class='user-card'>");
                out.println("                <div class='user-header'>#" + (i + 1) + " " + student.getName() + "</div>");
                out.println("                <div class='user-details'>");
                out.println("                    <strong>Email:</strong> " + student.getEmail() + "<br>");
                out.println("                    <strong>Department:</strong> " + student.getDepartment() + "<br>");
                out.println("                    <strong>Semester:</strong> " + student.getSemester());
                
                if (student.getCvLink() != null && !student.getCvLink().isEmpty()) {
                    out.println("                    <br><strong>CV:</strong> <a href='" + student.getCvLink() + "' target='_blank'>View CV</a>");
                }
                
                if (!student.getSkills().isEmpty()) {
                    out.println("                    <br><strong>Skills:</strong>");
                    out.println("                    <div class='skills-list'>");
                    for (String skill : student.getSkills()) {
                        out.println("                        <span class='skill-tag'>" + skill + "</span>");
                    }
                    out.println("                    </div>");
                }
                
                if (!student.getInterestAreas().isEmpty()) {
                    out.println("                    <br><strong>Interests:</strong>");
                    out.println("                    <div class='interests-list'>");
                    for (String interest : student.getInterestAreas()) {
                        out.println("                        <span class='interest-tag'>" + interest + "</span>");
                    }
                    out.println("                    </div>");
                }
                
                out.println("                </div>");
                out.println("            </div>");
            }
        }
        out.println("        </div>");
        
        // Organizers section
        out.println("        <div class='data-section'>");
        out.println("            <h3>🏢 Registered Organizers (" + organizers.size() + ")</h3>");
        
        if (organizers.isEmpty()) {
            out.println("            <p>No organizers registered yet.</p>");
        } else {
            for (int i = 0; i < organizers.size(); i++) {
                Organizer organizer = organizers.get(i);
                out.println("            <div class='user-card'>");
                out.println("                <div class='user-header'>#" + (i + 1) + " " + organizer.getName() + "</div>");
                out.println("                <div class='user-details'>");
                out.println("                    <strong>Email:</strong> " + organizer.getEmail() + "<br>");
                out.println("                    <strong>Department:</strong> " + organizer.getDepartment() + "<br>");
                out.println("                    <strong>Organization:</strong> " + organizer.getOrganizationName());
                
                if (!organizer.getRequiredSkills().isEmpty()) {
                    out.println("                    <br><strong>Required Skills:</strong>");
                    out.println("                    <div class='skills-list'>");
                    for (String skill : organizer.getRequiredSkills()) {
                        out.println("                        <span class='skill-tag'>" + skill + "</span>");
                    }
                    out.println("                    </div>");
                }
                
                out.println("                </div>");
                out.println("            </div>");
            }
        }
        out.println("        </div>");
        
        out.println("    </div>");
        
        // Footer
        out.println("    <footer>");
        out.println("        <p>&copy; 2024 SkillSync Admin Dashboard. All rights reserved.</p>");
        out.println("    </footer>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Sends an error response
     * 
     * @param out PrintWriter for response output
     * @param errorMessage The error message to display
     */
    private void sendErrorResponse(PrintWriter out, String errorMessage) {
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Admin Dashboard Error</title></head>");
        out.println("<body><h1>Error</h1><p>" + errorMessage + "</p>");
        out.println("<a href='admin'>Try Again</a></body></html>");
    }
}