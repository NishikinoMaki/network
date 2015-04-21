package nagaseiori.tmpbussiness.run;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import nagaseiori.http.HttpClientUtil;

import org.apache.commons.codec.binary.Hex;
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
	public final static String public_type = "1";
	public final static int error_code = 0;
	public final static int file_limit = 5242880;//5M
	public final static String test_url = "http://192.168.18.39:81/api/20150209/28ead3c9-6a94-4012-8390-8ff86fdfcb66/img/20150209144624520.jpg";
	public final static String upload_url = "/api/file/upload_smallfile/general";
	public final static String download_url = "/api/file/download/direct/general";
	public final static String host = "http://192.168.18.22:8282";
	public final static String error_code_name = "error_code";
	public final static String reply = "data";
	public final static String file_key = "file_key";
	public final static String test_token = "54A1E48103FE7328";
	final static String[] extFilter = new String[]{".jpg",".JPG",".bmp",".BMP",".png", ".PNG",".jpeg", ".JPEG",".gif", ".GIF" ,".spx", ".SPX"};
	final static Map<String, Long> blackIpFilter = new HashMap<>();//ip过滤
	final static long blackIpLimitTime = 3600000;//ip拉黑时间1小时
	
	public static String uploadFile(String fileUrl){
		String result = fileUrl;
		String url = fileUrl;
		if(StringUtils.isEmpty(url)){
			return result;
		}
		if(StringUtils.indexOf(url, "http://") == -1){
			return result;
		}
		if(StringUtils.indexOfAny(url, extFilter) == -1){
			return result;
		}
		String host = getHost(url);
		if(blackIpFilter.containsKey(host)){
			long putTime = blackIpFilter.get(host);
			long now = System.currentTimeMillis();
			if(now - putTime < blackIpLimitTime){//该ip在被拉黑阶段，直接返回，不建立http连接
				return result;
			}
		}
		byte[] file =  HttpClientUtil.httpGet4Byte(fileUrl);
		if(file == null){
			blackIpFilter.put(host, System.currentTimeMillis());
			return result;
		}
		String sha1 = sha1Tfs(file);
		return uploadNormalFile(file, sha1, result);
	}
	
	public static String uploadNormalFile(byte[] file, String sha1, String fileUrl){
		String file_name = getFileName(fileUrl);
		//MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
		//multipartEntityBuilder.addBinaryBody("file", file);//添加包体文件
		//HttpEntity multipartFormEntity = multipartEntityBuilder.build();
		HttpEntity byteArrayEntity = new ByteArrayEntity(file);
		Map<String,String> params = new HashMap<>();
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
			System.out.println(result);
			if(result != null && StringUtils.contains(result, "code")){
				JSONObject replyObj = JSON.parseObject(result);
				JSONObject replyText = (JSONObject) replyObj.get(reply);
				String fileKey = (String) replyText.get(file_key);
				return fileKey;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
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
	
	private static String getHost(String url){
		try{
			int beginIndex = url.indexOf("http://");
			if(beginIndex == -1){
				return null;
			}
			String substring = url.replaceFirst("http://", "");
			int endIndex = StringUtils.indexOf(substring, "/");
			return substring.substring(0, endIndex);
		}catch(Exception e){
			return null;
		}
	}
	
	public static void download(String key){
		String url = host + download_url + "?file_key="+key;
		String content = HttpClientUtil.httpGet(url);
		System.out.println(content);
	}
	
	private static String sha1Tfs(byte[] data){
		MessageDigest digest = DigestUtils.getSha1Digest();
		digest.update(data);
		String text = new String(Hex.encodeHex(data, false)).substring(0,10);
		return DigestUtils.sha1Hex(data);
		//return text;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(sha1Tfs("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes()));
	}
}
