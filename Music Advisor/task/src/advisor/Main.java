package advisor;

import advisor.parsers.CategoriesParser;
import advisor.parsers.FeaturedParser;
import advisor.parsers.NewParser;
import advisor.parsers.PlaylistParser;
import advisor.view.Page;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    static SpotifyService service;

    static final Scanner sc = new Scanner(System.in);

    static boolean finished = false;

    static Page view;

    static Config config;


    public static void main(String[] args){
        config = new Config(args);

        service = new SpotifyService(config);

        while (!finished) {
            if(config.isAuthorized()){
                authState();
            } else {
                unAuthState();
            }
        }


    }


    public static void unAuthState(){
        String option = sc.nextLine();

        switch (option) {
            case "auth":
                service.callAuth();
                break;
            case "exit":
                System.out.println("---GOODBYE!---\n");
                finished = true;
                break;
            default:
                System.out.println("Please, provide access for application.");

        }


    }

    public static void authState(){
        String option = sc.nextLine();

        List<String> command = Arrays.asList(option.split("\\s+"));

        switch (command.get(0)) {
            case "auth":
                service.callAuth();
                break;
            case "new":
                view = new Page(config, new NewParser(config).parse());
                view.getPage();
                break;
            case "featured":
                view = new Page(config, new FeaturedParser(config).parse());
                view.getPage();
                break;
            case "categories":
                view = new Page(config, new CategoriesParser(config).parse());
                view.getPage();
                break;
            case "playlists":
                view = new Page(config, new PlaylistParser(config, command.subList(1, command.size())).parse());
                view.getPage();
                break;
            case "prev":
                view.prev();
                break;
            case "next":
                view.next();
                break;
            case "exit":
                System.out.println("---GOODBYE!---\n");
                break;
            default:
                System.out.println("Unknown command");

        }

    }


}
