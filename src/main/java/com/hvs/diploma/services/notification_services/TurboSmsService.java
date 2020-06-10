package com.hvs.diploma.services.notification_services;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.dao.sms.TurboSmsMessageRepository;
import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.turbo_sms.TurboSmsMessage;
import com.hvs.diploma.turbo_sms.TurboSmsMessageBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

@Service
public class TurboSmsService {
    private final CurrentAccount currentAccount;
    private final InfoMessagesService messagesService;
    private final TurboSmsMessageRepository turboSmsMessageRepository;
    private static final String DEFAULT_SIGN = "Faceless";
    org.slf4j.Logger logger = LoggerFactory.getLogger(TurboSmsService.class);

    @Autowired
    public TurboSmsService(TurboSmsMessageRepository turboSmsMessageRepository, CurrentAccount currentAccount, InfoMessagesService messagesService) {
        this.turboSmsMessageRepository = turboSmsMessageRepository;
        this.currentAccount = currentAccount;
        this.messagesService = messagesService;
    }

    @Transactional
    public void sendSmsNotification(TaskDTO taskDTO) throws ParseException {
        String notificationMessage = messagesService.buildTaskNotificationMessage(taskDTO);
        TurboSmsMessageBuilder builder = new TurboSmsMessageBuilder();
        TurboSmsMessage message = builder.sign(DEFAULT_SIGN)
                .phoneNumber(currentAccount.getPhoneNumber())
                .message(notificationMessage)
                .sendTime(taskDTO.getNotificationDate())
                .build();
        turboSmsMessageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public Double getBalance() {
        TurboSmsMessage lastMessage = turboSmsMessageRepository.findTopByOrderByIdDesc();
        logger.warn(lastMessage.toString());
        return lastMessage.getBalance();
    }

    @Transactional(readOnly = true)
    public List<TurboSmsMessage> findAll() {
        return turboSmsMessageRepository.findAll();
    }
}
