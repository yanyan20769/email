package per.yan.email.constant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * 由于前后端分离，邮件服务所在服务器不存在静态资源
 * 邮件正文需要包含图片等静态资源时，
 * 可以将图片上传到静态资源服务器
 * 图片使用网络链接并且MailHelperEnum支持HTML即可
 *
 * @author gaoyan
 * @date 2018/10/29 10:20
 */
@Getter
@ApiModel("邮件帮助信息")
public enum MailHelperEnum {

    /**
     * 邮件帮助信息
     */
    @ApiModelProperty("包含HTML内容")
    WITHIN_HTML(1),

    @ApiModelProperty("包含静态资源")

    WITHIN_STATIC(2),

    @ApiModelProperty("包含附件")
    WITHIN_ATTACHMENT(4),

    @ApiModelProperty("包含HTML、静态资源")
    WITHIN_HTML_STATIC(3),

    @ApiModelProperty("包含HTML、附件")
    WITHIN_HTML_ATTACHMENT(5),

    @ApiModelProperty("包含静态资源、附件")
    WITHIN_STATIC_ATTACHMENT(6),

    @ApiModelProperty("包含所有")
    WITHIN_ALL(7);

    MailHelperEnum(Integer code) {
        this.code = code;
    }

    private Integer code;
}
