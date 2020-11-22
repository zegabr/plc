package Q1;

public class Competitor extends Thread {

   private Integer competitorId;
   private Team team;

    public Competitor(Integer competitorId, Team team){
        this.competitorId = competitorId;
        this.team = team;
    }

    /**
     * start competitor solving cycle.
     * called on thread start.
     * @throws InterruptedException
     */
    private void solve() throws InterruptedException {
        System.out.println("Competitor: " + competitorId + " Team: " + team.getTeamId() + " IS ALIVE");

        while(team.isActive()) {
            this.solveOnPaper();
            this.solveOnPc();
        }

    }

    /**
     * simulates solving on paper.
     * @throws InterruptedException
     */
    private void solveOnPaper() throws InterruptedException {
        if (!team.isActive())
            return;

        System.out.println("Competitor: " + competitorId + " Team: " + team.getTeamId() + " AT PAPER");
        this.sleep(1500);
    }

    /**
     * simulates solving on pc
     * @throws InterruptedException
     */
    private void solveOnPc() throws InterruptedException {
        this.team.PC.lock();
        if (!team.isActive()){
            this.team.PC.unlock();
            return;
        }
        System.out.println("Competitor: " + competitorId + " Team: " + team.getTeamId() + " AT PC");
        this.sleep(1000);
        team.submitQuestion(this);
        this.team.PC.unlock();

    }

    /**
     * competitor id
     * @return
     */
    public Integer getCompetitorId() {
        return competitorId;
    }

    /**
     * start thread
     */
    public void run(){
        try {
            this.solve();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
