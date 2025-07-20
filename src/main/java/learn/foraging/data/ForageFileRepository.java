package learn.foraging.data;

import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ForageFileRepository implements ForageRepository, ForagerRepository {

    private static final String HEADER = "id,forager_id,item_id,kg";
    private final String directory;
    private final ForagerRepository foragerRepo;
    private final ItemRepository itemRepo;

    @Autowired
    public ForageFileRepository(@Value("${forage.data.dir:./data/forage_data}") String directory, ForagerRepository foragerRepo, ItemRepository itemRepo) {
        this.directory = directory;
        this.foragerRepo = foragerRepo;
        this.itemRepo = itemRepo;
    }

    @Override
    public List<Forage> findByDate(LocalDate date)  {
        ArrayList<Forage> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(date)))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] fields = line.split(",", -1);
                if (fields.length == 4) {
                    try {
                        result.add(deserialize(fields, date));
                    } catch (DataException e) {
                        System.err.println("Error deserialzing line: " + e.getMessage());
                    }
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    @Override
    public Forage add(Forage forage) throws DataException {
        if(forage.getForager() == null){
            System.out.println("forager is null when adding forage");
            return null;
        }

        if( forage.getItem() == null){
            System.out.println("Item is null when adding forage");
            return null;
        }

        if(forage.getKilograms() <= 0 || forage.getKilograms() > 250){
            throw new DataException("Kilograms must be positive number a 250");
        }

        List<Forage> all = findByDate(forage.getDate());
        for(Forage existingForage : all){
            if(existingForage.getForager().equals(forage.getForager()) &&
                existingForage.getItem().equals(forage.getItem()) &&
                existingForage.getDate().equals(forage.getDate())){
                System.out.println("Duplicate forage detected");
                return null;
            }
        }

        forage.setId(java.util.UUID.randomUUID().toString());
        all.add(forage);

      try {
          writeAll(all, forage.getDate());
         }catch (DataException ex){
          System.err.println("error writing to file: " + ex.getMessage());
      }
        return forage;
    }

    @Override
    public boolean update(Forage forage) throws DataException {
        List<Forage> all = findByDate(forage.getDate());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(forage.getId())) {
                all.set(i, forage);
                writeAll(all, forage.getDate());
                return true;
            }
        }
        return false;
    }

    private String getFilePath(LocalDate date) {
        return Paths.get(directory, date + ".csv").toString();
    }

    private void writeAll(List<Forage> forages, LocalDate date) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(date))) {

            writer.println(HEADER);

            for (Forage item : forages) {
                writer.println(serialize(item));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Forage item) {
        String foragerId = item.getForager() != null ? item.getForager().getId() : "Unknown";
        int itemId = item.getItem() != null ? item.getItem().getId() : -1;
        return String.format("%s,%s,%s,%s",
                item.getId(),
                item.getForager().getId(),
                itemId,
                item.getKilograms());
    }

    private Forage deserialize(String[] fields, LocalDate date) throws DataException{
        Forage result = new Forage();
        result.setId(fields[0]);
        result.setDate(date);
        result.setKilograms(Double.parseDouble(fields[3]));

        Forager forager = foragerRepo.findById(fields[1]);
        if(forager == null){
            throw new DataException("Forager not found with this id: " + fields[1]);
        }
        result.setForager(forager);

        Item item = itemRepo.findById(Integer.parseInt(fields[2]));
        if(item == null) {
            System.out.println("Warning: Item not found with id: " + fields[2]);
        }
        result.setItem(item);

        return result;
    }

    public Forager findById(String id) {
        return null;
    }

    public List<Forager> findAll() {
        return List.of();
    }

    public List<Forager> findByState(String stateAbbr) {
        return List.of();
    }

    public void add(Forager forager) throws DataException {

    }
}
