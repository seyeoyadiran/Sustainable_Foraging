package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.models.Forager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForagerServiceTest {

    ForagerService service;

    @BeforeEach
    void setUp(){
        service = new ForagerService(new ForagerRepositoryDouble());
    }

    @Test
    void shouldAddValidForager() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("John");
        forager.setLastName("Doe");
        forager.setState("OH");

        Result<Forager> result = service.add(forager);
        assertTrue(result.isSuccess());
        assertNotNull(result.isSuccess());
        assertEquals("John", result.getPayload().getFirstName());
    }

    @Test
    void shouldNotAddDuplicateForager() throws DataException{
        Forager duplicate = ForagerRepositoryDouble.FORAGER;

        Result<Forager> result = service.add(duplicate);
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().get(0).toLowerCase().contains("duplicate"));
    }

    @Test
    void shouldAddWhenMissingFirstName() throws DataException{
        Forager forager = new Forager();
        forager.setLastName("Smith");
        forager.setState("OH");

        Result<Forager> result = service.add(forager);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldAddWhenMissingLastName() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("John");
        forager.setState("OH");

        Result<Forager> result = service.add(forager);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldAddWhenMissingState() throws DataException {
        Forager forager = new Forager();
        forager.setFirstName("John");
        forager.setLastName("Smith");

        Result<Forager> result = service.add(forager);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldFindByState() throws DataException {
        List<Forager> result = service.findByState("CA");
        assertEquals(1, result.size());
        assertEquals("Jilly", result.get(0).getFirstName());
    }
}
