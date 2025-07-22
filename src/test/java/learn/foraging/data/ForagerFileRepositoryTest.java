package learn.foraging.data;

import learn.foraging.models.Forager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ForagerFileRepositoryTest {

    private final String TEST_FILE_PATH = "./data/test-fragers.csv";
    ForagerFileRepository repository;


    @BeforeEach
    void setUp() {
        //Clean or recreate the tes file before each test
        File file = new File(TEST_FILE_PATH);
        if(file.exists()){
            file.delete();
        }
        repository = new ForagerFileRepository(TEST_FILE_PATH);
    }

    @Test
    void shouldFindAll() {
        List<Forager> all = repository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    void shouldFindByID(){
        ForagerFileRepository repo = new ForagerFileRepository("./data/foragers.csv");

        //Fetch specific ID
        Forager forager = repo.findById("7f47325f-8c02-4fca-a4a4-c56d737ffadc");

        //Validate forager exists and has correct ID
        assertNotNull(forager, "Forager with ID 7f47325f-8c02-4fca-a4a4-c56d737ffadc should not be null");
        assertEquals("7f47325f-8c02-4fca-a4a4-c56d737ffadc", forager.getId(), "Forager ID does not match");
    }

    @Test
    void shouldAddForager() throws DataException {
        Forager forager = new Forager();
        forager.setId("ID123");
        forager.setFirstName("John");
        forager.setLastName("John");
        forager.setState("OH");

        repository.add(forager);

        List<Forager> foragers = repository.findAll();
        assertEquals(1, foragers.size());
        Forager addedForager = foragers.get(0);
        assertEquals("ID123", addedForager.getId());

    }

}
