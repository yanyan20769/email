package per.yan.email.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * @author gaoyan
 * @date 2018/10/31 10:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("邮件 信息(群发)")
public class GroupMailDTO extends BaseMailDTO {

    @ApiModelProperty("收件人列表")
    @NotBlank(message = "to can not be null!")
    private Set<String> to;
}
