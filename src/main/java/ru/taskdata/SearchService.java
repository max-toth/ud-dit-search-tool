package ru.taskdata;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.taskdata.dto.ProfileDataSearchRequestBObjType;

public class SearchService {

    private static final Logger log = LogManager.getLogger(SearchService.class);

    private static final String USER_LOGIN_HEADER = "dit_user_login";
    private static final String USER_PASSWORD_HEADER = "dit_user_password";

    private final HttpRequestFactory requestFactory;
    private final JsonFactory jsonFactory;
    private final GenericUrl url;

    public SearchService() {
        requestFactory = new NetHttpTransport().createRequestFactory();
        jsonFactory = new JacksonFactory();
        url = new GenericUrl("/api/dit/search");
    }

    public String doPost(ProfileDataSearchRequestBObjType requestBody) {
        HttpHeaders headers = new HttpHeaders()
                .set(USER_LOGIN_HEADER, "admin")
                .set(USER_PASSWORD_HEADER, "admin");
        try {
            JsonHttpContent content = new JsonHttpContent(jsonFactory, requestBody);
            HttpResponse httpResponse = requestFactory.buildPostRequest(url, content)
                    .setHeaders(headers)
                    .execute();

            return httpResponse.parseAsString();
        } catch (IOException e) {
            log.error("Failed to build POST request", e);
            return "";
        }
    }
}
