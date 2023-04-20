package com.example.springsecurityapp.util;

import com.example.springsecurityapp.models.Person;
import com.example.springsecurityapp.services.PersonService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class PersonValidator implements Validator { //org.springframework.validation.Validator; Внимание!!! Есть несколько Валидаторов. Нужен этот.

    private final PersonService personService;

    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    // в этом методе указывается с какой моделью работает данный валидатор
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    // В этом методе прописывается сама валидация. Мы принимаем из MainController, из метода resultRegistration() объект person (в виде объекта Object который мы даункастим, и объект ошибки (поторый может быть пуст)
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target; // В метод приходит объект Object его надо даункастить до объекта Person
        if (personService.findByLogin(person) != null) {// Если метод вернул объект а не Null - пользователь найден, т.е. такой логин уже есть в БД
            errors.rejectValue("login", "", "Данный логин уже зарегистрирован в системе"); // создаем объект ошибки, данная ошибка будет положена в bindingResult ипопадет в метод resultRegistration() в MineController
        }
    }

}
