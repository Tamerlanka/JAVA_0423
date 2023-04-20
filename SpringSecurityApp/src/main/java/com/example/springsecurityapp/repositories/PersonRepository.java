package com.example.springsecurityapp.repositories;

import com.example.springsecurityapp.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
// Это репозиторий, если он существует, под него необходимо создать сервисный слой
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    //    метод который позволит найти запись в сущности Person (таблице Person) по логину
    Optional<Person> findByLogin(String login); /* Optional используется потому что человек может быть найден и тогда он будет положен в объект option а может быть и не найден. Этот метод мы используем в методе  loadUserByUsername() из PersonDetailsService */

}
