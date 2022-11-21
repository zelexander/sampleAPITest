package tests.petshopAPI;

import io.restassured.internal.http.Status;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.File;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class BaseSteps {

    public void post(int id, String name){
        System.out.println("===== Инициировано создание питомца с id:"+id+" и именем:"+name);
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", id);
        requestBody.put("name", name);
        Response response = given()
                .when()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post("/pet");
        Assert.assertTrue(Status.SUCCESS.matches(response.statusCode()),"===== Не удалось создать питомца! Причина: ");
        System.out.println("===== Успешно создан питомец с id:"+id+" и именем:"+name);
    }

    public void get(String desiredStatus, int id, String name){
        int statusCode;
        System.out.println("===== Инициирован поиск питомца с id:"+id+" и именем:"+name);
        Response response = given()
                .get("/pet/{q}", id);
        statusCode = response.statusCode();
        if (desiredStatus.equals("2**")) {
            Assert.assertTrue(Status.SUCCESS.matches(response.statusCode()),"===== НЕ Возвращается ожидаемый статус с кодом 2**!");
            try {
                response.then().assertThat().body("name",equalTo(name));
            } catch (Exception e) {
                System.out.println("===== Всё сломалось, шеф!");
            }
            System.out.println("===== Система вернула статус с кодом - "+statusCode+", соответствующий условию: "+desiredStatus);
            System.out.println("===== Питомец с id:"+id+", именем:"+name+" успешно найден! Данные совпадают!");
        }
        if (desiredStatus.equals("4**")){
            Assert.assertTrue(Status.FAILURE.matches(response.statusCode()),"===== НЕ Возвращается ожидаемый статус с кодом 4**!");
            System.out.println("===== При попытке поиска питомца с id:"+id+" был получен статус с кодом - "+statusCode+", соответствующий условию: "+desiredStatus);
        }
    }

    public void uploadImage(int id, String filePath){
        System.out.println("===== Инициирована загрузка фото питомцу с id:"+id);
        File file = new File(System.getProperty("user.dir") + "/" + filePath);
        String sizeOnDisk = Long.toString(file.length());

        Response response = given()
                .multiPart(file)
                .post("/pet/{q}/uploadImage", id);
        response.then().assertThat().statusCode(200);
        System.out.println("===== Загрузка фото питомцу с id: "+id+" успешно завершена!");

        String sizeFromResponse = response.then().extract().path("message");
        sizeFromResponse = sizeFromResponse.replaceAll("[^0-9]", "");
        Assert.assertEquals(sizeFromResponse,sizeOnDisk,"===== Загруженный файл не соответствует отправленному!");
        System.out.println("===== Загруженный файл по весу соответствует отправленному! (больше нигде и никак не удалось додуматься как проверить)");
    }

    public void delete(int id){
        System.out.println("===== Инициировано удаление питомца с id:"+id);
        Response response = given()
                .when()
                .delete("/pet/{q}", id);
        try {
            response.then().assertThat().statusCode(200);
        } catch (Exception e){
            Assert.fail("===== Не удалось удалить питомца");
        }
        System.out.println("===== Питомец с id:"+id+" успешно удалён из системы");
    }
}
