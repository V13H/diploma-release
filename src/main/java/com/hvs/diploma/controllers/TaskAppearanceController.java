package com.hvs.diploma.controllers;

import com.hvs.diploma.components.CurrentAccount;
import com.hvs.diploma.components.SortAndFilterParams;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskDeadlines;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.services.data_access_services.MainService;
import com.hvs.diploma.services.notification_services.InfoMessagesService;
import com.hvs.diploma.services.notification_services.TurboSmsService;
import com.hvs.diploma.services.validation_services.form_validators.TaskFormValidator;
import com.hvs.diploma.services.validation_services.task_dto_validators.DeadlineValidator;
import com.hvs.diploma.services.validation_services.task_dto_validators.TimeValidator;
import com.hvs.diploma.util.PageableHelper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class TaskAppearanceController {
    org.slf4j.Logger logger = LoggerFactory.getLogger(TaskAppearanceController.class);
    private final MainService mainService;
    private final InfoMessagesService infoMessagesService;
    //'session' scoped component which stores Account instance for each session
    private final CurrentAccount currentAccount;
    //'session' scoped component which stores sort and filter params for each session;
    private final SortAndFilterParams sortAndFilterParams;

    @Autowired
    public TaskAppearanceController(TaskFormValidator addTaskFormValidator, MainService mainService, CurrentAccount currentAccount,
                                    SortAndFilterParams sortAndFilterParams, DeadlineValidator deadlineValidator, InfoMessagesService infoMessagesService, TurboSmsService turboSmsService, TimeValidator timeValidator) {
        this.mainService = mainService;
        this.currentAccount = currentAccount;
        this.sortAndFilterParams = sortAndFilterParams;
        this.infoMessagesService = infoMessagesService;
    }


    @GetMapping("/")
    public String getHomePage(Model model, @RequestParam(required = false, defaultValue = "0") Integer page) {
        logger.warn("currentAccount: " + currentAccount.toString());
        int size = 5;
        long count;
        List<Task> tasks;
        Account account = currentAccount.getAccountEntity();
        //looking for tasks with expired deadlines and updating their status
        if (!currentAccount.isTasksDeadlinesChecked()) {
            checkDeadlines();
        }
        logger.warn(sortAndFilterParams.toString());
        //checking filter parameters and getting tasks from db
        if (!sortAndFilterParams.isFilterParamsSpecified()) {
            count = mainService.countTasksByStatusIsNot(account, TaskStatus.DONE);
            page = PageableHelper.checkPageParam(page, count, size);
            tasks = mainService.getAllUndoneTasksForAccount(account, getPageable(count, size, page));
        } else {
            count = mainService.countTasksByFilterParams(account,
                    sortAndFilterParams.getPriorities(), sortAndFilterParams.getStatuses(), sortAndFilterParams.getDeadlines());
            page = PageableHelper.checkPageParam(page, count, size);
            tasks = mainService.getTasksByFilterParameters(account,
                    getPageable(count, size, page),
                    sortAndFilterParams.getPriorities(), sortAndFilterParams.getStatuses(), sortAndFilterParams.getDeadlines());
        }

        long pagesCount = PageableHelper.getPagesCount(count, size);
        String sortByToDisplay = sortAndFilterParams.getSortByToDisplay();
        String emptyListMessage = sortAndFilterParams.getEmptyListMessage(count);
        String greetingsMessage = infoMessagesService.getGreetingsMessage(currentAccount.getAccountEntity());
        boolean setAddButtonPulse = emptyListMessage.equals("You don`t have tasks yet");

        model.addAttribute("greetingsMessage", greetingsMessage);
        model.addAttribute("filterParamsExists", sortAndFilterParams.isFilterParamsSpecified());
        model.addAttribute("setPulse", setAddButtonPulse);
        model.addAttribute("message", emptyListMessage);
        model.addAttribute("today", TaskDeadlines.TODAY);
        model.addAttribute("tomorrow", TaskDeadlines.TOMORROW);
        model.addAttribute("thisWeek", TaskDeadlines.THIS_WEEK);
        model.addAttribute("pagesCount", pagesCount);
        model.addAttribute("tasksCount", count);
        model.addAttribute("account", account);
        model.addAttribute("page", page);
        model.addAttribute("tasks", tasks);
        model.addAttribute("sortBy", sortByToDisplay);
        model.addAttribute("order", sortAndFilterParams.getSortOrder());
        model.addAttribute("priorities", sortAndFilterParams.getPriorities());
        model.addAttribute("statuses", sortAndFilterParams.getStatuses());
        model.addAttribute("deadlines", sortAndFilterParams.getDeadlines());
        model.addAttribute("isAdmin", currentAccount.isAdmin());
        return "index";
    }

    //if task`s deadline > DateHelper.today() sets it`s status to EXPIRED
    private void checkDeadlines() {
        mainService.checkDeadlines(currentAccount.getAccountEntity());
        currentAccount.setTasksDeadlinesChecked(true);
    }

    //sets sort parameters to SortAndFilterParams instance
    @PostMapping("/filter")
    public void filter(HttpServletResponse response,
                       @RequestParam(required = false) List<TaskPriority> priority,
                       @RequestParam(required = false) List<TaskStatus> status,
                       @RequestParam(required = false) List<TaskDeadlines> date) throws IOException {
        sortAndFilterParams.setPriorities(priority);
        sortAndFilterParams.setStatuses(status);
        sortAndFilterParams.setDeadlines(date);
        response.sendRedirect("/");
    }

    //called when user clicks on chip`s 'close' icon
    //removes chosen parameter from SortAndFilterParams instance
    @GetMapping("/filter")
    public void removeFilterParam(HttpServletResponse response,
                                  @RequestParam String criterion, @RequestParam String value) throws IOException {
        sortAndFilterParams.removeFilterParameter(criterion, value);
        response.sendRedirect("/");
    }

    @GetMapping("/reset")
    public void resetFilterParams(HttpServletResponse response) throws IOException {
        sortAndFilterParams.resetFilterParams();
        response.sendRedirect("/");
    }


    //sets sort parameters to SortAndFilterParams instance
    @GetMapping("/sort")
    public void sort(@RequestParam String by, @RequestParam String order,
                     @RequestParam(required = false, defaultValue = "0") Integer page,
                     HttpServletResponse response) throws IOException {
        sortAndFilterParams.setSortBy(by);
        sortAndFilterParams.setSortOrder(order);
        response.sendRedirect("/");
    }

    //redirects user from greetings modal accordingly to user`s choice(index or settings)
    @GetMapping("/modal-dismiss")
    public void dismissGreetingsModal(@RequestParam(required = false, defaultValue = "/") String redirectEndpoint,
                                      HttpServletResponse response) throws IOException {
        currentAccount.getAccountEntity().setHasWatchedGreetingsMessage(true);
        mainService.saveAccount(currentAccount.getAccountEntity());
        response.sendRedirect(redirectEndpoint);
    }

    private Pageable getPageable(long count, int size, int page) {
        return PageableHelper.getPageable(page, count, size, sortAndFilterParams);
    }

}
