package storage.postgresql;

import domain.Group;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import storage.GroupRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class GroupRepositoryImplTest {

    private GroupRepository repository;
    private Connector connector;

    @Before
    public void setUp() {
        Connector.Builder builder = new Connector.Builder();
        builder.setUrl("jdbc:postgresql://localhost:5432/experiment").setUser("postgres").setPassword("root");

        connector = new Connector(builder);
        connector.getConnection();
        repository = new GroupRepositoryImpl(connector);
    }

    @Test
    public void saveNewEntity() {
        Group group1 = new Group(null, "IP-94", "vasya092", List.of());
        Group group2 = new Group(10, "10th Group", "kolya073", List.of());
        Integer groupId1 = group1.getId();
        Integer groupId2 = group2.getId();
        Group savedGroup1 = repository.saveNewEntity(group1);
        Group savedGroup2 = repository.saveNewEntity(group2);

        assertNotEquals(groupId1, savedGroup1.getId());
        assertNotEquals(groupId2, savedGroup2.getId());

        repository.deleteById(groupId1);
        repository.deleteById(groupId2);
    }

    @Test
    public void findById() {
        Integer id1 = 1;
        Optional<Group> groupById1 = repository.findById(id1);
        assertTrue(groupById1.isPresent());

        Integer id2 = -3;
        Optional<Group> groupById2 = repository.findById(id2);
        assertFalse(groupById2.isPresent());
    }

    @Test
    public void findAll() {
        Iterable<Group> groups = repository.findAll();
        assertNotNull(groups);
    }

    @Test
    public void deleteById() {
        Group group = repository.saveNewEntity(new Group(null, "Deleted Group", "wqa092", List.of()));
        repository.deleteById(group.getId());
        assertFalse(repository.existsById(group.getId()));
    }

    @Test
    public void existsById() {
        boolean isGroupExist1 = repository.existsById(1);
        boolean isGroupExist2 = repository.existsById(2);
        boolean isGroupExist3 = repository.existsById(3);

        assertTrue(isGroupExist1);
        assertTrue(isGroupExist2);
        assertTrue(isGroupExist3);

        boolean isGroupExist4 = repository.existsById(4);
        boolean isGroupExist5 = repository.existsById(5);
        boolean isGroupExist6 = repository.existsById(6);

        assertFalse(isGroupExist4);
        assertFalse(isGroupExist5);
        assertFalse(isGroupExist6);
    }

    @Test
    public void findGroupByName() {
        Optional<Group> optionalGroup1 = repository.findGroupByName("First Group");
        Optional<Group> optionalGroup2 = repository.findGroupByName("Second Group");
        Optional<Group> optionalGroup3 = repository.findGroupByName("Third Group");

        assertEquals(optionalGroup1, Optional.of(new Group(1, "First Group", "vasya092",
                List.of("vasya092", "dominar3000", "wqa092", "eeesya092"))));
        assertEquals(optionalGroup2, Optional.of(new Group(2, "Second Group", "sssya092",
                List.of("sssya092", "dominat00", "wireas34", "va9rkw2"))));
        assertEquals(optionalGroup3, Optional.of(new Group(3, "Third Group", "fsf1467",
                List.of("fsf1467", "nsddw002", "port92", "kolya073"))));

        Optional<Group> optionalGroup4 = repository.findGroupByName("IP-99");
        Optional<Group> optionalGroup5 = repository.findGroupByName("HONOR");
        Optional<Group> optionalGroup6 = repository.findGroupByName("JustGroup");

        assertEquals(optionalGroup4, Optional.empty());
        assertEquals(optionalGroup5, Optional.empty());
        assertEquals(optionalGroup6, Optional.empty());
    }

    @Test
    public void update() {
        Group group1 = new Group(1, "First Group", "vasya092",
                List.of("vasya092", "dominar3000", "eeesya092"));
        Group group2 = new Group(2, "2 Group", "sssya092",
                List.of("sssya092", "dominat00", "wireas34", "va9rkw2"));
        Group group3 = new Group(3, "3", "fsf1467",
                List.of("fsf1467", "nsddw002", "port92", "kolya073"));

        repository.update(group1);
        repository.update(group2);
        repository.update(group3);

        Optional<Group> optionalGroup1 = repository.findGroupByName("First Group");
        Optional<Group> optionalGroup2 = repository.findGroupByName("2 Group");
        Optional<Group> optionalGroup3 = repository.findGroupByName("3");

        assertEquals(optionalGroup1, Optional.of(new Group(1, "First Group", "vasya092",
                List.of("vasya092", "dominar3000", "wqa092", "eeesya092"))));
        assertEquals(optionalGroup2, Optional.of(new Group(2, "2 Group", "sssya092",
                List.of("sssya092", "dominat00", "wireas34", "va9rkw2"))));
        assertEquals(optionalGroup3, Optional.of(new Group(3, "3", "fsf1467",
                List.of("fsf1467", "nsddw002", "port92", "kolya073"))));

        assertNotEquals(optionalGroup1, Optional.of(new Group(1, "Group", "vasya092",
                List.of("vasya092", "dominar3000", "wqa092", "eeesya092"))));
        assertNotEquals(optionalGroup2, Optional.of(new Group(2, "Secoup", "sssya092",
                List.of("sssya092", "dominat00", "wireas34", "va9rkw2"))));
        assertNotEquals(optionalGroup3, Optional.of(new Group(3, "Thioup", "fsf1467",
                List.of("fsf1467", "nsddw002", "port92", "kolya073"))));
    }

    @After
    public void setDown() {
        Group group1 = new Group(1, "First Group", "vasya092",
                List.of("vasya092", "dominar3000", "wqa092", "eeesya092"));
        Group group2 = new Group(2, "Second Group", "sssya092",
                List.of("sssya092", "dominat00", "wireas34", "va9rkw2"));
        Group group3 = new Group(3, "Third Group", "fsf1467",
                List.of("fsf1467", "nsddw002", "port92", "kolya073"));

        repository.update(group1);
        repository.update(group2);
        repository.update(group3);
    }
}
