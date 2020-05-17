package com.hvs.diploma.controllers;

import com.hvs.diploma.dto.AccountDTO;
import com.hvs.diploma.dto.TaskDTO;
import com.hvs.diploma.entities.Account;
import com.hvs.diploma.entities.Task;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import com.hvs.diploma.services.MainService;
import com.hvs.diploma.util.DateHelper;
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
import java.sql.Timestamp;
import java.util.List;

@Controller
@Scope(value = "session")
public class TasksController {
    org.slf4j.Logger logger = LoggerFactory.getLogger(TasksController.class);
    private final AddTaskValidator validator;
    private final MainService mainService;
    private String testSessionScope = "Just created";

    private Account currentUser;
    private String sortBy = "deadline";
    private String sortOrder = "Ascending";
    private TaskPriority[] priorityFilterParams;
    private TaskStatus[] statusFilterParams;
    private Timestamp[] deadlineFilterParams;

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
            logger.warn("DTO values below:");
            logger.warn("Deadline " + taskDTO.getDeadline());
            logger.warn("TaskDescription :" + taskDTO.getDescription());
            logger.warn("Priority :" + taskDTO.getPriority());
            logger.warn("///////////////");
            logger.warn("Entity values below:");
            Task task = taskDTO.buildTaskInstance();
            logger.warn("Description: " + task.getTaskDescription());
            logger.warn("Deadline: " + task.getDeadline().toString());
            logger.warn("Priority: " + task.getPriority());
            logger.warn("Priority value :" + task.getPriorityValue());
            logger.warn("Status :" + task.getStatus());
            Account owner = mainService.findAccountByEmail(principal.getName());
            if (owner == null) {
                owner = mainService.findAccountBySocialId(principal.getName());
            }
            task.setOwner(owner);
            logger.warn("Owner :" + principal.toString());
            mainService.saveTask(task);
            return "redirect:/";
        }
    }

    @GetMapping("/")
    public String getHomePage(Model model, Principal principal,
                              @RequestParam(required = false, defaultValue = "0") Integer page) {
        Account userAccount = loadAccountInfo(principal);
        int size = 5;
        long count;
        page = checkPageParam(page);
        List<Task> tasks;
        if (priorityFilterParams == null && statusFilterParams == null && deadlineFilterParams == null) {
            count = mainService.countTasksByStatusIsNot(userAccount, TaskStatus.DONE);
            tasks = mainService.getAllUndoneTasksForAccount(userAccount, getPageable(count, size, page, sortBy, sortOrder));
        } else {
            count = mainService.countTasksByFilterParams(userAccount, priorityFilterParams, statusFilterParams, deadlineFilterParams);
            tasks = mainService.getTasksByFilterParameters(userAccount,
                    getPageable(count, size, page, sortBy, sortOrder), priorityFilterParams, statusFilterParams, deadlineFilterParams);
        }
        long pagesCount = count % 5 == 0 ? count / 5 : count / 5 + 1;
        sortBy = sortBy.equalsIgnoreCase("priorityValue") ? "Priority" : "Deadline";
        String emptyListMessage = mainService.countTasksByOwner(userAccount) == 0 ? "You don`t have tasks yet" : "Tasks not found";
        boolean setAddButtonPulse = emptyListMessage.equals("You don`t have tasks yet");
        model.addAttribute("setPulse", setAddButtonPulse);
        model.addAttribute("message", emptyListMessage);
        model.addAttribute("today", DateHelper.today());
        model.addAttribute("tomorrow", DateHelper.tomorrow());
        model.addAttribute("thisWeek", DateHelper.lastDayOfCurrentWeek());
        model.addAttribute("pagesCount", pagesCount);
        model.addAttribute("tasksCount", count);
        model.addAttribute("account", userAccount);
        model.addAttribute("page", page);
        model.addAttribute("tasks", tasks);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("order", sortOrder);
        model.addAttribute("priorities", priorityFilterParams);
        model.addAttribute("statuses", statusFilterParams);
        model.addAttribute("deadlines", deadlineFilterParams);
        return "index";
    }

    @PostMapping("/filter")
    public void filter(HttpServletResponse response,
                       @RequestParam(required = false) TaskPriority[] priority,
                       @RequestParam(required = false) TaskStatus[] status,
                       @RequestParam(required = false) Timestamp[] date) throws IOException {
        priorityFilterParams = priority;
        statusFilterParams = status;
        deadlineFilterParams = date;
        response.sendRedirect("/");
    }

    @GetMapping("/reset")
    public void reset(HttpServletResponse response) throws IOException {
        priorityFilterParams = null;
        statusFilterParams = null;
        deadlineFilterParams = null;
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
                           @RequestParam(required = false) String sortBy,
                           @RequestParam(required = false) String order,
                           HttpServletResponse response) throws IOException {
        mainService.markTaskAsDoneById(id);
        page = checkPageParam(page);
        response.sendRedirect("/?page=" + page + "&sortBy=" + sortBy + "&sortOrder=" + order);
    }

    @GetMapping("/sort")
    public String sort(@RequestParam String by, @RequestParam String order,
                       @RequestParam(required = false, defaultValue = "0") Integer page) {
        sortBy = by;
        sortOrder = order;
        return "redirect:/";
    }

    private Integer checkPageParam(Integer page) {
        if (page < 0) {
            page = 0;
        }
        return page;
    }

    private Pageable getPageable(long count, int size, int page, String sortBy, String order) {
        Sort.Direction direction = order.equalsIgnoreCase("Ascending") ? Sort.Direction.ASC : Sort.Direction.DESC;
        long pagesCount = count % 5 == 0 ? count / 5 : count / 5 + 1;
        if (page > pagesCount - 1) {
            page = (int) pagesCount - 1;
        }
        return PageRequest.of(checkPageParam(page), size, Sort.by(direction, sortBy));
    }

    private Account loadAccountInfo(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account userAccount;
        if (currentUser == null) {
            if (principal instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                AccountDTO accountDTO = new AccountDTO();
                Account account = accountDTO.getAccount(token);
                userAccount = mainService.findAccountBySocialId(account.getSocialId());
            } else {
                User user = (User) authentication.getPrincipal();
                userAccount = mainService.findAccountByEmail(user.getUsername());
            }
            return userAccount;
        } else {
            return currentUser;
        }
//        return userAccount;
    }
}
