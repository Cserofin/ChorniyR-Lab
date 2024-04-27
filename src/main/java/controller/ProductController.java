package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import service.ProductToExcel;
import service.WebParser;
import storage.Storage;

@Controller
public class ProductController {

    private final WebParser webParser;

    @Autowired
    public ProductController(WebParser webParser, ProductToExcel productToExcel) {
        this.webParser = webParser;
    }

    @GetMapping("/")
    public String index(@org.jetbrains.annotations.NotNull Model model) {
        webParser.parse();
        ProductToExcel.convertIntoFile();
        model.addAttribute("products", Storage.productMap.values());
        return "index";
    }
}
