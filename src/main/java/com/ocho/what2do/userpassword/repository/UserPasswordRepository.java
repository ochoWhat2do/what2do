package com.ocho.what2do.userpassword.repository;

import com.ocho.what2do.userpassword.entity.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPasswordRepository extends JpaRepository<UserPassword, Long> {
    @Query("select up from UserPassword up where up.user.id = :id order by up.createdAt desc limit 3")
    List<UserPassword> get3RecentPasswords(@Param("id") Long id);

    void deleteAllByUser_Id(Long id);
}
