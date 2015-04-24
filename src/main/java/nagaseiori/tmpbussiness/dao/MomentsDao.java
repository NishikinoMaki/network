package nagaseiori.tmpbussiness.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component("momentsDao")
public class MomentsDao {

	@Resource(name="momentsTemplate")
	private NamedParameterJdbcTemplate momentsTemplate;
	
	private RowMapper<MomentsMsgContent> rowMapper = new BeanPropertyRowMapper<>(MomentsMsgContent.class);
	
	public List<MomentsMsgContent> selectList(){
		try{
			String sql = "select contentId, content from moments_msg_content where contentDetailType in(2,7) ";
			return momentsTemplate.query(sql, rowMapper);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void update(MomentsMsgContent momentsMsgContent){
		String sql = "update moments_msg_content set content=:content where contentId=:contentId limit 1";
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("content", momentsMsgContent.getContent());
		paramSource.addValue("contentId", momentsMsgContent.getContentId());
		momentsTemplate.update(sql, paramSource);
	}
}
