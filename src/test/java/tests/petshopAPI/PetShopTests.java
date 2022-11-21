package tests.petshopAPI;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static service.Generator.getRandomID;
import static service.Values.PETSHOP_URL;
import static service.Values.PET_PIC;

public class PetShopTests extends BaseSteps{

    @BeforeTest
    public static void setUp() {
        RestAssured.baseURI = PETSHOP_URL;
    }

    @Test
    public void petAddingAndDeleteTest(){
// ===== СТАРТОВО-ВСПОМОГАТЕЛЬНОЕ ===== //
        int petID = getRandomID(1,99999);
        String petName = "QAsimodo-" + petID;
// ===== (1-2) ЗАХОДИМ НА САЙТ? + СОЗДАНИЕ ПИТОМЦА ===== //
        post(petID,petName);
// ===== (3+4) ДОБАВЛЕНИЕ ФОТО ПИТОМЦУ + (ПСЕВДО)ПРОВЕРКА ЗАГРУЗКИ ФОТО ===== //
        uploadImage(petID,PET_PIC);
//// ===== (4+5) ПОИСК РАНЕЕ СОЗДАННОГО + ПРОВЕРКА КОРРЕКТНОСТИ ID И ИМЕНИ + ПОЛУЧЕНИЕ 2** СТАТУСА ===== //
        get("2**",petID,petName);
//// ===== (6) УДАЛЕНИЕ ПИТОМЦА ===== //
        delete(petID);
//// ===== (7+8) ПОИСК РАНЕЕ СОЗДАННОГО + ПОЛУЧЕНИЕ 4** СТАТУСА ===== //
        get("4**",petID,petName);
    }
}
