package fnote.user.domain.infrastructure;

import fly4j.common.util.RepositoryException;
import fnote.user.domain.entity.IUserInfo;

import java.io.IOException;
import java.util.List;

public interface UserRepository {
    void insertUserInfo(IUserInfo userInfo) throws RepositoryException;

    void updateUserInfo(IUserInfo userInfo) throws RepositoryException;

    IUserInfo getUserinfo(String pin) throws RepositoryException;

    void delUser(String pin) throws RepositoryException;

    List<IUserInfo> findAllUserInfo() throws RepositoryException;

    String getDefaultUserArticlePwd();

    void createUserDirs(String pin) throws IOException;

}
