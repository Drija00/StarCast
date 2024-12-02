package com.example.Stars.queries.query;

import com.example.Stars.apis.api.UserLogingEvent;
import com.example.Stars.apis.api.UserRegisteredEvent;
import com.example.Stars.queries.read_model.UserSummary;
import com.example.Stars.write_model.User;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

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

    @EventHandler
    public void on(UserLogingEvent evt){
        UserSummary user = repository.findById(evt.getUserId()).orElse(null);
        if(user != null){
            user.setActive(evt.getActive());
            repository.save(user);
        }else{
            throw new RuntimeException("User not found");
        }
    }

    @QueryHandler
    public List<UserSummary> on(GetUsersQuery qry) {
        try {
                List<UserSummary> users = repository.findAll();
                System.out.println(users.toArray());
                return users;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to fetch users", e);
        }
    }

    @QueryHandler
    public UserSummary on(GetUserForRegistrationQuery qry){
            return repository.findByUsernameOrEmail(qry.getUsername(), qry.getEmail()).orElse(null);
    }

    @QueryHandler
    public UserSummary on(GetUserByIdQuery qry){
        return repository.findById(qry.getUserId()).orElse(null);
    }

    @QueryHandler
    public UserSummary on(GetUserByUsernameQuery qry){
        return repository.findByUsername(qry.getUsername()).orElse(null);
    }

    @QueryHandler
    public UserSummary on(GetLoginUserQuery qry) {
        try {
            return repository.findByUsernameAndPassword(qry.getUsername(), qry.getPassword()).orElseThrow(() -> new RuntimeException("User not found"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to fetch a user", e);
        }
    }

}
