package farticle.domain.entity;

public record WorkSpaceParam(String spaceName) {
    public static final String WORKSPACE_PERSON = "person";
    public static final String WORKSPACE_COMPANY = "company";

    public static WorkSpaceParam of(String spaceName) {
        return new WorkSpaceParam(spaceName);
    }
}
