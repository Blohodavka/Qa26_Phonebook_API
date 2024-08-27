package okhttp;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class GetAllContactsTests {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoiYmxvaG9kYXZrYUBtYWlsLnJ1IiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE3MjUzODA1MzgsImlhdCI6MTcyNDc4MDUzOH0.CFJ2G_kgXnHzEKtu92Yywj-2QzkZ9ma4HyMHez5Btlo";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();


    @Test
    public void getAllContactsSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

    }

    @Test
    public void getAllContactsWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .addHeader("Authorization", "kfnbjkdhbvjdklzbvjxkdbjkd")
                .build();

        Response response = client.newCall(request).execute();

        Assert.assertEquals(response.code(), 401);
    }
}
