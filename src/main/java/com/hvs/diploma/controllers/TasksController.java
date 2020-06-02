package com.hvs.diploma.controllers;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskDeadlines;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.pojo.CurrentAccount;
import com.hvs.diploma.pojo.SortAndFilterParams;
import com.hvs.diploma.services.MainService;
import com.hvs.diploma.util.DateTimeHelper;
import com.hvs.diploma.validators.TaskFormValidator;
import com.hvs.diploma.validators.task_dto_validators.DeadlineValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
import java.security.Principal;
import java.util.List;

@Controller
public class TasksController {
    org.slf4j.Logger logger = LoggerFactory.getLogger(TasksController.class);
    private final TaskFormValidator addTaskFormValidator;
    private final DeadlineValidator deadlineValidator;
    private final MainService mainService;
    //'session' scoped component which stores Account instance for each session
    private final CurrentAccount currentAccount;
    //'session' scoped component which stores sort and filter params for each session;
    private final SortAndFilterParams sortAndFilterParams;
//    private final TurboSmsMessageRepository smsMessageRepository;

    @Autowired
    public TasksController(TaskFormValidator addTaskFormValidator, MainService mainService, CurrentAccount currentAccount,
                           SortAndFilterParams sortAndFilterParams, DeadlineValidator deadlineValidator) {
        this.addTaskFormValidator = addTaskFormValidator;
        this.mainService = mainService;
        this.currentAccount = currentAccount;
        this.sortAndFilterParams = sortAndFilterParams;
        this.deadlineValidator = deadlineValidator;
    }

    @GetMapping("/add-task")
    public String getAddNewTaskForm(Model model) {
        model.addAttribute("taskDTO", new TaskDTO());
        return "add-task";
    }

    @PostMapping("/add-task")
    public String addTask(@Valid @ModelAttribute("taskDTO") TaskDTO taskDTO,
                          BindingResult bindingResult,
                          @RequestParam String group1) {
        //validates form with TaskForm validator
        addTaskFormValidator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "add-task";
        } else {
            taskDTO.setPriority(group1);
            Task task = taskDTO.buildTaskInstance();
            task.setOwner(currentAccount.getAccount());
            mainService.saveTask(task);
            return "redirect:/";
        }
    }

    @GetMapping("/")
    public String getHomePage(Model model, Principal principal,
                              @RequestParam(required = false, defaultValue = "0") Integer page) {
        logger.warn("pattern: " + DateTimeHelper.simpleDateFormat().toPattern());
        loadAccountInfo(principal);
        int size = 5;
        long count;
        page = checkPageParam(page);
        List<Task> tasks;
        Account account = currentAccount.getAccount();
        //looking for tasks with expired deadlines and updating their status
        if (!currentAccount.isTasksDeadlinesChecked()) {
            checkDeadlines();
        }
        logger.warn(sortAndFilterParams.toString());
        //checking filter parameters and getting tasks from db
        if (!sortAndFilterParams.isFilterParamsSpecified()) {
            count = mainService.countTasksByStatusIsNot(account, TaskStatus.DONE);
            tasks = mainService.getAllUndoneTasksForAccount(account, getPageable(count, size, page));
        } else {
            count = mainService.countTasksByFilterParams(account,
                    sortAndFilterParams.getPriorities(), sortAndFilterParams.getStatuses(), sortAndFilterParams.getDeadlines());
            tasks = mainService.getTasksByFilterParameters(account,
                    getPageable(count, size, page),
                    sortAndFilterParams.getPriorities(), sortAndFilterParams.getStatuses(), sortAndFilterParams.getDeadlines());
        }

        long pagesCount = count % 5 == 0 ? count / 5 : count / 5 + 1;
        String sortByToDisplay = sortAndFilterParams.getSortByToDisplay();
        String emptyListMessage = sortAndFilterParams.getEmptyListMessage(count);
        String greetingsMessage = getGreetingsMessage(currentAccount.getAccount());
        boolean setAddButtonPulse = emptyListMessage.equals("You don`t have tasks yet");
        //filling in the model
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

        return "index";
    }

    //if task`s deadline > DateHelper.today() sets it`s status to EXPIRED
    private void checkDeadlines() {
        mainService.checkDeadlines(currentAccount.getAccount());
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

    @GetMapping("/delete")
    public void delete(@RequestParam Long id, @RequestParam Integer page,
                       @RequestParam(required = false) String sortBy,
                       @RequestParam(required = false) String order,
                       HttpServletResponse response) throws IOException {

        mainService.deleteTaskById(id);
        page = checkPageParam(page);
        response.sendRedirect("/?page=" + page + "&sortBy=" + sortBy + "&sortOrder=" + order);
    }

    @GetMapping("/mark-as-done")
    public void markAsDone(@RequestParam Long id, @RequestParam Integer page,
                           HttpServletResponse response) throws IOException {
        mainService.markTaskAsDoneById(id);
        page = checkPageParam(page);
        response.sendRedirect("/?page=" + page);
    }

    @GetMapping("/retry")
    public String showRestartTaskForm(@RequestParam Long taskId, Model model) {
        TaskDTO taskDTO = mainService.findTaskById(taskId).toDTO();
        //setting deadline to null just to not display it in template
        taskDTO.setDeadline(null);
        model.addAttribute("taskDTO", taskDTO);
        TaskPriority taskPriority = TaskPriority.valueOf(taskDTO.getPriority());
        //The attributes below are added to model because I want to
        // display priority badge at right-top corner of card.
        //Badge`s color depends on TaskPriority,therefore we need to pass css class to template
        //to set badge`s color and title dynamically
        model.addAttribute("priority", taskPriority.getValueToTemplate());
        model.addAttribute("priorityBadgeColor", taskPriority.getCssClass());
        return "restart-task";

    }

    @PostMapping("/retry")
    public String restartTask(@Valid @ModelAttribute("taskDTO") TaskDTO taskDTO,
                              BindingResult bindingResult) {
        //here we validating the taskDTO with DeadlineValidator which will check only deadline input
        deadlineValidator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "restart-task";
        } else {
            //getting Task by id and updating it`s deadline
            mainService.retry(taskDTO.getId(), taskDTO.getDeadlineDate());
            return "redirect:/";
        }
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
        currentAccount.getAccount().setHasWatchedGreetingsMessage(true);
        mainService.saveAccount(currentAccount.getAccount());
        response.sendRedirect(redirectEndpoint);
    }

    //sets Account to CurrentSession instance
    private void loadAccountInfo(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (currentAccount.getAccount() == null) {
            if (principal instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                AccountDTO accountDTO = new AccountDTO();
                Account account = accountDTO.getAccount(token);
                currentAccount.setAccount(mainService.findAccountBySocialId(account.getSocialId()));
            } else {
                User user = (User) authentication.getPrincipal();
                currentAccount.setAccount(mainService.findAccountByEmail(user.getUsername()));
            }
        }
    }

    //check whether page parameter is >=0
    private Integer checkPageParam(Integer page) {
        if (page < 0) {
            page = 0;
        }
        return page;
    }

    private Pageable getPageable(long count, int size, int page) {
        Sort.Direction direction = sortAndFilterParams.getSortOrder()
                .equalsIgnoreCase("Ascending") ? Sort.Direction.ASC : Sort.Direction.DESC;
        long pagesCount = count % 5 == 0 ? count / 5 : count / 5 + 1;
        if (page > pagesCount - 1) {
            page = (int) pagesCount - 1;
        }
        return PageRequest.of(checkPageParam(page), size, Sort.by(direction, sortAndFilterParams.getSortBy()));
    }

    //returns hardcoded greetings message
    private String getGreetingsMessage(Account account) {
        if (!account.hasWatchedGreetingsMessage()) {
            return "Greetings," + account.getUserName() + ".We are glad to see you here." +
                    "By default SMS-notifications are disabled.Check out \"Settings\" and specify your phone number" +
                    " if you want to receive notifications about your tasks." +
                    "Once you enable SMS-notifications you`ll be able to set notification time for each task individually." +
                    "Good luck,have fun:)";
        } else {
            return null;
        }
    }
}
