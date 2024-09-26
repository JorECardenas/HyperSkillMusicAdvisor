package advisor;

import java.util.Arrays;


public class Config {


    String clientId = "21549620314c4daa9cfba8897eb33d83";
    String clientSecret = "fabf40d666b24e6d844e8f28167e83f3";

    String secrets = clientId + ":" + clientSecret;

    String redirectUrl = "http://localhost:8080";

    String accessServer = "https://accounts.spotify.com";

    public String getAccessServer(){ return accessServer; }

    String resourceServer = "https://api.spotify.com";

    public String getResourceServer(){ return resourceServer; }

    public int pageLimit = 5;

    public int getPageLimit(){ return pageLimit; }


    String code = "";
    String accessToken = "";

    boolean authorized = false;

    public void setAuthorized(boolean auth){ this.authorized = auth; }

    public boolean isAuthorized() {
        return authorized;
    }

    public Config(String[] args){

        if(args.length > 0){
            for(int i = 0; i < args.length; i++){
                switch (args[i]){
                    case "-access":
                        accessServer = args[i+1];
                        break;
                    case "-resource":
                        resourceServer = args[i+1];
                        break;
                    case "-page":
                        pageLimit = Integer.parseInt(args[i+1]);
                        break;
                }
            }
        }



    }

    public String getAuthUrl(){
        return accessServer + "/authorize?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUrl +
                "&response_type=code";
    }

    public String getAccessUrl(){
        return accessServer + "/api/token";
    }

    public void setCode(String code){ this.code = code; }

    public String getCode(){ return code; }

    public void setAccessToken(String token){ this.accessToken = token; }

    public String getAccessToken(){ return accessToken; }

}
