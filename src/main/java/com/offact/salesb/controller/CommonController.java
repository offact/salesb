package com.offact.salesb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Random;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.security.SecureRandom;
import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.offact.framework.constants.CodeConstant;
import com.offact.framework.exception.BizException;
import com.offact.framework.jsonrpc.JSONRpcService;
import com.offact.framework.util.StringUtil;
import com.offact.framework.util.XmlUtil;
import com.offact.salesb.service.CustomerService;
import com.offact.salesb.service.common.CommonService;
import com.offact.salesb.service.common.CommonService;
import com.offact.salesb.service.common.SmsService;
import com.offact.salesb.service.comunity.AsService;
import com.offact.salesb.service.comunity.ComunityService;
import com.offact.salesb.vo.CustomerVO;
import com.offact.salesb.vo.common.GroupVO;
import com.offact.salesb.vo.common.SmsVO;
import com.offact.salesb.vo.comunity.AsVO;
import com.offact.salesb.vo.comunity.ComunityVO;

/**
 * Handles requests for the application home page.
 */
@Controller

public class CommonController {

	private final Logger logger = Logger.getLogger(getClass());
	/*
    * log id 생성 
    */
	public String logid(){
		
		double id=Math.random();
		long t1 = System.currentTimeMillis ( ); 
		
		String logid=""+t1+id;
		
		return logid;
	}
	
    @Value("#{config['offact.domain.url']}")
    private String domainUrl;
    
    @Value("#{config['offact.host.url']}")
    private String hostUrl;
    
    @Value("#{config['oauth.redirect.url']}")
    private String redirectUrl;
    
    @Value("#{config['oauth.redirect2.url']}")
    private String redirectUrl2;
    
    @Value("#{config['oauth.kakao.client_id']}")
    private String kakaoclient_id;
    
    @Value("#{config['oauth.naver.client_id']}")
    private String naverclient_id;
    
    @Value("#{config['oauth.naver.client_secret']}")
    private String naverclient_secret;
    
    @Value("#{config['oauth.facebook.fbAppId']}")
    private String facebookfbAppId;
    
    @Value("#{config['oauth.google.client_id']}")
    private String googleclient_id;
    
    @Value("#{config['oauth.pinterest.client_id']}")
    private String pinterestclient_id;
	
    @Value("#{config['offact.dev.option']}")
    private String devOption;
    
	@Value("#{config['offact.dev.sms']}")
    private String devSms;
    
    @Value("#{config['offact.sms.smsid']}")
    private String smsId;
    
    @Value("#{config['offact.sms.smspw']}")
    private String smsPw;
    
    @Value("#{config['offact.sms.smstype']}")
    private String smsType;
    
    @Value("#{config['offact.sms.sendno']}")
    private String sendno;
    
    @Autowired
    private CommonService commonSvc;
    
	@Autowired
	private CustomerService customerSvc;
	
	@Autowired
	private ComunityService comunitySvc;
	
    @Autowired
    private SmsService smsSvc;
    
    @Autowired
    private AsService asSvc;
    
    public String generateState()
    {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
    
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws BizException
	 */
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView admin(HttpServletRequest request,
			                   HttpServletResponse response,  
			                   Model model, 
			                   Locale locale) throws BizException 
	{

		logger.info("Welcome admin");
		
		ModelAndView  mv = new ModelAndView();

    	mv.setViewName("/admin/index");

		return mv;
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws BizException
	 */
	@RequestMapping(value = "/intro", method = RequestMethod.GET)
	public ModelAndView intro(HttpServletRequest request,
			                   HttpServletResponse response,  
			                   Model model, 
			                   Locale locale) throws BizException 
	{

		logger.info("Welcome intro");
		
		ModelAndView  mv = new ModelAndView();

    	mv.setViewName("/common/intro");

		return mv;
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws BizException
	 */
	@RequestMapping(value = "/customerloginform", method = RequestMethod.GET)
	public ModelAndView customerLoginForm(HttpServletRequest request,
			                   HttpServletResponse response,  
			                   Model model, 
			                   Locale locale) throws BizException 
	{

		logger.info("Welcome customer");
		
		ModelAndView  mv = new ModelAndView();
		
		// 사용자 세션정보
        HttpSession session = request.getSession();
        
        String customerKey = StringUtil.nvl((String) session.getAttribute("customerKey")); 
        
        logger.info("customerKey:"+customerKey);
        
        if(customerKey.equals("") || customerKey.equals("null") || customerKey.equals(null)){

        	//조직정보 조회
        	//GroupVO group = new GroupVO();
        	
        	//List<GroupVO> group_comboList = commonSvc.getGroupComboList(group);
        	//mv.addObject("group_comboList", group_comboList);
        	
        	
        	String state = generateState();

        	session = request.getSession(true);
			session.setAttribute("state", state);
			
			mv.addObject("state", state);
			mv.addObject("hostUrl", hostUrl);
			mv.addObject("domainUrl", domainUrl);
			mv.addObject("redirectUrl", redirectUrl);
			mv.addObject("redirectUrl2", redirectUrl2);
			mv.addObject("kakaoclient_id", kakaoclient_id);
			mv.addObject("naverclient_id", naverclient_id);
			mv.addObject("naverclient_secret", naverclient_secret);
			mv.addObject("facebookfbAppId", facebookfbAppId);
			mv.addObject("googleclient_id", googleclient_id);
			mv.addObject("pinterestclient_id", pinterestclient_id);
        	
 	       	mv.setViewName("/common/customerLoginForm");
       		return mv;
		}

        mv.addObject("hostUrl", hostUrl);
		mv.setViewName("comunity/comunityManage");

		return mv;
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws BizException
	 */
	@RequestMapping(value = "/customerregistform", method = RequestMethod.GET)
	public ModelAndView customerRegistForm(String type ,
			                   HttpServletRequest request,
			                   HttpServletResponse response,  
			                   Model model, 
			                   Locale locale) throws BizException 
	{

		logger.info("Welcome customer");
		
		ModelAndView  mv = new ModelAndView();
		
		mv.addObject("type", type);
		
    	mv.setViewName("/common/customerRegistForm");

		return mv;
	}
	
	 /**
     * 고객 등록
     *
     * @param UserManageVO
     * @param request
     * @param response
     * @param model
     * @param locale
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/common/customerregist", method = RequestMethod.POST)
    public @ResponseBody
    String  customerRegist(@ModelAttribute("customerVo") CustomerVO customerVo, 
									   HttpServletRequest request) throws BizException 
    {
		
    	//log Controller execute time start
		String logid=logid();
		long t1 = System.currentTimeMillis();
		logger.info("["+logid+"] Controller start : customerVO" + customerVo);
    			
		int retVal=-1;
		String token="";
		
		//등록여부 확인
		
		CustomerVO customerChk = customerSvc.getTokenInfo(customerVo);	
				
		if(customerChk != null) {
			
			token=customerChk.getToken();
			
			if(!token.equals(customerVo.getToken())){ //인증실패-인증번호 다름
				
				//log Controller execute time end
		       	long t2 = System.currentTimeMillis();
		       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	            
		       	return "3";
				
			}
			
			
		}else{//인증실패-등록사용자 정보없음
			
			//log Controller execute time end
	       	long t2 = System.currentTimeMillis();
	       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
            
	       	return "2";
			
		}

		retVal = customerSvc.customerRegist(customerVo);	
		
		if(retVal>0){
			retVal=1;
		}
		
		//log Controller execute time end
       	long t2 = System.currentTimeMillis();
       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");

      return ""+retVal;
	}
    
    /**
     * 고객 등록
     *
     * @param UserManageVO
     * @param request
     * @param response
     * @param model
     * @param locale
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/common/gettokenconfirm", method = RequestMethod.POST)
    public @ResponseBody
    String  getTokenConfirm(@ModelAttribute("customerVo") CustomerVO customerVo, 
									   HttpServletRequest request) throws BizException 
    {
		
    	//log Controller execute time start
		String logid=logid();
		long t1 = System.currentTimeMillis();
		logger.info("["+logid+"] Controller start : customerVO" + customerVo);
    			
		int retVal=-1;
		String token="";
		
		//등록여부 확인
		
		CustomerVO customerChk = customerSvc.getTokenInfo(customerVo);	
				
		if(customerChk != null) {
			
			token=customerChk.getToken();
			
			if(!token.equals(customerVo.getToken())){ //인증실패-인증번호 다름
				
				//log Controller execute time end
		       	long t2 = System.currentTimeMillis();
		       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	            
		       	return "3";
				
			}
			
			
		}else{//인증실패-등록사용자 정보없음
			
			//log Controller execute time end
	       	long t2 = System.currentTimeMillis();
	       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
            
	       	return "2";
			
		}

		//log Controller execute time end
       	long t2 = System.currentTimeMillis();
       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");

      return "1";
	}
    /**
     * 임시 비밀번호 발급
     *
     * @param UserManageVO
     * @param request
     * @param response
     * @param model
     * @param locale
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/common/gettoken", method = RequestMethod.POST)
    public @ResponseBody
    String  getToken(@ModelAttribute("customerVo") CustomerVO customerVo, 
									   HttpServletRequest request) throws BizException 
    {
		
    	//log Controller execute time start
		String logid=logid();
		long t1 = System.currentTimeMillis();
		logger.info("["+logid+"] Controller start : customerVO" + customerVo);
    			
		int retVal=-1;
		
        //등록여부 확인
		
		CustomerVO customerChk = customerSvc.getCustomer(customerVo);	
		
		if(customerChk != null) {
			
			//log Controller execute time end
	       	long t2 = System.currentTimeMillis();
	       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
            
	       	return "1";
			
		}
		
		String token="";
		token=tokenCreate();
		
		if(token.length()==3){
			token=token+"0";
		}

		customerVo.setToken(token);

		retVal = customerSvc.customerUpdateToken(customerVo);	
		logger.debug("#########getToken retVal"+retVal);
		
			try{
				//SMS발송
				SmsVO smsVO = new SmsVO();
				SmsVO resultSmsVO = new SmsVO();
				
				//즉시전송 세팅
				smsVO.setSmsDirectYn("Y");
				
				smsVO.setSmsId(smsId);
				smsVO.setSmsPw(smsPw);
				smsVO.setSmsType(smsType);
				smsVO.setSmsTo(customerVo.getCustomerKey());
				smsVO.setSmsFrom(sendno);
				smsVO.setSmsMsg("["+token+"]애디스에서 발송된 인증번호입니다");

				logger.debug("#########devOption :"+devOption);
				String[] devSmss= devSms.split("\\^");
				
	    		if(devOption.equals("true")){
					for(int i=0;i<devSmss.length;i++){
						
						if(devSmss[i].equals(customerVo.getCustomerKey().trim().replace("-", ""))){
							resultSmsVO=smsSvc.sendSms(smsVO);
						}
					}
				}else{
					resultSmsVO=smsSvc.sendSms(smsVO);
				}
	
				logger.debug("sms resultSmsVO.getResultCode() :"+resultSmsVO.getResultCode());
				logger.debug("sms resultSmsVO.getResultMessage() :"+resultSmsVO.getResultMessage());
				logger.debug("sms resultSmsVO.getResultLastPoint() :"+resultSmsVO.getResultLastPoint());
				
			}catch(BizException e){
				
				logger.info("["+logid+"] Controller SMS전송오류");
				//log Controller execute time end
		       	long t2 = System.currentTimeMillis();
		       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	            
		       	return "-1";
				
			}
			
		
		//log Controller execute time end
       	long t2 = System.currentTimeMillis();
       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");

      return "0";
	}
    /**
     * 임시 비밀번호 발급
     *
     * @param UserManageVO
     * @param request
     * @param response
     * @param model
     * @param locale
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/common/getpwtoken", method = RequestMethod.POST)
    public @ResponseBody
    String  getPwToken(@ModelAttribute("customerVo") CustomerVO customerVo, 
									   HttpServletRequest request) throws BizException 
    {
		
    	//log Controller execute time start
		String logid=logid();
		long t1 = System.currentTimeMillis();
		logger.info("["+logid+"] Controller start : customerVO" + customerVo);
    			
		int retVal=-1;
		
        //등록여부 확인
		
		CustomerVO customerChk = customerSvc.getCustomer(customerVo);	
		
		if(customerChk == null) {
			
			//log Controller execute time end
	       	long t2 = System.currentTimeMillis();
	       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
            
	       	return "1";
			
		}
		
		String token="";
		token=tokenCreate();
		
		customerVo.setToken(token);

		retVal = customerSvc.customerUpdateToken(customerVo);	
		logger.debug("#########getToken retVal"+retVal);
		
			try{
				//SMS발송
				SmsVO smsVO = new SmsVO();
				SmsVO resultSmsVO = new SmsVO();
				
				//즉시전송 세팅
				smsVO.setSmsDirectYn("Y");
				
				smsVO.setSmsId(smsId);
				smsVO.setSmsPw(smsPw);
				smsVO.setSmsType(smsType);
				smsVO.setSmsTo(customerVo.getCustomerKey());
				smsVO.setSmsFrom(sendno);
				smsVO.setSmsMsg("["+token+"] 애디스에서 발송된 인증번호입니다");

				logger.debug("#########devOption :"+devOption);
				String[] devSmss= devSms.split("\\^");
				
	    		if(devOption.equals("true")){
					for(int i=0;i<devSmss.length;i++){
						
						if(devSmss[i].equals(customerVo.getCustomerKey().trim().replace("-", ""))){
							resultSmsVO=smsSvc.sendSms(smsVO);
						}
					}
				}else{
					resultSmsVO=smsSvc.sendSms(smsVO);
				}
	
				logger.debug("sms resultSmsVO.getResultCode() :"+resultSmsVO.getResultCode());
				logger.debug("sms resultSmsVO.getResultMessage() :"+resultSmsVO.getResultMessage());
				logger.debug("sms resultSmsVO.getResultLastPoint() :"+resultSmsVO.getResultLastPoint());
				
			}catch(BizException e){
				
				logger.info("["+logid+"] Controller SMS전송오류");
				//log Controller execute time end
		       	long t2 = System.currentTimeMillis();
		       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	            
		       	return "-1";
				
			}
			
		
		//log Controller execute time end
       	long t2 = System.currentTimeMillis();
       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");

      return "0";
	}
	 /**
     * 임시 비밀번호 발급
     *
     * @param UserManageVO
     * @param request
     * @param response
     * @param model
     * @param locale
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/common/temppassword", method = RequestMethod.POST)
    public @ResponseBody
    String  tempPassword(@ModelAttribute("customerVo") CustomerVO customerVo, 
									   HttpServletRequest request) throws BizException 
    {
		
    	//log Controller execute time start
		String logid=logid();
		long t1 = System.currentTimeMillis();
		logger.info("["+logid+"] Controller start : customerVO" + customerVo);
    			
		int retVal=-1;
		String token="";
		token=tokenCreate();
		String temppassword="";
		temppassword=tokenCreate();
		
		//@생성
		
		//등록여부 확인
		
		CustomerVO customerChk = customerSvc.getTokenInfo(customerVo);	
				
		if(customerChk != null) {
			
			token=customerChk.getToken();
			
			if(!token.equals(customerVo.getToken())){ //인증실패-인증번호 다름
				
				//log Controller execute time end
		       	long t2 = System.currentTimeMillis();
		       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	            
		       	return "3";
				
			}
		
		}else{//사용자 정보없음
			
			//log Controller execute time end
	       	long t2 = System.currentTimeMillis();
	       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
            
	       	return "2";

		}
		
		customerVo.setPw_modifyYn("Y");
		customerVo.setCustomerPw(temppassword);

		retVal = customerSvc.customerUpdateProc(customerVo);
		
		if(retVal>0){
			retVal=1;
		}
		
		logger.debug("#########customerUpdateProc retVal"+retVal);
		
			try{
				//SMS발송
				SmsVO smsVO = new SmsVO();
				SmsVO resultSmsVO = new SmsVO();
				
				//즉시전송 세팅
				smsVO.setSmsDirectYn("Y");
				
				smsVO.setSmsId(smsId);
				smsVO.setSmsPw(smsPw);
				smsVO.setSmsType(smsType);
				smsVO.setSmsTo(customerVo.getCustomerKey());
				smsVO.setSmsFrom(sendno);
				smsVO.setSmsMsg("애디스에서 발송된 임시 비밀번호 입니다 ["+temppassword+"]");

				logger.debug("#########devOption :"+devOption);
				String[] devSmss= devSms.split("\\^");
				
	    		if(devOption.equals("true")){
					for(int i=0;i<devSmss.length;i++){
						
						if(devSmss[i].equals(customerVo.getCustomerKey().trim().replace("-", ""))){
							resultSmsVO=smsSvc.sendSms(smsVO);
						}
					}
				}else{
					resultSmsVO=smsSvc.sendSms(smsVO);
				}
	
				logger.debug("sms resultSmsVO.getResultCode() :"+resultSmsVO.getResultCode());
				logger.debug("sms resultSmsVO.getResultMessage() :"+resultSmsVO.getResultMessage());
				logger.debug("sms resultSmsVO.getResultLastPoint() :"+resultSmsVO.getResultLastPoint());
				
			}catch(BizException e){
				
				logger.info("["+logid+"] Controller SMS전송오류");
				//log Controller execute time end
		       	long t2 = System.currentTimeMillis();
		       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	            
		       	return "-1";
			}
			
		
		//log Controller execute time end
       	long t2 = System.currentTimeMillis();
       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");

      return ""+retVal;
	}
    /**
     * 고객 패스워드 변경
     *
     * @param UserManageVO
     * @param request
     * @param response
     * @param model
     * @param locale
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/common/customerpwmodify", method = RequestMethod.POST)
    public @ResponseBody
    String  customerPwModify(@ModelAttribute("customerVo") CustomerVO customerVo, 
									   HttpServletRequest request) throws BizException 
    {
		
    	//log Controller execute time start
		String logid=logid();
		long t1 = System.currentTimeMillis();
		logger.info("["+logid+"] Controller start : customerVO" + customerVo);
    			
		int retVal=-1;
		String token="";
		
		//등록여부 확인
		
		CustomerVO customerChk = customerSvc.getTokenInfo(customerVo);	
				
		if(customerChk != null) {
			
			token=customerChk.getToken();
			
			if(!token.equals(customerVo.getToken())){ //인증실패-인증번호 다름
				
				//log Controller execute time end
		       	long t2 = System.currentTimeMillis();
		       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	            
		       	return "3";
				
			}
			
			
		}else{//인증실패-등록사용자 정보없음
			
			//log Controller execute time end
	       	long t2 = System.currentTimeMillis();
	       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
            
	       	return "2";
			
		}

		customerVo.setPw_modifyYn("Y");
		
		retVal=this.customerSvc.customerUpdateProc(customerVo);
		
		if(retVal>0){
			retVal=1;
		}
		
		//log Controller execute time end
       	long t2 = System.currentTimeMillis();
       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");

      return ""+retVal;
	}
    
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws BizException
	 */
	@RequestMapping(value = "/customerpwform", method = RequestMethod.GET)
	public ModelAndView customerPwForm(String type ,
			                   HttpServletRequest request,
			                   HttpServletResponse response,  
			                   Model model, 
			                   Locale locale) throws BizException 
	{

		logger.info("Welcome customer");
		
		ModelAndView  mv = new ModelAndView();
		
		mv.addObject("type", type);
		
    	mv.setViewName("/common/customerPwForm");

		return mv;
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws BizException
	 */
	@RequestMapping(value = "/surveyloginform", method = RequestMethod.GET)
	public ModelAndView surveyLoginForm(HttpServletRequest request,
			                   HttpServletResponse response,  
			                   Model model, 
			                   Locale locale) throws BizException 
	{

		logger.info("Welcome customer");
		
		ModelAndView  mv = new ModelAndView();
		
		// 사용자 세션정보
        HttpSession session = request.getSession();
        
        String customerKey = StringUtil.nvl((String) session.getAttribute("customerKey")); 
        
        logger.info("customerKey:"+customerKey);
        
        if(customerKey.equals("") || customerKey.equals("null") || customerKey.equals(null)){

        	//조직정보 조회
        	GroupVO group = new GroupVO();
        	
        	List<GroupVO> group_comboList = commonSvc.getGroupComboList(group);
        	mv.addObject("group_comboList", group_comboList);
        	
 	       	mv.setViewName("/common/surveyLoginForm");
       		return mv;
		}

		mv.setViewName("survey/surveyManage");

		return mv;
	}
	
	/**
	 * Login 처리
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("null")
	@RequestMapping(value = "/customer/login", method = RequestMethod.POST)
	public ModelAndView salesblogin(String loginType ,
			                       HttpServletRequest request,
			                       HttpServletResponse response) throws Exception
	{
		
		//log Controller execute time start
		String logid=logid();
		long t1 = System.currentTimeMillis();
		logger.info("["+logid+"] Controller start loginType::"+loginType);
		
		ModelAndView  mv = new ModelAndView();
		
		String memberType = StringUtil.nvl(request.getParameter("memberType"));
		String sbPhoneNumber = StringUtil.nvl(request.getParameter("sbPhoneNumber"));
		String sbPw = StringUtil.nvl(request.getParameter("sbPw"));
		String groupId = StringUtil.nvl(request.getParameter("groupId"),"SM001");
		String groupName = StringUtil.nvl(request.getParameter("groupName"),"스마트매장");
		
		String id = StringUtil.nvl(request.getParameter("id"),sbPhoneNumber);
		String name = StringUtil.nvl(request.getParameter("name"),"");
		String restfulltype = StringUtil.nvl(request.getParameter("restfulltype"),"일반");
		String photo = StringUtil.nvl(request.getParameter("photo"),"");
		
		logger.info(">>>> sbPhoneNumber :"+sbPhoneNumber);
		logger.info(">>>> sbPw :"+sbPw);

		String strMainUrl = "";
		
		// # 2. 넘겨받은 아이디로 데이터베이스를 조회하여 사용자가 있는지를 체크한다.
		CustomerVO customerVo = new CustomerVO();
		customerVo.setSbPhoneNumber(sbPhoneNumber);
		customerVo.setInCustomerPw(sbPw);
		
		CustomerVO customerChk = customerSvc.getCustomer(customerVo);		

		String customerKey="";
		String lastLat = "";
		String lastlon = "";
	
		if(customerChk != null)
		{

			//패스워드 체크
			if(!customerChk.getCustomerPw().equals(customerChk.getInCustomerPw())){
				
				logger.info(">>> 비밀번호 오류");
				strMainUrl = "common/loginFail";
				
				mv.addObject("memberType", memberType);
				mv.addObject("loginType", loginType);
				mv.addObject("sbPhoneNumber", sbPhoneNumber);
				
				mv.setViewName(strMainUrl);
				
				//log Controller execute time end
		      	long t2 = System.currentTimeMillis();
		      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
		      	
				return mv;
				
			}

			customerKey = customerChk.getCustomerKey();
			sbPhoneNumber = customerChk.getSbPhoneNumber();
			lastLat = customerChk.getLastLat();
			lastlon=customerChk.getLastlon();

			// # 3. Session 객체에 셋팅
			
			HttpSession session = request.getSession(false);
			
			if(session != null)
			{
				session.invalidate();
			}
				
				session = request.getSession(true);
				
				session.setAttribute("memberType", memberType);
				session.setAttribute("customerKey", customerKey);
				session.setAttribute("sbPhoneNumber", sbPhoneNumber);
				session.setAttribute("lastLat", lastLat);
				session.setAttribute("lastlon", lastlon);
				
				session.setAttribute("id", id);
				session.setAttribute("name", name);
				session.setAttribute("restfulltype", restfulltype);
				session.setAttribute("photo", photo);

				if("01".equals(memberType)){
					strMainUrl = "business/goodsManage";
				}else{
					
					List<AsVO> asList = null;
					AsVO asConVO = new AsVO();
			        asConVO.setCustomerKey(customerKey);
			        asConVO.setGroupId(groupId);
			        
			        // 조회조건저장
			        mv.addObject("asConVO", asConVO);

			        // 페이징코드
			        asConVO.setPage_limit_val1(StringUtil.getCalcLimitStart(asConVO.getCurPage(), asConVO.getRowCount()));
			        asConVO.setPage_limit_val2(StringUtil.nvl(asConVO.getRowCount(), "10"));
			        
			        // 사용자목록조회
			        asList = asSvc.getAsList(asConVO);
			        mv.addObject("asList", asList);

			        // totalCount 조회
			        String totalCount = String.valueOf(asSvc.getAsCnt(asConVO));
			        mv.addObject("totalCount", totalCount);

					strMainUrl = "member/mytokenList";
				}

				
			} else {//고객 정보가 없는경우
				
				logger.info(">>> 고객 정보 없음");
				strMainUrl = "common/loginFail";

			}

			mv.setViewName(strMainUrl);
			
			//log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
			return mv;
		}
	/**
	 * Login 처리
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("null")
    @RequestMapping({"/customer/snslogin"})
	public ModelAndView snslogin( HttpServletRequest request,
			                      HttpServletResponse response) throws Exception
	{
		
		//log Controller execute time start
		String logid=logid();
		long t1 = System.currentTimeMillis();
		logger.info("["+logid+"] Controller start snslogin::");
		
		ModelAndView  mv = new ModelAndView();

		String loginType = StringUtil.nvl(request.getParameter("loginType"));
		String memberType = StringUtil.nvl(request.getParameter("memberType"));
		String sbPhoneNumber = StringUtil.nvl(request.getParameter("sbPhoneNumber"));
		String sbPw = StringUtil.nvl(request.getParameter("sbPw"));
		String groupId = StringUtil.nvl(request.getParameter("groupId"),"SM001");
		String groupName = StringUtil.nvl(request.getParameter("groupName"),"스마트매장");
		
		String id = StringUtil.nvl(request.getParameter("id"),sbPhoneNumber);
		String name = StringUtil.nvl(request.getParameter("name"),"");
		String restfulltype = StringUtil.nvl(request.getParameter("restfulltype"),"일반");
		String photo = StringUtil.nvl(request.getParameter("photo"),"");
		String access_token = StringUtil.nvl(request.getParameter("access_token"),"");
		
		logger.info(">>>> sbPhoneNumber :"+sbPhoneNumber);
		logger.info(">>>> sbPw :"+sbPw);
		logger.info(">>>> id :"+id);
		logger.info(">>>> name :"+name);
		logger.info(">>>> restfulltype :"+restfulltype);
		logger.info(">>>> photo :"+photo);
		logger.info(">>>> access_token :"+access_token);
	
		String strMainUrl = "";
		
		CustomerVO customerVo = new CustomerVO();
		customerVo.setSbPhoneNumber(sbPhoneNumber);
		customerVo.setInCustomerPw(sbPw);
		
		CustomerVO customerChk = customerSvc.getCustomer(customerVo);		

		String customerKey="";
		String lastLat = "";
		String lastlon = "";
	
		if(customerChk != null)
		{

			//패스워드 체크
			if(!customerChk.getCustomerPw().equals(customerChk.getInCustomerPw())){
				
				logger.info(">>> 비밀번호 오류");
				strMainUrl = "common/loginFail";
				
				mv.addObject("memberType", memberType);
				mv.addObject("loginType", loginType);
				mv.addObject("customerKey", customerKey);
				
				mv.setViewName(strMainUrl);
				
				//log Controller execute time end
		      	long t2 = System.currentTimeMillis();
		      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
		      	
				return mv;
				
			}

			customerKey = customerChk.getCustomerKey();
			sbPhoneNumber = customerChk.getSbPhoneNumber();
			lastLat = customerChk.getLastLat();
			lastlon=customerChk.getLastlon();

			// # 3. Session 객체에 셋팅
			
			HttpSession session = request.getSession(false);
			
			if(session != null)
			{
				session.invalidate();
			}
				
				session = request.getSession(true);
				
				session.setAttribute("memberType", memberType);
				session.setAttribute("customerKey", customerKey);
				session.setAttribute("sbPhoneNumber", sbPhoneNumber);
				session.setAttribute("lastLat", lastLat);
				session.setAttribute("lastlon", lastlon);
				
				session.setAttribute("id", id);
				session.setAttribute("name", name);
				session.setAttribute("restfulltype", restfulltype);
				session.setAttribute("photo", photo);
				session.setAttribute("access_token", access_token);
				
				List<AsVO> asList = null;
				AsVO asConVO = new AsVO();
		        asConVO.setCustomerKey(customerKey);
		        asConVO.setGroupId(groupId);
		        
		        // 조회조건저장
		        mv.addObject("asConVO", asConVO);

		        // 페이징코드
		        asConVO.setPage_limit_val1(StringUtil.getCalcLimitStart(asConVO.getCurPage(), asConVO.getRowCount()));
		        asConVO.setPage_limit_val2(StringUtil.nvl(asConVO.getRowCount(), "10"));
		        
		        // 사용자목록조회
		        asList = asSvc.getAsList(asConVO);
		        mv.addObject("asList", asList);

		        // totalCount 조회
		        String totalCount = String.valueOf(asSvc.getAsCnt(asConVO));
		        mv.addObject("totalCount", totalCount);

				strMainUrl = "member/mytokenList";
				
			} else {//고객 정보가 없는경우
				
				logger.info(">>> 고객 정보 없음");
				strMainUrl = "common/loginFail";

			}

			mv.setViewName(strMainUrl);
			
			//log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
			return mv;
		}
		/**
		 * Login 처리
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception 
		 */
		@SuppressWarnings("null")
		@RequestMapping(value = "/facebooklogin", method = RequestMethod.GET)
		public ModelAndView facebookLogin(HttpServletRequest request,
				                          HttpServletResponse response) throws Exception
		{
			
			//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			logger.info("["+logid+"] Controller start facebookLogin::"+request.getQueryString());
			
			ModelAndView  mv = new ModelAndView();
			
			mv.addObject("hostUrl", hostUrl);
			mv.addObject("domainUrl", domainUrl);
			mv.addObject("redirectUrl", redirectUrl);
			
			mv.addObject("facebookfbAppId", facebookfbAppId);
			
			mv.setViewName("common/oauth2facebook");
			
			//log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
			return mv;
		}
		
		/**
		 * Login 처리
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception 
		 */
		@SuppressWarnings("null")
		@RequestMapping(value = "/googlelogin", method = RequestMethod.GET)
		public ModelAndView googleLogin(HttpServletRequest request,
				                          HttpServletResponse response) throws Exception
		{
			
			//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			logger.info("["+logid+"] Controller start googleLogin::"+request.getQueryString());
			
			ModelAndView  mv = new ModelAndView();
		
			mv.addObject("hostUrl", hostUrl);
			mv.addObject("domainUrl", domainUrl);
			mv.addObject("redirectUrl", redirectUrl);
			
			mv.addObject("googleclient_id", googleclient_id);
			
			mv.setViewName("common/oauth2google");
			
			//log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
			return mv;
		}
		
		/**
		 * Login 처리
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception 
		 */
		@SuppressWarnings("null")
		@RequestMapping(value = "/pinlogin", method = RequestMethod.GET)
		public ModelAndView pinLogin(String totalurl,
				                     HttpServletRequest request,
				                     HttpServletResponse response) throws Exception
		{
			
			//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			logger.info("["+logid+"] Controller start totalurl::"+totalurl);
			
			ModelAndView  mv = new ModelAndView();
		
			mv.addObject("totalurl", totalurl);
			mv.addObject("hostUrl", hostUrl);
			mv.addObject("domainUrl", domainUrl);
			mv.addObject("redirectUrl", redirectUrl);
			mv.addObject("redirectUrl2", redirectUrl2);
			
			mv.addObject("pinterestclient_id", pinterestclient_id);
			
			mv.setViewName("common/oauth2pinterest");
			
			//log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
			return mv;
		}
		
		/**
		 * Login 처리
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception 
		 */
		@SuppressWarnings("null")
		@RequestMapping(value = "/kakaologin", method = RequestMethod.GET)
		public ModelAndView kakaoLogin(HttpServletRequest request,
				                          HttpServletResponse response) throws Exception
		{
			
			//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			logger.info("["+logid+"] Controller start kakaoLogin::"+request.getQueryString());
			
			ModelAndView  mv = new ModelAndView();
			
			mv.addObject("hostUrl", hostUrl);
			mv.addObject("domainUrl", domainUrl);
			mv.addObject("redirectUrl", redirectUrl);
			
			mv.addObject("kakaoclient_id", kakaoclient_id);
			
			mv.setViewName("common/oauth2kakao");
			
			//log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
			return mv;
		}
		
		/**
		 * Login 처리
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception 
		 */
		@SuppressWarnings("null")
		@RequestMapping(value = "/naverlogin", method = RequestMethod.GET)
		public ModelAndView naverLogin(HttpServletRequest request,
				                          HttpServletResponse response) throws Exception
		{
			
			//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			
			
			ModelAndView  mv = new ModelAndView();
			
			// 사용자 세션정보
	        HttpSession session = request.getSession();
	        
	        String state = StringUtil.nvl((String) session.getAttribute("state")); 
	        
	        logger.info("["+logid+"] Controller start naverlogin state::"+state);
			
	        mv.addObject("state", state);
			mv.addObject("hostUrl", hostUrl);
			mv.addObject("domainUrl", domainUrl);
			mv.addObject("redirectUrl", redirectUrl);
			
			mv.addObject("naverclient_id", naverclient_id);
			mv.addObject("naverclient_secret", naverclient_secret);
			
			mv.setViewName("common/oauth2naver");
			
			//log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
			return mv;
		}
		
		/**
		 * Logout 처리
		 * @param request
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(value = "/common/staffselect")
		public ModelAndView staffSelect(String staffYn ,
				                   HttpServletRequest request) throws BizException
		{
			
			logger.info(" staffYn : "+staffYn);
	
			HttpSession session = request.getSession(false);
	
			session = request.getSession(true);
			session.setAttribute("staffYn", staffYn);

	        ModelAndView mv = new ModelAndView();
	        
	        mv.setViewName("/comunity/comunityManage");
	
			return mv;
		}
		/**
		 * Logout 처리
		 * @param request
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(value = "/common/logout")
		public ModelAndView logout(String loginType ,
				                   HttpServletRequest request) throws BizException
		{
			
			logger.info("Good bye salesb! ");
	
			HttpSession session = request.getSession(false);

		 	session.removeAttribute("customerKey");
		 	session.removeAttribute("customerId");
	        session.removeAttribute("customerName");
	        session.removeAttribute("customerKey1");
	        session.removeAttribute("customerKey2");
	        session.removeAttribute("customerKey3");
	        session.removeAttribute("customerKey4");
	        session.removeAttribute("customerKey5");
	        session.removeAttribute("customerKey6");
	        session.removeAttribute("customerKey7");
	        session.removeAttribute("customerKey8");
	        session.removeAttribute("customerKey9");
	        session.removeAttribute("customerKey10");
	        session.removeAttribute("staffYn");
	        session.removeAttribute("groupId");
	        session.removeAttribute("groupName");
	        
	        logger.info("logout ok!");
	        
	        ModelAndView mv = new ModelAndView();
	        
        	//조직정보 조회
        	GroupVO group = new GroupVO();
        	
        	List<GroupVO> group_comboList = commonSvc.getGroupComboList(group);
        	mv.addObject("group_comboList", group_comboList);
	        
	        if(StringUtil.nvl(loginType,"").equals("survey")){
	         	mv.setViewName("/common/surveyLoginForm");
			}else{
				
	        	String state = generateState();

	        	session = request.getSession(true);
				session.setAttribute("state", state);

				mv.addObject("state", state);
				mv.addObject("hostUrl", hostUrl);
				mv.addObject("domainUrl", domainUrl);
				mv.addObject("redirectUrl", redirectUrl);
				mv.addObject("redirectUrl2", redirectUrl2);
				mv.addObject("kakaoclient_id", kakaoclient_id);
				mv.addObject("naverclient_id", naverclient_id);
				mv.addObject("naverclient_secret", naverclient_secret);
				mv.addObject("facebookfbAppId", facebookfbAppId);
				mv.addObject("googleclient_id", googleclient_id);
				mv.addObject("pinterestclient_id", pinterestclient_id);
	
			 	mv.setViewName("/common/customerLoginForm");
			}
	
			return mv;
		}
		/**
	     * 고객정보 수정폼
	     *
	     * @param request
	     * @param response
	     * @param model
	     * @param locale
	     * @return
	     * @throws BizException
	     */
	    @RequestMapping(value = "/common/customermodifyform")
	    public ModelAndView customerModifyForm(HttpServletRequest request, 
	    		                       HttpServletResponse response,
			                           String customerKey) throws BizException 
	    {
	        
	    	//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			logger.info("["+logid+"] Controller start customerKey:"+customerKey);
	
	        ModelAndView mv = new ModelAndView();
	        
	     // 사용자 세션정보
	        HttpSession session = request.getSession();
	        
	        customerKey = StringUtil.nvl((String) session.getAttribute("customerKey")); 
	        String customerName = StringUtil.nvl((String) session.getAttribute("customerName")); 
	        String customerId = StringUtil.nvl((String) session.getAttribute("customerId"));
	        
	        if(customerKey.equals("") || customerKey.equals("null") || customerKey.equals(null)){

	 	       	//mv.setViewName("/common/customerLoginForm");
	        	mv.setViewName("/common/sessionOut");
	        	return mv;
			}
	        
			CustomerVO customerVo = new CustomerVO();
			customerVo.setCustomerKey(customerKey);
			
			CustomerVO customer = customerSvc.getCustomer(customerVo);	
	        
			mv.addObject("customer", customer);
	        mv.setViewName("/common/customerModifyForm");
	        
	       //log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
	        return mv;
	    }
	    /**
		 * 패스워드 체크
		 */
		@RequestMapping("/common/passwordcheck")
		public @ResponseBody
		String passwordCheck(@RequestParam(value = "curPwd") String curPwd) 
		{

			logger.info("[pwd]" + curPwd);
			
			CustomerVO customerVo = new CustomerVO();
			customerVo.setCurPwd(curPwd);

			try{
	        	
				customerVo = customerSvc.getEncPassword(customerVo);

		    }catch(BizException e){
		       	
		    	e.printStackTrace();
		    }

			return ""+customerVo.getEncPwd();

		 }
		/**
	     * 고객정보 수정처리
	     *
	     * @param UserManageVO
	     * @param request
	     * @param response
	     * @param model
	     * @param locale
	     * @return
	     * @throws BizException
	     */
	    @RequestMapping(value = "/common/customermodify", method = RequestMethod.POST)
	    public @ResponseBody
	    String customerModify(@ModelAttribute("customerVO") CustomerVO customerVO, 
	    		          HttpServletRequest request, 
	    		          HttpServletResponse response) throws BizException
	    {
	    	//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			logger.info("["+logid+"] Controller start : customerVO" + customerVO);
			
			int retVal=this.customerSvc.customerUpdateProc(customerVO);

			//log Controller execute time end
	       	long t2 = System.currentTimeMillis();
	       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");

	      return ""+retVal;
	    }
	    
	    public String tokenCreate(){
	    
	    	String token="1234";
			
	    	Random rand = new Random(12);
			rand.setSeed(System.currentTimeMillis());
			
			token=""+rand.nextInt(10000);
			logger.info("##### create token :: " + token);
	    	
	    	return token;
	    }
	    
		/**
		 * Simply selects the home view to render by returning its name.
		 * @throws BizException
		 */
		@RequestMapping(value = "/customerprivateinfo", method = RequestMethod.GET)
		public ModelAndView customerPrivateInfo(String type ,
				                   HttpServletRequest request,
				                   HttpServletResponse response,  
				                   Model model, 
				                   Locale locale) throws BizException 
		{

			logger.info("Welcome customer");
			
			ModelAndView  mv = new ModelAndView();

	    	mv.setViewName("/common/customerPrivateInfo");

			return mv;
		}
		
		/* RestFull 정보받기
	     *
	     * @param request
	     * @param response
	     * @param model
	     * @param locale
	     * @return
	     * @throws BizException
	     */
	    @RequestMapping({"/common/restfullinfo"})
	    public @ResponseBody
	    JSONObject restFullInfo(String restfullurl,
	            HttpServletRequest request, 
	            HttpServletResponse response) throws BizException
	    {
	        
	    	//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			
			logger.info("["+logid+"] Controller start restfullurl="+restfullurl);

			 JSONObject object =null;
			 String inputLine = null;
			 String content = "";

		    try{

	            BufferedReader input = null;
	
	            URL url = new URL(restfullurl);
	            input = new BufferedReader(new InputStreamReader(url.openStream()));

	            while ((inputLine = input.readLine()) != null) {
	            	
	            	 content += inputLine;
	            }
	            
	            input.close();

	            logger.info("["+logid+"] content::"+content);
	            
	            Object obj = JSONValue.parse(content);
	            
	            object = (JSONObject)obj;

	            /*
	            JSONArray array = (JSONArray)obj;
	            this.logger.debug("array ==>" + array);
	            List jasonList = new ArrayList();

	            Object arryObj = null;

	            for (int i = 0; i < array.size(); i++)
	            {
	              arryObj = JSONValue.parse(array.get(i).toString());
	              JSONObject arryObject = (JSONObject)arryObj;
	              jasonList.add(arryObject);
	            }
	            */
	        	    
	          }
	          catch (Exception e) {
	            e.printStackTrace();
	          }
			
	       //log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
	        return object;
	    }
	    
	    /* RestFull 정보받기
	     *
	     * @param request
	     * @param response
	     * @param model
	     * @param locale
	     * @return
	     * @throws BizException
	     */
	    @RequestMapping({"/common/restfullinfo2"})
	    public @ResponseBody
	    JSONObject restFullInfo2(String restfullurl,
	            HttpServletRequest request, 
	            HttpServletResponse response) throws BizException
	    {
	        
	    	//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			
			logger.info("["+logid+"] Controller start restFullInfo2="+restfullurl);

			 JSONObject object =null;
			 JSONObject object2 =null;
			 String inputLine = null;
			 String content = "";

		    try{

	            BufferedReader input = null;
	
	            URL url = new URL(restfullurl);
	            input = new BufferedReader(new InputStreamReader(url.openStream()));

	            while ((inputLine = input.readLine()) != null) {
	            	
	            	 content += inputLine;
	            }
	            
	            input.close();

	            logger.info("["+logid+"] content::"+content);
	            
	            Object obj = JSONValue.parse(content);
	            
	            object = (JSONObject)obj;

	            logger.info("["+logid+"] restFullInfo2::"+object.get("data"));
	            
	            Object obj2 = JSONValue.parse(object.get("data").toString());
	            
	            object2 = (JSONObject)obj2;
	            logger.info("["+logid+"] CipherDecipherUtil url::"+object2.get("url"));
	            logger.info("["+logid+"] CipherDecipherUtil first_name::"+object2.get("first_name"));
	            logger.info("["+logid+"] CipherDecipherUtil first_name::"+object2.get("id"));

	            /*
	            JSONArray array = (JSONArray)obj;
	            this.logger.debug("array ==>" + array);
	            List jasonList = new ArrayList();

	            Object arryObj = null;

	            for (int i = 0; i < array.size(); i++)
	            {
	              arryObj = JSONValue.parse(array.get(i).toString());
	              JSONObject arryObject = (JSONObject)arryObj;
	              jasonList.add(arryObject);
	            }
	            */
	        	    
	          }
	          catch (Exception e) {
	            e.printStackTrace();
	          }
			
	       //log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
	        return object2;
	    }
	    
	    /* RestFull 정보받기
	     *
	     * @param request
	     * @param response
	     * @param model
	     * @param locale
	     * @return
	     * @throws BizException
	     */
	    @RequestMapping({"/common/restfullinfo3"})
	    public @ResponseBody
	    JSONObject restFullInfo3(String restfullurl3,
	    		String access_token,
	            HttpServletRequest request, 
	            HttpServletResponse response) throws BizException
	    {
	        
	    	//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			
			logger.info("["+logid+"] Controller start restfullinfo3 "+restfullurl3);
			logger.info("["+logid+"] Controller start access_token "+access_token);

			 JSONObject object =null;
			 JSONObject object2 =null;
			 String inputLine = null;
			 String content = "";

		    try{

	            BufferedReader input = null;
	
	            URL url = new URL(restfullurl3);
	            
	            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	           
	            connection.setRequestProperty("Authorization", access_token);

		    	connection.setDoOutput(true);
		    	
		    	input= new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

	            while ((inputLine = input.readLine()) != null) {
	            	
	            	 content += inputLine;
	            }
	            
	            input.close();

	            logger.info("["+logid+"] content::"+content);
	            
	            Object obj = JSONValue.parse(content);
	            
	            object = (JSONObject)obj;

	            logger.info("["+logid+"] restfullinfo3 id::"+object.get("id"));
	            logger.info("["+logid+"] restfullinfo3 properties::"+object.get("properties"));
	            
	            Object obj2 = JSONValue.parse(object.get("properties").toString());
	            
	            object2 = (JSONObject)obj2;
	            
	            object2.put("id", object.get("id"));
	            
	            logger.info("["+logid+"] CipherDecipherUtil nickname::"+object2.get("nickname"));
	            logger.info("["+logid+"] CipherDecipherUtil custom_field1::"+object2.get("custom_field1"));
	            logger.info("["+logid+"] CipherDecipherUtil custom_field2::"+object2.get("custom_field2"));

	            /*
	            JSONArray array = (JSONArray)obj;
	            this.logger.debug("array ==>" + array);
	            List jasonList = new ArrayList();

	            Object arryObj = null;

	            for (int i = 0; i < array.size(); i++)
	            {
	              arryObj = JSONValue.parse(array.get(i).toString());
	              JSONObject arryObject = (JSONObject)arryObj;
	              jasonList.add(arryObject);
	            }
	            */
	        	    
	          }
	          catch (Exception e) {
	            e.printStackTrace();
	          }
			
	       //log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
	        return object2;
	    }
	    
	    /* RestFull 정보받기
	     *
	     * @param request
	     * @param response
	     * @param model
	     * @param locale
	     * @return
	     * @throws BizException
	     */
	    @RequestMapping({"/common/restfullinfo4"})
	    public @ResponseBody
	    JSONObject restFullInfo4(String restfullurl3,
	    		String access_token,
	            HttpServletRequest request, 
	            HttpServletResponse response) throws BizException
	    {
	        
	    	//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			
			logger.info("["+logid+"] Controller start restfullinfo4 "+restfullurl3);
			logger.info("["+logid+"] Controller start access_token "+access_token);

			 JSONObject object =new JSONObject();
			 String inputLine = null;
			 String content = "";

		    try{

	            BufferedReader input = null;
	
	            URL url = new URL(restfullurl3);
	            
	            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	           
	            connection.setRequestProperty("Authorization",access_token);

		    	connection.setDoOutput(true);
		    	
		    	input= new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

	            while ((inputLine = input.readLine()) != null) {
	            	
	            	 content += inputLine;
	            }
	            
	            input.close();

	            logger.info("["+logid+"] content::"+content);
	            
		        Element root = XmlUtil.loadStringDocument(content.toString());
		       	   
	       	    String resultcode =XmlUtil.getTagValue(root, "resultcode");
	       	    String message =XmlUtil.getTagValue(root, "message");
	       	    String id =XmlUtil.getTagValue(root, "id");
	       	    String nickname =XmlUtil.getTagValue(root, "nickname");
	       	    String profile_image =XmlUtil.getTagValue(root, "profile_image");
	       	    
	       	    logger.info("["+logid+"] resultcode::"+resultcode);
	       	    logger.info("["+logid+"] message::"+message);
	            
	            //<?xml version="1.0" encoding="UTF-8" ?><data><result><resultcode>00</resultcode><message>success</message></result><response><email><![CDATA[ranrhdwn76@naver.com]]></email><nickname><![CDATA[ranrhd****]]></nickname><enc_id><![CDATA[fa53b513469bce9ab8f80513c9bd691256416ec50676dc8fc264b7713e4e1077]]></enc_id><profile_image><![CDATA[https://phinf.pstatic.net/contactthumb/profile/blog/1/13/ranrhdwn76.jpg?type=s80]]></profile_image><age><![CDATA[40-49]]></age><gender>F</gender><id><![CDATA[57891596]]></id><name><![CDATA[김정란]]></name><birthday><![CDATA[02-08]]></birthday></response></data>

	            object.put("resultcode", resultcode);
	            object.put("id", id);
	            object.put("nickname", nickname);
	            object.put("profile_image", profile_image);
	            
	            logger.info("["+logid+"] CipherDecipherUtil nickname::"+object.get("nickname"));
	            logger.info("["+logid+"] CipherDecipherUtil id::"+object.get("id"));
	            logger.info("["+logid+"] CipherDecipherUtil profile_image::"+object.get("profile_image"));

	          }
	          catch (Exception e) {
	            e.printStackTrace();
	          }
			
	       //log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
	        return object;
	    }
	    
		/* chat test (cross domain)
	     *
	     * @param request
	     * @param response
	     * @param model
	     * @param locale
	     * @return
	     * @throws BizException
	     */
	    @RequestMapping({"/common/chattest"})
	    public @ResponseBody
	    String chatTest(String redirecturl,
	            HttpServletRequest request, 
	            HttpServletResponse response) throws BizException
	    {
	        
	    	//log Controller execute time start
			String logid=logid();
			long t1 = System.currentTimeMillis();
			
			logger.info("["+logid+"] Controller start chatTest="+redirecturl);

			 JSONObject object =null;
			 String inputLine = null;
			 String content = "";

		    try{

	            BufferedReader input = null;
	
	            URL url = new URL(redirecturl);
	            input = new BufferedReader(new InputStreamReader(url.openStream()));

	            while ((inputLine = input.readLine()) != null) {
	            	
	            	 content += inputLine;
	            }
	            
	            input.close();

	            logger.info("["+logid+"] content::"+content);
    
	          }
	          catch (Exception e) {
	            e.printStackTrace();
	          }
			
	       //log Controller execute time end
	      	long t2 = System.currentTimeMillis();
	      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	      	
	        return content;
	    }
}
