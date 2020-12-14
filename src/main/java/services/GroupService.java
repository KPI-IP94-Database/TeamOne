package services;

import domain.Group;
import domain.User;
import exceptions.PermissionException;
import storage.GroupRepository;

import java.util.List;
import java.util.Optional;

import static exceptions.EntityExistException.groupAlreadyExists;
import static exceptions.EntityNotExistException.groupIsNotExist;
import static exceptions.PermissionException.notEnoughPermission;

public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public void saveNewGroup(User caller, Group group) throws PermissionException, IllegalArgumentException {
        if (!caller.isAdmin()) {
            throw notEnoughPermission(caller);
        }
        if (groupRepository.existsById(group.getId())) {
            throw groupAlreadyExists(group);
        }
        group.setId(null);
        groupRepository.saveNewEntity(group);
    }

    public void editGroup(User caller, Group group) throws PermissionException {
        if (!caller.isAdmin()) {
            throw notEnoughPermission(caller);
        }
        if (!groupRepository.existsById(group.getId())) {
            throw groupIsNotExist(group);
        }
        groupRepository.update(group);
    }

    public void deleteGroup(User caller, Integer groupId) throws PermissionException {
        if (!caller.isAdmin()) {
            throw notEnoughPermission(caller);
        }
        if (!groupRepository.existsById(groupId)) {
            throw groupIsNotExist(groupId);
        }
        groupRepository.deleteById(groupId);
    }

    public Optional<Group> findGroupByName(String groupName) {
        return groupRepository.findGroupByName(groupName);
    }

    public List<String> getGroupMembers(Integer id) {
        return groupRepository.findById(id).orElseThrow(() -> groupIsNotExist(id)).getMemberLogins();
    }
}
