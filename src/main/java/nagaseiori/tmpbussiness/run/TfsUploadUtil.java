package nagaseiori.tmpbussiness.run;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nagaseiori.http.HttpClientUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TfsUploadUtil {

	public final static String appid = "1";
	public final static String file_type = "1001";
	public final static String expiry_date = "0";
	public final static String public_type = "2";
	public final static int error_code = 0;
	public final static int file_limit = 5242880;//5M
	public final static String test_url = "http://192.168.18.39:81/api/20150209/28ead3c9-6a94-4012-8390-8ff86fdfcb66/img/20150209144624520.jpg";
	public final static String upload_url = "/api/file/upload_smallfile/general";
	public final static String download_url = "/api/file/download/direct/general";
	public final static String host = "http://192.168.18.22:8282";
	public final static String error_code_name = "error_code";
	public final static String reply = "reply";
	public final static String file_key = "file_key";
	public final static String test_token = "1";
	
	public static String uploadFile(String fileUrl){
		byte[] file  = HttpClientUtil.httpGet4Byte(fileUrl);
		String sha1 = DigestUtils.sha1Hex(file);
		return uploadNormalFile(file, sha1, fileUrl);
	}
	
	public static String uploadNormalFile(byte[] file, String sha1, String fileUrl){
		String file_name = getFileName(fileUrl);
		//MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		//multipartEntityBuilder.addBinaryBody("file", file);//添加包体文件
		//HttpEntity multipartFormEntity = multipartEntityBuilder.build();
		HttpEntity byteArrayEntity = new ByteArrayEntity(file);
		Map<String,String> params = new HashMap<String,String>();
		params.put("token", test_token);
		params.put("file_name", file_name);
		params.put("file_type", file_type);
		params.put("file_size", String.valueOf(file.length));
		params.put("expiry_date", expiry_date);
		params.put("appid", appid);
		params.put("sha1", sha1);
		params.put("public_type", public_type);
		try {
			String url = host + upload_url;
			String result = HttpClientUtil.httpPostSetParamInHeaders(url , byteArrayEntity, params, "UTF-8");
			if(result != null && StringUtils.contains(result, "error_code")){
				JSONObject replyObj = JSON.parseObject(result);
				JSONObject replyText = (JSONObject) replyObj.get(reply);
				String fileKey = (String) replyText.get(file_key);
				return fileKey;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getFileName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			if(!filename.contains("http://")){
				return filename;
			}
			int dot = filename.lastIndexOf('/');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}
	
	public static void download(String key){
		String url = host + download_url + "?file_key="+key;
		String content = HttpClientUtil.httpGet(url);
		System.out.println(content);
	}
	
	public static void main(String[] args) throws IOException {
//		File f = new File("E:\\file_test\\test.txt");
//		InputStream is = new FileInputStream(f);
//		StringBuilder str = new StringBuilder();
//		int b = 1;
//		while((b = is.read()) != -1){
//			str.append((char)b);
//		}
//		is.close();
//		byte[] file = str.toString().getBytes();
//		String sha1 = DigestUtils.sha1Hex(file);
//		System.out.println(str);
//		String key = uploadNormalFile(file , sha1, "test.txt");
//		System.out.println(key);
//		download("2T1qzdTByJT1RCvBVdK_0_1_1001_jpg");
	}
}
