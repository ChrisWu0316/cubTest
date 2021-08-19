package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.AppsApplication;
import app.bean.CurrentPriceBean;

@SpringBootTest
@ContextConfiguration(classes= {AppsApplication.class})
@AutoConfigureMockMvc
public class TestJunit{
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
		
	//1. 測試呼叫查詢幣別對應表資料API，並顯示其內容。
	@Test
	void test1() throws Exception {		
		mockMvc.perform(get("/coindesk/USD"))
		.andExpect(status().isOk())
		.andDo(print());
	}

	//2. 測試呼叫新增幣別對應表資料API。
	@Test
	void test2() throws Exception {
		CurrentPriceBean cpBean = new CurrentPriceBean();
		cpBean.setCode("TTT");
		cpBean.setSymbol("&#36;");
		cpBean.setRate_float(100);
		cpBean.setRate("100");
		cpBean.setDescription("test");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN);
		LocalDateTime ldt = LocalDateTime.now(); 
		cpBean.setUpdated(ldt.format(formatter));
		
		mockMvc.perform(post("/coindesk")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cpBean)))
		.andExpect(status().isCreated())
		.andDo(print());
	}
	
	//3. 測試呼叫更新幣別對應表資料API，並顯示其內容。
	@Test
	void test3() throws Exception {
		CurrentPriceBean cpBean = new CurrentPriceBean();
		cpBean.setCode("USD");
		cpBean.setSymbol("&#36;");
		cpBean.setRate_float(100);
		cpBean.setRate("100");
		cpBean.setDescription("test");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss", Locale.TAIWAN);
		LocalDateTime ldt = LocalDateTime.now(); 
		cpBean.setUpdated(ldt.format(formatter));
		
		mockMvc.perform(put("/coindesk")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cpBean)))
		.andExpect(status().isOk())
		.andDo(print());		
	}
	
	//4. 測試呼叫刪除幣別對應表資料API。
	@Test
	void test4() throws Exception {
		mockMvc.perform(delete("/coindesk/USD"))
		.andExpect(status().isOk())
		.andDo(print());
	}
	
	//5. 測試呼叫coindesk API成功，並顯示其內容。
	@Test
	void test5() throws Exception {
		mockMvc.perform(get("/coindesk"))
		.andExpect(status().isOk())
		.andDo(print());		
	}
	
	//6. 測試呼叫資料轉換的API，並顯示其內容。
	@Test
	void test6() throws Exception {
		mockMvc.perform(get("/transfer"))
		.andExpect(status().isOk())
		.andDo(print());
	}
}
