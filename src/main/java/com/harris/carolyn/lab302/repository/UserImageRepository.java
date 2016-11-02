package com.harris.carolyn.lab302.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.harris.carolyn.lab302.beans.UserImage;

@Repository
public interface UserImageRepository extends CrudRepository<UserImage, Long> {

	UserImage findByUserId(Long id);
}
