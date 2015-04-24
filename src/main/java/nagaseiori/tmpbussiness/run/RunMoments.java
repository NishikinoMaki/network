package nagaseiori.tmpbussiness.run;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nagaseiori.commons.SpringContextUtil;
import nagaseiori.tmpbussiness.dao.MomentsDao;
import nagaseiori.tmpbussiness.dao.MomentsMsgContent;

import org.apache.commons.lang3.StringUtils;



public class RunMoments {

	public static void main(String[] args){
		MomentsDao momentsDao = SpringContextUtil.getBean("momentsDao", MomentsDao.class);
		List<MomentsMsgContent> list = momentsDao.selectList();
		if(list == null){
			return;
		}
		Map<String,String> urlCache = new HashMap<>();
		for(MomentsMsgContent momentsMsgContent : list){
			String content = momentsMsgContent.getContent();
			String fileKey = null;
			if(urlCache.containsKey(content)){
				fileKey = urlCache.get(content);
			}else{
				fileKey = TfsUploadUtil.uploadFile(content);
				urlCache.put(content, fileKey);
			}
			if(StringUtils.equals(fileKey, content)){
				continue;
			}
			momentsMsgContent.setContent(fileKey);
			momentsDao.update(momentsMsgContent);
		}
	}
}
