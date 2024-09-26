package advisor.parsers;

import advisor.Config;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NewParser extends BasicParser{


    public NewParser(Config config){
        super(config);
    }

    @Override
    public String getInfo() {
        HttpRequest req = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + config.getAccessToken())
                .uri(URI.create(config.getResourceServer() + "/v1/browse/new-releases"))
                .GET()
                .build();

        try{

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            return res.body();

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<String> parse(){

        String info = getInfo();

        JsonObject json = JsonParser.parseString(info).getAsJsonObject();

        JsonObject albums = json.get("albums").getAsJsonObject();

        JsonArray items = albums.getAsJsonArray("items");

        ArrayList<String> res = new ArrayList<>();

        for (JsonElement item : items) {
            JsonObject obj = item.getAsJsonObject();

            String option = obj.get("name").getAsString();

            JsonArray artInfo = obj.getAsJsonArray("artists");

            List<String> artists = new ArrayList<>();

            for(JsonElement art: artInfo){
                artists.add(art.getAsJsonObject().get("name").getAsString());
            }

            String url = obj.get("external_urls").getAsJsonObject().get("spotify").getAsString();

            res.add(option + "\n" + artists + "\n" + url + "\n");


        }

        return res;
    }


}
