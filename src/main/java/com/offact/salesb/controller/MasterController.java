package com.offact.salesb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.offact.framework.util.StringUtil;
import com.offact.framework.constants.CodeConstant;
import com.offact.framework.exception.BizException;
import com.offact.framework.jsonrpc.JSONRpcService;
import com.offact.salesb.service.common.CommonService;
import com.offact.salesb.service.common.UserService;
import com.offact.salesb.service.master.ProductMasterService;
import com.offact.salesb.vo.common.CodeVO;
import com.offact.salesb.vo.common.GroupVO;
import com.offact.salesb.vo.common.SmsVO;
import com.offact.salesb.vo.common.UserVO;
import com.offact.salesb.vo.common.CompanyVO;
import com.offact.salesb.vo.common.WorkVO;
import com.offact.salesb.vo.manage.UserManageVO;
import com.offact.salesb.vo.master.ProductMasterVO;
import com.offact.salesb.vo.MultipartFileVO;

/**
 * Handles requests for the application home page.
 */
@Controller

public class MasterController {

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
    @Autowired
    private CommonService commonSvc;
    
	@Autowired
	private UserService userSvc;
    
    @Autowired
    private ProductMasterService productMasterSvc;
   
    
    /**
     * 품목현황 관리화면
     *
     * @param request
     * @param response
     * @param model
     * @param locale
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/master/productmanage")
    public ModelAndView productManage(HttpServletRequest request, 
    		                       HttpServletResponse response) throws BizException 
    {
        
    	//log Controller execute time start
		String logid=logid();
		long t1 = System.currentTimeMillis();
		logger.info("["+logid+"] Controller start ");

        ModelAndView mv = new ModelAndView();
        
     // 사용자 세션정보
        HttpSession session = request.getSession();
        String strUserId = StringUtil.nvl((String) session.getAttribute("strUserId"));
        String strGroupId = StringUtil.nvl((String) session.getAttribute("strGroupId"));
        String strIp = StringUtil.nvl((String) session.getAttribute("strIp"));
        String sClientIP = StringUtil.nvl((String) session.getAttribute("sClientIP"));
        
        if(strUserId.equals("") || strUserId.equals("null") || strUserId.equals(null)){

        	strIp = request.getRemoteAddr(); //로그인 상태처리		
    		UserVO userState =new UserVO();
    		userState.setUserId(strUserId);
    		userState.setLoginYn("N");
    		userState.setIp(strIp);
    		userState.setConnectIp(sClientIP);
    		userSvc.regiLoginYnUpdate(userState);
            
            //작업이력
	 		WorkVO work = new WorkVO();
	 		work.setWorkUserId(strUserId);
	 		work.setWorkIp(strIp);
	 		work.setWorkCategory("CM");
	 		work.setWorkCode("CM004");
	 		commonSvc.regiHistoryInsert(work);
    		
        	mv.setViewName("/admin/loginForm");
       		return mv;
		}
        
        UserManageVO userConVO = new UserManageVO();
        
        userConVO.setUserId(strUserId);
        userConVO.setGroupId(strGroupId);

        // 조회조건저장
        mv.addObject("userConVO", userConVO);

        // 공통코드 조회 (사용자그룹코드)
        /*
        ADCodeManageVO code = new ADCodeManageVO();
        code.setCodeId("IG11");
        List<ADCodeManageVO> searchCondition1 = codeService.getCodeComboList(code);
        mv.addObject("searchCondition1", searchCondition1);
       */
       
        mv.setViewName("/master/productManage");
        
       //log Controller execute time end
      	long t2 = System.currentTimeMillis();
      	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
      	
        return mv;
    }
    /**
     * 품목 목록조회
     * 
     * @param UserManageVO
     * @param request
     * @param response
     * @param model
     * @param locale
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/master/productpagelist")
    public ModelAndView productPageList(@ModelAttribute("productConVO") ProductMasterVO productConVO, 
    		                         HttpServletRequest request, 
    		                         HttpServletResponse response) throws BizException 
    {
        
    	//log Controller execute time start
		String logid=logid();
		long t1 = System.currentTimeMillis();
		logger.info("["+logid+"] Controller start : productConVO" + productConVO);

        ModelAndView mv = new ModelAndView();
        
     // 사용자 세션정보
        HttpSession session = request.getSession();
        String strUserId = StringUtil.nvl((String) session.getAttribute("strUserId"));
        String strGroupId = StringUtil.nvl((String) session.getAttribute("strGroupId"));
        String strIp = StringUtil.nvl((String) session.getAttribute("strIp"));
        String sClientIP = StringUtil.nvl((String) session.getAttribute("sClientIP"));
        
        if(strUserId.equals("") || strUserId.equals("null") || strUserId.equals(null)){
        	
        	strIp = request.getRemoteAddr(); //로그인 상태처리		
    		UserVO userState =new UserVO();
    		userState.setUserId(strUserId);
    		userState.setLoginYn("N");
    		userState.setIp(strIp);
    		userState.setConnectIp(sClientIP);
    		userSvc.regiLoginYnUpdate(userState);
            
            //작업이력
	 		WorkVO work = new WorkVO();
	 		work.setWorkUserId(strUserId);
	 		work.setWorkIp(strIp);
	 		work.setWorkCategory("CM");
	 		work.setWorkCode("CM004");
	 		commonSvc.regiHistoryInsert(work);
    		
        	mv.setViewName("/admin/loginForm");
       		return mv;
		}
        
        List<ProductMasterVO> productList = null;

        // 조회조건 null 일때 공백처리
        if (productConVO.getSearchGubun() == null) {
        	productConVO.setSearchGubun("01");
        }
        
        // 조회값 null 일때 공백처리
        if (productConVO.getSearchValue() == null) {
        	productConVO.setSearchValue("");
        }
        
        // 조회조건저장
        mv.addObject("productCon", productConVO);

        // 페이징코드
        productConVO.setPage_limit_val1(StringUtil.getCalcLimitStart(productConVO.getCurPage(), productConVO.getRowCount()));
        productConVO.setPage_limit_val2(StringUtil.nvl(productConVO.getRowCount(), "10"));
        
        // 사용자목록조회
        productList = productMasterSvc.getProductList(productConVO);
        mv.addObject("productList", productList);

        // totalCount 조회
        String totalCount = String.valueOf(productMasterSvc.getProductCnt(productConVO));
        mv.addObject("totalCount", totalCount);

        mv.setViewName("/master/productPageList");
        
        //log Controller execute time end
       	long t2 = System.currentTimeMillis();
       	logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
       	
        return mv;
    }
    /**
   	 * Simply selects the home view to render by returning its name.
   	 * @throws BizException
   	 */
    @RequestMapping(value = "/master/productexcelform")
   	public ModelAndView productExcelForm(HttpServletRequest request) throws BizException 
       {
   		
   		ModelAndView mv = new ModelAndView();
   		
   		mv.setViewName("/master/productExcelForm");
   		
   		return mv;
   	}
   /**
    * 품목관리 일괄등록
    *
    * @param MultipartFileVO
    * @param request
    * @param response
    * @param model
    * @param locale
    * @return
    * @throws BizException
    */
   @RequestMapping({"/master/productexcelimport"})
   public ModelAndView productExcelImport(@ModelAttribute("MultipartFileVO") MultipartFileVO fileVO, 
   		                            HttpServletRequest request, 
   		                            HttpServletResponse response, 
   		                            String fileName, 
   		                            String extension ) throws IOException, BizException
   {
     
     //log Controller execute time start
	 String logid=logid();
     long t1 = System.currentTimeMillis();
     logger.info("["+logid+"] Controller start : fileVO" + fileVO);
   			
     ModelAndView mv = new ModelAndView();
     
     String fname="";

  // 사용자 세션정보
     HttpSession session = request.getSession();
     String strUserId = StringUtil.nvl((String) session.getAttribute("strUserId"));
     String strGroupId = StringUtil.nvl((String) session.getAttribute("strGroupId"));
     String strIp = StringUtil.nvl((String) session.getAttribute("strIp"));
     String sClientIP = StringUtil.nvl((String) session.getAttribute("sClientIP"));
     
   //오늘 날짜
     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd", Locale.KOREA);
     Date currentTime = new Date();
     String strToday = simpleDateFormat.format(currentTime);
     
     if(strUserId.equals("") || strUserId.equals("null") || strUserId.equals(null)){
     	
     	strIp = request.getRemoteAddr(); //로그인 상태처리		
 		UserVO userState =new UserVO();
 		userState.setUserId(strUserId);
 		userState.setLoginYn("N");
 		userState.setIp(strIp);
 		userState.setConnectIp(sClientIP);
 		userSvc.regiLoginYnUpdate(userState);
         
         //작업이력
	 		WorkVO work = new WorkVO();
	 		work.setWorkUserId(strUserId);
	 		work.setWorkIp(strIp);
	 		work.setWorkCategory("CM");
	 		work.setWorkCode("CM004");
	 		commonSvc.regiHistoryInsert(work);
 		
     	mv.setViewName("/admin/loginForm");
    		return mv;
		}

     ResourceBundle rb = ResourceBundle.getBundle("config");
     String uploadFilePath = rb.getString("offact.upload.path") + "excel/productmaster/"+strToday+"/";
     
     this.logger.debug("파일정보:" + fileName + extension);
     this.logger.debug("file:" + fileVO);

     List excelUploadList = new ArrayList();//업로드 대상 데이타
     
     String excelInfo = "";//excel 추출데이타
     List rtnErrorList = new ArrayList(); //DB 에러 대상데이타
     List rtnSuccessList = new ArrayList(); //DB 성공 대상데이타
     
     String errorMsgList="";

     if (fileName != null) {
   	  
       List<MultipartFile> files = fileVO.getFiles();
       List fileNames = new ArrayList();
       String orgFileName = null;

       if ((files != null) && (files.size() > 0))
       {
         for (MultipartFile multipartFile : files)
         {
           orgFileName = t1 +"."+ extension;
           boolean check=setDirectory(uploadFilePath);
           
           String filePath = uploadFilePath;

           File file = new File(filePath + orgFileName);
           multipartFile.transferTo(file);
           fileNames.add(orgFileName);
         }
    
       }

       fname = uploadFilePath + orgFileName;

       FileInputStream fileInput = null;

       fileInput = new FileInputStream(fname);

       XSSFWorkbook workbook = new XSSFWorkbook(fileInput);
       XSSFSheet sheet = workbook.getSheetAt(0);//첫번째 sheet
  
       int TITLE_POINT =0;//타이틀 항목위치
       int ROW_START = 1;//data row 시작지점
       
       int TOTAL_ROWS=sheet.getPhysicalNumberOfRows(); //전체 ROW 수를 가져온다.
       int TOTAL_CELLS=sheet.getRow(TITLE_POINT).getPhysicalNumberOfCells(); //전체 셀의 항목 수를 가져온다.
       
       XSSFCell myCell = null;
     
       this.logger.debug("TOTAL_ROWS :" + TOTAL_ROWS);
       this.logger.debug("TOTAL_CELLS :" + TOTAL_CELLS);
           
           try {
 
	           for (int rowcnt = ROW_START; rowcnt < TOTAL_ROWS; rowcnt++) {
	             
	             ProductMasterVO productMasterVO = new ProductMasterVO();
	             XSSFRow row = sheet.getRow(rowcnt);

	             //cell type 구분하여 담기  
	             String[] cellItemTmp = new String[TOTAL_CELLS]; 
		         for(int cellcnt=0;cellcnt<TOTAL_CELLS;cellcnt++){
		            myCell = row.getCell(cellcnt); 
		            
		            if(myCell!=null){
			            if(myCell.getCellType()==0){ //cell type 이 숫자인경우
			            	String rawCell = String.valueOf(myCell.getNumericCellValue());
			            	int endChoice = rawCell.lastIndexOf("E");
			            	if(endChoice>0){
			            		rawCell= rawCell.substring(0, endChoice);
				            	rawCell= rawCell.replace(".", "");
			            	}
			            	cellItemTmp[cellcnt]=rawCell;
			            }else if(myCell.getCellType()==1){ //cell type 이 일반/문자 인경우
			            	cellItemTmp[cellcnt] = myCell.getStringCellValue(); 
			            }else{//그외 cell type
			            	cellItemTmp[cellcnt] = ""; 
			            }
			            this.logger.debug("row : ["+rowcnt+"] cell : ["+cellcnt+"] celltype : ["+myCell.getCellType()+"] ->"+ cellItemTmp[cellcnt]);
			            excelInfo="row : ["+rowcnt+"] cell : ["+cellcnt+"] celltype : ["+myCell.getCellType()+"] ->"+ cellItemTmp[cellcnt];
		            }else{
		            	
		            	cellItemTmp[cellcnt] = "";
		            }
		         }
	         
		         if(cellItemTmp.length>0 && cellItemTmp[0] != ""){
		        	 
		        	 //productCode 값 celltype에 의해 뒤에 0이 없는경우 처리
		        	 String cellProductCode="";
		
		        	 if(cellItemTmp[0].length()<8){
		        		 int fill=8-cellItemTmp[0].length();
		        		 String fillString="0";
		        		 
		        		 for (int f=0; f<fill-1;f++){
		        			 fillString=fillString+"0";
		        		 }    		 
		        		 cellProductCode= cellItemTmp[0]+fillString;
		        		 
		        	 }else{
		        		 cellProductCode= cellItemTmp[0];
		        	 }
		        		 
		        	 if(cellItemTmp.length>0){ productMasterVO.setProductCode(cellProductCode);}
		        	 if(cellItemTmp.length>1){ productMasterVO.setBarCode(cellItemTmp[1]);}
		        	 if(cellItemTmp.length>2){ productMasterVO.setProductName(cellItemTmp[2]);}
		        	 if(cellItemTmp.length>3){ productMasterVO.setProductPrice(cellItemTmp[3]);}
		        	 if(cellItemTmp.length>4){ productMasterVO.setVatRate(cellItemTmp[4]);}
		        	 if(cellItemTmp.length>5){ productMasterVO.setCompanyCode(cellItemTmp[5]);}
		        	 if(cellItemTmp.length>6){ productMasterVO.setGroup1(cellItemTmp[6]);}
		        	 if(cellItemTmp.length>7){ productMasterVO.setGroup1Name(cellItemTmp[7]);}
		        	 if(cellItemTmp.length>8){ productMasterVO.setGroup2(cellItemTmp[8]);}
		        	 if(cellItemTmp.length>9){ productMasterVO.setGroup2Name(cellItemTmp[9]);}
		        	 if(cellItemTmp.length>10){ productMasterVO.setGroup3(cellItemTmp[10]);}
		        	 if(cellItemTmp.length>11){ productMasterVO.setGroup3Name(cellItemTmp[11]);}
	
		             productMasterVO.setCreateUserId(strUserId);
		             productMasterVO.setUpdateUserId(strUserId);
		             productMasterVO.setDeletedYn("N");
			
			         excelUploadList.add(productMasterVO);
		         }
		     	
		       }
           }catch (Exception e){
  
   	    	  excelInfo = excelInfo+"[error] : "+e.getMessage();
	    	  ProductMasterVO productMasterVO = new ProductMasterVO();
	    	  productMasterVO.setErrMsg(excelInfo);
	    	 
	    	  this.logger.info("["+logid+"] Controller getErrMsg : "+productMasterVO.getErrMsg());
	         
	    	  rtnErrorList.add(productMasterVO);
	
	          mv.addObject("rtnErrorList", rtnErrorList);
	          mv.addObject("rtnSuccessList", rtnSuccessList);
	
	          mv.setViewName("/master/uploadResult");
	          
	          //작업이력
	    	  WorkVO work = new WorkVO();
	    	  work.setWorkUserId(strUserId);
	    	  work.setWorkCategory("PD");
	    	  work.setWorkCode("PD005");
	    	  work.setWorkKey3(fname);
	    	  commonSvc.regiHistoryInsert(work);
	          
	          //log Controller execute time end
	          long t2 = System.currentTimeMillis();
	          logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	  	 	
	          return mv;
    	   
       	}
     }
     
     //DB처리
     Map rtmMap = this.productMasterSvc.regiExcelUpload(excelUploadList);

     rtnErrorList = (List)rtmMap.get("rtnErrorList");
     rtnSuccessList = (List)rtmMap.get("rtnSuccessList");
     errorMsgList = (String)rtmMap.get("errorMsgList");

     this.logger.info("rtnErrorList.size() :"+ rtnErrorList.size()+"rtnSuccessList.size() :"+ rtnSuccessList.size());
  
     mv.addObject("rtnErrorList", rtnErrorList);
     mv.addObject("rtnSuccessList", rtnSuccessList);
     
     mv.addObject("errorMsgList", errorMsgList);
       
     mv.setViewName("/master/uploadResult");
     
     //작업이력
	  WorkVO work = new WorkVO();
	  work.setWorkUserId(strUserId);
	  work.setWorkCategory("PD");
	  work.setWorkCode("PD001");
	  work.setWorkKey3(fname);
	  commonSvc.regiHistoryInsert(work);
	 
     //log Controller execute time end
     long t2 = System.currentTimeMillis();
     logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
 	
     return mv;
         
    }
   /**
    * 품목상세현황
    *
    * @param request
    * @param response
    * @param model
    * @param locale
    * @return
    * @throws BizException
    */
   @RequestMapping(value = "/master/productmasterdetail")
   public ModelAndView productMasterDetail(HttpServletRequest request, 
   		                                 HttpServletResponse response,
   		                                 String productCode ) throws BizException 
   {
       
	   //log Controller execute time start
	   String logid=logid();
	   long t1 = System.currentTimeMillis();
	   logger.info("["+logid+"] Controller start ");
	
	   ModelAndView mv = new ModelAndView();
	   
	// 사용자 세션정보
       HttpSession session = request.getSession();
       String strUserId = StringUtil.nvl((String) session.getAttribute("strUserId"));
       String strGroupId = StringUtil.nvl((String) session.getAttribute("strGroupId"));
       String strIp = StringUtil.nvl((String) session.getAttribute("strIp"));
       String sClientIP = StringUtil.nvl((String) session.getAttribute("sClientIP"));
       
       if(strUserId.equals("") || strUserId.equals("null") || strUserId.equals(null)){
       	
       	strIp = request.getRemoteAddr(); //로그인 상태처리		
   		UserVO userState =new UserVO();
   		userState.setUserId(strUserId);
   		userState.setLoginYn("N");
   		userState.setIp(strIp);
   		userState.setConnectIp(sClientIP);
   		userSvc.regiLoginYnUpdate(userState);
           
           //작업이력
	 		WorkVO work = new WorkVO();
	 		work.setWorkUserId(strUserId);
	 		work.setWorkIp(strIp);
	 		work.setWorkCategory("CM");
	 		work.setWorkCode("CM004");
	 		commonSvc.regiHistoryInsert(work);
   		
       	mv.setViewName("/admin/loginForm");
      		return mv;
		}
	   
	   ProductMasterVO productConVO = new ProductMasterVO();
	   ProductMasterVO productMasterVO = new ProductMasterVO();
	 
	   productConVO.setProductCode(productCode);
	   
       // 품목 상세조회
	   productMasterVO = productMasterSvc.getProductDetail(productConVO);
       mv.addObject("productMasterVO", productMasterVO);
	  
	   mv.setViewName("/master/productMasterDetail");
	   
	  //log Controller execute time end
	  long t2 = System.currentTimeMillis();
	  logger.info("["+logid+"] Controller end execute time:[" + (t2-t1)/1000.0 + "] seconds");
	 	
	   return mv;
   }
   
   /**
 	 * 업로드 디렉토리 세팅
 	 */
 	private static boolean setDirectory( String directory) {
 		File wantedDirectory = new File(directory);
 		if (wantedDirectory.isDirectory())
 			return true;
 	    
 		return wantedDirectory.mkdirs();
 	}
 	
   
}
