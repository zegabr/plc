package Q1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Contest{
    private List<Team> teams;
    private Integer numberOfQuestions;
    public AtomicInteger winner;// will contain an index >= 1 if some team win

    /**
     * Constructor.
     * @param numberOfTeams
     * @param numberOfQuestions
     */
    public Contest(Integer numberOfTeams, Integer numberOfQuestions){
        this.teams = new ArrayList<>();
        this.winner = new AtomicInteger(0);
        this.numberOfQuestions = numberOfQuestions;
        for(int i = 0; i < numberOfTeams; i++){
            this.teams.add(new Team(i+1,this));
        }
    }

    /**
     * Starts contest.
     */
    public void start(){
        for(Team team : this.teams){
            team.start();
        }

        for(Team team : this.teams){
            try {
                team.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        showRank();
    }

    /**
     * check if contest is running.
     * @return
     */
    public boolean isRunning() {
        return this.winner.get() == 0;
    }

    /**
     * show final rank.
     */
    private void showRank() {
        System.out.println("=================================================");
        System.out.println("Team " + this.winner.get() + " finished first.");
        System.out.println("Position Team Solved");
        Collections.sort(teams, (a,b) -> b.getNumberOfQuestionsSolved() - a.getNumberOfQuestionsSolved());
        for(int i = 0; i < teams.size(); i++){
            System.out.println(i+1 + "         " + teams.get(i).getTeamId() + "     " + teams.get(i).getNumberOfQuestionsSolved());
        }
        System.out.println("=================================================");

    }

    /**
     * Get number of questions in contest
     * @return
     */
    public int getNumberOfQuestions() {
        return this.numberOfQuestions;
    }

    /**
     * get the atomic integer that represents the winner
     * @return
     */
    public AtomicInteger getWinner() {
        return this.winner;
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);

        System.out.println("How many teams are in the contest?");
        int N = in.nextInt(); // number of teams
        System.out.println("How many questions are in the contest?");
        int X = in.nextInt(); // number of questions

        Contest contest = new Contest(N, X);

        Long st = System.currentTimeMillis();
        contest.start();
        System.out.println("total time = " + (System.currentTimeMillis() - st));

        in.close();
    }
}
