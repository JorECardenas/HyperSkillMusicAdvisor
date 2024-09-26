package advisor.parsers;

import advisor.Config;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistParser extends BasicParser {

    List<String> cat;

    public PlaylistParser(Config config, List<String> cat){
        super(config);

        this.cat = cat;
    }

    public Map<String, String> getCategoriesIds(){
        HttpRequest req = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + config.getAccessToken())
                .uri(URI.create(config.getResourceServer() + "/v1/browse/categories"))
                .GET()
                .build();
        Map<String,String> cats = new HashMap<>();

        String name;
        String id;

        try{

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(res.body()).getAsJsonObject();

            JsonObject categories = json.get("categories").getAsJsonObject();

            JsonArray items = categories.getAsJsonArray("items");

            for(JsonElement ele: items){
                JsonObject obj = ele.getAsJsonObject();

                name = obj.get("name").getAsString();
                id = obj.get("id").getAsString();

                cats.put(name, id);
            }

            System.out.println("\n");

        }catch (Exception e){
            e.printStackTrace();
        }

        return cats;
    }


    @Override
    public String getInfo() {
        Map<String, String> categories = getCategoriesIds();

        System.out.println(categories.toString());

        String id = categories.get(String.join(" ", cat));


        HttpRequest req = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + config.getAccessToken())
                .uri(URI.create(config.getResourceServer() + "/v1/browse/categories/" + id + "/playlists"))
                .GET()
                .build();

        //System.out.println(req);

        try{

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            if(res.statusCode() != 200 || res.body().contains("error")) {
                JsonObject obj = JsonParser.parseString(res.body()).getAsJsonObject();

                System.out.println(obj.get("error").getAsJsonObject().get("message").getAsString());

                //System.out.println("Unknown category name.");

                return null;
            }


            return res.body();

        }catch (IOException | InterruptedException e){
            e.printStackTrace();

            //System.out.println("Unknown category name.\n");
        }

        return null;
    }

    @Override
    public List<String> parse() {

        String info = getInfo();

        JsonObject json = JsonParser.parseString(info).getAsJsonObject();

        JsonObject playlists = json.get("playlists").getAsJsonObject();

        JsonArray items = playlists.getAsJsonArray("items");

        ArrayList<String> res = new ArrayList<>();

        for(JsonElement ele: items){
            JsonObject obj = ele.getAsJsonObject();

            String option = obj.get("name").getAsString() + "\n"
                    + obj.get("external_urls").getAsJsonObject().get("spotify").getAsString() + "\n";

            res.add(option);

        }

        return res;
    }
}
