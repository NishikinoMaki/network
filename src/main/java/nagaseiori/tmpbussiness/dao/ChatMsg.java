package nagaseiori.tmpbussiness.dao;

public class ChatMsg {

	private long msg_serial_t;
	private long uid;
	private long send_id;
	private long recv_id;
	private String avatar_url;
	
	public long getMsg_serial_t() {
		return msg_serial_t;
	}
	public void setMsg_serial_t(long msg_serial_t) {
		this.msg_serial_t = msg_serial_t;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getSend_id() {
		return send_id;
	}
	public void setSend_id(long send_id) {
		this.send_id = send_id;
	}
	public long getRecv_id() {
		return recv_id;
	}
	public void setRecv_id(long recv_id) {
		this.recv_id = recv_id;
	}
	@Override
	public String toString() {
		return "ChatMsg [msg_serial_t=" + msg_serial_t + ", uid=" + uid + ", send_id=" + send_id + ", recv_id=" + recv_id + ", avatar_url=" + avatar_url + "]";
	}
}
