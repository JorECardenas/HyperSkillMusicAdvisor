package advisor;


import advisor.parsers.CategoriesParser;
import advisor.parsers.FeaturedParser;
import advisor.parsers.NewParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpotifyService {


    Config config;

    AuthService auth;

    static final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

    public SpotifyService(Config config){
        this.config = config;

        this.auth = new AuthService(config);
    }


    public void callAuth() {
        config.setAuthorized(auth.authorize());
    }










}
