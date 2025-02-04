package com.kerbino.bcpredict.repository.userRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.kerbino.bcpredict.entity.userEntities.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepository extends JpaRepository<UserEntity, Long> {
}
