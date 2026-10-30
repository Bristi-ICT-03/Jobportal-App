import java.util.List;
import java.util.Set;

public class BFSRoadmapStrategy implements RoadmapStrategy {

    @Override
    public List<Skill> generateRoadmap(SkillGraph graph, Set<Skill> known, Skill target) {
        return graph.bfsShortestPath(known, target);
    }
}