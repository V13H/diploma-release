package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.components.TaskStatistic;
import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.services.data_access_services.TaskService;
import com.hvs.diploma.services.notification_services.InfoMessagesService;
import com.hvs.diploma.services.notification_services.TurboSmsService;
import com.hvs.diploma.services.validation_services.form_validators.TaskFormValidator;
import com.hvs.diploma.services.validation_services.task_dto_validators.DeadlineValidator;
import com.hvs.diploma.services.validation_services.task_dto_validators.TimeValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;

@Controller
public class TaskActionController {
    org.slf4j.Logger logger = LoggerFactory.getLogger(TaskActionController.class);
    private final CurrentAccount currentAccount;
    private final MainService mainService;
    private final DeadlineValidator deadlineValidator;
    private final TimeValidator timeValidator;
    private final TaskService taskService;
    private final TurboSmsService turboSmsService;
    private final InfoMessagesService infoMessagesService;
    private final TaskFormValidator addTaskFormValidator;

    @Autowired
    public TaskActionController(CurrentAccount currentAccount, MainService mainService, DeadlineValidator deadlineValidator, TimeValidator timeValidator, TaskService taskService, TurboSmsService turboSmsService, InfoMessagesService infoMessagesService, TaskFormValidator addTaskFormValidator) {
        this.currentAccount = currentAccount;
        this.mainService = mainService;
        this.deadlineValidator = deadlineValidator;
        this.timeValidator = timeValidator;
        this.taskService = taskService;
        this.turboSmsService = turboSmsService;
        this.infoMessagesService = infoMessagesService;
        this.addTaskFormValidator = addTaskFormValidator;
    }

    @GetMapping("/delete")
    public void delete(@RequestParam Long id, @RequestParam Integer page,
                       HttpServletResponse response) throws IOException {

        mainService.deleteTask(id);
        response.sendRedirect("/?page=" + page);
    }

    @GetMapping("/mark-as-done")
    public void markAsDone(@RequestParam Long id, @RequestParam Integer page,
                           HttpServletResponse response) throws IOException {
        taskService.markTaskAsDoneById(id);
        updateStat();

        response.sendRedirect("/?page=" + page);
    }

    private void updateStat() {
        TaskStatistic updatedStat = mainService.getTaskStatistic(currentAccount);
        currentAccount.setTaskStatistic(updatedStat);
    }


    @PostMapping("/retry")
    public String restartTask(@Valid @ModelAttribute("taskDTO") TaskDTO taskDTO, Model model,
                              BindingResult bindingResult) throws ParseException {
        //here we validating the taskDTO with DeadlineValidator which will check only deadline input
        deadlineValidator.validate(taskDTO, bindingResult);
        timeValidator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("notificationsEnabled", currentAccount.hasPhoneNumber());
            return "restart-task";
        } else {
            //getting Task by id and updating it`s deadline
            mainService.retry(taskDTO.getId(), taskDTO.getDeadlineDate());
            updateStat();
            if (notificationTimeExists(taskDTO)) {
                turboSmsService.sendSmsNotification(taskDTO);
            }
            return "redirect:/";
        }
    }


    @PostMapping("/add-task")
    public String addTask(@Valid @ModelAttribute("taskDTO") TaskDTO taskDTO,
                          BindingResult bindingResult,
                          @RequestParam String group1) throws ParseException {
        //validates form with TaskForm validator
        addTaskFormValidator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "add-task";
        } else {
            taskDTO.setPriority(group1);
            Task task = taskDTO.buildTaskInstance();
            task.setOwner(currentAccount.getAccountEntity());
            mainService.saveTask(task);
            if (notificationTimeExists(taskDTO)) {
                turboSmsService.sendSmsNotification(taskDTO);
            }
            logger.warn("add: " + taskDTO.toString());
            return "redirect:/";
        }
    }

    private boolean notificationTimeExists(TaskDTO taskDTO) {
        return taskDTO.getNotificationTime() != null && !taskDTO.getNotificationTime().isEmpty();
    }
}
