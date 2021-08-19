package app.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import app.bean.CurrentPriceBean;
import app.service.CurrentPriceService;

@RestController
public class CurrentPriceController {
	@Autowired
	private CurrentPriceService cpService;
	
	@GetMapping("/coindesk")
	public String coindesk() throws MalformedURLException, IOException {		
		String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
		InputStream is = new URL(url).openStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String jsonText = readAll(rd);
		JSONObject json = new JSONObject(jsonText);
		JSONObject timeJson = (JSONObject)json.get("time");
		String updated = timeJson.getString("updated");
		updated = updated.replace("UTC", "").trim();
		updated = updated.replace(",", "");		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss", Locale.ENGLISH);
		LocalDateTime ldt = LocalDateTime.parse(updated, formatter);
		formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN);
		updated = ldt.format(formatter);

		JSONObject bpiJson = (JSONObject)json.get("bpi");
		for (String currency : bpiJson.keySet()) {
			JSONObject currencyJson = (JSONObject)bpiJson.get(currency);
			CurrentPriceBean cpBean = new CurrentPriceBean();
			for (String key : currencyJson.keySet()) {
				switch (key) {
					case "code":
						cpBean.setCode(currencyJson.getString(key));
						break;
					case "symbol":
						cpBean.setSymbol(currencyJson.getString(key));
						break;
					case "rate":
						cpBean.setRate(currencyJson.getString(key));
						break;
					case "description":
						cpBean.setDescription(currencyJson.getString(key));
						break;
					case "rate_float":
						cpBean.setRate_float(currencyJson.getDouble(key));
						break;
				}
			}
			cpBean.setUpdated(updated);
			cpService.addData(cpBean);
		}
		return json.toString();
	}
	
	/**
	 * 查詢幣別對應表資料
	 * @param code
	 * @return
	 */
	@GetMapping("/coindesk/{code}")
	public String findById(@PathVariable String code) {
		CurrentPriceBean cpBean = cpService.findByCurrent(code);
		JSONObject dataJson = new JSONObject(cpBean);
		return dataJson.toString();
	}
	
	/**
	 * 更新幣別對應表資料
	 * @param cpBean
	 * @return
	 */
	@PutMapping("/coindesk")
	public String update(@RequestBody CurrentPriceBean cpBean) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN);
		LocalDateTime ldt = LocalDateTime.now(); 
		cpBean.setUpdated(ldt.format(formatter));
		CurrentPriceBean updatedCpBean = cpService.update(cpBean);
		JSONObject dataJson = new JSONObject(updatedCpBean);
		return dataJson.toString();
	}
	
	/**
	 * 新增幣別對應表資料
	 * @param cpBean
	 * @return
	 */
	@PostMapping("/coindesk")
	public String add(@RequestBody CurrentPriceBean cpBean) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN);
		LocalDateTime ldt = LocalDateTime.now(); 
		cpBean.setUpdated(ldt.format(formatter));
		CurrentPriceBean updatedCpBean = cpService.add(cpBean);
		JSONObject dataJson = new JSONObject(updatedCpBean);
		return dataJson.toString();
	}
	
	/**
	 * 刪除幣別對應表資料
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/coindesk/{code}")
	public HttpStatus deletePost(@PathVariable String code) throws Exception {
		cpService.delete(code);
		return HttpStatus.ACCEPTED;
	}
	
	/**
	 * 呼叫coindesk的API，並進行資料轉換，組成新API。 此新API提供： A. 更新時間（時間格式範例：1990/01/01 00:00:00）。 B. 幣別相關資訊（幣別，幣別中文名稱，以及匯率）。
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	@GetMapping("/transfer")
	public String transfer() throws MalformedURLException, IOException {	
		String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
		InputStream is = new URL(url).openStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		String jsonText = readAll(rd);
		JSONObject json = new JSONObject(jsonText);
		JSONObject timeJson = (JSONObject)json.get("time");
		String updated = timeJson.getString("updated");
		updated = updated.replace("UTC", "").trim();
		updated = updated.replace(",", "");		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss", Locale.ENGLISH);
		LocalDateTime ldt = LocalDateTime.parse(updated, formatter);
		formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN);
		updated = ldt.format(formatter);
		
		JSONObject newJson = new JSONObject();
		newJson.put("updated", updated);

		JSONObject bpiJson = (JSONObject)json.get("bpi");
		JSONObject headJson = new JSONObject();
		for (String currencyCode : bpiJson.keySet()) {
			JSONObject currencyJson = (JSONObject)bpiJson.get(currencyCode);
			JSONObject deltailJson = new JSONObject();
			String currency = "";
			for (String key : currencyJson.keySet()) {
				switch (key) {
					case "code":
						currency = currencyJson.getString(key);
						deltailJson.put("currency", currency);
						deltailJson.put("country", cpService.getCountry(currency));
						break;
					case "rate":
						deltailJson.put("rate", currencyJson.getString(key));
						break;
				}
			}
			headJson.append(currency, deltailJson);
		}
		newJson.put("bpi", headJson);
		return newJson.toString();
	}
	
	/**
	 * Json設取
	 * @param rd
	 * @return
	 * @throws IOException
	 */
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }
}
