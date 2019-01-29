package per.yan.email.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import per.yan.email.constant.MailHelperEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @author gaoyan
 * @date 2018/10/29 10:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("邮件 信息")
public class BaseMailDTO {

    @ApiModelProperty("Id")
    private String id;
    @ApiModelProperty("主题")
    @NotBlank(message = "subject can not be blank!")
    private String subject;
    @ApiModelProperty("邮件内容")
    private String content;
    @ApiModelProperty("抄送人列表")
    private Set<String> copyTo;
    @ApiModelProperty("附件列表")
    List<AttachmentDTO> attachments;
    @ApiModelProperty("邮件帮助信息")
    @NotNull(message = "mail helper can not be null!")
    private MailHelperEnum helper;

}
