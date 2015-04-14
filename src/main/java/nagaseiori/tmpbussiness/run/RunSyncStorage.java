package nagaseiori.tmpbussiness.run;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import nagaseiori.commons.SpringContextUtil;
import nagaseiori.tmpbussiness.dao.SyncMsg;
import nagaseiori.tmpbussiness.dao.SyncStorageDao;

import org.apache.commons.lang3.StringUtils;

import common.Common.chat_body_msg;
import common.Common.msg_content;

public class RunSyncStorage {

	final static String[] extFilter = new String[]{".jpg",".JPG",".bmp",".BMP",".png", ".PNG",".jpeg", ".JPEG",".gif", ".GIF" ,".spx", ".SPX"};
	
	public static void main(String[] args) throws SQLException, Exception {
		SyncStorageDao syncStorageDao = SpringContextUtil.getBean("syncStorageDao", SyncStorageDao.class);
		List<SyncMsg> list = syncStorageDao.selectList("262660_27");
		for(SyncMsg syncMsg : list){
			Blob pbBody = syncMsg.getPb_body();
			chat_body_msg body_msg = chat_body_msg.parseFrom(pbBody.getBytes(1, (int)pbBody.length()));
			msg_content msg = body_msg.getMsg();
			String avatar_url = msg.getAvatarUrl().toStringUtf8();
			String msgText = msg.getMsg().toStringUtf8();
			int index = StringUtils.indexOfAny(msgText, extFilter);
			String msgKey = null;
			if(index > -1){//符合过滤条件
				msgKey = TfsUploadUtil.uploadFile(msgText);
			}
			String avatarKey = TfsUploadUtil.uploadFile(avatar_url);
			//上传完成，重新组包
		}
	}
}
