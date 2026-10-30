
public class JobSeeker extends BaseUser {

    public JobSeeker(String name) {
        super(name);
    }

    @Override
    public String getRole() {
        return "Job Seeker";
    }

    @Override
    public boolean canApply() {
        return true;
    }

    @Override
    public boolean canViewApplicants() {
        return false;
    }
}