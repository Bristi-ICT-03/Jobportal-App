import java.util.List;

public interface IMatcher {
    int calculateMatch(List<String> userSkills, List<String> requiredSkills);
    List<String> getMissingSkills(List<String> userSkills, List<String> requiredSkills);
}