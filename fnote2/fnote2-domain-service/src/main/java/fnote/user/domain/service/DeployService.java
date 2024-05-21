package fnote.user.domain.service;

import fly4j.common.util.RepositoryException;

import java.io.IOException;

public interface DeployService {
    boolean isSiteInit() throws RepositoryException;

    void installFlySite(String email) throws IOException, RepositoryException;


    String getAdminEmail() throws RepositoryException;

    void checkSiteDeploy();
}
