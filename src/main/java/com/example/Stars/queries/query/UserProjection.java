package com.example.Stars.queries.query;

import com.example.Stars.DTOs.UserDTO;
import com.example.Stars.apis.api.UserLogingEvent;
import com.example.Stars.apis.api.UserRegisteredEvent;
import com.example.Stars.converter.impl.UserConverter;
import com.example.Stars.queries.read_model.UserSummary;
import com.example.Stars.write_model.User;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("query")
public class UserProjection {
private final UserSummaryRepository repository;
    private final UserConverter userConverter;

    public UserProjection(UserSummaryRepository repository, UserConverter userConverter) {
        this.repository = repository;
        this.userConverter = userConverter;
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
    public List<UserDTO> on(GetUsersQuery qry) {
        try {
                List<UserDTO> users = repository.findAll().stream().map(entity -> userConverter.toDto(entity))
                        .collect(Collectors.toList());;
                System.out.println(users.toArray());
                return users;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to fetch users", e);
        }
    }

    @QueryHandler
    public UserDTO on(GetUserForRegistrationQuery qry){
            UserSummary us = repository.findByUsernameOrEmail(qry.getUsername(), qry.getEmail()).orElse(null);
            return us!=null?userConverter.toDto(us):null;
    }

    @QueryHandler
    public UserDTO on(GetUserByIdQuery qry){
        UserSummary us = repository.findById(qry.getUserId()).orElse(null);
        return us!=null?userConverter.toDto(us):null;
    }

    @QueryHandler
    public UserDTO on(GetUserByUsernameQuery qry){
        UserSummary us = repository.findByUsername(qry.getUsername()).orElse(null);
        return us!=null?userConverter.toDto(us):null;
    }

    @QueryHandler
    public UserDTO on(GetLoginUserQuery qry) {
        try {
            UserSummary us = repository.findByUsernameAndPassword(qry.getUsername(), qry.getPassword()).orElseThrow(() -> new RuntimeException("User not found"));
            return userConverter.toDto(us);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to fetch a user", e);
        }
    }

}
