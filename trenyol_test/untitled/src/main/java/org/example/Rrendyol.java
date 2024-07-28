package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public class Rrendyol {
    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Trendyol ana sayfasına gidin
            driver.get("https://www.trendyol.com/");
            // Çerez onayı
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='onetrust-accept-btn-handler']"))).click();


            // Arama çubuğunu bulun ve "laptop" yazın
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='sfx-discovery-search-suggestions']/div/div/input")));
            searchBox.sendKeys("laptop");

            // Arama butonuna veya Enter tuşuna basın
            driver.findElement(By.xpath("//*[@data-testid=\"search-icon\"]")).click();

            // Arama sonuçlarının yüklendiğini ve sonuçların göründüğünü doğrulayın
            List<WebElement> searchResults = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".p-card-wrppr")));
            if (searchResults.isEmpty()) {
                System.out.println("Arama sonucu bulunamadı!");
                return;
            }

            String mainWindow = driver.getWindowHandle();

            // Listelenen ilk ürüne tıklayın
            WebElement firstProduct = searchResults.getFirst();
            firstProduct.click();

            // Tüm pencerelerin kimliklerini alın
            Set<String> allWindows = driver.getWindowHandles();
            for (String window : allWindows) {
                if (!window.equals(mainWindow)) {
                    driver.switchTo().window(window);
                    break;
                }
            }

            // Ürün sayfasının yüklendiğini doğrulayın
            WebElement productTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-brand-name-with-link")));
            if (!productTitle.isDisplayed()) {
                System.out.println("Ürün sayfası yüklenmedi!");
                return;
            }

            // Ürün ismi ve fiyatının göründüğünden emin olun
            WebElement productPrice = driver.findElement(By.cssSelector(".pr-new-br"));
            if (!productTitle.isDisplayed() || !productPrice.isDisplayed()) {
                System.out.println("Ürün ismi veya görünmüyor!");
                return;
            }

            // "Sepete Ekle" butonuna tıklayın
            WebElement addToCartButton = driver.findElement(By.cssSelector(".product-button-container .add-to-basket"));
            addToCartButton.click();

            // Sepetinize gidin (genellikle sağ üst köşede bir sepet ikonu olur)
            WebElement goToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"account-navigation-container\"]/div/div[2]/a")));
            goToCartButton.click();
            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()=\"Anladım\"]")));
            okButton.click();

            // Sepette eklediğiniz ürünün göründüğünü doğrulayın
            WebElement cartProductTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".pb-basket-item-wrapper-v2")));
            if (cartProductTitle.getText().toLowerCase().contains("laptop")) {
                System.out.println("Ürün sepete başarıyla eklendi!");
            } else {
                System.out.println("Ürün sepete eklenemedi!");
            }

            // İşlem tamamlandığında ana pencereye geri dönün
            driver.switchTo().window(mainWindow);

        } finally {
            driver.quit();
        }
    }
}
