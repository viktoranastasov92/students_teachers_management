package com.test.example.service.impl;

import com.test.example.model.Group;
import com.test.example.repository.GroupRepository;
import com.test.example.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public Group saveGroup(Group group) {
        if (group == null) {
            return null;
        }

        Optional<Group> existingGroupOpt = groupRepository.findByName(group.getName());
        if (existingGroupOpt.isEmpty()) {
            return groupRepository.save(group);
        }

        return existingGroupOpt.get();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Group> findGroupBy(String groupName) {
        return groupRepository.findByName(groupName);
    }
}
