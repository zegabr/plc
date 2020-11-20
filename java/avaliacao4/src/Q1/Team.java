package Q1;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Team extends Thread {
    List<Competitor> competitors;
    Integer numberOfQuestionsToSolve;
    Integer id;
    Integer questionsSolved;
    Contest contest;
    Lock PC;

    public Team(Integer id, Contest contest) {
        this.contest = contest;
        this.numberOfQuestionsToSolve = contest.getNumberOfQuestions();
        this.questionsSolved = 0;
        this.id = id;
        this.PC = new ReentrantLock();
        this.competitors = Arrays.asList(
                new Competitor(1, this),
                new Competitor(2, this),
                new Competitor(3, this)
        );
    }

    public void run() {
        System.out.println("Team: " + this.id + " started");
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

    public boolean isActive() {
        synchronized (this.contest.getWinner()) {
            return this.contest.isRunning();
        }
    }

    public synchronized void submitQuestion(Competitor c) {
        synchronized (this.contest.getWinner()) {
            if (!this.isActive() || this.numberOfQuestionsToSolve == 0)
                return;

            this.questionsSolved++;
            this.numberOfQuestionsToSolve -= 1;
            System.out.println("Competitor: " + c.getCompetitorId() + " Team: " + this.getTeamId() + " SOLVED: " + this.getQuestionsSolved());

            if (this.numberOfQuestionsToSolve == 0) {
                this.contest.getWinner().compareAndSet(0, this.id);
            }
        }
    }

    private synchronized Integer getQuestionsSolved() {
        return this.questionsSolved;
    }

    public Integer getTeamId() {
        return this.id;
    }

    public Integer getNumberOfQuestionsToSolve() {
        return this.numberOfQuestionsToSolve;
    }

    public int getSolvedQuestions() {
        return contest.getNumberOfQuestions() - this.getNumberOfQuestionsToSolve();
    }

}
