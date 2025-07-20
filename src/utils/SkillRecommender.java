package utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * AI-powered Skill Recommendation Engine for SkillSync
 * Uses rule-based logic to suggest relevant skills based on student interests
 * 
 * This class simulates an AI recommendation system that can later be enhanced
 * with machine learning algorithms, user behavior analysis, and collaborative filtering.
 * 
 * @author SkillSync Development Team
 * @version 1.0
 */
public class SkillRecommender {
    
    // Predefined skill mappings based on interest areas
    private static final Map<String, List<String>> INTEREST_SKILL_MAPPINGS = new HashMap<>();
    
    // Advanced skill mappings for cross-domain recommendations
    private static final Map<String, List<String>> ADVANCED_SKILL_MAPPINGS = new HashMap<>();
    
    // Skill importance weights for prioritization
    private static final Map<String, Integer> SKILL_WEIGHTS = new HashMap<>();
    
    // Static initializer to populate the recommendation database
    static {
        initializeBasicMappings();
        initializeAdvancedMappings();
        initializeSkillWeights();
    }
    
    /**
     * Main recommendation method that suggests skills based on interest areas
     * 
     * @param interestAreas List of student's interest areas
     * @param existingSkills List of skills the student already has
     * @return List of recommended skills, excluding existing ones
     */
    public static List<String> recommendSkills(List<String> interestAreas, List<String> existingSkills) {
        if (interestAreas == null || interestAreas.isEmpty()) {
            return new ArrayList<>();
        }
        
        Set<String> recommendations = new LinkedHashSet<>();
        Set<String> existingSkillsSet = existingSkills != null ? 
            existingSkills.stream().map(String::toLowerCase).collect(Collectors.toSet()) : 
            new HashSet<>();
        
        // Process each interest area
        for (String interest : interestAreas) {
            List<String> suggestedSkills = getSkillsForInterest(interest.toLowerCase().trim());
            
            // Add skills that the student doesn't already have
            for (String skill : suggestedSkills) {
                if (!existingSkillsSet.contains(skill.toLowerCase())) {
                    recommendations.add(skill);
                }
            }
        }
        
        // Sort recommendations by importance/weight
        return prioritizeRecommendations(new ArrayList<>(recommendations));
    }
    
    /**
     * Enhanced recommendation method with skill level consideration
     * 
     * @param interestAreas List of student's interest areas
     * @param existingSkills List of skills the student already has
     * @param semester Student's current semester (for skill level appropriateness)
     * @return List of recommended skills appropriate for the student's level
     */
    public static List<String> recommendSkillsAdvanced(List<String> interestAreas, 
                                                      List<String> existingSkills, 
                                                      int semester) {
        List<String> basicRecommendations = recommendSkills(interestAreas, existingSkills);
        
        // Filter recommendations based on semester (skill level)
        if (semester <= 2) {
            // Beginner level - focus on foundational skills
            return basicRecommendations.stream()
                    .filter(skill -> isBeginnerSkill(skill))
                    .limit(5)
                    .collect(Collectors.toList());
        } else if (semester <= 6) {
            // Intermediate level - mix of foundational and intermediate skills
            return basicRecommendations.stream()
                    .limit(8)
                    .collect(Collectors.toList());
        } else {
            // Advanced level - include specialized and leadership skills
            List<String> advanced = new ArrayList<>(basicRecommendations);
            advanced.addAll(getAdvancedSkillRecommendations(interestAreas, existingSkills));
            return advanced.stream()
                    .distinct()
                    .limit(10)
                    .collect(Collectors.toList());
        }
    }
    
    /**
     * Get complementary skills that work well with existing skills
     * 
     * @param existingSkills List of skills the student already has
     * @return List of complementary skills
     */
    public static List<String> recommendComplementarySkills(List<String> existingSkills) {
        if (existingSkills == null || existingSkills.isEmpty()) {
            return new ArrayList<>();
        }
        
        Set<String> complementarySkills = new LinkedHashSet<>();
        Set<String> existingSkillsSet = existingSkills.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        
        // Java ecosystem skills
        if (hasSkill(existingSkills, "java")) {
            complementarySkills.addAll(Arrays.asList("Spring Framework", "Maven", "JUnit", "Hibernate"));
        }
        
        // Web development ecosystem
        if (hasSkill(existingSkills, "html") || hasSkill(existingSkills, "css") || hasSkill(existingSkills, "javascript")) {
            complementarySkills.addAll(Arrays.asList("React", "Node.js", "Bootstrap", "Responsive Design"));
        }
        
        // Data science ecosystem
        if (hasSkill(existingSkills, "python")) {
            complementarySkills.addAll(Arrays.asList("Pandas", "NumPy", "Data Visualization", "Machine Learning"));
        }
        
        // Design ecosystem
        if (hasSkill(existingSkills, "photoshop") || hasSkill(existingSkills, "canva")) {
            complementarySkills.addAll(Arrays.asList("UI/UX Design", "Color Theory", "Typography", "Adobe Illustrator"));
        }
        
        // Filter out existing skills
        return complementarySkills.stream()
                .filter(skill -> !existingSkillsSet.contains(skill.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    /**
     * Get trending skills in the industry
     * 
     * @return List of currently trending skills
     */
    public static List<String> getTrendingSkills() {
        return Arrays.asList(
            "Artificial Intelligence",
            "Machine Learning",
            "Cloud Computing",
            "Cybersecurity",
            "Data Science",
            "DevOps",
            "Blockchain",
            "Mobile Development",
            "Digital Marketing",
            "Agile Methodology"
        );
    }
    
    /**
     * Initialize basic interest-to-skill mappings
     */
    private static void initializeBasicMappings() {
        // Core mappings as specified
        INTEREST_SKILL_MAPPINGS.put("public speaking", Arrays.asList("Communication", "Presentation", "Confidence", "Storytelling"));
        INTEREST_SKILL_MAPPINGS.put("programming", Arrays.asList("Git", "Problem Solving", "OOP", "Debugging", "Code Review"));
        INTEREST_SKILL_MAPPINGS.put("design", Arrays.asList("Canva", "Color Theory", "Typography", "Adobe Photoshop"));
        INTEREST_SKILL_MAPPINGS.put("hosting", Arrays.asList("Confidence", "Time Management", "Event Planning", "Leadership"));
        
        // Additional comprehensive mappings
        INTEREST_SKILL_MAPPINGS.put("web development", Arrays.asList("HTML", "CSS", "JavaScript", "React", "Node.js"));
        INTEREST_SKILL_MAPPINGS.put("data science", Arrays.asList("Python", "Statistics", "Data Analysis", "Machine Learning", "SQL"));
        INTEREST_SKILL_MAPPINGS.put("mobile development", Arrays.asList("Java", "Kotlin", "Swift", "React Native", "Flutter"));
        INTEREST_SKILL_MAPPINGS.put("cybersecurity", Arrays.asList("Network Security", "Ethical Hacking", "Risk Assessment", "Cryptography"));
        INTEREST_SKILL_MAPPINGS.put("digital marketing", Arrays.asList("SEO", "Social Media Marketing", "Content Creation", "Analytics"));
        INTEREST_SKILL_MAPPINGS.put("entrepreneurship", Arrays.asList("Business Planning", "Financial Management", "Networking", "Innovation"));
        INTEREST_SKILL_MAPPINGS.put("ai", Arrays.asList("Machine Learning", "Deep Learning", "Neural Networks", "Python", "TensorFlow"));
        INTEREST_SKILL_MAPPINGS.put("artificial intelligence", Arrays.asList("Machine Learning", "Deep Learning", "Neural Networks", "Python", "TensorFlow"));
        INTEREST_SKILL_MAPPINGS.put("research", Arrays.asList("Critical Thinking", "Data Analysis", "Academic Writing", "Literature Review"));
        INTEREST_SKILL_MAPPINGS.put("photography", Arrays.asList("Adobe Lightroom", "Composition", "Photo Editing", "Visual Storytelling"));
        INTEREST_SKILL_MAPPINGS.put("music", Arrays.asList("Audio Editing", "Music Theory", "Performance", "Digital Audio Workstation"));
        INTEREST_SKILL_MAPPINGS.put("writing", Arrays.asList("Creative Writing", "Copywriting", "Editing", "Content Strategy"));
        INTEREST_SKILL_MAPPINGS.put("management", Arrays.asList("Project Management", "Team Leadership", "Strategic Planning", "Decision Making"));
        INTEREST_SKILL_MAPPINGS.put("finance", Arrays.asList("Financial Analysis", "Excel", "Risk Management", "Investment Analysis"));
        INTEREST_SKILL_MAPPINGS.put("gaming", Arrays.asList("Game Development", "Unity", "C#", "Game Design", "3D Modeling"));
    }
    
    /**
     * Initialize advanced cross-domain skill mappings
     */
    private static void initializeAdvancedMappings() {
        ADVANCED_SKILL_MAPPINGS.put("leadership", Arrays.asList("Strategic Thinking", "Emotional Intelligence", "Conflict Resolution", "Mentoring"));
        ADVANCED_SKILL_MAPPINGS.put("innovation", Arrays.asList("Design Thinking", "Creativity", "Problem Solving", "Prototyping"));
        ADVANCED_SKILL_MAPPINGS.put("collaboration", Arrays.asList("Team Communication", "Cross-functional Collaboration", "Remote Work", "Agile Methodology"));
    }
    
    /**
     * Initialize skill importance weights for prioritization
     */
    private static void initializeSkillWeights() {
        // Higher weights = higher priority
        SKILL_WEIGHTS.put("Communication", 10);
        SKILL_WEIGHTS.put("Problem Solving", 9);
        SKILL_WEIGHTS.put("Leadership", 8);
        SKILL_WEIGHTS.put("Time Management", 8);
        SKILL_WEIGHTS.put("Critical Thinking", 7);
        SKILL_WEIGHTS.put("Teamwork", 7);
        SKILL_WEIGHTS.put("Git", 6);
        SKILL_WEIGHTS.put("Programming", 6);
        // Default weight for unlisted skills is 5
    }
    
    /**
     * Get skills for a specific interest area
     * 
     * @param interest The interest area (case-insensitive)
     * @return List of recommended skills for that interest
     */
    private static List<String> getSkillsForInterest(String interest) {
        // Direct mapping
        List<String> directSkills = INTEREST_SKILL_MAPPINGS.getOrDefault(interest, new ArrayList<>());
        
        // Fuzzy matching for partial matches
        List<String> fuzzySkills = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : INTEREST_SKILL_MAPPINGS.entrySet()) {
            if (entry.getKey().contains(interest) || interest.contains(entry.getKey())) {
                fuzzySkills.addAll(entry.getValue());
            }
        }
        
        // Combine and deduplicate
        Set<String> allSkills = new LinkedHashSet<>(directSkills);
        allSkills.addAll(fuzzySkills);
        
        return new ArrayList<>(allSkills);
    }
    
    /**
     * Prioritize recommendations based on skill weights and relevance
     * 
     * @param recommendations List of recommended skills
     * @return Sorted list of recommendations by priority
     */
    private static List<String> prioritizeRecommendations(List<String> recommendations) {
        return recommendations.stream()
                .sorted((skill1, skill2) -> {
                    int weight1 = SKILL_WEIGHTS.getOrDefault(skill1, 5);
                    int weight2 = SKILL_WEIGHTS.getOrDefault(skill2, 5);
                    return Integer.compare(weight2, weight1); // Descending order
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Check if a skill is appropriate for beginner level students
     * 
     * @param skill The skill to check
     * @return true if it's a beginner-friendly skill
     */
    private static boolean isBeginnerSkill(String skill) {
        Set<String> beginnerSkills = new HashSet<>(Arrays.asList(
            "Communication", "Time Management", "Problem Solving", "HTML", "CSS", 
            "Git", "Canva", "Microsoft Office", "Presentation", "Teamwork"
        ));
        return beginnerSkills.contains(skill);
    }
    
    /**
     * Get advanced skill recommendations for senior students
     * 
     * @param interestAreas Student's interest areas
     * @param existingSkills Student's existing skills
     * @return List of advanced skills
     */
    private static List<String> getAdvancedSkillRecommendations(List<String> interestAreas, List<String> existingSkills) {
        Set<String> advancedSkills = new LinkedHashSet<>();
        
        // Add leadership and management skills for senior students
        advancedSkills.addAll(Arrays.asList("Project Management", "Team Leadership", "Strategic Planning", "Mentoring"));
        
        // Add advanced technical skills based on interests
        for (String interest : interestAreas) {
            List<String> advanced = ADVANCED_SKILL_MAPPINGS.getOrDefault(interest.toLowerCase(), new ArrayList<>());
            advancedSkills.addAll(advanced);
        }
        
        return new ArrayList<>(advancedSkills);
    }
    
    /**
     * Check if a student has a specific skill (case-insensitive)
     * 
     * @param skills List of student's skills
     * @param targetSkill Skill to check for
     * @return true if the student has the skill
     */
    private static boolean hasSkill(List<String> skills, String targetSkill) {
        if (skills == null) return false;
        return skills.stream()
                .anyMatch(skill -> skill.toLowerCase().contains(targetSkill.toLowerCase()));
    }
    
    /**
     * Generate a skill recommendation report for logging/debugging
     * 
     * @param interestAreas Student's interest areas
     * @param existingSkills Student's existing skills
     * @param recommendations Generated recommendations
     * @return Formatted report string
     */
    public static String generateRecommendationReport(List<String> interestAreas, 
                                                    List<String> existingSkills, 
                                                    List<String> recommendations) {
        StringBuilder report = new StringBuilder();
        report.append("=== SKILL RECOMMENDATION REPORT ===\n");
        report.append("Interest Areas: ").append(interestAreas).append("\n");
        report.append("Existing Skills: ").append(existingSkills).append("\n");
        report.append("Recommended Skills: ").append(recommendations).append("\n");
        report.append("Recommendation Count: ").append(recommendations.size()).append("\n");
        report.append("=====================================\n");
        return report.toString();
    }
    
    /**
     * Get all available interest areas that have skill mappings
     * 
     * @return Set of supported interest areas
     */
    public static Set<String> getSupportedInterestAreas() {
        return new HashSet<>(INTEREST_SKILL_MAPPINGS.keySet());
    }
    
    /**
     * Get statistics about the recommendation engine
     * 
     * @return Map containing various statistics
     */
    public static Map<String, Object> getRecommendationEngineStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalInterestAreas", INTEREST_SKILL_MAPPINGS.size());
        stats.put("totalUniqueSkills", INTEREST_SKILL_MAPPINGS.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toSet()).size());
        stats.put("averageSkillsPerInterest", INTEREST_SKILL_MAPPINGS.values().stream()
                .mapToInt(List::size)
                .average()
                .orElse(0.0));
        stats.put("supportedInterestAreas", getSupportedInterestAreas());
        return stats;
    }
}