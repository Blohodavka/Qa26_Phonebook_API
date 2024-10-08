package okhttp;

import com.google.gson.Gson;
import dto.ContactDTO;
import dto.ErrorDTO;
import dto.MessageDTO;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactByIDOkhttp {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiYmxvaG9kYXZrYUBtYWlsLnJ1IiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3MjU2MDI2NjIsImlhdCI6MTcyNTAwMjY2Mn0.bFFbDLNVQpH43bxYxmW6t_LRHj3g6kJAs5eeDAVUKuo";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    String id;

    @BeforeMethod
    public void preCondition () throws IOException {
        // create contact
        int i = new Random().nextInt(1000)+1000;
        ContactDTO contactDTO = ContactDTO.builder()
                .name("Maria")
                .lastName("Madonna")
                .email("mariam" +i + "@mail.ru")
                .phone("123456789101" +i)
                .address("NY")
                .description("girl")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contactDTO),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(), 200);
        MessageDTO messageDTO = gson.fromJson(response.body().string(),MessageDTO.class);
        String message = messageDTO.getMessage();
        //get id from message
        //Message: "Contact was added! ID:***"
        String[] all = message.split(": ");
        //id
        id = all[1];
        System.out.println(id);




    }


    @Test
    public void deleteContactByIDSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 200);

        MessageDTO dto = gson.fromJson(response.body().string(), MessageDTO.class);
        System.out.println(dto.getMessage());

        Assert.assertEquals(dto.getMessage(), "Contact was deleted!");
    }

    @Test
    public void deleteContactByIDWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .delete()
                .addHeader("Authorization", "jknvhjsfvyjdgkfjvhvbljdg")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 401);

        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Unauthorized");
    }

    @Test
    public void deleteContactByIDNotFound() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+123)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 400);

        ErrorDTO errorDTO = gson.fromJson(response.body().string(), ErrorDTO.class);
        Assert.assertEquals(errorDTO.getError(), "Bad Request");
        System.out.println(errorDTO.getMessage());
        Assert.assertEquals(errorDTO.getMessage(), "Contact with id: 123 not found in your contacts!");
    }
}

