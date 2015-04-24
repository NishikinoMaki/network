package nagaseiori.tmpbussiness.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("detailStorageDao")
public class DetailStorageDao {

	@Resource(name="detailStorageTemplate")
	private NamedParameterJdbcTemplate detailStorageTemplate;
	
	@Resource(name="detailStorageDataSource")
	private DataSource detailStorageDataSource;
	
	public final static String CHAT_GROUP_MSG = "chat_group_msg_";
	
	public final static String CHAT_MSG ="chat_msg_";
	
	public <T> List<T> selectMsgList(String tableName, Class<T> rowMapperType){
		return selectList(tableName, new BeanPropertyRowMapper<>(rowMapperType));
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
	
	public <T extends Msg> void update(String tableName, T t){
		MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue("avatar_url", t.getAvatar_url());
		parameterSource.addValue("msg_serial_t", t.getMsg_serial_t());
		parameterSource.addValue("uid", t.getUid());
		parameterSource.addValue("send_id", t.getSend_id());
		parameterSource.addValue("recv_id", t.getRecv_id());
		StringBuilder sql = new StringBuilder();
		sql.append("update ").append(tableName).append(" set avatar_url=:avatar_url where 1=1 and msg_serial_t=:msg_serial_t and uid=:uid and send_id=:send_id and recv_id=:recv_id limit 1");
		detailStorageTemplate.update(sql.toString(), parameterSource);
	}
	
	/** 
	  * 获取指定数据库和用户的所有表名 
	  * @return 
	 * @throws SQLException 
	  */  
	public List<String> getTableNames(String tableNameFilter) {
		List<String> tableNames = new ArrayList<>();
		Connection conn = null;
		try {
			conn = detailStorageDataSource.getConnection();
			DatabaseMetaData dbmd = conn.getMetaData();
			// 表名列表
			String cataLog = conn.getCatalog();
			ResultSet rest = dbmd.getTables(cataLog, null, null, new String[] {"TABLE"});
			// 输出 table_name
			while (rest.next()) {
				String tableName = rest.getString("TABLE_NAME");
				if(StringUtils.contains(tableName, tableNameFilter)){
					tableNames.add(tableName);
				}
			}
			conn.close();
			return tableNames;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
