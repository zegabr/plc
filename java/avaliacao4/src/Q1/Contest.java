package Q1;

import java.util.ArrayList;
import java.util.List;

public class Contest extends Thread{
    List<Team> teams;
    Integer numberOfQuestions;
    // lockable winnerToken

    public Contest(Integer numberOfTeams, Integer numberOfQuestions){
        this.teams = new ArrayList<>();
        //this.winner = new lockable
        for(int i = 0; i < numberOfTeams; i++)
            this.teams.add(new Team(i, numberOfQuestions/*, winnnerToken*/));

        this.numberOfQuestions = numberOfQuestions;
    }

    public void start(){
        //should make all teams start at the same time and should watch teams to see which one ends first
        for(Team team : this.teams){
            team.start();
        }
        //winnerToken.lock

        //wait for some team unlock winnerToken
        // lock token

        for(Team team : this.teams){
            // team.stop
        }

        getRank();
    }

    private void getRank() {
        //sort teams by getQuestionsToSolve in increasing order
        System.out.println("Team\tPosition\tSolved");
        for(int i = 0; i < teams.size(); i++){
            System.out.println(teams.get(i).getIndex() + "\t" + i + "\t" + (numberOfQuestions - teams.get(i).getNumberOfQuestionsToSolve()));
        }
    }

    public static void main(String[] args) {
        int N = 10; // number of teams
        int X = 12; // number of questions
        Contest contest = new Contest(N, X);
        contest.start();
    }
}
