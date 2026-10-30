import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Holds the roles, the skill graph, and builds the learning roadmap.
// (No GUI code here - the GUI only shows what this class returns.)
public class SkillCatalog {

    // Virtual root, so a beginner always has a starting point
    private static final Skill START = new Skill("START");

    private final String[] allSkills = {
            "C++", "Java", "OOP", "Data Structures", "Algorithms",
            "Git", "SQL", "Python", "NumPy", "Pandas",
            "Machine Learning", "Scikit-learn", "TensorFlow", "Statistics"
    };

    private final Map<String, List<String>> roleSkills = new LinkedHashMap<>();
    private final SkillGraph graph = new SkillGraph();

    public SkillCatalog() {
        roleSkills.put("Software Engineer",
                Arrays.asList("C++", "Java", "OOP", "Data Structures", "Algorithms", "Git", "SQL"));
        roleSkills.put("Machine Learning Engineer",
                Arrays.asList("Python", "NumPy", "Pandas", "Statistics", "Machine Learning", "Scikit-learn", "TensorFlow", "SQL"));
        roleSkills.put("Data Scientist",
                Arrays.asList("Python", "NumPy", "Pandas", "Statistics", "Machine Learning", "SQL"));
        roleSkills.put("Backend Developer",
                Arrays.asList("Java", "OOP", "Data Structures", "Algorithms", "SQL", "Git"));
        roleSkills.put("Data Analyst",
                Arrays.asList("Python", "Pandas", "NumPy", "SQL", "Statistics"));

        // Edge A -> B means "B comes after A"
        String[][] edges = {
                {"START", "C++"}, {"START", "Java"}, {"START", "Python"},
                {"START", "Statistics"}, {"START", "Git"}, {"START", "SQL"},
                {"Java", "OOP"}, {"C++", "Data Structures"}, {"OOP", "Data Structures"},
                {"Data Structures", "Algorithms"},
                {"Python", "NumPy"}, {"NumPy", "Pandas"},
                {"Pandas", "Machine Learning"}, {"Statistics", "Machine Learning"},
                {"Machine Learning", "Scikit-learn"}, {"Scikit-learn", "TensorFlow"}
        };
        for (String[] e : edges) {
            graph.addEdge(new Skill(e[0]), new Skill(e[1]));
        }
    }

    public String[] getAllSkills() {
        return allSkills;
    }

    public List<String> getRoles() {
        return new ArrayList<>(roleSkills.keySet());
    }

    public List<String> getRequiredSkills(String role) {
        return roleSkills.get(role);
    }

    // true if "to" is a direct next step after "from"
    public boolean isDirectStep(Skill from, Skill to) {
        return graph.hasEdge(from, to);
    }

    // For every missing skill, find a path from the nearest known skill
    // (using the given strategy), then merge all paths in order.
    public Set<Skill> buildRoadmap(String role, Set<Skill> userSkills, RoadmapStrategy strategy) {
        Set<Skill> known = new LinkedHashSet<>();
        known.add(START);
        known.addAll(userSkills);

        Set<Skill> roadmap = new LinkedHashSet<>();
        for (String req : roleSkills.get(role)) {
            Skill target = new Skill(req);
            if (known.contains(target)) continue;

            List<Skill> path = strategy.generateRoadmap(graph, known, target);
            if (path.isEmpty()) {
                roadmap.add(target);
            } else {
                roadmap.addAll(path);
            }
        }
        roadmap.remove(START);
        return roadmap;
    }
}