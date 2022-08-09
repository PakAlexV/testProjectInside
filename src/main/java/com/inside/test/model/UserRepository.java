package com.inside.test.model;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    /**
     * find user by name
     * @param name user name (string)
     * @return found object of type user
     */
    User findByName(String name);
 }
