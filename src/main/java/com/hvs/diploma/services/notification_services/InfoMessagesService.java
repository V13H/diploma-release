package com.hvs.diploma.services.notification_services;

import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.entities.Account;
import org.springframework.stereotype.Service;

@Service
public class InfoMessagesService {
    public String getGreetingsMessage(Account account) {
        if (!account.hasWatchedGreetingsMessage()) {
            return "Greetings," + account.getUserName() + ".We are glad to see you here." +
                    "Our application provides SMS-notifications service for remind users about their tasks." +
                    "The only thing you need to do to start use it is specify your phone number.For this purpose" +
                    " check out \"Settings\".Good luck,have fun :)";
        } else {
            return null;
        }
    }

    public String buildTaskNotificationMessage(TaskDTO taskDTO) {
        return "Greetings from \"You can do it\"\n" +
                "Don`t forget about your task for today: " +
                "" + taskDTO.getDescription() + "\n";
    }

}
