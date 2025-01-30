package com.kerbino.bcpredict.repository.UserRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kerbino.bcpredict.entity.UserEntities.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepository extends JpaRepository<UserEntity, Long> {
}
