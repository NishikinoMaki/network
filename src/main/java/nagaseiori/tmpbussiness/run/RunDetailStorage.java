package nagaseiori.tmpbussiness.run;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nagaseiori.commons.SpringContextUtil;
import nagaseiori.tmpbussiness.dao.ChatGroupMsg;
import nagaseiori.tmpbussiness.dao.DetailStorageDao;


public class RunDetailStorage {

	public static void main(String[] args){
		
		DetailStorageDao detailStorageDao = SpringContextUtil.getBean("detailStorageDao", DetailStorageDao.class);
		String tableName = DetailStorageDao.chat_group_msg + "30";
		List<ChatGroupMsg> list = detailStorageDao.selectChatGroupMsgList(tableName);
		for(ChatGroupMsg msg : list){
			String avatar_url = msg.getAvatar_url();
			String fileKey = TfsUploadUtil.uploadFile(avatar_url);
			if(StringUtils.equals(avatar_url, fileKey)){
				//上传前后的key相同，不用更新
				continue;
			}
			if(fileKey != null){
				msg.setAvatar_url(fileKey);
				detailStorageDao.updateChatGroupMsg(tableName, msg);
			}
		}
		
		//SyncStorageDao syncStorageDao = SpringContextUtil.getBean("syncStorageDao", SyncStorageDao.class);
		//System.out.println(syncStorageDao.getAllTableNames());
	}
}
