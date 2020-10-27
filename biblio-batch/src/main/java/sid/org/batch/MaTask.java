package sid.org.batch;

import java.util.TimerTask;

public class MaTask extends TimerTask {

	private Long idPret;

	public MaTask(Long idPret) {
		super();
		this.idPret = idPret;
	}

	@Override
	public void run() {
		System.out.println("task est fonctionnel");

	}

}
