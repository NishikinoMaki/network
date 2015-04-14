package nagaseiori.tmpbussiness.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("detailStorageDao")
public class DetailStorageDao {

	@Resource(name="detailStorageTemplate")
	private NamedParameterJdbcTemplate detailStorageTemplate;
	
	public final static String chat_group_msg = "chat_group_msg_";
	
	public final static String chat_msg_="chat_msg_";
	
	RowMapper<ChatMsg> chatMsgRowMapper = new BeanPropertyRowMapper<ChatMsg>(ChatMsg.class);
	RowMapper<ChatGroupMsg> chatGroupRowMapper = new BeanPropertyRowMapper<ChatGroupMsg>(ChatGroupMsg.class);
	
	
	public List<ChatMsg> selectChatMsgList(String tableName){
		return selectList(tableName, chatMsgRowMapper);
	}
	
	public List<ChatGroupMsg> selectChatGroupMsgList(String tableName){
		return selectList(tableName, chatGroupRowMapper);
	}
	
	<T> List<T> selectList(String tableName, RowMapper<T> rowMapper){
		try{
			StringBuilder sql = new StringBuilder();
			sql.append("select msg_serial_t, uid, send_id, recv_id, avatar_url from ").append(tableName).append(" where 1=1 and avatar_url is not null and avatar_url != ''");
			return detailStorageTemplate.query(sql.toString(), rowMapper);
		}catch(Exception e){
			return null;
		}
	}
	
	public void updateChatMsg(String tableName, ChatMsg chatMsg){
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("avatar_url", chatMsg.getAvatar_url());
		parameterSource.addValue("msg_serial_t", chatMsg.getMsg_serial_t());
		parameterSource.addValue("uid", chatMsg.getUid());
		parameterSource.addValue("send_id", chatMsg.getSend_id());
		parameterSource.addValue("recv_id", chatMsg.getRecv_id());
		update(tableName, parameterSource);
	}
	
	public void updateChatGroupMsg(String tableName, ChatGroupMsg chatGroupMsg){
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("avatar_url", chatGroupMsg.getAvatar_url());
		parameterSource.addValue("msg_serial_t", chatGroupMsg.getMsg_serial_t());
		parameterSource.addValue("uid", chatGroupMsg.getUid());
		parameterSource.addValue("send_id", chatGroupMsg.getSend_id());
		parameterSource.addValue("recv_id", chatGroupMsg.getRecv_id());
		update(tableName, parameterSource);
	}
	
	<T> void update(String tableName, MapSqlParameterSource parameterSource){
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(tableName).append(" set avatar_url=:avatar_url where 1=1 and msg_serial_t=:msg_serial_t and uid=:uid and send_id=:send_id and recv_id=:recv_id limit 1");
		detailStorageTemplate.update(sql.toString(), parameterSource);
	}
}
