package extra;

/**
 * Created by Adrian on 18/02/2018.
 */

public class People_score {

    private String name;
    private String score;

    public People_score(String n, String s){
        name = n;
        score = s;
    }

    public String getName(){
        return name;
    }

    public String getScore(){
        return score;
    }

    public void setName(String a){
        name = a;
    }

    public void setScore(String c){
        score = c;
    }
}
