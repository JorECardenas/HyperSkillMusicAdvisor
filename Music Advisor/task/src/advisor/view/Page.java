package advisor.view;

import advisor.Config;

import java.util.List;

public class Page {


    List<String> data;

    Config config;

    int currentPage;

    int maxPages;

    public Page() {}


    public Page(Config config, List<String> data){

        this.config = config;

        this.data = data;

        this.maxPages = data.size() / this.config.getPageLimit();

        this.currentPage = 0;

        System.out.println("Data szie: " + data.size());
        System.out.println("Pages: " + maxPages);



    }

    public void getPage(){

        int startPos = currentPage * config.getPageLimit();

        int endPos = Math.min(data.size(), (currentPage + 1) * config.getPageLimit());

        for(int i = startPos; i < endPos; i++){
            System.out.println(data.get(i));
        }

        String footer = "---PAGE " + (currentPage + 1) + " OF " + maxPages + "---\n";

        System.out.println(footer);

    }


    public void prev(){
        if(currentPage == 0){
            System.out.println("No more pages.");
            return;
        }


        currentPage -= 1;

        getPage();

    }

    public void next(){
        if((currentPage + 1) >= maxPages){
            System.out.println("No more pages.");
            return;
        }


        currentPage += 1;

        getPage();
    }











}
