package zw.co.cassavasmartech.auctionsystem.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import zw.co.cassavasmartech.auctionsystem.common.enums.UserRole;
import zw.co.cassavasmartech.auctionsystem.forms.PersonForm;
import zw.co.cassavasmartech.auctionsystem.model.Person;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.CategoryService;
import zw.co.cassavasmartech.auctionsystem.service.ifaces.PersonService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by alfred on 14 September 2020
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;
    private final CategoryService categoryService;

    @GetMapping
    public String indexPage(@AuthenticationPrincipal Person person, Model model) {
        model.addAttribute("authUser", person);
        return "admin/index";
    }

    @GetMapping("/users")
    public String usersPage(@AuthenticationPrincipal Person person,
                            @RequestParam(required = false, value = "searchPhrase")
                                    String searchPhrase, Model model) {
        model.addAttribute("authUser", person);
        if(StringUtils.isEmpty(searchPhrase)) {
            model.addAttribute("people", personService.findAll());
        } else {
            model.addAttribute("people", personService.searchPersons(searchPhrase));
        }
        model.addAttribute("df", DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String newUserPage(@AuthenticationPrincipal Person person, Model model) {
        return configureCreatePage(person, model, new PersonForm(), "");
    }

    @PostMapping("/users")
    public String createUser(@AuthenticationPrincipal Person person,@Valid PersonForm personForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        log.info("Got sign up request");

        @NotNull(message = "Person role is required") UserRole personRole = personForm.getUserRole();
        if (personRole == null) {
            model.addAttribute("action", "register");
            redirectAttributes.addFlashAttribute("error", "Could not create person");
            return "redirect:/admin/users";
        }

        if (bindingResult.hasErrors()) {
            return configureCreatePage(person, model, personForm, "Bad Request");
        }

        final Person newPerson = personForm.getPerson(passwordEncoder);
        newPerson.setCreatedBy("SELF");
        newPerson.setLastUpdatedBy("SELF");

        final Optional<Person> personOptional = personService.createPerson(newPerson);
        log.info("Creation response successful? => {}", personOptional.isPresent());
        if (personOptional.isPresent()) {
            redirectAttributes.addFlashAttribute("info", "Registered successfully, please sign in");
            return "redirect:/admin/users";
        } else {
            return configureCreatePage(person, model, personForm, "Could not register person, try again later");
        }
    }

    private String configureCreatePage(Person person, Model model, PersonForm personForm, String errors) {
        model.addAttribute("authUser", person);
        model.addAttribute("personForm", personForm);
        model.addAttribute("errors", errors);
        model.addAttribute("personRoles", Arrays.asList(UserRole.values()));
        return "admin/create_user";
    }
}
