import java.util.List;
import java.util.Set;

public interface RoadmapStrategy {
    List<Skill> generateRoadmap(SkillGraph graph, Set<Skill> known, Skill target);
}