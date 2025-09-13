import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrackerTest {

    @BeforeClass
    public void browser() {
        ApiReuse.uri("http://localhost:5000");
    }

    @Test
    public void Test1() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");

        // 1️⃣ Execute GET first to get a valid ID
        Response getRes = ApiReuse.execute(new ApiReuse("/api/jobs", null, "GET"), header);

        // 2️⃣ Extract token/id safely
        String token = getRes.jsonPath().getString("[0]._id");  // first job id
        String tokens = getRes.jsonPath().getString("[10]._id");  // first job id

        // 3️⃣ Prepare list of API calls
        ArrayList<ApiReuse> list = new ArrayList<>();
        list.add(new ApiReuse("/api/jobs", null, "GET"));
        list.add(new ApiReuse("/api/jobs", "{"
                + "\"company\": \"Amazon\","
                + "\"role\": \"Frontend Developer\","
                + "\"status\": \"Applied\","
                + "\"appliedDate\": \"2024-04-09\","
                + "\"link\": \"https://example.com\""
                + "}", "POST"));

            list.add(new ApiReuse("/api/jobs/" + token, null, "DELETE"));  // Add slash before ID
            // 1️⃣ Execute GET first to get a valid ID
            list.add(new ApiReuse("/api/jobs/" + tokens, "{\"status\": \"Reject\"}", "PUT"));

        // 4️⃣ Execute all requests
        for (ApiReuse r : list) {
            Response res = ApiReuse.execute(r, header);
            System.out.println("Method : " + r.getMethod());
            System.out.println("Endpoint : " + r.getEndpoint());
            System.out.println("Status Code : " + res.getStatusCode());
              res.prettyPrint();
        }
    }
}
