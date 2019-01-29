package per.yan.email.service;

import per.yan.email.model.request.GroupMailDTO;
import per.yan.email.model.request.SingleMailDTO;
import per.yan.email.model.response.MailVO;

import java.util.List;

/**
 * @author gaoyan
 * @date 2018/10/29 10:46
 */
public interface MailService {

    MailVO sendSingleMail(SingleMailDTO mailDTO);

    List<MailVO> sendSingleMail(List<SingleMailDTO> mailDTOList);

    MailVO sendGroupMail(GroupMailDTO mailDTO);

    List<MailVO> sendGroupMail(List<GroupMailDTO> mailDTOList);
}
