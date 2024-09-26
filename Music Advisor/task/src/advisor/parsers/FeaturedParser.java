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
import java.util.List;

public class FeaturedParser extends BasicParser{

    public FeaturedParser(Config config){
        super(config);
    }

    @Override
    public String getInfo() {
        HttpRequest req = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + config.getAccessToken())
                .uri(URI.create(config.getResourceServer() + "/v1/browse/featured-playlists"))
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
