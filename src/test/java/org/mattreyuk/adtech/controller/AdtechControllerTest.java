package org.mattreyuk.adtech.controller;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mattreyuk.adtech.domain.AdMessage;
import org.mattreyuk.adtech.domain.Bid;
import org.mattreyuk.adtech.domain.Transaction;
import org.mattreyuk.adtech.domain.Transaction.ClickResult;
import org.mattreyuk.adtech.service.AdtechService;
import org.mockito.Mockito;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@RunWith(SpringRunner.class)
@WebMvcTest(AdtechController.class)
public class AdtechControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private AdtechService mockService;

	@Test
	public void testCallAd() throws Exception {
		UUID tid = UUID.randomUUID();
		String html = "<h1>ad!</h1>";
		Integer userid = 10;
		Integer width = 200;
		Integer height = 300;
		URL url = new URL("http://user.com/page");
		String userAgent = "browser-agent";
		String userip = "2.2.2.2";

		given(this.mockService.findAd(userid, width, height, url, userAgent, userip)).
			willReturn(AdMessage.builder().tid(tid).html(html).build());

		this.mvc.perform(
				get("/ad?width=200&height=300&userid=10&url=http%3A%2F%2Fuser.com%2Fpage").header("User-Agent",
						userAgent)
						.header("Accept", MediaType.APPLICATION_JSON)
						.with(new RequestPostProcessor() {
							public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
								request.setRemoteAddr("2.2.2.2");
								return request;
							}
						}))
				.andExpect(status().isOk())
				.andExpect(content().json("{'tid':'" + tid + "','html':'" + html + "'}"));

	}
	
	@Test
	public void testCallClick() throws Exception{
		UUID tid = UUID.randomUUID();
		Integer userid = 10;
		
		this.mvc.perform(get("/click?tid="+tid+"&userid="+userid)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		Mockito.verify(mockService).registerClick(Matchers.eq(tid), Matchers.eq(userid),Matchers.any(LocalDateTime.class));
		
	}

	@Test
	public void testCallHistory() throws Exception{
		BigDecimal bidPrice= new BigDecimal("0.0035");
		Integer providerId=20;
		UUID transactionId=UUID.randomUUID();
		Integer userid=10;
		ClickResult clickResult = ClickResult.CLICK;
		BigDecimal winningPrice= new BigDecimal("0.0035");
		ArrayList<Transaction> history = new ArrayList<Transaction>();
		history.add(Transaction.builder().
				bid(Bid.builder().bidPrice(bidPrice).providerId(providerId).build()).
				transactionId(transactionId).userid(userid).clickResult(clickResult).
				winningPrice(winningPrice).winningProvider(providerId).build());
		
		given(this.mockService.getTransactions()).
		willReturn((List<Transaction>)history);

		this.mvc.perform(get("/history")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("[{'transactionId':'"+transactionId+"','userid':"+
				userid+",'bids':[{'providerId':"+providerId+",'bidPrice':"+bidPrice+"}],"+
				"'winningPrice':"+winningPrice+",'winningProvider':"+providerId+",'clickResult':'"+
				clickResult+"'}]"));

	}

}
