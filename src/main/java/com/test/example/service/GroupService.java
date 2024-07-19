package com.test.example.service;

import com.test.example.model.Group;

import java.util.Optional;

public interface GroupService {
    Group saveGroup(Group group);

    Optional<Group> findGroupBy(String groupName);
}
