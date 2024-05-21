package farticle.domain.view;

public interface ArticleTreeSPI {
    /**
     * 自定义构建树
     * @param param Dtree当前树，file 当前文件
     * @param treeService
     */
    public void buildTree(ArticleTreeSpiParam param, TreeService treeService);
}
