
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SmartJobMatcher implements IMatcher {

    @Override
    public int calculateMatch(List<String> userSkills, List<String> requiredSkills) {
        List<String> normalizedUser = userSkills.stream()
                .map(s -> s.toLowerCase().trim())
                .collect(Collectors.toList());
        List<String> normalizedReq = requiredSkills.stream()
                .map(s -> s.toLowerCase().trim())
                .collect(Collectors.toList());

        int matched = 0;
        for (String req : normalizedReq) {
            if (normalizedUser.contains(req)) matched++;
        }
        if (requiredSkills.isEmpty()) return 0;
        return (matched * 100) / requiredSkills.size();
    }

    @Override
    public List<String> getMissingSkills(List<String> userSkills, List<String> requiredSkills) {
        List<String> missing = new ArrayList<>();
        List<String> normalizedUser = userSkills.stream()
                .map(s -> s.toLowerCase().trim())
                .collect(Collectors.toList());

        for (String req : requiredSkills) {
            if (!normalizedUser.contains(req.toLowerCase().trim())) {
                missing.add(req);
            }
        }
        return missing;
    }
}