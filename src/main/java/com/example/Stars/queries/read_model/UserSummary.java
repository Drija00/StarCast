package com.example.Stars.queries.read_model;

import com.example.Stars.write_model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Data
@Table(name = "Users")
@NoArgsConstructor
@Getter
@Setter
public class UserSummary {

    @Id
    @Column(name = "user_id")
    private UUID userId;
    private String username;
    private String email;
    private String password;
    private boolean active = false;
    private String firstName;
    private String lastName;
    private LocalDateTime joinDate;
    private String description;
    private String profileImage;
    private String backgroundImage;

    public UserSummary(UUID userId) {
        this.userId = userId;
    }


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="follows",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "followee_id"))
    @JsonIgnore
    private Set<UserSummary> following = new HashSet<>();

    @ManyToMany(mappedBy = "following",fetch = FetchType.LAZY)
    private Set<UserSummary> followers = new HashSet<>();


    public UserSummary(UUID userId, String username, String email, String password, boolean active, String firstName, String lastName, LocalDateTime joinDate, String profileImage, String backgroundImage, String description) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
        this.firstName = firstName;
        this.lastName = lastName;
        this.joinDate = joinDate;
        this.profileImage = profileImage;
        this.backgroundImage = backgroundImage;
        this.description = description;
    }

    @Override
    public String toString() {
        return "UserSummary{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", joinDate=" + joinDate +
                ", description='" + description + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", backgroundImage='" + backgroundImage + '\'' +
                ", following=" + following +
                ", followers=" + followers +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSummary that = (UserSummary) o;
        return active == that.active && Objects.equals(userId, that.userId) && Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, email, password, active, firstName, lastName);
    }
}
