package constellation;

/*
 * Setup:
 * connect hue to network
 * https://www.meethue.com/api/nupnp
 * http://<bridge ip address>/debug/clip.html
 * GET http://<bridge ip address>/api/newdeveloper
 * POST http://<bridge ip address>/api {"devicetype":"my_hue_app#iphone peter"}
 * press the button
 * repeat last post
 * print response and save username
 */
import java.io.*;
import java.net.*;

public class HueControl {

	String username;
	String url;

	HueControl(String username, String url) {
		this.username = username;
		this.url = url;
	}

	public void printStatus() throws Exception {
		System.out.println(sendGET(url + username + "/lights"));
	}

	public String getJsonStatus() throws Exception {
		return sendGET(url + username + "/lights");
	}

	public void setJsonStatus(String payload) throws Exception {
		sendPUT(url + username + "/lights", payload);
	}

	public void printStatus(int ID) throws Exception {
		System.out.println(sendGET(url + username + "/lights/" + ID + ""));
	}

	public String getLightJson(int ID) throws Exception {
		return sendGET(url + username + "/lights/" + ID + "");
	}

	public void setLightJson(int ID, String payload) throws Exception {
		sendPUT(url + username + "/lights/" + ID + "/state", payload);
	}

	public void turnLightOn(int ID) throws Exception {
		String payload = "{\"on\":true}";
		sendPUT(url + username + "/lights/" + ID + "/state", payload);
	}

	public void turnLightOff(int ID) throws Exception {
		String payload = "{\"on\":false}";
		String rurl = url + username + "/lights/" + ID + "/state";
		sendPUT(rurl, payload);
	}

	public void setAll(int ID, String hue, String sat, String bri) throws Exception {
		String payload = "{\"hue\":" + hue + ",\"sat\":" + sat + ",\"bri\":" + bri + "}";
		String rurl = url + username + "/lights/" + ID + "/state";
		sendPUT(rurl, payload);
	}

	public void setHue(int ID, String hue) throws Exception {
		String payload = "{\"hue\":" + hue + "}";
		sendPUT(url + username + "/lights/" + ID + "/state", payload);
	}

	public void setSat(int ID, String sat) throws Exception {
		String payload = "{\"sat\":" + sat + "}";
		sendPUT(url + username + "/lights/" + ID + "/state", payload);
	}

	public void setBri(int ID, String bri) throws Exception {
		String payload = "{\"bri\":" + bri + "}";
		sendPUT(url + username + "/lights/" + ID + "/state", payload);
	}

	public void incBri(int ID, String bri_inc) throws Exception {
		String payload = "{\"bri_inc\":" + bri_inc + "}";
		sendPUT(url + username + "/lights/" + ID + "/state", payload);
	}

	public void colorloopOn(int ID) throws Exception {
		String payload = "{\"effect\": \"colorloop\"}";
		sendPUT(url + username + "/lights/" + ID + "/state", payload);
	}

	public void colorloopOff(int ID) throws Exception {
		String payload = "{\"effect\": \"none\"}";
		sendPUT(url + username + "/lights/" + ID + "/state", payload);
	}

	public void alert(int ID) throws Exception {
		String payload = "{\"alert\": \"select\"}";
		sendPUT(url + username + "/lights/" + ID + "/state", payload);
	}

	public void printConfig() throws Exception {
		System.out.println(sendGET(url + username + "/config"));
	}

	private void sendPUT(String requesturl, String payload) throws Exception {
		URL connectionUrl = new URL(requesturl);
		HttpURLConnection conn = (HttpURLConnection) connectionUrl.openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestMethod("PUT");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		OutputStreamWriter dataOutputStream = new OutputStreamWriter(conn.getOutputStream());
		dataOutputStream.write(payload);
		if (dataOutputStream != null) {
			try {
				dataOutputStream.flush();
				dataOutputStream.close();
				conn.getResponseCode();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}
		if (conn != null) {
			conn.disconnect();
		}

	}

	private String sendGET(String requesturl) throws Exception {
		StringBuilder response = new StringBuilder();
		URL connectionUrl = new URL(requesturl);
		HttpURLConnection conn = (HttpURLConnection) connectionUrl.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			response.append(line);
		}
		rd.close();
		if (conn != null) {
			conn.disconnect();
		}
		return response.toString();

	}
	
	// dirty way to parse JSON
	public String extractState(String jsonResponse) {
		String result="{";
		int pos=10;
		int bracketcounter=1;
		System.out.println(jsonResponse.substring(pos, pos+1));
		while(pos<jsonResponse.length()) {
			if(jsonResponse.substring(pos, pos+1).equals("{")){
				bracketcounter++;
			}else if (jsonResponse.substring(pos, pos+1).equals("}")) {
				bracketcounter--;
			}
			result+=jsonResponse.substring(pos, pos+1);
			if(bracketcounter==0){
				break;
			}
			pos++;
		}
		return result;
	}
}
