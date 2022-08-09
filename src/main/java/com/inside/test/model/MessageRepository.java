package com.inside.test.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository  extends CrudRepository<Message, Integer> {
    /**
     * Get N messages from a user by his id
     * @param userId user id to search
     * @param pageable range from 0 to N
     * @return object with found messages
     */
    Page<Message> findAllByUserIdOrderByCreateDateDesc(int userId, Pageable pageable);
}
