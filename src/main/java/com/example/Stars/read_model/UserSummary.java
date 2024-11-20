package com.example.Stars.read_model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    @JsonIgnore
    @ManyToMany(mappedBy = "following")
    private Set<UserSummary> followers = new HashSet<>();

    public UserSummary(UUID userId, String username, String email, String password, boolean active) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = active;
    }

    @Override
    public String toString() {
        return "UserSummary{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active+
                '}';
    }
}
