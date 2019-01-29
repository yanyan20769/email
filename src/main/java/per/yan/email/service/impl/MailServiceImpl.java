package per.yan.email.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import per.yan.email.async.FileAsync;
import per.yan.email.constant.MailHelperEnum;
import per.yan.email.model.request.AttachmentDTO;
import per.yan.email.model.request.BaseMailDTO;
import per.yan.email.model.request.GroupMailDTO;
import per.yan.email.model.request.SingleMailDTO;
import per.yan.email.model.response.MailVO;
import per.yan.email.service.MailService;
import per.yan.email.util.NetFileUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author gaoyan
 * @date 2018/10/29 10:47
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private FileAsync async;

    @Override
    public MailVO sendSingleMail(SingleMailDTO mailDTO) {
        MailVO mailVO = doSend(mailDTO);
        async.cleanFileFinally(NetFileUtils.assemblePath());
        return mailVO;
    }

    @Override
    public List<MailVO> sendSingleMail(List<SingleMailDTO> mailDTOList) {
        return sendMail(mailDTOList);
    }

    @Override
    public MailVO sendGroupMail(GroupMailDTO mailDTO) {
        MailVO mailVO = doSend(mailDTO);
        async.cleanFileFinally(NetFileUtils.assemblePath());
        return mailVO;
    }

    @Override
    public List<MailVO> sendGroupMail(List<GroupMailDTO> mailDTOList) {
        return sendMail(mailDTOList);
    }

    private void handleAttachmentPath(BaseMailDTO mailDTO, Map<String, String> pathMap) {
        mailDTO.getAttachments().stream()
                .filter(a -> StringUtils.isEmpty(a.getLocalPath()) && !StringUtils.isEmpty(a.getOnlineUrl()))
                .forEach(a -> {
                    String path = pathMap.get(a.getOnlineUrl());
                    if (StringUtils.isEmpty(path) && StringUtils.isEmpty(a.getLocalPath())) {
                        path = assembleLocalPath(a);
                        pathMap.put(a.getOnlineUrl(), path);
                        a.setLocalPath(path);
                    }
                });
    }

    private MailVO doSend(BaseMailDTO mailDTO) {
        MailVO mailVO = new MailVO();
        if (mailDTO != null) {
            mailVO.setId(mailDTO.getId());
            MimeMessage message = sender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, containsAttachment(mailDTO.getHelper()) && !CollectionUtils.isEmpty(mailDTO.getAttachments())
                        || containsHTML(mailDTO.getHelper())
                        || containsStatic(mailDTO.getHelper()));
                helper.setFrom(from);
                if (mailDTO instanceof SingleMailDTO) {
                    helper.setTo(((SingleMailDTO) mailDTO).getTo());
                }
                if (mailDTO instanceof GroupMailDTO) {
                    Set set = ((GroupMailDTO) mailDTO).getTo();
                    helper.setTo((String[]) set.toArray(new String[set.size()]));
                }
                if (!CollectionUtils.isEmpty(mailDTO.getCopyTo())) {
                    Set set = mailDTO.getCopyTo();
                    helper.setCc((String[]) set.toArray(new String[set.size()]));
                }
                helper.setSubject(mailDTO.getSubject());
                helper.setText(mailDTO.getContent(), containsHTML(mailDTO.getHelper()) || containsStatic(mailDTO.getHelper()));
                if (addAttachments(mailDTO, helper)) {
                    sender.send(message);
                    mailVO.setIsSuccess(true);
                } else {
                    mailVO.setIsSuccess(false);
                    mailVO.setMessage("send mail error!");
                }
            } catch (MessagingException e) {
                mailVO.setIsSuccess(false);
                mailVO.setMessage(Optional.ofNullable(e.getMessage()).orElse(""));
                log.error("sendMail error! message:{}", e);
            }
        }
        return mailVO;
    }

    private boolean containsHTML(MailHelperEnum helperEnum) {
        return (helperEnum.getCode() & 1) == 1;
    }

    private boolean containsStatic(MailHelperEnum helperEnum) {
        return (helperEnum.getCode() >> 1 & 1) == 1;
    }

    private boolean containsAttachment(MailHelperEnum helperEnum) {
        return (helperEnum.getCode() >> 2 & 1) == 1;
    }

    private String assembleLocalPath(AttachmentDTO attachment) {
        return attachment == null ? null : NetFileUtils.downloadFile(attachment.getOnlineUrl(), attachment.getOnlineName()).getPath();
    }

    private boolean addAttachments(BaseMailDTO mailDTO, MimeMessageHelper helper) {
        AtomicBoolean flag = new AtomicBoolean(true);
        if (containsAttachment(mailDTO.getHelper()) && !CollectionUtils.isEmpty(mailDTO.getAttachments())) {
            mailDTO.getAttachments()
                    .forEach(a -> {
                        if (StringUtils.isEmpty(a.getLocalPath())) {
                            a.setLocalPath(assembleLocalPath(a));
                        }
                        File file = new File(a.getLocalPath());
                        FileSystemResource fsr = new FileSystemResource(file);
                        String fileName = file.getPath().substring(file.getPath().lastIndexOf(File.separator) + 1);
                        try {
                            helper.addAttachment(fileName, fsr);
                        } catch (MessagingException e) {
                            log.error("addAttachment error! message:{}", e);
                            flag.set(false);
                        }
                    });
        }
        return flag.get();
    }

    private <T extends BaseMailDTO> List<MailVO> sendMail(List<T> mailDTOList) {
        if (!CollectionUtils.isEmpty(mailDTOList)) {
            Map<String, String> pathMap = new HashMap<>(256);
            mailDTOList.stream()
                    .filter(mail -> containsAttachment(mail.getHelper()) && !CollectionUtils.isEmpty(mail.getAttachments()))
                    .forEach(m -> handleAttachmentPath(m, pathMap));
            List<MailVO> mailVOList = mailDTOList.stream().map(this::doSend).collect(Collectors.toList());
            async.cleanFileFinally(NetFileUtils.assemblePath());
            return mailVOList;
        }
        return null;
    }
}
