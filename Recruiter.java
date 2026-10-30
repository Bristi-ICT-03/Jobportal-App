import java.util.ArrayList;
import java.util.List;

public class Recruiter extends BaseUser {

    public Recruiter(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "Recruiter";
    }

    @Override
    public boolean canApply() {
        return false;
    }

    @Override
    public boolean canViewApplicants() {
        return true;
    }

    // Returns applicants of one job, best match first
    public List<Application> getApplicantsFor(List<Application> all, String jobTitle) {
        List<Application> result = new ArrayList<>();
        for (Application a : all) {
            if (a.getJobTitle().equals(jobTitle)) {
                result.add(a);
            }
        }
        result.sort((a, b) -> b.getScore() - a.getScore());
        return result;
    }
}