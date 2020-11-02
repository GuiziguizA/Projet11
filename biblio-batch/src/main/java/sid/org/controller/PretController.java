package sid.org.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sid.org.batch.PretBatchService;

@RestController
public class PretController {
	@Autowired
	private PretBatchService pretBatchService;

	@PostMapping(value = "/timer")
	public void LancerTimer(@RequestBody Long idPret) {
		pretBatchService.TimerDisponibiliteLivre(idPret);

	}

}
