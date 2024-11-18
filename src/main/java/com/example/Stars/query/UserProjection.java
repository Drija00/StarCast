package com.example.Stars.query;

import com.example.Stars.api.UserRegisteredEvent;
import com.example.Stars.read_model.UserSummary;
import com.example.Stars.write_model.User;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.util.List;

@Component
public class UserProjection {
private final UserSummaryRepository repository;
    private final User user;

    public UserProjection(UserSummaryRepository repository, User user) {this.repository = repository;
        this.user = user;
    }


    @EventHandler
    public void on(UserRegisteredEvent event) {
        UserSummary user = new UserSummary(
                event.getUserId(),
                event.getUsername(),
                event.getEmail(),
                event.getPassword(),
                event.getActive()
        );
        repository.save(user);
    }
    @QueryHandler
    public List<UserSummary> on(GetUsersQuery qry) {
        try {
                List<UserSummary> users = repository.findAll();
                System.out.println(users.toArray());
                return users;
        } catch (Exception e) {
            // Log the exception for further inspection
            System.out.println(e.getMessage());
            // Handle the exception, possibly returning an empty list or throwing a custom exception
            throw new RuntimeException("Failed to fetch users", e);
        }
    }

    @QueryHandler
    public UserSummary on(GetUserForRegistrationQuery qry){
            return repository.findByUsernameOrEmail(qry.getUsername(), qry.getEmail()).orElse(null);
    }

    @QueryHandler
    public UserSummary on(GetLoginUserQuery qry) {
        try {
            return repository.findByUsernameAndPassword(qry.getUsername(), qry.getPassword()).orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {
            // Log the exception for further inspection
            System.out.println(e.getMessage());
            // Handle the exception, possibly returning an empty list or throwing a custom exception
            throw new RuntimeException("Failed to fetch a user", e);
        }
    }

}
