package Q1;

public class Competitor extends Thread {

   private Integer competitorId;
   private Team team;

    public Competitor(Integer competitorId, Team team){
        this.competitorId = competitorId;
        this.team = team;
    }

    private void solve() throws InterruptedException {
        System.out.println("Competitor: " + competitorId + " Team: " + team.getTeamId() + " IS ALIVE");

        while(team.isActive()) {
            this.solveOnPaper();
            this.solveOnPc();
        }

    }

    private void solveOnPaper() throws InterruptedException {
        if (!team.isActive())
            return;

        System.out.println("Competitor: " + competitorId + " Team: " + team.getTeamId() + " AT PAPER");
        this.sleep(1500);
    }

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

    public Integer getCompetitorId() {
        return competitorId;
    }

    public void run(){
        try {
            this.solve();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
