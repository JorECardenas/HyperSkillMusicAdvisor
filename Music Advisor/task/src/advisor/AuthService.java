package advisor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

public class AuthService {

    Config config;

    public AuthService(Config config){
        this.config = config;
    }

    public void createServer(){

        try {
            HttpServer server = HttpServer.create();

            server.bind(new InetSocketAddress(8080), 0);

            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {

                    String req = exchange.getRequestURI().getQuery();

                    String res = "";
                    if(req != null && req.contains("code")){
                        System.out.println("code received");

                        config.setCode(req);;
                        res = "Got the code. Return back to your program.";


                    } else {
                        res = "Authorization code not found. Try again.";
                    }

                    exchange.sendResponseHeaders(200, res.length());
                    exchange.getResponseBody().write(res.getBytes());
                    exchange.getResponseBody().close();


                }
            });

            server.start();

            System.out.println(config.getAuthUrl());
            System.out.println("waiting for code...");

            Instant expires = Instant.now().plus(30, ChronoUnit.SECONDS);
            while(config.getCode().isEmpty() && expires.isAfter(Instant.now())){
                Thread.sleep(10);
            }

            server.stop(1);

        }catch(IOException e){
            System.out.println(e.toString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAccessToken(){

        String params = config.getCode() +
                "&grant_type=authorization_code" +
                "&redirect_uri=http://localhost:8080";

        HttpClient client = HttpClient.newBuilder().build();


        HttpRequest req = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(config.secrets.getBytes()))
                .uri(URI.create(config.getAccessUrl()))
                .POST(HttpRequest.BodyPublishers.ofString(params))
                .build();

        try{
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("response:");
            System.out.println(res.body());

            JsonObject json = (JsonObject) JsonParser.parseString(res.body());


            config.setAccessToken(json.get("access_token").getAsString());



        }catch (Exception e){
            System.out.println(e);
        }

    }

    public boolean authorize() {


        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest req = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(config.getAuthUrl()))
                .GET()
                .build();


        try {

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println("use this link to request the access code:");


            createServer();

            if(config.getCode().isEmpty()){
                System.out.println("Authorization code not found. Try again.");
                return false;
            }
            System.out.println(config.getCode());

            System.out.println("making http request for access_token...");


            getAccessToken();


        } catch (Exception e) {
            System.out.println("Error in getting code: " + e.toString());
        }

        if(config.getAccessToken().isEmpty()){
            System.out.println("---FAILURE---");
            return false;
        }

        System.out.println("---SUCCESS---");
        return true;

    }


}
