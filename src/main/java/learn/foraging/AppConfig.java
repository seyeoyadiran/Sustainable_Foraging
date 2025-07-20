package learn.foraging;


import learn.foraging.data.ForageFileRepository;
import learn.foraging.data.ForagerFileRepository;
import learn.foraging.data.ItemFileRepository;
import learn.foraging.domain.ForageService;
import learn.foraging.domain.ForagerService;
import learn.foraging.domain.ItemService;
import learn.foraging.ui.ConsoleIO;
import learn.foraging.ui.Controller;
import learn.foraging.ui.View;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")

public class AppConfig {

    @Value("${forager.file.path}")
    private String foragerFilePath;

    @Value("${item.file.path}")
    private String itemFilePath;

    @Value("${forage.data.dir}")
    private String forageDataDir;

    @Bean
    public ConsoleIO consoleIO(){
        return new ConsoleIO();
    }

    @Bean
    public View view(ConsoleIO io){
        return new View(io);
    }

    @Bean
    public ForagerFileRepository foragerFileRepository(){
        return new ForagerFileRepository(foragerFilePath);
    }

    @Bean
    public ItemFileRepository itemFileRepository(){
        return new ItemFileRepository(itemFilePath);
    }

    @Bean
    public ForageFileRepository forageFileRepository(
            ForagerFileRepository foragerRepo,
            ItemFileRepository itemRepo){
        return new ForageFileRepository(forageDataDir, foragerRepo, itemRepo);
     }

     @Bean
    public ForagerService foragerService(ForagerFileRepository repo){
        return new ForagerService(repo);
     }

     @Bean
    public ItemService itemService(ItemFileRepository repo){
        return new ItemService(repo);
     }

     @Bean
    public ForageService forageService(
            ForageFileRepository forageRepo,
            ForagerFileRepository foragerRepo,
            ItemFileRepository itemRepo){
        return new ForageService(forageRepo, foragerRepo, itemRepo);
     }

     @Bean
    public Controller controller(
        ForagerService foragerService,
        ForageService forageService,
        ItemService itemService,
        View view){
        return new Controller(foragerService,forageService, itemService, view);
     }


}
