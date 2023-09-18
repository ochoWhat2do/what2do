package com.ocho.what2do.userpassword.entity;

import com.ocho.what2do.common.entity.Timestamped;
import com.ocho.what2do.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_password")
public class UserPassword extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserPassword(String password, User user) {
        this.password = password;
        this.user = user;
    }
}