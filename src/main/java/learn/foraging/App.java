package learn.foraging;

import learn.foraging.ui.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Controller controller = context.getBean(Controller.class);
        controller.run();
//        ConsoleIO io = new ConsoleIO();
//        View view = new View(io);
//
//
//        ForagerFileRepository foragerFileRepository = new ForagerFileRepository("./data/foragers.csv");
//        ItemFileRepository itemFileRepository = new ItemFileRepository("./data/items.txt");
//        ForageFileRepository forageFileRepository = new ForageFileRepository("./data/forage_data",foragerFileRepository, itemFileRepository);
//
//        ForagerService foragerService = new ForagerService(foragerFileRepository);
//        ForageService forageService = new ForageService(forageFileRepository, foragerFileRepository, itemFileRepository);
//        ItemService itemService = new ItemService(itemFileRepository);
//
//        Controller controller = new Controller(foragerService, forageService, itemService, view);
//        controller.run();
    }
}
