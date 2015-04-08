package nagaseiori.tmpbussiness.run;

import nagaseiori.http.HttpClientUtil;

public class TestTfs {

	public final static int appid = 1;
	public final static int file_type = 1001;
	public final static int error_code = 0;
	public final static int file_limit = 5242880;//5M
	public final static String test_url = "http://192.168.18.39:81/api/20150209/28ead3c9-6a94-4012-8390-8ff86fdfcb66/img/20150209144624520.jpg";
	
	public static void main(String[] args) {
		byte[] pic  = HttpClientUtil.httpGet4Byte(test_url);
		if(pic.length > file_limit){//超过5M，调用大文件接口
			
		}
		System.out.println(pic.length);
	}
}
