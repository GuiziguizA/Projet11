package sid.org.exception;

import java.util.Date;

public class ExceptionReponse {
  private Date date;
  private String message;
  private int status;
  private String httpCodeMessage;
public ExceptionReponse() {
	super();
	// TODO Auto-generated constructor stub
}
public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public int getStatus() {
	return status;
}
public void setStatus(int status) {
	this.status =status;
}
public String getHttpCodeMessage() {
	return httpCodeMessage;
}
public void setHttpCodeMessage(String httpCodeMessage) {
	this.httpCodeMessage = httpCodeMessage;
}


} 