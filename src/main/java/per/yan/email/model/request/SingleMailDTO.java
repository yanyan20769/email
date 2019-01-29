package per.yan.email.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author gaoyan
 * @date 2018/10/31 10:15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("邮件 信息(单发)")
public class SingleMailDTO extends BaseMailDTO {
    @ApiModelProperty("收件人")
    @NotBlank(message = "to can not be blank!")
    private String to;
}
