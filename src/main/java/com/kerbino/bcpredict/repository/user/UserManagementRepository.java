package com.kerbino.bcpredict.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kerbino.bcpredict.entity.user.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepository extends JpaRepository<UserEntity, Long> {
}
