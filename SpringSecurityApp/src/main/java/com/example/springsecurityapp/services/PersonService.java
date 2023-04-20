package com.example.springsecurityapp.services;

import com.example.springsecurityapp.models.Person;
import com.example.springsecurityapp.repositories.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;


    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }
// Метод проверяет логин пользователя(не должно быть двух одинаковых логинов)
    public Person findByLogin(Person person){
        Optional<Person> person_db = personRepository.findByLogin(person.getLogin());
        return person_db.orElse(null);
    }
//    Метод добавляет нового пользователя в БД получая объект для регистрации из метода resultRegistration() bp MineController
@Transactional //org.springframework.transaction.annotation.Transactional; т.к. идет изменение состояния БД
    public  void registr(Person person){ // Принимаем объект пользователя которого надо зарегистрировать
        person.setPassword(passwordEncoder.encode(person.getPassword())); /* Устанавливаем пользователю новый пароль.
        Обращаемся к passwordEncoder и вызываем метод encode() этот метод с помощью функции Бикрипт преобразует
        обычный пароль введенный пользователем в хэш код */
        person.setRole("ROLE_USER"); // роль пользователя по умолчанию
        personRepository.save(person);
    }
}
