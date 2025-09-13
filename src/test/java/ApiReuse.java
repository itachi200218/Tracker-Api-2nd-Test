import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class ApiReuse {
    private String endpoint;
    private String method;
    private String payload;

    public ApiReuse(String endpoint, String payload, String method) {
        this.endpoint = endpoint;
        this.method = method;
        this.payload = payload;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getMethod() {
        return method;
    }
    public String getPayload() {
        return payload;
    }

    public static void uri(String Baseuri) {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.baseURI = Baseuri;
    }

    public static Response execute(ApiReuse api, Map<String, String> headers) {
        RequestSpecification request = RestAssured
                .given()
                .headers(headers);
        if (api.getPayload() != null) {
            request.body(api.getPayload());
        }
        return request
                .request(Method.valueOf(api.getMethod()), api.getEndpoint())
                .then()
                .extract().response();
    }
}