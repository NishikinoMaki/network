package nagaseiori.tmpbussiness.run;

import java.util.List;

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
			if(fileKey != null){
				msg.setAvatar_url(fileKey);
				detailStorageDao.updateChatGroupMsg(tableName, msg);
			}
		}
		
		//SyncStorageDao syncStorageDao = SpringContextUtil.getBean("syncStorageDao", SyncStorageDao.class);
		//System.out.println(syncStorageDao.getAllTableNames());
	}
}
