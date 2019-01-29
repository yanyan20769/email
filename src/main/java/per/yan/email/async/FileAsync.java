package per.yan.email.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import per.yan.email.util.NetFileUtils;

/**
 * @author gaoyan
 * @date 2018/11/23 10:18
 */
@Component
public class FileAsync {

    @Async("taskExecutor")
    public void cleanFileFinally(String path) {
        NetFileUtils.cleanFileFinally(path);
    }
}
