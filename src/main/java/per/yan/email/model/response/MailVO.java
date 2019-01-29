package per.yan.email.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaoyan
 * @date 2018/10/31 09:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailVO {

    /**
     * 邮件标识
     */
    private String id;
    /**
     * 是否成功发送
     */
    private Boolean isSuccess;

    /**
     * 失败原因
     */
    private String message;
}
