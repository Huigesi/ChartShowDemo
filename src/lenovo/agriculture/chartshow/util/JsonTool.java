package lenovo.agriculture.chartshow.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class JsonTool {
	
	
	public JsonTool() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static  String sendMessage(String url,String postUrl){
		String result="";
		HttpClient mClient=new DefaultHttpClient();
		HttpPost post=new HttpPost(url);
		try {
			post.setEntity(new StringEntity(postUrl));
			HttpResponse response=mClient.execute(post);
			if(response.getStatusLine().getStatusCode()==200){
				result=EntityUtils.toString(response.getEntity());
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			mClient.getConnectionManager().shutdown();
		}
		return result;
		
		
	}

}
