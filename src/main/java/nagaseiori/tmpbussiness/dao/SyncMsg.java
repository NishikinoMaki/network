package nagaseiori.tmpbussiness.dao;

import java.sql.Blob;

public class SyncMsg {

	private long msg_serial_t;
	private long uid;
	private long client_id;
	private long to_id;
	private Blob pb_body;
	public long getMsg_serial_t() {
		return msg_serial_t;
	}
	public void setMsg_serial_t(long msg_serial_t) {
		this.msg_serial_t = msg_serial_t;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getClient_id() {
		return client_id;
	}
	public void setClient_id(long client_id) {
		this.client_id = client_id;
	}
	public long getTo_id() {
		return to_id;
	}
	public void setTo_id(long to_id) {
		this.to_id = to_id;
	}
	public Blob getPb_body() {
		return pb_body;
	}
	public void setPb_body(Blob pb_body) {
		this.pb_body = pb_body;
	}
	
}
