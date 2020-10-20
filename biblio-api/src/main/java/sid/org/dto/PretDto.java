package sid.org.dto;

import javax.validation.constraints.NotNull;



/**
 * @author guali
 *
 */
public class PretDto {
	@NotNull
	Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
