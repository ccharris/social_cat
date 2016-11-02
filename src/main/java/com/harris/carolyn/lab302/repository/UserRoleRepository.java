package com.harris.carolyn.lab302.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.harris.carolyn.lab302.beans.UserRole;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

}