package com.inside.test.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    private String name;
    private String password;

    /**
     * messages from user
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    List<Message> messages = new ArrayList<>();

    /**
     * get related messages from user
     * @return will return a LIST with sorted DESC messages of the user
     */
    public List<Message> getMessagesOrderedDesc() {
        messages.sort(new Comparator<Message>() {
            @Override
            public int compare(Message lhs, Message rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getCreateDate().getTime() > rhs.getCreateDate().getTime()
                        ? -1 : (lhs.getCreateDate().getTime() < rhs.getCreateDate().getTime())
                            ? 1 : 0;
            }
        });
        return messages;
    }

    /**
     * set message for user
     * @param messages List<Message>
     */
    public void setMessages(List<Message> messages){
        this.messages = messages;
    }

    /**
     * get given user password
     * @return password (string)
     */
    public String getPassword() {
        byte[] decodedBytes = Base64.getDecoder().decode(password);
        return new String(decodedBytes);
    }

    /**
     * set password for user
     * @param password password (string)
     */
    public void setPassword(
            String password
    ) {
        this.password = Base64.getEncoder().encodeToString(password.getBytes());
    }

    /**
     * get user id
     * @return id (int)
     */
    public Integer getId() {
        return id;
    }

    /**
     * set id for user
     * @param id set id (int)
     */
    public void setId(
            Integer id
    ) {
        this.id = id;
    }

    /**
     * get user name
     * @return user name (string)
     */
    public String getName() {
        return name;
    }

    /**
     * set user name
     * @param name name (string)
     */
    public void setName(
            String name
    ) {
        this.name = name;
    }

}
