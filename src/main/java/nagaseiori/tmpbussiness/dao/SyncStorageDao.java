package nagaseiori.tmpbussiness.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;

@Repository("syncStorageDao")
public class SyncStorageDao {

	RowMapper<SyncMsg> rowMapper = new BeanPropertyRowMapper<>(SyncMsg.class);
	
	@Resource(name="syncStorageTemplate")
	private NamedParameterJdbcTemplate syncStorageTemplate;
	@Resource(name="syncStorageDataSource")
	private DataSource syncStorageDataSource;
	
	LobHandler lobHandler = new DefaultLobHandler();
	
	/** 
	  * 获取指定数据库和用户的所有表名 
	  * @return 
	 * @throws SQLException 
	  */  
	public List<String> getAllTableNames() {
		List<String> tableNames = new ArrayList<>();
		Connection conn = null;
		try {
			conn = syncStorageDataSource.getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();
			// 表名列表
			String cataLog = conn.getCatalog();
			ResultSet rest = dbmd.getTables(cataLog, null, null, new String[] {"TABLE"});
			// 输出 table_name
			while (rest.next()) {
				tableNames.add(rest.getString("TABLE_NAME"));
			}
			conn.close();
			return tableNames;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<SyncMsg> selectList(String tableName){
		try{
			StringBuilder sql = new StringBuilder();
			sql.append("select msg_serial_t, uid, to_id, client_id, pb_body from ").append(tableName).append(" where 1=1 and is_valid=1");
			return syncStorageTemplate.query(sql.toString(), rowMapper);
		}catch(Exception e){
			return null;
		}
	}
	
	public void update(String tableName, SyncMsg syncMsg, byte[] pb_body){
		try{
			StringBuilder sql = new StringBuilder();
			sql.append("update ").append(tableName).append(" set pb_body=:pb_body, pb_body_len=:pb_body_len where 1=1 and msg_serial_t=:msg_serial_t and uid=:uid and client_id=:client_id and to_id=to_id limit 1");
			MapSqlParameterSource parameterSource = new MapSqlParameterSource();
			parameterSource.addValue("pb_body", new SqlLobValue(pb_body, lobHandler) , Types.BLOB);
			parameterSource.addValue("pb_body_len", pb_body.length);
			parameterSource.addValue("msg_serial_t", syncMsg.getMsg_serial_t());
			parameterSource.addValue("uid", syncMsg.getUid());
			parameterSource.addValue("client_id", syncMsg.getClient_id());
			parameterSource.addValue("to_id", syncMsg.getTo_id());
			syncStorageTemplate.update(sql.toString(), parameterSource);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
