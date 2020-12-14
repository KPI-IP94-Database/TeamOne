package services;

import domain.Group;
import domain.User;
import domain.UserType;
import exceptions.EntityExistException;
import exceptions.EntityNotExistException;
import exceptions.PermissionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import storage.GroupRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class GroupServiceTest {

    private GroupService service;
    private GroupRepository mockedRepo;
    private User student;
    private User admin;
    private Group group;

    @Before
    public void setUp() {
        mockedRepo = mock(GroupRepository.class);
        service = new GroupService(mockedRepo);
        student = new User("max", "1234", "Maxim Perevalov", UserType.STUDENT, 0);
        admin = new User("vova", "1234", "Vova Pomidor", UserType.ADMIN, 0);
        group = new Group(1, "IP-94", "max", List.of("Vania", "Oleg", "Vladimir"));
    }

    @Test(expected = PermissionException.class)
    public void saveNewGroupByNotAdmin() throws PermissionException {
        User caller = student;
        service.saveNewGroup(caller, group);
    }

    @Test(expected = EntityExistException.class)
    public void saveAlreadyExistsGroup() throws PermissionException {
        User caller = admin;
        when(mockedRepo.existsById(group.getId())).thenReturn(true);
        service.saveNewGroup(caller, group);
    }

    @Test
    public void saveNewGroup() throws PermissionException {
        User caller = admin;
        when(mockedRepo.existsById(group.getId())).thenReturn(false);
        service.saveNewGroup(caller, group);
        verify(mockedRepo, times(1)).saveNewEntity(group);
    }

    @Test(expected = PermissionException.class)
    public void editGroupByNotAdmin() throws PermissionException {
        User caller = student;
        service.editGroup(caller, group);
    }

    @Test(expected = EntityNotExistException.class)
    public void editGroupNotExistsGroup() throws PermissionException {
        User caller = admin;
        when(mockedRepo.existsById(group.getId())).thenReturn(false);
        service.editGroup(caller, group);
    }

    @Test
    public void editGroup() throws PermissionException {
        User caller = admin;
        when(mockedRepo.existsById(group.getId())).thenReturn(true);
        service.editGroup(caller, group);
        verify(mockedRepo, times(1)).update(group);
    }


    @Test(expected = PermissionException.class)
    public void deleteGroupByNotAdmit() throws PermissionException {
        User caller = student;
        when(mockedRepo.existsById(group.getId())).thenReturn(true);
        service.deleteGroup(caller, group.getId());
    }


    @Test(expected = EntityNotExistException.class)
    public void deleteNotExistsGroup() throws PermissionException {
        User caller = admin;
        when(mockedRepo.existsById(group.getId())).thenReturn(false);
        service.deleteGroup(caller, group.getId());
    }

    @Test
    public void deleteGroup() throws PermissionException {
        User caller = admin;
        when(mockedRepo.existsById(group.getId())).thenReturn(true);
        service.deleteGroup(caller, group.getId());
        verify(mockedRepo, times(1)).deleteById(group.getId());
    }

    @Test
    public void findNotExistsGroupByName() {
        when(mockedRepo.findGroupByName(group.getName())).thenReturn(Optional.empty());
        Optional<Group> groupByName = service.findGroupByName(group.getName());
        Assert.assertFalse(groupByName.isPresent());
    }

    @Test
    public void findGroupByName() {
        when(mockedRepo.findGroupByName(group.getName())).thenReturn(Optional.of(group));
        Optional<Group> groupByName = service.findGroupByName(group.getName());
        Assert.assertTrue(groupByName.isPresent());
    }

    @Test(expected = EntityNotExistException.class)
    public void getNotExistsGroupMembers() {
        when(mockedRepo.findById(group.getId())).thenReturn(Optional.empty());
        service.getGroupMembers(group.getId());
    }

    @Test
    public void getGroupMembers() {
        when(mockedRepo.findById(group.getId())).thenReturn(Optional.of(group));
        List<String> groupMembers = service.getGroupMembers(group.getId());
        Assert.assertEquals(group.getMemberLogins(), groupMembers);
    }
}
