package com.kerbino.bcpredict.entity.userEntities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "UsersLoginData")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String login_name;
    private String login_password;
}
