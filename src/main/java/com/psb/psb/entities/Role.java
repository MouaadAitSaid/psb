package com.psb.psb.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public Long id() {
        return id;
    }

    public Role setId(Long id) {
        this.id = id;
        return this;
    }

    public String name() {
        return name;
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }

    public Set<User> users() {
        return users;
    }

    public Role setUsers(Set<User> users) {
        this.users = users;
        return this;
    }
}
