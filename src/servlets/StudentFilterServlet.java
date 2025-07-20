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
import java.util.stream.Collectors;

/**
 * StudentFilterServlet handles filtering of students based on various criteria
 * Supports filtering by skill, department, and semester
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public class StudentFilterServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Handles GET requests - displays the filter form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if this is a filter request with parameters
        String skill = request.getParameter("skill");
        String department = request.getParameter("department");
        String semester = request.getParameter("semester");
        
        if (skill != null || department != null || semester != null) {
            // Process filter request
            processFilterRequest(request, response);
        } else {
            // Display filter form
            displayFilterForm(response);
        }
    }
    
    /**
     * Handles POST requests - processes filter criteria
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processFilterRequest(request, response);
    }
    
    /**
     * Processes the filter request and displays results
     */
    private void processFilterRequest(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        // Extract filter parameters
        String skillFilter = request.getParameter("skill");
        String departmentFilter = request.getParameter("department");
        String semesterFilter = request.getParameter("semester");
        
        // Clean and normalize filters
        skillFilter = cleanFilter(skillFilter);
        departmentFilter = cleanFilter(departmentFilter);
        semesterFilter = cleanFilter(semesterFilter);
        
        try {
            // Get all students from mock database
            List<Student> allStudents = DatabaseMock.getAllStudents();
            
            // Apply filters
            List<Student> filteredStudents = filterStudents(allStudents, skillFilter, departmentFilter, semesterFilter);
            
            // Generate and send HTML response
            generateFilteredResultsHTML(response, filteredStudents, skillFilter, departmentFilter, semesterFilter);
            
        } catch (Exception e) {
            sendErrorResponse(response, "Error filtering students: " + e.getMessage());
        }
    }
    
    /**
     * Filters students based on provided criteria
     */
    private List<Student> filterStudents(List<Student> students, String skill, String department, String semester) {
        return students.stream()
                .filter(student -> matchesSkillFilter(student, skill))
                .filter(student -> matchesDepartmentFilter(student, department))
                .filter(student -> matchesSemesterFilter(student, semester))
                .collect(Collectors.toList());
    }
    
    /**
     * Checks if student matches skill filter
     */
    private boolean matchesSkillFilter(Student student, String skillFilter) {
        if (skillFilter == null || skillFilter.isEmpty()) {
            return true; // No filter applied
        }
        
        // Check if any of the student's skills contains the filter text (case-insensitive)
        return student.getSkills().stream()
                .anyMatch(skill -> skill.toLowerCase().contains(skillFilter.toLowerCase()));
    }
    
    /**
     * Checks if student matches department filter
     */
    private boolean matchesDepartmentFilter(Student student, String departmentFilter) {
        if (departmentFilter == null || departmentFilter.isEmpty()) {
            return true; // No filter applied
        }
        
        return student.getDepartment().toLowerCase().contains(departmentFilter.toLowerCase());
    }
    
    /**
     * Checks if student matches semester filter
     */
    private boolean matchesSemesterFilter(Student student, String semesterFilter) {
        if (semesterFilter == null || semesterFilter.isEmpty()) {
            return true; // No filter applied
        }
        
        try {
            int filterSemester = Integer.parseInt(semesterFilter);
            return student.getSemester() == filterSemester;
        } catch (NumberFormatException e) {
            // If semester filter is not a valid number, ignore this filter
            return true;
        }
    }
    
    /**
     * Cleans and normalizes filter input
     */
    private String cleanFilter(String filter) {
        if (filter == null) {
            return null;
        }
        filter = filter.trim();
        return filter.isEmpty() ? null : filter;
    }
    
    /**
     * Displays the filter form
     */
    private void displayFilterForm(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Filter Students - SkillSync</title>");
        out.println("    <style>");
        out.println("        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }");
        out.println("        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); }");
        out.println("        h1 { color: #333; text-align: center; margin-bottom: 30px; font-size: 2.5em; }");
        out.println("        .filter-form { display: grid; gap: 20px; }");
        out.println("        .form-group { display: flex; flex-direction: column; }");
        out.println("        label { font-weight: bold; margin-bottom: 8px; color: #555; }");
        out.println("        input, select { padding: 12px; border: 2px solid #ddd; border-radius: 8px; font-size: 16px; transition: border-color 0.3s; }");
        out.println("        input:focus, select:focus { outline: none; border-color: #667eea; }");
        out.println("        .button-group { display: flex; gap: 15px; margin-top: 20px; }");
        out.println("        button { padding: 12px 30px; border: none; border-radius: 8px; font-size: 16px; cursor: pointer; transition: background-color 0.3s; }");
        out.println("        .btn-primary { background: #667eea; color: white; }");
        out.println("        .btn-primary:hover { background: #5a6fd8; }");
        out.println("        .btn-secondary { background: #6c757d; color: white; }");
        out.println("        .btn-secondary:hover { background: #5a6268; }");
        out.println("        .info-box { background: #e3f2fd; border-left: 4px solid #2196f3; padding: 15px; margin-bottom: 20px; border-radius: 5px; }");
        out.println("        .stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; margin-top: 30px; }");
        out.println("        .stat-card { background: #f8f9fa; padding: 20px; border-radius: 10px; text-align: center; border: 1px solid #dee2e6; }");
        out.println("        .stat-number { font-size: 2em; font-weight: bold; color: #667eea; }");
        out.println("        .stat-label { color: #6c757d; margin-top: 5px; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <h1>🔍 Filter Students</h1>");
        
        // Display current database stats
        int totalStudents = DatabaseMock.getStudentCount();
        out.println("        <div class='info-box'>");
        out.println("            <strong>📊 Database Status:</strong> " + totalStudents + " students available for filtering");
        out.println("        </div>");
        
        out.println("        <form class='filter-form' method='GET' action='/skillsync/filter-students'>");
        out.println("            <div class='form-group'>");
        out.println("                <label for='skill'>🛠️ Skill (contains):</label>");
        out.println("                <input type='text' id='skill' name='skill' placeholder='e.g., Java, Python, JavaScript' title='Filter by skill name (partial match)'>");
        out.println("            </div>");
        
        out.println("            <div class='form-group'>");
        out.println("                <label for='department'>🏛️ Department:</label>");
        out.println("                <select id='department' name='department'>");
        out.println("                    <option value=''>-- All Departments --</option>");
        out.println("                    <option value='Computer Science'>Computer Science</option>");
        out.println("                    <option value='Engineering'>Engineering</option>");
        out.println("                    <option value='Business'>Business</option>");
        out.println("                    <option value='Mathematics'>Mathematics</option>");
        out.println("                    <option value='Physics'>Physics</option>");
        out.println("                    <option value='Chemistry'>Chemistry</option>");
        out.println("                    <option value='Biology'>Biology</option>");
        out.println("                    <option value='Psychology'>Psychology</option>");
        out.println("                </select>");
        out.println("            </div>");
        
        out.println("            <div class='form-group'>");
        out.println("                <label for='semester'>📚 Semester:</label>");
        out.println("                <select id='semester' name='semester'>");
        out.println("                    <option value=''>-- All Semesters --</option>");
        out.println("                    <option value='1'>1st Semester</option>");
        out.println("                    <option value='2'>2nd Semester</option>");
        out.println("                    <option value='3'>3rd Semester</option>");
        out.println("                    <option value='4'>4th Semester</option>");
        out.println("                    <option value='5'>5th Semester</option>");
        out.println("                    <option value='6'>6th Semester</option>");
        out.println("                    <option value='7'>7th Semester</option>");
        out.println("                    <option value='8'>8th Semester</option>");
        out.println("                </select>");
        out.println("            </div>");
        
        out.println("            <div class='button-group'>");
        out.println("                <button type='submit' class='btn-primary'>🔍 Filter Students</button>");
        out.println("                <button type='reset' class='btn-secondary'>🔄 Clear Filters</button>");
        out.println("            </div>");
        out.println("        </form>");
        
        // Show current statistics
        if (totalStudents > 0) {
            out.println("        <div class='stats'>");
            out.println("            <div class='stat-card'>");
            out.println("                <div class='stat-number'>" + totalStudents + "</div>");
            out.println("                <div class='stat-label'>Total Students</div>");
            out.println("            </div>");
            out.println("            <div class='stat-card'>");
            out.println("                <div class='stat-number'>" + DatabaseMock.getOrganizerCount() + "</div>");
            out.println("                <div class='stat-label'>Total Organizers</div>");
            out.println("            </div>");
            out.println("        </div>");
        }
        
        out.println("        <div style='margin-top: 30px; text-align: center;'>");
        out.println("            <a href='/skillsync/student_registration.html' style='margin-right: 15px; text-decoration: none; color: #667eea;'>📝 Register Student</a>");
        out.println("            <a href='/skillsync/admin-dashboard' style='text-decoration: none; color: #667eea;'>📊 Admin Dashboard</a>");
        out.println("        </div>");
        
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }
    
    /**
     * Generates HTML response with filtered results
     */
    private void generateFilteredResultsHTML(HttpServletResponse response, List<Student> filteredStudents, 
                                           String skillFilter, String departmentFilter, String semesterFilter) 
            throws IOException {
        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Filtered Results - SkillSync</title>");
        out.println("    <style>");
        out.println("        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 20px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); min-height: 100vh; }");
        out.println("        .container { max-width: 1200px; margin: 0 auto; background: white; padding: 30px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.2); }");
        out.println("        h1 { color: #333; text-align: center; margin-bottom: 30px; font-size: 2.5em; }");
        out.println("        .filter-summary { background: #e3f2fd; border-left: 4px solid #2196f3; padding: 15px; margin-bottom: 30px; border-radius: 5px; }");
        out.println("        .filter-summary h3 { margin-top: 0; color: #1976d2; }");
        out.println("        .results-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }");
        out.println("        .results-count { font-size: 1.2em; color: #666; }");
        out.println("        .btn { padding: 10px 20px; background: #667eea; color: white; text-decoration: none; border-radius: 5px; transition: background-color 0.3s; }");
        out.println("        .btn:hover { background: #5a6fd8; }");
        out.println("        .table-container { overflow-x: auto; }");
        out.println("        table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        out.println("        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
        out.println("        th { background-color: #667eea; color: white; position: sticky; top: 0; }");
        out.println("        tr:hover { background-color: #f5f5f5; }");
        out.println("        .skills-list { display: flex; flex-wrap: wrap; gap: 5px; }");
        out.println("        .skill-tag { background: #e3f2fd; color: #1976d2; padding: 3px 8px; border-radius: 12px; font-size: 0.85em; }");
        out.println("        .cv-link { color: #667eea; text-decoration: none; }");
        out.println("        .cv-link:hover { text-decoration: underline; }");
        out.println("        .no-results { text-align: center; padding: 50px; color: #666; }");
        out.println("        .no-results h3 { color: #999; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='container'>");
        out.println("        <h1>📋 Filtered Student Results</h1>");
        
        // Display filter summary
        out.println("        <div class='filter-summary'>");
        out.println("            <h3>🔍 Applied Filters</h3>");
        out.println("            <p>");
        if (skillFilter != null) {
            out.println("                <strong>Skill:</strong> " + escapeHtml(skillFilter) + "<br>");
        }
        if (departmentFilter != null) {
            out.println("                <strong>Department:</strong> " + escapeHtml(departmentFilter) + "<br>");
        }
        if (semesterFilter != null) {
            out.println("                <strong>Semester:</strong> " + escapeHtml(semesterFilter) + "<br>");
        }
        if (skillFilter == null && departmentFilter == null && semesterFilter == null) {
            out.println("                <em>No filters applied - showing all students</em>");
        }
        out.println("            </p>");
        out.println("        </div>");
        
        // Results header
        out.println("        <div class='results-header'>");
        out.println("            <div class='results-count'>");
        out.println("                📊 Found <strong>" + filteredStudents.size() + "</strong> student(s) matching your criteria");
        out.println("            </div>");
        out.println("            <a href='/skillsync/filter-students' class='btn'>🔄 New Search</a>");
        out.println("        </div>");
        
        if (filteredStudents.isEmpty()) {
            // No results found
            out.println("        <div class='no-results'>");
            out.println("            <h3>😔 No Students Found</h3>");
            out.println("            <p>No students match your filter criteria. Try:</p>");
            out.println("            <ul style='text-align: left; display: inline-block;'>");
            out.println("                <li>Removing some filters</li>");
            out.println("                <li>Using more general search terms</li>");
            out.println("                <li>Checking if there are students in the database</li>");
            out.println("            </ul>");
            out.println("            <p><a href='/skillsync/filter-students' class='btn'>🔄 Try Again</a></p>");
            out.println("        </div>");
        } else {
            // Display results table
            out.println("        <div class='table-container'>");
            out.println("            <table>");
            out.println("                <thead>");
            out.println("                    <tr>");
            out.println("                        <th>👤 Name</th>");
            out.println("                        <th>📧 Email</th>");
            out.println("                        <th>🏛️ Department</th>");
            out.println("                        <th>📚 Semester</th>");
            out.println("                        <th>🛠️ Skills</th>");
            out.println("                        <th>📄 CV</th>");
            out.println("                        <th>🎯 Interests</th>");
            out.println("                    </tr>");
            out.println("                </thead>");
            out.println("                <tbody>");
            
            for (Student student : filteredStudents) {
                out.println("                    <tr>");
                out.println("                        <td><strong>" + escapeHtml(student.getName()) + "</strong></td>");
                out.println("                        <td>" + escapeHtml(student.getEmail()) + "</td>");
                out.println("                        <td>" + escapeHtml(student.getDepartment()) + "</td>");
                out.println("                        <td>" + student.getSemester() + "</td>");
                
                // Skills column
                out.println("                        <td>");
                out.println("                            <div class='skills-list'>");
                for (String skill : student.getSkills()) {
                    out.println("                                <span class='skill-tag'>" + escapeHtml(skill) + "</span>");
                }
                out.println("                            </div>");
                out.println("                        </td>");
                
                // CV column
                out.println("                        <td>");
                if (student.getCvLink() != null && !student.getCvLink().trim().isEmpty()) {
                    out.println("                            <a href='" + escapeHtml(student.getCvLink()) + "' target='_blank' class='cv-link'>📄 View CV</a>");
                } else {
                    out.println("                            <span style='color: #999;'>No CV</span>");
                }
                out.println("                        </td>");
                
                // Interest areas column
                out.println("                        <td>");
                if (!student.getInterestAreas().isEmpty()) {
                    out.println("                            <div class='skills-list'>");
                    for (String interest : student.getInterestAreas()) {
                        out.println("                                <span class='skill-tag' style='background: #f3e5f5; color: #7b1fa2;'>" + escapeHtml(interest) + "</span>");
                    }
                    out.println("                            </div>");
                } else {
                    out.println("                            <span style='color: #999;'>No interests listed</span>");
                }
                out.println("                        </td>");
                
                out.println("                    </tr>");
            }
            
            out.println("                </tbody>");
            out.println("            </table>");
            out.println("        </div>");
        }
        
        // Footer navigation
        out.println("        <div style='margin-top: 30px; text-align: center; padding-top: 20px; border-top: 1px solid #ddd;'>");
        out.println("            <a href='/skillsync/filter-students' style='margin-right: 15px; text-decoration: none; color: #667eea;'>🔄 New Search</a>");
        out.println("            <a href='/skillsync/admin-dashboard' style='margin-right: 15px; text-decoration: none; color: #667eea;'>📊 Admin Dashboard</a>");
        out.println("            <a href='/skillsync/student_registration.html' style='text-decoration: none; color: #667eea;'>📝 Register Student</a>");
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
        out.println("<html>");
        out.println("<head><title>Error - SkillSync</title></head>");
        out.println("<body>");
        out.println("<h1>Error</h1>");
        out.println("<p>" + escapeHtml(errorMessage) + "</p>");
        out.println("<a href='/skillsync/filter-students'>Back to Filter</a>");
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