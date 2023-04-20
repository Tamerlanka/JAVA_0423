package com.example.springsecurityapp.security;

import com.example.springsecurityapp.models.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// Данный клас отвечает за получение подробной информации о пользователе
public class PersonDetails implements UserDetails { // Название кемелкейсом, первое слово название модели, второе Details

    private final Person person;

    public Person getPerson(){
        return this.person;
    }

    public PersonDetails(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // Данный метод позволяет вернуть роль текущего пользователя. По его роли можно понять какие страницы ему доступны.
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getLogin();
    }
//  Метод позволяет проверить действительность аккаунта
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
//  Метод позволяет проверить является ли аккаунт заблокированным
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
//  Метод позволяет проверить действительность пароля
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
//  Метод позволяет проверить активность аккаунта (акк может быть дезактивирован)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
