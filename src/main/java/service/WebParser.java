package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Product;
import storage.Storage;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebParser {
    private final List<String> productLinksFromMainPage;
    private final ParserHelper parserHelper;
    private int totalRequests;
    private static final String PARSE_URL = "/c/maenner/bekleidung-20290";
    private static final String CONTEXT_PATH = "https://www.aboutyou.de";

    public WebParser() {
        this.productLinksFromMainPage = new ArrayList<>();
        this.parserHelper = new ParserHelper();
        this.totalRequests = 0;
    }

    public void parse() {
        System.out.print("Process: ");
        parsePageIntoProductLinks();
        for (String link : productLinksFromMainPage) {
            parseProductFromLink(link);
            System.out.print("#");
        }
        System.out.println(" Done");
        System.out.println("\nTOTAL REQUESTS: " + totalRequests);
        System.out.println("\nNumber of product links found: " + productLinksFromMainPage.size());
        System.out.println("TOTAL PRODUCTS EXTRACTED: " + Storage.productMap.size());

    }

    private void parsePageIntoProductLinks() {
        Document document = getDocumentFromUrl(PARSE_URL).orElseThrow();
        String productRowClass = "sc-1n50fuf-0 kOgykq";

        Elements mainContainerRows = parserHelper.getMainContainerRows(document);
        for (Element row : mainContainerRows) {
            if (row.hasClass(productRowClass)) {
                for (Element productTile : row.children()) {
                    String productLink = productTile.attr("href");
                    System.out.println("Found product link: " + productLink);
                    if (!productLinksFromMainPage.contains(productLink)) {
                        productLinksFromMainPage.add(productLink);
                    }
                }
            }
        }
    }



    //recursive function
    private void parseProductFromLink(String link) {
        //exit condition
        if (Storage.productMap.containsKey(link)) {
            return;
        }
        Document document = getDocumentFromUrl(link).orElseThrow();

        Product product = new Product();
        product.setArticleID(parserHelper.getProductArticle(document));
        product.setName(parserHelper.getProductName(document));
        product.setBrand(parserHelper.getProductBrand(document));
        product.setBasePrice(parserHelper.getProductBasePrice(document));
        product.setSalePrice(parserHelper.getProductSalePrice(document));
        // Add debug output to check extracted product data
        System.out.println("Product Article ID: " + product.getArticleID());
        System.out.println("Product Name: " + product.getName());
        System.out.println("Product Brand: " + product.getBrand());
        System.out.println("Product Base Price: " + product.getBasePrice());
        System.out.println("Product Sale Price: " + product.getSalePrice());

        Elements colorLinks = parserHelper.getProductColorLinks(document);
        product.setColor(colorLinks.first().text());
        Storage.productMap.put(link, product);

        //recursively get and parse other color versions of that product
        for (Element colorLink : colorLinks) {
            parseProductFromLink(colorLink.attr("href"));
        }
    }


    private Optional<Document> getDocumentFromUrl(String url) {
        try {
            Document document = Jsoup.connect(CONTEXT_PATH + url)
                    .userAgent("Chrome/51.0.2704.103")
                    .cookie("auth", "token")
                    .timeout(20000)
                    .method(Connection.Method.GET)
                    .execute()
                    .parse();
            this.totalRequests++;
            return Optional.of(document);
        } catch (IOException e) {
            System.err.println("Error connecting to " + CONTEXT_PATH + url + ": " + e.getMessage());
            return Optional.empty();
        }
    }

}
