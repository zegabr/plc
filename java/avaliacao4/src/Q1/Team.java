package Q1;

import java.util.Arrays;
import java.util.List;

public class Team extends Thread{
    List<Competitor> competitors;
    Integer numberOfQuestions;
    Integer index;
    // lockable winnerToken
    // lockable PC; must be usable by only one competitor at a time

    public Team(Integer index, Integer numberOfQuestions/*, winnerToken*/){
        this.numberOfQuestions = numberOfQuestions;
        this.index = index;
        competitors = Arrays.asList(new Competitor(), new Competitor(), new Competitor());
        // PC = new PC
        System.out.println("Team " + index + " initialized");
    }

    public void start(){
        System.out.println("Team " + this.index + " started.");
        while(this.numberOfQuestions > 0 /* and winnertoken is locked*/){
            for(Competitor c : competitors){
                if(!c.isSolving()){
                    this.putCompetitorToSolve(c);
                }
            }
        }
        System.out.println("Team " + this.index + " ended first.");
        //unlock this.winnerToken

        for(Competitor c : competitors){
            //stop threads
        }
    }

    private void putCompetitorToSolve(Competitor c) {
        c.trySolveWith(/*lockable PC*/);
        //join c;
        this.numberOfQuestions -= 1;
    }


    public Integer getIndex() {
        return this.index;
    }

    public Integer getNumberOfQuestionsToSolve() {
        return this.numberOfQuestions;
    }
}
