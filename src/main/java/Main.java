import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import service.ProductToExcel;
import service.WebParser;

@SpringBootApplication
public class Main {
    public static void main(String[] args){
        WebParser parser = new WebParser();
        parser.parse();
        ProductToExcel.convertIntoFile();
    }
}
