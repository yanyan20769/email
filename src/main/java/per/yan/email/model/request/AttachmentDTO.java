package per.yan.email.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaoyan
 * @date 2018/10/29 10:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("邮件 附件 信息")
public class AttachmentDTO {

    @ApiModelProperty("本地附件地址")
    private String localPath;

    @ApiModelProperty("网络附件地址")
    private String onlineUrl;

    @ApiModelProperty("网络附件下载名称")
    private String onlineName;
}
