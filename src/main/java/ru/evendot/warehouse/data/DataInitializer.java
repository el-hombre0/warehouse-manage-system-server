package ru.evendot.warehouse.data;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.evendot.warehouse.model.User;
import ru.evendot.warehouse.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultUserIfNotExists();
    }

    private void createDefaultUserIfNotExists() {
        User user = new User();
        user.setFirstname("Иван");
        user.setMiddlename("Иванов");
        user.setLastname("Иванович");
        user.setEmail("ivanov@mail.ru");
        user.setPassword("16Tgd)%gG<31");
        userRepository.save(user);
        System.out.println("Default user " + user.getEmail() + " was created!");
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
