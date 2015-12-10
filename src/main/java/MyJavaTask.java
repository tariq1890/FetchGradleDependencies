/**
* Created by tariq.ibrahim on 09-12-2015.
*/

import org.gradle.tooling.model.GradleProject;
import org.gradle.tooling.model.GradleTask;

public class MyJavaTask implements GradleTask {

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean isPublic() {
        return false;
    }

    @Override
    public GradleProject getProject() {
        return null;
    }

    @Override
    public String getGroup() {
        return null;
    }
}