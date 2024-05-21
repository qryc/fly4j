package farticle.domain.view;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qryc on 2020/4/5.
 */
class Mark {
    private final List<String> marks = new ArrayList<>();

    public void appendMark(String appendMark) {
        if (StringUtils.isNoneBlank(appendMark))
            marks.add(appendMark);
    }

    public List<String> getMarks() {
        return marks;
    }
}
