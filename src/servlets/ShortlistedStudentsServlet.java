package servlets;

import models.Student;
import utils.DatabaseMock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * ShortlistedStudentsServlet displays all students that have been shortlisted by organizers
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public class ShortlistedStudentsServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Handles GET requests to display shortlisted students
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get any messages from URL parameters (for feedback after operations)
        String message = request.getParameter("message");
        String messageType = request.getParameter("type");
        
        generateShortlistedStudentsPage(response, message, messageType);
    }
    
    /**
     * Generates the shortlisted students page
     */
    private void generateShortlistedStudentsPage(HttpServletResponse response, String message, String messageType) 
            throws IOException {
        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Shortlisted Students - SkillSync</title>");
        out.println("    <style>");
        out.println("        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }");
        out.println("        .container { max-width: 1200px; margin: 0 auto; background: white; padding: 30px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); }");
        out.println("        h1 { color: #333; text-align: center; margin-bottom: 30px; font-size: 2.5em; }");
        out.println("        .message { padding: 15px; margin-bottom: 20px; border-radius: 5px; }");
        out.println("        .message.success { background: #d4edda; border-left: 4px solid #28a745; color: #155724; }");
        out.println("        .message.warning { background: #fff3cd; border-left: 4px solid #ffc107; color: #856404; }");
        out.println("        .message.error { background: #f8d7da; border-left: 4px solid #dc3545; color: #721c24; }");
        out.println("        .info-box { background: #e3f2fd; border-left: 4px solid #2196f3; padding: 15px; margin-bottom: 30px; border-radius: 5px; }");
        out.println("        .table-container { overflow-x: auto; }");
        out.println("        table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        out.println("        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
        out.println("        th { background-color: #667eea; color: white; position: sticky; top: 0; }");
        out.println("        tr:hover { background-color: #f5f5f5; }");
        out.println("        tr:nth-child(even) { background-color: #f9f9f9; }");
        out.println("        .skills-list { display: flex; flex-wrap: wrap; gap: 5px; }");
        out.println("        .skill-tag { background: #e3f2fd; color: #1976d2; padding: 3px 8px; border-radius: 12px; font-size: 0.85em; }");
        out.println("        .interest-tag { background: #f3e5f5; color: #7b1fa2; padding: 3px 8px; border-radius: 12px; font-size: 0.85em; }");
        out.println("        .cv-link { color: #667eea; text-decoration: none; }");
        out.println("        .cv-link:hover { text-decoration: underline; }");
        out.println("        .btn { padding: 8px 16px; margin: 2px; border: none; border-radius: 4px; cursor: pointer; text-decoration: none; display: inline-block; font-size: 0.9em; transition: background-color 0.3s; }");
        out.println("        .btn-primary { background: #667eea; color: white; }");
        out.println("        .btn-primary:hover { background: #5a6fd8; }");
        out.println("        .btn-danger { background: #dc3545; color: white; }");
        out.println("        .btn-danger:hover { background: #c82333; }");
        out.println("        .btn-secondary { background: #6c757d; color: white; }");
        out.println("        .btn-secondary:hover { background: #5a6268; }");
        out.println("        .btn-success { background: #28a745; color: white; }");
        out.println("        .btn-success:hover { background: #218838; }");
        out.println("        .no-students { text-align: center; padding: 50px; color: #666; }");
        out.println("        .actions-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }");
        out.println("        .bulk-actions { display: flex; gap: 10px; align-items: center; }");
        out.println("        .student-count { font-size: 1.2em; color: #666; }");
        out.println("        .navigation { margin-top: 30px; text-align: center; padding-top: 20px; border-top: 1px solid #ddd; }");
        out.println("        .navigation a { margin: 0 10px; }");
        out.println("        .shortlist-date { font-size: 0.85em; color: #666; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <h1>👥 Shortlisted Students</h1>");
        
        // Display message if present
        if (message != null && !message.isEmpty()) {
            String messageClass = messageType != null ? messageType : "info";
            out.println("        <div class='message " + messageClass + "'>");
            out.println("            " + escapeHtml(message));
            out.println("        </div>");
        }
        
        // Get shortlisted students
        List<Student> shortlistedStudents = DatabaseMock.getShortlistedStudents();
        int shortlistCount = shortlistedStudents.size();
        
        // Display info box
        out.println("        <div class='info-box'>");
        if (shortlistCount > 0) {
            out.println("            <strong>📊 Shortlist Summary:</strong> " + shortlistCount + " students are ready for invitations. You can review their profiles and send invitations or remove them from the shortlist.");
        } else {
            out.println("            <strong>📝 Getting Started:</strong> No students shortlisted yet. Use the student filter to find candidates and add them to your shortlist.");
        }
        out.println("        </div>");
        
        if (shortlistCount == 0) {
            // No shortlisted students
            out.println("        <div class='no-students'>");
            out.println("            <h3>🔍 No Students Shortlisted Yet</h3>");
            out.println("            <p>Start building your candidate list by searching for students and adding them to your shortlist.</p>");
            out.println("            <div style='margin-top: 30px;'>");
            out.println("                <a href='/skillsync/filter-students' class='btn btn-primary'>🔍 Find Students</a>");
            out.println("                <a href='/skillsync/admin-dashboard' class='btn btn-secondary'>📊 Dashboard</a>");
            out.println("            </div>");
            out.println("        </div>");
        } else {
            // Display shortlisted students table
            out.println("        <div class='actions-header'>");
            out.println("            <div class='student-count'>");
            out.println("                📊 <strong>" + shortlistCount + "</strong> student" + (shortlistCount != 1 ? "s" : "") + " shortlisted");
            out.println("            </div>");
            out.println("            <div class='bulk-actions'>");
            out.println("                <form method='post' action='/skillsync/invite-student' style='display: inline;'>");
            out.println("                    <input type='hidden' name='action' value='clear-all'>");
            out.println("                    <button type='submit' class='btn btn-danger' onclick='return confirm(\"Are you sure you want to clear all shortlisted students?\");'>🗑️ Clear All</button>");
            out.println("                </form>");
            out.println("            </div>");
            out.println("        </div>");
            
            out.println("        <div class='table-container'>");
            out.println("            <table>");
            out.println("                <thead>");
            out.println("                    <tr>");
            out.println("                        <th>👤 Student</th>");
            out.println("                        <th>📧 Contact</th>");
            out.println("                        <th>🏛️ Academic Info</th>");
            out.println("                        <th>🛠️ Skills</th>");
            out.println("                        <th>🎯 Interests</th>");
            out.println("                        <th>📄 CV</th>");
            out.println("                        <th>⚡ Actions</th>");
            out.println("                    </tr>");
            out.println("                </thead>");
            out.println("                <tbody>");
            
            for (Student student : shortlistedStudents) {
                out.println("                    <tr>");
                
                // Student name and basic info
                out.println("                        <td>");
                out.println("                            <strong>" + escapeHtml(student.getName()) + "</strong><br>");
                out.println("                            <span class='shortlist-date'>ID: " + student.getEmail().hashCode() + "</span>");
                out.println("                        </td>");
                
                // Contact information
                out.println("                        <td>");
                out.println("                            📧 " + escapeHtml(student.getEmail()) + "<br>");
                out.println("                            📞 <span style='color: #666;'>Contact via email</span>");
                out.println("                        </td>");
                
                // Academic information
                out.println("                        <td>");
                out.println("                            🏛️ " + escapeHtml(student.getDepartment()) + "<br>");
                out.println("                            📚 Semester " + student.getSemester());
                out.println("                        </td>");
                
                // Skills
                out.println("                        <td>");
                if (!student.getSkills().isEmpty()) {
                    out.println("                            <div class='skills-list'>");
                    for (String skill : student.getSkills()) {
                        out.println("                                <span class='skill-tag'>" + escapeHtml(skill) + "</span>");
                    }
                    out.println("                            </div>");
                } else {
                    out.println("                            <span style='color: #999;'>No skills listed</span>");
                }
                out.println("                        </td>");
                
                // Interest areas
                out.println("                        <td>");
                if (!student.getInterestAreas().isEmpty()) {
                    out.println("                            <div class='skills-list'>");
                    for (String interest : student.getInterestAreas()) {
                        out.println("                                <span class='interest-tag'>" + escapeHtml(interest) + "</span>");
                    }
                    out.println("                            </div>");
                } else {
                    out.println("                            <span style='color: #999;'>No interests listed</span>");
                }
                out.println("                        </td>");
                
                // CV link
                out.println("                        <td>");
                if (student.getCvLink() != null && !student.getCvLink().trim().isEmpty()) {
                    out.println("                            <a href='" + escapeHtml(student.getCvLink()) + "' target='_blank' class='cv-link'>📄 View CV</a>");
                } else {
                    out.println("                            <span style='color: #999;'>No CV</span>");
                }
                out.println("                        </td>");
                
                // Actions
                out.println("                        <td>");
                out.println("                            <form method='post' action='/skillsync/invite-student' style='display: inline;'>");
                out.println("                                <input type='hidden' name='studentEmail' value='" + escapeHtml(student.getEmail()) + "'>");
                out.println("                                <input type='hidden' name='action' value='invite'>");
                out.println("                                <input type='hidden' name='redirectUrl' value='/skillsync/shortlisted-students'>");
                out.println("                                <button type='submit' class='btn btn-success' title='Send invitation email'>📧 Invite</button>");
                out.println("                            </form>");
                out.println("                            <form method='post' action='/skillsync/invite-student' style='display: inline;'>");
                out.println("                                <input type='hidden' name='studentEmail' value='" + escapeHtml(student.getEmail()) + "'>");
                out.println("                                <input type='hidden' name='action' value='remove'>");
                out.println("                                <input type='hidden' name='redirectUrl' value='/skillsync/shortlisted-students'>");
                out.println("                                <button type='submit' class='btn btn-danger' title='Remove from shortlist' onclick='return confirm(\"Remove " + escapeHtml(student.getName()) + " from shortlist?\");'>🗑️ Remove</button>");
                out.println("                            </form>");
                out.println("                        </td>");
                
                out.println("                    </tr>");
            }
            
            out.println("                </tbody>");
            out.println("            </table>");
            out.println("        </div>");
        }
        
        // Navigation footer
        out.println("        <div class='navigation'>");
        out.println("            <a href='/skillsync/filter-students' class='btn btn-primary'>🔍 Find More Students</a>");
        out.println("            <a href='/skillsync/invite-student' class='btn btn-secondary'>📋 Shortlist Management</a>");
        out.println("            <a href='/skillsync/admin-dashboard' class='btn btn-secondary'>📊 Admin Dashboard</a>");
        out.println("        </div>");
        
        out.println("    </div>");
        
        // Add JavaScript for enhanced functionality
        out.println("    <script>");
        out.println("        // Auto-hide success messages after 5 seconds");
        out.println("        setTimeout(function() {");
        out.println("            const messages = document.querySelectorAll('.message.success');");
        out.println("            messages.forEach(function(msg) {");
        out.println("                msg.style.opacity = '0';");
        out.println("                msg.style.transition = 'opacity 1s';");
        out.println("                setTimeout(function() { msg.style.display = 'none'; }, 1000);");
        out.println("            });");
        out.println("        }, 5000);");
        out.println("        ");
        out.println("        // Add keyboard shortcuts");
        out.println("        document.addEventListener('keydown', function(e) {");
        out.println("            if (e.ctrlKey && e.key === 'f') {");
        out.println("                e.preventDefault();");
        out.println("                window.location.href = '/skillsync/filter-students';");
        out.println("            }");
        out.println("        });");
        out.println("    </script>");
        
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Escapes HTML special characters to prevent XSS
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#x27;");
    }
}