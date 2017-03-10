package org.mattreyuk.adtech.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.mattreyuk.adtech.domain.AdMessage;
import org.mattreyuk.adtech.domain.Transaction;
import org.mattreyuk.adtech.service.AdtechService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdtechController {

	  private AdtechService service;
  	  private static final Logger LOGGER = LoggerFactory.getLogger(AdtechController.class);

  @Autowired
  public AdtechController(AdtechService service){
	  this.service=service;
  }

  @ApiOperation(value = "Get an ad", 
	notes = "Returns a tid uuid and html for the winning ad")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "AdMessage")})
  @GetMapping(path="/ad", produces="application/json")
  public AdMessage getAd(@ApiParam(value = "ad width", required=true) @RequestParam(value="width", required = true) Integer width,
	      @ApiParam(value = "ad height", required=true) @RequestParam(value="height", required = true) Integer height,
	      @ApiParam(value = "user id", required=true) @RequestParam(value="userid", required = true) Integer userid,
	      @ApiParam(value = "url of page for ad", required=true) @RequestParam(value="url", required = true) String url,
	      @RequestHeader(value="User-Agent") String userAgent,
	      HttpServletRequest request
      ) throws IOException {
    
	LOGGER.debug("finding ad for user:{} width:{} height:{} url:{}",userid,width,height,url);
	URL decodedUrl = new URL(URLDecoder.decode(url,"UTF-8"));
    return service.findAd(userid,width,height,decodedUrl,userAgent,request.getRemoteAddr());
  }

  @ApiOperation(value = "Register click", 
	notes = "used to show an ad was clicked on")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Void")})
  @GetMapping(path="/click", produces="application/json")
  public Void clickAd(@ApiParam(value = "tid (uuid)", required=true) @RequestParam(value="tid", required = true) UUID tid,
	      @ApiParam(value = "user id", required=true) @RequestParam(value="userid", required = true) Integer userid
      ) {
    
	LOGGER.debug("registering click for ad tid: {} on user:{}",tid,userid);
    return service.registerClick(tid,userid);
  }

  @ApiOperation(value = "Get transaction history", 
	notes = "Returns an array of upto 100 of the last transactions")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "List<Transaction>")})
  @GetMapping(path="/history", produces="application/json")
  public List<Transaction> getTransactionHistory() {
    
	LOGGER.debug("getting transaction history");
    return service.getTransactions();
  }

}
