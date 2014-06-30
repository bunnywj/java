package httpsample;

import java.io.InputStream;
import java.net.URLDecoder;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Request {
	private InputStream input;
	private String uri;
	private String method;
	private String body;
	private String encoding;

	public Request() {
		this.input = null;
		this.uri = "";
		this.method = "";
		this.body = "";
		this.encoding = "UTF-8";
	}

	public Request(InputStream input) {
		this.input = input;
		this.uri = "";
		this.method = "";
		this.body = "";
		this.encoding = "UTF-8";
	}

	public String getBody() {
		return this.body;
	}

	public String getUri() {
		return this.uri;
	}

	public String getMethod() {
		return this.method;
	}

	public String getEncoding() {
		return this.encoding;
	}

	public void parse() {
		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					this.input));

			System.out
					.println("--------------------------濞村繗顫嶉崳銊ュ絺闁浇顕Ч锟�----------------------------");
			String line = reader.readLine();
			System.out.println("濞村繗顫嶉崳銊ュ絺閺夈儳娈戠拠閿嬬湴鐞涘苯鍞寸�圭櫢绱癨n" + line);
			System.out.println();
			if (line != null) {
				this.uri = parseUri(line);
				this.method = parseMethod(line);

				System.out
						.println("閺嶈宓佺拠閿嬬湴鐞涘本鐗稿寤玕\"Method Request-URI HTTP-Version CRLF\","
								+ "瀵版鍩岄惃鍑磂quest-URI閸愬懎顔愰敍锟� " + uri);
				System.out.println();
				// 瀵邦亞骞嗘潏鎾冲毉濞戝牊浼呮径瀵告畱閸愬懎顔�
				System.out.println("鏉堟挸鍤☉鍫熶紖婢跺娈戦崘鍛啇閿涳拷");
				int bodyLen = 0;
				while (!(line = reader.readLine()).equals("")) {
					System.out.println(line);
					// 婵″倹鐏夊ù蹇氼潔閸ｃ劎顏崣鎴︼拷浣烘畱濞戝牊浼呮稉顓炲瘶閸氱嵆ccept-Charset.閻妲搁崥锕�瀵橀崥顐︾帛鐠併倗绱惍渚婄礉婵″倹鐏夋稉宥呭瘶閸氼偓绱濋崚娆愭箛閸斺�虫珤閹稿鍙嶢ccept-Charset閸欐垿锟戒胶娈戠紓鏍垳閺傜懓绱℃稉锟�
					// 缁涘楠囬張锟芥妯兼畱閺傜懓绱￠崣鎴︼拷锟�
					parseCharset(line);
					// 鐠囬攱鐪版稉绡淥ST閺冭绱濈拠璇插絿body閻ㄥ嫰鏆辨惔锟�
					if (line.startsWith("Content-Length")) {
						bodyLen = Integer.parseInt(line.split(":")[1].trim());
					}
				}
				if (bodyLen != 0) {
					// 閸掋倖鏌囬張澶婂瘶閸氼偆娈慴ody閸愬懎顔愰敍灞藉灟閹稿鍙庨梹鍨鐠囪褰嘼ody鐎涙濡ù锟�
					byte[] bodyContent = new byte[bodyLen];
					for (int i = 0; i < bodyLen; ++i) {
						bodyContent[i] = (byte) reader.read();
					}
					String tmpBody = new String(bodyContent, this.encoding);
					tmpBody = tmpBody.split("&")[0].trim();
					if ((tmpBody.split("=")[0].length() + 1) == tmpBody.length()) {
						tmpBody = "";
					} else {
						tmpBody = tmpBody.split("=")[1].trim();					
					}
					body = URLDecoder.decode(tmpBody, this.encoding);
					System.out.println(body);
				}
				System.out.println();
			}
		} catch (UnsupportedEncodingException ex) {
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private String parseUri(String requestLine) {
		return requestLine.split(" ")[1].trim();
	}

	private String parseMethod(String requestLine) {
		return requestLine.split(" ")[0].trim();
	}

	private void parseCharset(String line) {
		boolean flag = false;
		// 閸掓帒鍨庡☉鍫熶紖閻ㄥ嫬鍞寸�圭櫢绱濋懢宄板絿charset缂傛牜鐖�
		int idx1 = line.indexOf(":");
		if (idx1 != -1
				&& "Accept-Charset".equals(line.substring(0, idx1).trim())) {
			String[] charset = line.split(",");
			for (int i = 0; i < charset.length; ++i) {
				int idx = 0;
				if ((idx = charset[i].indexOf(";")) != -1) {
					charset[i] = charset[i].substring(0, idx);
				}
				if (charset[i].startsWith(encoding)) {
					flag = true;
				}
			}
			if (!flag) {
				encoding = charset[0];
			}
		}
	}

}
