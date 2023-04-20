package com.example.springsecurityapp.services;

import com.example.springsecurityapp.models.Person;
import com.example.springsecurityapp.repositories.PersonRepository;
import com.example.springsecurityapp.security.PersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Указываем что класс будет являться сервисом
public class PersonDetailsService implements UserDetailsService { // Этот класс будет взаимодействовать с PersonDetails
    /* Данный сервис будет иметь отличия от Сервисов которые мы делали ранее  т.к. сервис должен получать пользователя от аутентификации и работать с этой аутентификацией поэтому имплиментируем UserDetailsService и реализуем его метод. Этот интерфейс позволяет раотать с объектом аутентификацйии он сравнивает логин и при корректном логине из объекта аутентификации возвращает объект пользователя */
    private final PersonRepository personRepository; // Поле будет хранить объект personRepository

    public PersonDetailsService(PersonRepository personRepository) { // Конструктор для внедрения объекта personRepository
        this.personRepository = personRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Данный метод позволяет найти пользователя по username которое поступает как аргумент
        /* В метод приходит логин пользователя, мы подставляем его в другой метод findByLogin() из PersonRepository и находим (или не находим) конкретного пользователя */
        Optional<Person> person = personRepository.findByLogin(username);
        if(person.isEmpty()) { // Проверяем найден ли пользователь с помощью метода isEmpty()
            // Если пользователь не был найден:
            throw new UsernameNotFoundException("Пользователь не найден"); /* Если пользователь не найден выбрасываем исключение. Данное исключение будет поймано Spring Security и будет им выведено на страницу */
        }
        // Если пользователь найден.
        return new PersonDetails(person.get()); /* найденную запись с помощью конструктора PersonDetails из класса PersonDetails мы засовываем методом get() в поле класса PersonDetails person */
    }
}
