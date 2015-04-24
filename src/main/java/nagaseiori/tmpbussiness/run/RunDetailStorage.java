package nagaseiori.tmpbussiness.run;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nagaseiori.commons.SpringContextUtil;
import nagaseiori.tmpbussiness.dao.ChatGroupMsg;
import nagaseiori.tmpbussiness.dao.ChatMsg;
import nagaseiori.tmpbussiness.dao.DetailStorageDao;
import nagaseiori.tmpbussiness.dao.Msg;

import org.apache.commons.lang3.StringUtils;



public class RunDetailStorage {

	public static void main(String[] args){
		DetailStorageDao detailStorageDao = SpringContextUtil.getBean("detailStorageDao", DetailStorageDao.class);
		List<String> chatGroupMsgTables = detailStorageDao.getTableNames(DetailStorageDao.CHAT_GROUP_MSG);
		List<String> chatMsgTables = detailStorageDao.getTableNames(DetailStorageDao.CHAT_MSG);
		
		for(String tableName : chatGroupMsgTables){
			update(tableName, detailStorageDao, ChatGroupMsg.class);
		}
		
		for(String tableName : chatMsgTables){
			update(tableName, detailStorageDao, ChatMsg.class);
		}
	}
	
	public static <T extends Msg> void update(String tableName, DetailStorageDao detailStorageDao,  Class<T> modelType){
		List<T> list = detailStorageDao.selectMsgList(tableName, modelType);
		Map<String,String> urlCache = new HashMap<>();
		for(T msg : list){
			String avatar_url = msg.getAvatar_url();
			String fileKey = null;
			if(urlCache.containsKey(avatar_url)){
				fileKey = urlCache.get(avatar_url);
			}else{
				fileKey = TfsUploadUtil.uploadFile(avatar_url);
				urlCache.put(avatar_url, fileKey);
			}
			if(StringUtils.equals(avatar_url, fileKey)){
				//上传前后的key相同，不用更新
				continue;
			}
			if(fileKey != null){
				msg.setAvatar_url(fileKey);
				//detailStorageDao.update(tableName, msg);
			}
		}
	}
}
