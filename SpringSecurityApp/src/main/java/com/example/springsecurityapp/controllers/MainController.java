package com.example.springsecurityapp.controllers;

import com.example.springsecurityapp.models.Person;
import com.example.springsecurityapp.security.PersonDetails;
import com.example.springsecurityapp.services.PersonService;
import com.example.springsecurityapp.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    private final PersonValidator personValidator;
    private final PersonService personService;

    public MainController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }

    @GetMapping("/index")
    public String index() {
        // получаем объект аутентификации с помощью SecurityContextHolder обращаемся к контексту и на нем вызываем метод аутентификации. Из сессии текущего пользователя получаем объект, который был положен в данную сессию после аутентификации.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // получаем объект аутентификации из сессии по запросу от пользователя к сессии с которым приходят куки пользователя.
        /* Преобразуем объект аутентификации в объект пользователя */
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getPerson());
        System.out.println("ID пользователя " + personDetails.getPerson().getId());
        System.out.println("Логин пользователя " + personDetails.getPerson().getLogin());
        System.out.println("Пароль пользователя " + personDetails.getPerson().getPassword());
        System.out.println(personDetails);
        return "index";
    }

    // Метод для регистрации нового пользователя 1-й способ работы с моделью аналогичен 2-му способу
//    @GetMapping("/registration")
//    public String registration(Model model){
//        model.addAttribute("person", new Person());
//        return "registration";
//    }

    /* Метод для регистрации нового пользователя 2-й способ работы с моделью аналогично 1-му способу. Метод сам обавит модель в шаблон и вернет шаблон registration */
    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person) { // Spring делает все автоматически, смотрит приходит ли в запросе атрибут person
        // если нет то он его создает.
        return "registration";
    }
    // Метод принимает и обрабатывает форму регистрации. Принимает объект с модели и создает объект ошибки
    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult){ /* принимаем из шаблона registration
    атрибут формы (person) указываем что модель должна валидироваться и в случае наличия ошибки они будут положены в объект bindingResult */
        personValidator.validate(person, bindingResult); // через объект personValidator вызываем метод validate() из PersonValidator с атрибутами в виде объекта person из формы и объекта ошибки. Если логин из нового person будет найден в БД validate() вернет ошибку в bindingResult
        if(bindingResult.hasErrors()) {// проверяем наличие ошибки в bindingResult
            return "registration"; // Если ошибки есть возвращаем их в шаблон registration для отображения
        }
        personService.registr(person); // Если ошибки нет, если пройдены все валидации полей класса Person, то предаем объект через personService и метод registr() на регистрацию в БД.
        return "redirect:/index"; // Если пользователь не является аутентифицированным(т.е. впервые прошел регистрацию) редирект на страницу index будет перехвачен и пользователь попадет на страницу ввода логина и пароля, и сможет зарегистрироваться т.к. уже добавлен в БД пользователей.
    }
}
    /* Аутентификация - проверка логина и пароля. Авторизация  - проверка роли и определение доступного функционала В Spring Security роли должны начинаться с ключевого слова role_ это обязательное требование  */
