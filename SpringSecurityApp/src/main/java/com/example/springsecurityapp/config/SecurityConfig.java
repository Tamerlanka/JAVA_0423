package com.example.springsecurityapp.config;

import com.example.springsecurityapp.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Переопределяем аннотацию, убрали наследование
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Bean
    public PasswordEncoder getPasswordEncoder(){ // Включаем шифрование паролей с помощью БиКрипт
        return new BCryptPasswordEncoder();
    }


    // Создаем метод где определяем конфигурацию
    @Bean
    public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception { // HttpSecurity http - объект аутентификации с помощью которого будет определяться вся безопасность приложения.
        //Конфигурируем работу Spring Security. Сначала идут настройки доступа.
    http
            .authorizeHttpRequests()// указываем что по умолчанию все страницы должны быть защищены аутентификацией. т.к. при переопределении базовой аутентификации (строка 15 кода) все страницы становятся доступными
//            Метод ниже изменяем в связи с организацие разграничения ролей
            .requestMatchers("/admin").hasRole("ADMIN") // ROLE_ADMIN писать не надо, достаточно ADMIN. Здесь указано что /admin джоступна только с ролью ADMIN.
            .requestMatchers("/authentication", "/error", "/registration", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**").permitAll()// Указываем какие станицы доступны ВСЕМ пользователям
            .anyRequest().hasAnyRole("USER", "ADMIN") // Остальные страницы доступны как администратору так и любому авторизованному пользователю.
//            Метод ниже комментируем в связи с организацие разграничения ролей
//            .anyRequest().authenticated()// Указываем что для всех остальных страниц необходимо вызвать метод authenticated() который открывает форму аутентификации(если не нашлась сессия с формой аутентификации пользователя).
            .and()// Указываем что дальше будет новый блок в котором настраивается аутентификация и соединяем ее с настройкой досупа которые производились до метода and()
            .formLogin().loginPage("/authentication")// Переобпределяем базовую страницу аутентификации. Указываем какой url запрос будет отправляться при заходе на защищенные страницы.
            .loginProcessingUrl("/process_login")// Указываем на какой url адресс будут отправляться данные из формы. Это позволит не создавать метод в контроллере для обработки данных из формы. Мы задали url, который используется по умолчанию для обработки формы аутентификации посредством Spring Security/ Spring Security будет ждать объект с формы аутентификации а затем сверять логин и пароль с данными БД.
            .defaultSuccessUrl("/index", true)// Указываем на какой url нужно направить пользователя после успешной аутентификации. Второй аргумент true чтобы перенаправленние шло в любом случае после успешной аутентификации.
            .failureUrl("/authentication?error")// Указываем на какой url нужно направить пользователя после проваленной аутентификации. В запрос будет передан объект error, который будет обработан в шаблоне странице в виде надписи "Неверный логин или пароль"
            .and()// Указываем что дальше будет новый блок
            .logout() // метод для логауте (удаление объекта пользователя из сессии) блок настройки выхода из аккаунта
            .logoutUrl("/logout") // Указываем по какому url будет происходить логаут т.е. при обращении на этот шаблон (страницу) будет запущена процедура которая по id в куке пользователя удаляет его сессию и удаляет саму куку в браузере
            .logoutSuccessUrl("/authentication"); // Указываем на какой url будет происходить переадресация после выхода из ЛК
            /* Нам не нужно реализовывать логику логаута за нас это делает Spring Security */


        return http.build();
    }

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception { //
        authenticationManagerBuilder.userDetailsService(personDetailsService)
                .passwordEncoder(getPasswordEncoder()); // Указываем что пароли надо хэшировать по БиКрипт
    }
}

/* Вид Хэшированного пароля : $2a$10$tltCWvHFyWwJqsxcKXC1reNW/EfycDxwIYD9NqoktZ2HerSYqSSiy где;
 $2a - указывает какой хэш алгоритм использовался для хэширования пароля
 $10 - стоимость чтения хэша (сколько мощности требуется для вычисления) т.е. сложность хэша
 $tltCWvHFyWwJqsxcKXC1re - соль  (добавка к паролю)
 NW/EfycDxwIYD9NqoktZ2HerSYqSSiy - сам пароль
 */