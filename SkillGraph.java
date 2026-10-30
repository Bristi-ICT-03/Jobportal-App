import java.util.*;

public class SkillGraph {

    private Map<Skill, List<Skill>> adjacency = new HashMap<>();

    public void addEdge(Skill from, Skill to) {
        adjacency.putIfAbsent(from, new ArrayList<>());
        adjacency.putIfAbsent(to, new ArrayList<>());
        adjacency.get(from).add(to);
    }

    public boolean hasSkill(Skill s) {
        return adjacency.containsKey(s);
    }

    public List<Skill> getNeighbors(Skill s) {
        return adjacency.getOrDefault(s, Collections.emptyList());
    }

    // true if "to" is a direct next step after "from"
    public boolean hasEdge(Skill from, Skill to) {
        return adjacency.getOrDefault(from, Collections.emptyList()).contains(to);
    }

    // Old single-start version (still works)
    public List<Skill> bfsShortestPath(Skill start, Skill target) {
        return bfsShortestPath(Collections.singleton(start), target);
    }

    // Multi-source BFS: starts from ALL sources at once,
    // so the path begins from the NEAREST source to the target.
    public List<Skill> bfsShortestPath(Set<Skill> sources, Skill target) {
        Map<Skill, Skill> parent = new HashMap<>();
        Queue<Skill> queue = new LinkedList<>();
        Set<Skill> visited = new HashSet<>();

        for (Skill s : sources) {
            if (hasSkill(s)) {
                queue.add(s);
                visited.add(s);
                parent.put(s, null);
            }
        }

        while (!queue.isEmpty()) {
            Skill current = queue.poll();

            if (current.equals(target)) {
                break;
            }

            for (Skill neighbor : adjacency.getOrDefault(current, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        List<Skill> path = new ArrayList<>();
        if (!visited.contains(target)) {
            return path;
        }

        Skill step = target;
        while (step != null) {
            path.add(0, step);
            step = parent.get(step);
        }
        return path;
    }
}