package training.system.core.domain.progress;

import training.system.core.domain.user.User;

import java.util.Set;

public interface IProgress {
    Progress createProgressForClient(Progress progress) throws Exception;
    Set<Progress> listUserProgress(User user) throws Exception;
}
