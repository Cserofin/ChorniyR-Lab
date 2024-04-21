package service;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ParserHelper {

    public Element getProductBuyBox(Document document) {
        return document.selectFirst("[data-test-id=BuyBox]");
    }

    public Elements getMainContainerRows(Document document) {
        return document.select("#app > section > section > div.sc-19tq43e-1.fIkTJO > div");
    }

    public String getProductName(Document document) {
        return getProductBuyBox(document)
                .selectFirst("[data-test-id=ProductName]")
                .text();
    }

    public String getProductBrand(Document document) {
        return getProductBuyBox(document)
                .selectFirst("img")
                .attr("alt");
    }

    public String getProductBasePrice(Document document) {
        return getProductBuyBox(document)
                .select("span")
                .last()
                .text();
    }

    public String getProductSalePrice(Document document) {
        Elements priceFields = getProductBuyBox(document).select("span");
        if (priceFields.size() == 2) {
            return priceFields.first().text();
        }
        return "-";
    }

    public Elements getProductColorLinks(Document document) {
        Elements colorLinks = getProductBuyBox(document)
                .select("[data-test-id=ColorSwatch] a");
        // Проверяем, есть ли кнопка, если да, то есть список
        if (colorLinks.isEmpty()) {
            colorLinks = getProductBuyBox(document)
                    .select("[data-test-id=ColorVariantSelector] a");
        }
        return colorLinks;
    }

    public String getProductArticle(Document document) {
        Elements productDetailedBoxTabPanel = document.select("[data-test-id=ProductDetailBox] > div");
        for (Element panel : productDetailedBoxTabPanel) {
            Element productDetails = panel
                    .selectFirst("[data-test-id^=ProductDetails]");
            if (productDetails != null) {
                List<Node> articleField = productDetails
                        .selectFirst("[data-test-id=ProductArticle]")
                        .childNodes();
                return articleField.get(articleField.size() - 1).toString();
            }
        }
        return "-";
    }
}