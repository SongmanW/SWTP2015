package issuetracking;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PictureFile {
	
	protected int pictureId;
	protected int ticketId;
	protected Date upload_date;
	protected String uploader;
	protected String type;
	
	/**
	 * 
	 * @param pictureId the unique Id of the picture
	 * @param ticketId the Id of the ticket the picture is attached to
	 * @param upload_date the upload date
	 * @param uploader the uploader
	 * @param type the type of the image
	 */
	public PictureFile(int pictureId, int ticketId, Date upload_date, String uploader, String type) {
		this.pictureId = pictureId;
		this.ticketId = ticketId;
		this.upload_date = upload_date;
		this.uploader = uploader;
		this.type = type;
	}

	/**
	 * @return the pictureId
	 */
	public int getPictureId() {
		return pictureId;
	}

	/**
	 * @param pictureId the pictureId to set
	 */
	public void setPictureId(int pictureId) {
		this.pictureId = pictureId;
	}

	/**
	 * @return the Id of the ticket the picture is attached to
	 */
	public int getTicketId() {
		return ticketId;
	}

	/**
	 * @param ticketId the Id of the ticket the picture is attached to
	 */
	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	/**
	 * @return the uploaddate
	 */
	public Date getUpload_date() {
		return upload_date;
	}

	/**
	 * @param upload_date the uploaddate
	 */
	public void setUpload_date(Date upload_date) {
		this.upload_date = upload_date;
	}

	/**
	 * @return the uploader
	 */
	public String getUploader() {
		return uploader;
	}

	/**
	 * @param uploader the uploader
	 */
	public void setUploader(String uploader) {
		this.uploader = uploader;
	}

	/**
	 * @return the image type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type of the image file
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the upload date for database usage
	 */
	public String getUploadDateAsStringForDatabase() {
		SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		String date=ft.format(upload_date);
		return date;
	}

	/**
	 * @return the upload date as a String
	 */
	public String getUploadDateAsString() {
		SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy HH:mm:ss");
		String date=ft.format(upload_date);
		return date;
	}
	
	/**
	 * Deletes the picture from the file system
	 */
	public void delete() {
		new File(DBManager.getFilesPath() + File.separator + this.pictureId + "." + this.type).delete();
	}

	
	
}
