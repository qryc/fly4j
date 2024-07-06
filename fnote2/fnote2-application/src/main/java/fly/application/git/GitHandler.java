package fly.application.git;

import farticle.domain.entity.ArticleEventObj;
import fly4j.common.event.AlterEvent;
import fly4j.common.event.EventHandler;
import flynote.applicaion.service.TreeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHandler implements EventHandler<ArticleEventObj> {
    private static final Logger log = LoggerFactory.getLogger(GitHandler.class);

    @Override
    public void doHandle(AlterEvent<ArticleEventObj> alterEvent) {
        ArticleEventObj eventObj = alterEvent.getEventObj();
        if ("article".equals(eventObj.getDomain())) {
            if (ArticleEventObj.EDIT == eventObj.getOpCode()
                    || ArticleEventObj.DELETE == eventObj.getOpCode()
                    || ArticleEventObj.INSERT == eventObj.getOpCode()) {
            }
        }
        TreeCache.clear(eventObj.getPin());

        GitService.asynPullAndCommitGit("GitHandler");
    }
}
