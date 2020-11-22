package Q1;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Team extends Thread {
    List<Competitor> competitors;
    Integer numberOfQuestionsToSolve;
    Integer teamId;
    Integer numberOfQuestionsSolved;
    Contest contest;
    Lock PC;

    public Team(Integer teamId, Contest contest) {
        this.contest = contest;
        this.numberOfQuestionsToSolve = contest.getNumberOfQuestions();
        this.numberOfQuestionsSolved = 0;
        this.teamId = teamId;
        this.PC = new ReentrantLock();
        this.competitors = Arrays.asList(
                new Competitor(1, this),
                new Competitor(2, this),
                new Competitor(3, this)
        );
    }

    /**
     * Start the team as a thread.
     */
    public void run() {
        System.out.println("Team: " + this.teamId + " started");
        try {
            for (Competitor c : competitors) {
                c.start();
            }
            for (Competitor c : competitors) {
                c.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * check if team is playing
     * @return
     */
    public boolean isActive() {
        synchronized (this.contest.getWinner()) {
            return this.contest.isRunning();
        }
    }

    /**
     * atomically submit and accepted solution to contest
     * @param c
     */
    public synchronized void submitQuestion(Competitor c) {
        synchronized (this.contest.getWinner()) {
            if (!this.isActive() || this.numberOfQuestionsToSolve == 0)
                return;

            this.numberOfQuestionsSolved++;
            this.numberOfQuestionsToSolve -= 1;
            System.out.println("Competitor: " + c.getCompetitorId() + " Team: " + this.getTeamId() + " SOLVED: " + this.getNumberOfQuestionsSolved());

            if (this.numberOfQuestionsToSolve == 0) {
                this.contest.getWinner().compareAndSet(0, this.teamId);
            }
        }
    }

    /**
     * number of solved questions of the team
     * @return
     */
    public synchronized Integer getNumberOfQuestionsSolved() {
        return this.numberOfQuestionsSolved;
    }

    /**
     * team identifier
     * @return
     */
    public Integer getTeamId() {
        return this.teamId;
    }

}
