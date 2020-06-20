package com.hvs.diploma.services.notification_services;

import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.services.data_access_services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoMessagesService {
    private final TaskService taskService;
    @Autowired
    public InfoMessagesService(TaskService taskService) {
        this.taskService = taskService;
    }

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

    public String getEmptyListMessage(Account account) {
        return taskService.countTasksByOwner(account) > 0 ? "Tasks not found" : "You don`t have tasks yet";
    }

    public String buildTaskNotificationMessage(TaskDTO taskDTO) {
        return "Greetings from \"You can do it\"\n" +
                "Don`t forget about your task for today: " +
                "" + taskDTO.getDescription() + "\n";
    }

    public String getNoStatDataMessage() {
        return "You don`t have any tasks yet.Therefore,there is no statistic to show";
    }

}
