package s21730745;

//Class for holding information about a given action during a given phase. E.g. during Phase.Nominate, Action might = "ABC" where ABC is the team to nominate
public class Action21730745{
	Phase21730745 phase;
	String action;
	
	public Action21730745(Phase21730745 aPhase, String aAction){
		this.phase = aPhase;
		this.action = aAction;
	}
	
	public String getAction(){
		return this.action;
	}
}