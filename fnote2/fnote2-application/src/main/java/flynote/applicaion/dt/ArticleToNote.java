package flynote.applicaion.dt;

import farticle.domain.infrastructure.ArticleRepository;
import fly4j.common.util.ExceptionUtil;
import fly4j.common.util.RepositoryException;
import fnote.user.domain.infrastructure.UserRepository;

/**
 * Created by qryc on 2021/10/21
 */
public class ArticleToNote {
    private UserRepository userRepository;
    private ArticleRepository articleRepositoryOld;
    private ArticleRepository articleRepositoryNew;

    public synchronized void dtDate() throws RepositoryException {
        userRepository.findAllUserInfo().forEach(userInfo -> ExceptionUtil.wrapperRuntime(() -> {
//            if (UserService.isAdmin(userInfo.getPin())) {
//                return;
//            }
//            System.out.println("开始迁徙：" + userInfo.getPin());
//            AtomicInteger count = new AtomicInteger(0);
//            AtomicInteger delete = new AtomicInteger(0);
//            List<CplArticle> cplArticles = articleRepositoryOld.findCplArticlesByPin(userInfo.getPin(), WorkSpaceParam.of(""));
//            cplArticles.forEach(cplArticle -> ExceptionUtil.wrapperRuntime(() -> {
//                articleRepositoryNew.insertCplArticle(cplArticle);
//                articleRepositoryOld.deleteCplArticleById(cplArticle.getIdPin());
//                count.incrementAndGet();
//                delete.incrementAndGet();
//            }));
//            System.out.println("迁徙结束，共迁徙:" + count.get());
//            System.out.println("迁徙结束，共删除:" + delete.get());
        }));
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setArticleRepositoryOld(ArticleRepository articleRepositoryOld) {
        this.articleRepositoryOld = articleRepositoryOld;
    }

    public void setArticleRepositoryNew(ArticleRepository articleRepositoryNew) {
        this.articleRepositoryNew = articleRepositoryNew;
    }
}
