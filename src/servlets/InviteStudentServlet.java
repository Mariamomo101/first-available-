package servlets;

import models.Student;
import utils.DatabaseMock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

/**
 * InviteStudentServlet handles shortlisting students for organizer invitations
 * Accepts POST requests with student email/ID and adds them to shortlist
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public class InviteStudentServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Handles POST requests for shortlisting students
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Extract student information from request
        String studentEmail = request.getParameter("studentEmail");
        String studentId = request.getParameter("studentId");
        String action = request.getParameter("action");
        String redirectUrl = request.getParameter("redirectUrl");
        
        // Clean inputs
        studentEmail = cleanInput(studentEmail);
        studentId = cleanInput(studentId);
        action = cleanInput(action);
        
        try {
            if ("shortlist".equalsIgnoreCase(action)) {
                handleShortlisting(request, response, studentEmail, studentId, redirectUrl);
            } else if ("remove".equalsIgnoreCase(action)) {
                handleRemoveFromShortlist(request, response, studentEmail, redirectUrl);
            } else {
                sendErrorResponse(response, "Invalid action. Please use 'shortlist' or 'remove'.");
            }
            
        } catch (Exception e) {
            sendErrorResponse(response, "Error processing request: " + e.getMessage());
        }
    }
    
    /**
     * Handles adding students to shortlist
     */
    private void handleShortlisting(HttpServletRequest request, HttpServletResponse response, 
                                  String studentEmail, String studentId, String redirectUrl) 
            throws IOException {
        
        // Determine which identifier to use
        String identifier = studentEmail != null ? studentEmail : studentId;
        
        if (identifier == null || identifier.isEmpty()) {
            sendErrorResponse(response, "Student email or ID is required for shortlisting.");
            return;
        }
        
        // Try to shortlist the student
        boolean success;
        Student student = null;
        
        if (studentEmail != null) {
            // Shortlist by email
            success = DatabaseMock.addToShortlistByEmail(studentEmail);
            student = DatabaseMock.findStudentByEmail(studentEmail);
        } else {
            // If using ID, we need to find by ID first (assuming ID is the email for now)
            success = DatabaseMock.addToShortlistByEmail(studentId);
            student = DatabaseMock.findStudentByEmail(studentId);
        }
        
        if (success && student != null) {
            // Success - student shortlisted
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                // Redirect back to the original page with success message
                String encodedMessage = URLEncoder.encode("Student " + student.getName() + " has been shortlisted successfully!", "UTF-8");
                response.sendRedirect(redirectUrl + "?message=" + encodedMessage + "&type=success");
            } else {
                // Display success page
                sendSuccessResponse(response, student, "shortlisted");
            }
        } else if (student != null && DatabaseMock.isStudentShortlisted(student.getEmail())) {
            // Student already shortlisted
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                String encodedMessage = URLEncoder.encode("Student " + student.getName() + " is already shortlisted.", "UTF-8");
                response.sendRedirect(redirectUrl + "?message=" + encodedMessage + "&type=warning");
            } else {
                sendWarningResponse(response, student, "already shortlisted");
            }
        } else {
            // Student not found
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                String encodedMessage = URLEncoder.encode("Student not found with the provided information.", "UTF-8");
                response.sendRedirect(redirectUrl + "?message=" + encodedMessage + "&type=error");
            } else {
                sendErrorResponse(response, "Student not found with email/ID: " + identifier);
            }
        }
    }
    
    /**
     * Handles removing students from shortlist
     */
    private void handleRemoveFromShortlist(HttpServletRequest request, HttpServletResponse response, 
                                         String studentEmail, String redirectUrl) 
            throws IOException {
        
        if (studentEmail == null || studentEmail.isEmpty()) {
            sendErrorResponse(response, "Student email is required for removing from shortlist.");
            return;
        }
        
        // Find student before removing
        Student student = DatabaseMock.findStudentByEmail(studentEmail);
        boolean removed = DatabaseMock.removeFromShortlist(studentEmail);
        
        if (removed && student != null) {
            // Success - student removed from shortlist
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                String encodedMessage = URLEncoder.encode("Student " + student.getName() + " has been removed from shortlist.", "UTF-8");
                response.sendRedirect(redirectUrl + "?message=" + encodedMessage + "&type=success");
            } else {
                sendSuccessResponse(response, student, "removed from shortlist");
            }
        } else {
            // Student not found in shortlist
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                String encodedMessage = URLEncoder.encode("Student not found in shortlist.", "UTF-8");
                response.sendRedirect(redirectUrl + "?message=" + encodedMessage + "&type=error");
            } else {
                sendErrorResponse(response, "Student not found in shortlist: " + studentEmail);
            }
        }
    }
    
    /**
     * Handles GET requests - displays shortlist management page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Display shortlist management page
        displayShortlistPage(response);
    }
    
    /**
     * Displays the shortlist management page
     */
    private void displayShortlistPage(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Shortlist Management - SkillSync</title>");
        out.println("    <style>");
        out.println("        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }");
        out.println("        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); }");
        out.println("        h1 { color: #333; text-align: center; margin-bottom: 30px; font-size: 2.5em; }");
        out.println("        .info-box { background: #e3f2fd; border-left: 4px solid #2196f3; padding: 15px; margin-bottom: 20px; border-radius: 5px; }");
        out.println("        .btn { padding: 10px 20px; margin: 5px; background: #667eea; color: white; text-decoration: none; border-radius: 5px; display: inline-block; transition: background-color 0.3s; }");
        out.println("        .btn:hover { background: #5a6fd8; }");
        out.println("        .btn-secondary { background: #6c757d; }");
        out.println("        .btn-secondary:hover { background: #5a6268; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <h1>📋 Shortlist Management</h1>");
        
        int shortlistCount = DatabaseMock.getShortlistedStudentCount();
        out.println("        <div class='info-box'>");
        out.println("            <strong>📊 Current Status:</strong> " + shortlistCount + " students are currently shortlisted for invitations.");
        out.println("        </div>");
        
        out.println("        <div style='text-align: center; margin-top: 30px;'>");
        out.println("            <a href='/skillsync/shortlisted-students' class='btn'>👥 View Shortlisted Students</a>");
        out.println("            <a href='/skillsync/filter-students' class='btn btn-secondary'>🔍 Find More Students</a>");
        out.println("            <a href='/skillsync/admin-dashboard' class='btn btn-secondary'>📊 Admin Dashboard</a>");
        out.println("        </div>");
        
        out.println("        <div style='margin-top: 40px; padding: 20px; background: #f8f9fa; border-radius: 8px;'>");
        out.println("            <h3>💡 How to Use Shortlisting:</h3>");
        out.println("            <ol>");
        out.println("                <li>Use the <strong>Filter Students</strong> page to find candidates</li>");
        out.println("                <li>Click the <strong>Shortlist</strong> button next to any student</li>");
        out.println("                <li>View all shortlisted students on the dedicated page</li>");
        out.println("                <li>Remove students from shortlist if needed</li>");
        out.println("            </ol>");
        out.println("        </div>");
        
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Sends success response
     */
    private void sendSuccessResponse(HttpServletResponse response, Student student, String action) 
            throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Success - SkillSync</title>");
        out.println("    <style>");
        out.println("        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }");
        out.println("        .container { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); text-align: center; }");
        out.println("        .success-icon { font-size: 4em; color: #28a745; margin-bottom: 20px; }");
        out.println("        h1 { color: #333; margin-bottom: 20px; }");
        out.println("        .student-info { background: #f8f9fa; padding: 20px; border-radius: 8px; margin: 20px 0; text-align: left; }");
        out.println("        .btn { padding: 12px 24px; margin: 10px; background: #667eea; color: white; text-decoration: none; border-radius: 5px; display: inline-block; transition: background-color 0.3s; }");
        out.println("        .btn:hover { background: #5a6fd8; }");
        out.println("        .btn-secondary { background: #6c757d; }");
        out.println("        .btn-secondary:hover { background: #5a6268; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <div class='success-icon'>✅</div>");
        out.println("        <h1>Student " + action + " successfully!</h1>");
        
        out.println("        <div class='student-info'>");
        out.println("            <h3>📋 Student Details:</h3>");
        out.println("            <p><strong>Name:</strong> " + escapeHtml(student.getName()) + "</p>");
        out.println("            <p><strong>Email:</strong> " + escapeHtml(student.getEmail()) + "</p>");
        out.println("            <p><strong>Department:</strong> " + escapeHtml(student.getDepartment()) + "</p>");
        out.println("            <p><strong>Semester:</strong> " + student.getSemester() + "</p>");
        out.println("            <p><strong>Skills:</strong> " + String.join(", ", student.getSkills()) + "</p>");
        out.println("        </div>");
        
        int totalShortlisted = DatabaseMock.getShortlistedStudentCount();
        out.println("        <p><strong>📊 Total Shortlisted Students:</strong> " + totalShortlisted + "</p>");
        
        out.println("        <div style='margin-top: 30px;'>");
        out.println("            <a href='/skillsync/shortlisted-students' class='btn'>👥 View All Shortlisted</a>");
        out.println("            <a href='/skillsync/filter-students' class='btn btn-secondary'>🔍 Find More Students</a>");
        out.println("        </div>");
        
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Sends warning response
     */
    private void sendWarningResponse(HttpServletResponse response, Student student, String message) 
            throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Already Shortlisted - SkillSync</title>");
        out.println("    <style>");
        out.println("        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }");
        out.println("        .container { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); text-align: center; }");
        out.println("        .warning-icon { font-size: 4em; color: #ffc107; margin-bottom: 20px; }");
        out.println("        h1 { color: #333; margin-bottom: 20px; }");
        out.println("        .student-info { background: #fff3cd; padding: 20px; border-radius: 8px; margin: 20px 0; text-align: left; border-left: 4px solid #ffc107; }");
        out.println("        .btn { padding: 12px 24px; margin: 10px; background: #667eea; color: white; text-decoration: none; border-radius: 5px; display: inline-block; transition: background-color 0.3s; }");
        out.println("        .btn:hover { background: #5a6fd8; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <div class='warning-icon'>⚠️</div>");
        out.println("        <h1>Student " + message + "</h1>");
        
        out.println("        <div class='student-info'>");
        out.println("            <h3>📋 Student Details:</h3>");
        out.println("            <p><strong>Name:</strong> " + escapeHtml(student.getName()) + "</p>");
        out.println("            <p><strong>Email:</strong> " + escapeHtml(student.getEmail()) + "</p>");
        out.println("            <p>This student is already in your shortlist.</p>");
        out.println("        </div>");
        
        out.println("        <div style='margin-top: 30px;'>");
        out.println("            <a href='/skillsync/shortlisted-students' class='btn'>👥 View Shortlisted Students</a>");
        out.println("            <a href='/skillsync/filter-students' class='btn'>🔍 Find More Students</a>");
        out.println("        </div>");
        
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Sends error response
     */
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Error - SkillSync</title>");
        out.println("    <style>");
        out.println("        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }");
        out.println("        .container { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); text-align: center; }");
        out.println("        .error-icon { font-size: 4em; color: #dc3545; margin-bottom: 20px; }");
        out.println("        h1 { color: #333; margin-bottom: 20px; }");
        out.println("        .error-message { background: #f8d7da; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #dc3545; }");
        out.println("        .btn { padding: 12px 24px; margin: 10px; background: #667eea; color: white; text-decoration: none; border-radius: 5px; display: inline-block; transition: background-color 0.3s; }");
        out.println("        .btn:hover { background: #5a6fd8; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <div class='error-icon'>❌</div>");
        out.println("        <h1>Error</h1>");
        
        out.println("        <div class='error-message'>");
        out.println("            <p>" + escapeHtml(errorMessage) + "</p>");
        out.println("        </div>");
        
        out.println("        <div style='margin-top: 30px;'>");
        out.println("            <a href='/skillsync/filter-students' class='btn'>🔍 Back to Student Search</a>");
        out.println("            <a href='/skillsync/admin-dashboard' class='btn'>📊 Admin Dashboard</a>");
        out.println("        </div>");
        
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Cleans input parameters
     */
    private String cleanInput(String input) {
        if (input == null) return null;
        return input.trim().isEmpty() ? null : input.trim();
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