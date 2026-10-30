public abstract class BaseUser {

    protected String name;

    public BaseUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String getRole();

    public abstract boolean canApply();

    public abstract boolean canViewApplicants();
}