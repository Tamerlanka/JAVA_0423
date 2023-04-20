package com.example.springsecurityapp.models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Entity // Указываем, что данный класс является моделью
@Table(name="Person") // Указываем, что будем подключатся к существующей таблице
public class Person {
    @Id // import jakarta.persistence.Id;
    @Column(name="id") // с каким полем связываем
    @GeneratedValue(strategy = GenerationType.IDENTITY) // тип генерации значения поля
    private int id;
    @NotEmpty(message = "Логин не может быть пустым") // Валидация на пустоту
    @Size(min = 5, max = 100, message = "Логин от 5 до 100 символов") // Валидация на размер логина
    @Column(name="login") // с каким полем связываем
    private String login;
    @NotEmpty(message = "Поле password не может быть пустым") // Валидация на пустоту
    @Column(name="password") // с каким полем связываем
//    @Pattern() - валидация пароля на сложность число символов и т.п. Сейчас пока не реализуем
    private String password;
    @Column(name="role") // Валидации нет так как значение будет определяться не пользователем. Будет 2 роли user - для всех зарегистрированных, и admin.
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && Objects.equals(login, person.login) && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() { // Метод преобразует объект класса в чило и эти числа можно сравнивать обратно преобразовать из числа в объект фактически невозможно
        return Objects.hash(id, login, password);
    }
}
