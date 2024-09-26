package advisor.parsers;

import advisor.Config;

import java.net.http.HttpClient;
import java.util.List;

public abstract class BasicParser {

    Config config;

    static final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

    public BasicParser(Config config){
        this.config = config;
    }


    public abstract String getInfo();


    public abstract List<String> parse();





}
