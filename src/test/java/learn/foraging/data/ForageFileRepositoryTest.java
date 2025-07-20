package learn.foraging.data;

import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForageFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/forage-seed-2020-06-26.csv";
    static final String TEST_FILE_PATH = "./data/forage_data_test/2020-06-26.csv";
    static final String TEST_DIR_PATH = "./data/forage_data_test";
    static final int FORAGE_COUNT = 54;

    final LocalDate date = LocalDate.of(2020, 6, 26);


    ForageFileRepository repository = new ForageFileRepository(TEST_DIR_PATH, new ForagerRepositoryDouble(), new ItemRepositoryDouble());

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindByDate() {
        List<Forage> forages = repository.findByDate(date);
        assertEquals(FORAGE_COUNT, forages.size());
    }

    @Test
    void shouldAdd() throws DataException {
        Forage forage = new Forage();
        forage.setDate(date);
        forage.setKilograms(0.75);

        Item item = new Item();
        item.setId(12);
        forage.setItem(item);

        Forager forager = new Forager();
        forager.setId("AAAA-1111-2222-FFFF");
        forage.setForager(forager);

        forage = repository.add(forage);

        assertEquals(36, forage.getId().length());
    }

    @Test
    void shouldNotAddWhenForagerIsNull() throws DataException {
        Forage forage = new Forage();
        forage.setDate(date);
        forage.setKilograms(0.75);

        Item item = new Item();
        item.setId(12);
        forage.setItem(item);

        //Setting forager to null
        forage.setForager(null);

        forage = repository.add(forage);
        assertNull(forage);
    }

    @Test
    void shouldNotAddWhenItemIsNull() throws DataException {
        Forage forage = new Forage();
        forage.setDate(date);
        forage.setKilograms(0.75);

        Forager forager = new Forager();
        forage.setId("12312-Random-1234-fff");
        forage.setForager(forager);

        forage.setItem(null);

        forage = repository.add(forage);
        assertNull(forage);
    }

    @Test
    void shouldUpdateForage() throws DataException {
        List<Forage> forages = repository.findByDate(date);
        Forage originalForage = forages.get(0);
        originalForage.setKilograms(1.25);

        boolean updated = repository.update(originalForage);
        assertTrue(updated);

        //Verifying
        Forage updatedForage = repository.findByDate(date).get(0);
        assertEquals(1.25, updatedForage.getKilograms());
    }

    @Test
    void shouldReturnEmptyListWhenNoForageFoundForDate(){
        LocalDate newDate = LocalDate.of(2020, 9, 1);
        List<Forage> forages = repository.findByDate(newDate);
        assertTrue(forages.isEmpty());
    }

    @Test
    void shouldNotAddForageWithMissingForager() throws DataException {
        LocalDate date = LocalDate.now();
        Item item = new Item();
        item.setId(16);
        Forage forage = new Forage();
        forage.setForager(null);
        forage.setItem(item);
        forage.setDate(date);
        forage.setKilograms(10.0);

        Forage addedForage = repository.add(forage);

        assertNull(addedForage);
    }
}
