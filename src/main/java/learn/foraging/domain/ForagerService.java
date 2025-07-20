package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForagerRepository;
import learn.foraging.models.Forager;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ForagerService {

    private final ForagerRepository repository;

    public ForagerService(ForagerRepository repository) {
        this.repository = repository;
    }

    public List<Forager> findByState(String stateAbbr) {
        return repository.findByState(stateAbbr);
    }

    public List<Forager> findByLastName(String prefix) {
        return repository.findAll().stream()
                .filter(i -> i.getLastName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public Result<Forager> add(Forager forager) throws DataException{
        Result<Forager> result = validate(forager);
        if(!result.isSuccess()){
            return result;
        }

        forager.setId(UUID.randomUUID().toString());
        repository.add(forager);
        result.setPayload(forager);
        return result;
    }

    private Result<Forager> validate(Forager forager) throws DataException{
        Result<Forager> result = new Result<>();

        if(forager == null){
            result.addErrorMessage("Forager cannot be null");
            return result;
        }
        if(forager.getFirstName() == null || forager.getFirstName().isBlank()){
            result.addErrorMessage("First name is required");
        }
        if(forager.getLastName() == null || forager.getLastName().isBlank()){
            result.addErrorMessage("First name is required");
        }
        if(forager.getState() == null || forager.getState().isBlank() || forager.getState().length() != 2){
            result.addErrorMessage("State abbreviation is required and must be 2 characters");
        }
        //Prevent duplicates
        List<Forager> all = repository.findAll();
        for(Forager existing : all){
            if(existing.getFirstName().equalsIgnoreCase(forager.getFirstName()) &&
                existing.getLastName().equalsIgnoreCase(forager.getLastName()) &&
                existing.getState().equalsIgnoreCase(forager.getState())
            ){
                result.addErrorMessage("Duplicate forager is not allowed");
                break;
            }
        }
        return result;
    }


}
