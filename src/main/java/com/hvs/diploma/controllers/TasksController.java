package com.hvs.diploma.controllers;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskDeadlines;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.pojo.SortAndFilterParams;
import com.hvs.diploma.services.MainService;
import com.hvs.diploma.validators.AddTaskValidator;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
@Scope(value = "session")
public class TasksController {
    org.slf4j.Logger logger = LoggerFactory.getLogger(TasksController.class);
    private final AddTaskValidator validator;
    private final MainService mainService;
    private boolean isTasksDeadlinesChecked;
    private Account currentUser;
    private SortAndFilterParams sortAndFilterParams = new SortAndFilterParams();
//    private final TurboSmsMessageRepository smsMessageRepository;

    @Autowired
    public TasksController(AddTaskValidator validator, MainService mainService) {
        this.validator = validator;
        this.mainService = mainService;
    }

    @GetMapping("/add-task")
    public String getAddNewTaskForm(Model model) {
        model.addAttribute("taskDTO", new TaskDTO());
        return "/add-task";
    }

    @PostMapping("/add-task")
    public String addTask(@Valid @ModelAttribute("taskDTO") TaskDTO taskDTO,
                          BindingResult bindingResult,
                          Principal principal,
                          @RequestParam String group1) {
        validator.validate(taskDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/add-task";
        } else {
            taskDTO.setPriority(group1);
            Task task = taskDTO.buildTaskInstance();
            Account owner = mainService.findAccountByEmail(principal.getName());
            if (owner == null) {
                owner = mainService.findAccountBySocialId(principal.getName());
            }
            task.setOwner(owner);
            mainService.saveTask(task);
            return "redirect:/";
        }
    }

    @GetMapping("/")
    public String getHomePage(Model model, Principal principal,
                              @RequestParam(required = false, defaultValue = "0") Integer page) {
        loadAccountInfo(principal);
        int size = 5;
        long count;
        page = checkPageParam(page);
        List<Task> tasks;
        //looking for tasks with expired deadlines and updating their status
        if (!isTasksDeadlinesChecked) {
            checkDeadlines(currentUser);
        }
        logger.warn(sortAndFilterParams.toString());
        //checking filter parameters and getting tasks from db
        if (!sortAndFilterParams.isFilterParamsSpecified()) {
            count = mainService.countTasksByStatusIsNot(currentUser, TaskStatus.DONE);
            tasks = mainService.getAllUndoneTasksForAccount(currentUser, getPageable(count, size, page));
        } else {
            count = mainService.countTasksByFilterParams(currentUser,
                    sortAndFilterParams.getPriorities(), sortAndFilterParams.getStatuses(), sortAndFilterParams.getDeadlines());
            tasks = mainService.getTasksByFilterParameters(currentUser,
                    getPageable(count, size, page),
                    sortAndFilterParams.getPriorities(), sortAndFilterParams.getStatuses(), sortAndFilterParams.getDeadlines());
        }

        long pagesCount = count % 5 == 0 ? count / 5 : count / 5 + 1;
        String sortByToDisplay = sortAndFilterParams.getSortByToDisplay();
        String emptyListMessage = sortAndFilterParams.getEmptyListMessage(count);
        String greetingsMessage = getGreetingsMessage(currentUser);
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
        model.addAttribute("account", currentUser);
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
    private void checkDeadlines(Account userAccount) {
        mainService.checkDeadlines(userAccount);
        isTasksDeadlinesChecked = true;
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
        //set deadline to null just to not display it in template
        taskDTO.setDeadline(null);
        logger.warn("taskDTO: " + taskDTO.toString());
        model.addAttribute("taskDTO", taskDTO);
        model.addAttribute("restartMode", true);
        model.addAttribute("action", "/restart-task");
        model.addAttribute("title", "Restart task");
        return "/add-task";

    }

    @PostMapping("/restart-task")
    public void restartTask(@ModelAttribute("taskDTO") TaskDTO taskDTO, HttpServletResponse response) throws IOException {
        mainService.retry(taskDTO.getId(), taskDTO.getDeadlineDate());
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

    @GetMapping("/modal-dismiss")
    public void dismissGreetingsModal(HttpServletResponse response) throws IOException {
        currentUser.setHasWatchedGreetingsMessage(true);
        mainService.saveAccount(currentUser);
        response.sendRedirect("/");
    }


    private void loadAccountInfo(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            if (principal instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                AccountDTO accountDTO = new AccountDTO();
                Account account = accountDTO.getAccount(token);
                currentUser = mainService.findAccountBySocialId(account.getSocialId());
            } else {
                User user = (User) authentication.getPrincipal();
                currentUser = mainService.findAccountByEmail(user.getUsername());
            }
        }
    }

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
