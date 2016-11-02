package com.harris.carolyn.lab302.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.harris.carolyn.lab302.beans.UserProperty;

@Repository
public interface UserPropertyRepository extends CrudRepository<UserProperty, Long> {

}
