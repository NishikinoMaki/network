package nagaseiori.tmpbussiness.dao;

public interface Msg {
	public long getMsg_serial_t();
	public void setMsg_serial_t(long msg_serial_t);
	public String getAvatar_url();
	public void setAvatar_url(String avatar_url);
	public long getUid();
	public void setUid(long uid);
	public long getSend_id();
	public void setSend_id(long send_id);
	public long getRecv_id();
	public void setRecv_id(long recv_id);
}
