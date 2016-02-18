<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/taglib.tld" prefix="taglib"%>

<%@ page language="java" contentType="text/html;charset=euc-kr"%>

<%
    /* ============================================================================== */
    /* =   PAGE : 결제 요청 PAGE                                                    = */
    /* = -------------------------------------------------------------------------- = */
    /* =   이 페이지는 Payplus Plug-in을 통해서 결제자가 결제 요청을 하는 페이지    = */
    /* =   입니다. 아래의 ※ 필수, ※ 옵션 부분과 매뉴얼을 참조하셔서 연동을        = */
    /* =   진행하여 주시기 바랍니다.                                                = */
    /* = -------------------------------------------------------------------------- = */
    /* =   연동시 오류가 발생하는 경우 아래의 주소로 접속하셔서 확인하시기 바랍니다.= */
    /* =   접속 주소 : http://kcp.co.kr/technique.requestcode.do			        = */
    /* = -------------------------------------------------------------------------- = */
    /* =   Copyright (c)  2013   KCP Inc.   All Rights Reserverd.                   = */
    /* ============================================================================== */
%>
<%
    /* ============================================================================== */
    /* =   환경 설정 파일 Include                                                   = */
    /* = -------------------------------------------------------------------------- = */
    /* =   ※ 필수                                                                  = */
    /* =   테스트 및 실결제 연동시 site_conf_inc.jsp 파일을 수정하시기 바랍니다.    = */
    /* = -------------------------------------------------------------------------- = */
%>
	<%@ include file="/kcp/cfg/site_conf_inc.jsp" %>
<%
    /* = -------------------------------------------------------------------------- = */
    /* =   환경 설정 파일 Include END                                               = */
    /* ============================================================================== */
%>
<%!
    /* ============================================================================== */
    /* =   null 값을 처리하는 메소드                                                = */
    /* = -------------------------------------------------------------------------- = */
        public String f_get_parm( String val )
        {
          if ( val == null ) val = "";
          return  val;
        }
    /* ============================================================================== */
%>
<%
  request.setCharacterEncoding( "euc-kr" );

    /* kcp와 통신후 kcp 서버에서 전송되는 결제 요청 정보 */
    String req_tx          = f_get_parm( request.getParameter( "req_tx"         ) ); // 요청 종류         
    String res_cd          = f_get_parm( request.getParameter( "res_cd"         ) ); // 응답 코드         
    String tran_cd         = f_get_parm( request.getParameter( "tran_cd"        ) ); // 트랜잭션 코드     
    String ordr_idxx       = f_get_parm( request.getParameter( "ordr_idxx"      ) ); // 쇼핑몰 주문번호   
    String good_name       = f_get_parm( request.getParameter( "good_name"      ) ); // 상품명            
    String good_mny        = f_get_parm( request.getParameter( "good_mny"       ) ); // 결제 총금액       
    String buyr_name       = f_get_parm( request.getParameter( "buyr_name"      ) ); // 주문자명          
    String buyr_tel1       = f_get_parm( request.getParameter( "buyr_tel1"      ) ); // 주문자 전화번호   
    String buyr_tel2       = f_get_parm( request.getParameter( "buyr_tel2"      ) ); // 주문자 핸드폰 번호
    String buyr_mail       = f_get_parm( request.getParameter( "buyr_mail"      ) ); // 주문자 E-mail 주소
    String use_pay_method  = f_get_parm( request.getParameter( "use_pay_method" ) ); // 결제 방법         
    String ipgm_date       = f_get_parm( request.getParameter( "ipgm_date"      ) ); // 가상계좌 마감시간 
	String enc_info        = f_get_parm( request.getParameter( "enc_info"       ) ); // 암호화 정보       
    String enc_data        = f_get_parm( request.getParameter( "enc_data"       ) ); // 암호화 데이터     
    String van_code        = f_get_parm( request.getParameter( "van_code"       ) );
    String cash_yn         = f_get_parm( request.getParameter( "cash_yn"        ) );
    String cash_tr_code    = f_get_parm( request.getParameter( "cash_tr_code"   ) );
    /* 기타 파라메터 추가 부분 - Start - */
    String param_opt_1    = f_get_parm( request.getParameter( "param_opt_1"     ) ); // 기타 파라메터 추가 부분
    String param_opt_2    = f_get_parm( request.getParameter( "param_opt_2"     ) ); // 기타 파라메터 추가 부분
    String param_opt_3    = f_get_parm( request.getParameter( "param_opt_3"     ) ); // 기타 파라메터 추가 부분
    /* 기타 파라메터 추가 부분 - End -   */

  String tablet_size     = "1.0"; // 화면 사이즈 고정
  String url             = request.getRequestURL().toString();
%>    
<!DOCTYPE html>
<html>
  <head> 
    <title>모바일 결제</title>
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/images/favicon.ico" type='image/ico'>
    <!--  <meta name="viewport" content="width=device-width, initial-scale=1"> -->
    <meta name="viewport" content="width=device-width, user-scalable=<%=tablet_size%>, initial-scale=<%=tablet_size%>, maximum-scale=<%=tablet_size%>, minimum-scale=<%=tablet_size%>">
    <meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
    <meta http-equiv="Cache-Control" content="No-Cache">
    <meta http-equiv="Pragma" content="No-Cache">
    
    <meta property="og:type" content="website">
    <meta property="og:title" content="Sales Baron">
    <meta property="og:description" content="나도 판매왕 되기">
    <meta property="og:image" content="<%= request.getContextPath() %>/images/salesbaron.jpg">

    <!-- Latest compiled and minified CSS-->
	<link href="<%= request.getContextPath() %>/css/reset.css" rel="stylesheet">
	<link href="<%= request.getContextPath() %>/css/common.css" rel="stylesheet">
	<link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
	<link href="<%= request.getContextPath() %>/css/comunity.css" rel="stylesheet">
	
	<link href="<%= request.getContextPath() %>/kcp/mobile_sample/css/style.css" rel="stylesheet" type="text/css" id="cssLink"/>

	<script>

	  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');


		  ga('create', 'UA-73697440-1', 'auto');

		  ga('set', 'userId', '116577077'); // 로그인한 User-ID를 사용하여 User-ID를 설정합니다.

		  ga('send', 'pageview');

	</script>


	<!-- 거래등록 하는 kcp 서버와 통신을 위한 스크립트-->
	<script type="text/javascript" src="<%= request.getContextPath() %>/kcp/mobile_sample/js/approval_key.js"></script>
	
<!--	<script src="http://lite.payapp.kr/public/api/payapp-lite.js"></script> -->
		

<%
    /* ============================================================================== */
    /* =   Javascript source Include                                                = */
    /* = -------------------------------------------------------------------------- = */
    /* =   ※ 필수                                                                  = */
    /* =   테스트 및 실결제 연동시 site_conf_inc.jsp파일을 수정하시기 바랍니다.     = */
    /* = -------------------------------------------------------------------------- = */
%>
    <script type="text/javascript" src="<%= g_conf_js_url %>"></script>
<%
    /* = -------------------------------------------------------------------------- = */
    /* =   Javascript source Include END                                            = */
    /* ============================================================================== */
%>
  <script type="text/javascript">
  /* 
  PayApp.setDefault('userid',     'payapptest');
  PayApp.setDefault('shopname',   '좋은상점');

  function myPay() {
	  PayApp.setParam('userid', 'jeonpro');
	  PayApp.setParam('recvphone', '01067471995');
      PayApp.setParam('goodname', '착한상품');
      PayApp.setParam('price',    '1000');
      PayApp.setParam('vccode', 'krw');
      PayApp.call();
  }
  */
  var controlCss = "<%= request.getContextPath() %>/kcp/mobile_sample/css/style.css";
  var isMobile = {
    Android: function() {
      return navigator.userAgent.match(/Android/i);
    },
    BlackBerry: function() {
      return navigator.userAgent.match(/BlackBerry/i);
    },
    iOS: function() {
      return navigator.userAgent.match(/iPhone|iPad|iPod/i);
    },
    Opera: function() {
      return navigator.userAgent.match(/Opera Mini/i);
    },
    Windows: function() {
      return navigator.userAgent.match(/IEMobile/i);
    },
    any: function() {
      return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
    }
  };

  //alert( isMobile.any() )
  
  var key='${key}';

  if( isMobile.any()==null){
	location.href='<%= request.getContextPath() %>/custsale?key='+key;  
	//location.href='<%= request.getContextPath() %>/kcp/mobile_sample/order_mobile.jsp';    
  }else{
	// location.href='<%= request.getContextPath() %>/kcp/mobile_sample/order_mobile.jsp';  
  }
  
  /* 주문번호 생성 예제 */
  function init_orderid()
  {
    var today = new Date();
    var year  = today.getFullYear();
    var month = today.getMonth() + 1;
    var date  = today.getDate();
    var time  = today.getTime();

    if (parseInt(month) < 10)
      month = "0" + month;

    if (parseInt(date) < 10)
      date  = "0" + date;

    var order_idxx = "TEST" + year + "" + month + "" + date + "" + time;
    var ipgm_date  = year + "" + month + "" + date;

    document.order_info.ordr_idxx.value = order_idxx;
    document.order_info.ipgm_date.value = ipgm_date;
  }

   /* kcp web 결제창 호츨 (변경불가) */
  function call_pay_form()
  {
    var v_frm = document.order_info;

    document.getElementById("wrap").style.display = "none";
    document.getElementById("layer_all").style.display  = "block";

    v_frm.target = "frm_all";
    v_frm.action = PayUrl;

    if (v_frm.Ret_URL.value == "")
    {
	  /* Ret_URL값은 현 페이지의 URL 입니다. */
	  alert("연동시 Ret_URL을 반드시 설정하셔야 됩니다.");
      return false;
    }
    else
    {
      v_frm.submit();
    }
  }

   /* kcp 통신을 통해 받은 암호화 정보 체크 후 결제 요청 (변경불가) */
  function chk_pay()
  {

    self.name = "tar_opener";
    var pay_form = document.pay_form;

    if (pay_form.res_cd.value == "3001" )
    {
      alert("사용자가 취소하였습니다.");
      pay_form.res_cd.value = "";
    }
    else if (pay_form.res_cd.value == "3000" )
    {
      alert("30만원 이상 결제를 할 수 없습니다.");
      pay_form.res_cd.value = "";
    }

    document.getElementById("wrap").style.display = "block";
    document.getElementById("layer_all").style.display  = "none";

    if (pay_form.enc_info.value)
      pay_form.submit();
  }

  function jsf__chk_type()
  {
    if ( document.order_info.ActionResult.value == "card" )
    {
      document.order_info.pay_method.value = "CARD";
    }
    else if ( document.order_info.ActionResult.value == "acnt" )
    {
      document.order_info.pay_method.value = "BANK";
    }
    else if ( document.order_info.ActionResult.value == "vcnt" )
    {
      document.order_info.pay_method.value = "VCNT";
    }
    else if ( document.order_info.ActionResult.value == "mobx" )
    {
      document.order_info.pay_method.value = "MOBX";
    }
    else if ( document.order_info.ActionResult.value == "ocb" )
    {
      document.order_info.pay_method.value = "TPNT";
      document.order_info.van_code.value = "SCSK";
    }
    else if ( document.order_info.ActionResult.value == "tpnt" )
    {
      document.order_info.pay_method.value = "TPNT";
      document.order_info.van_code.value = "SCWB";
    }
    else if ( document.order_info.ActionResult.value == "scbl" )
    {
      document.order_info.pay_method.value = "GIFT";
      document.order_info.van_code.value = "SCBL";
    }
    else if ( document.order_info.ActionResult.value == "sccl" )
    {
      document.order_info.pay_method.value = "GIFT";
      document.order_info.van_code.value = "SCCL";
    }
    else if ( document.order_info.ActionResult.value == "schm" )
    {
      document.order_info.pay_method.value = "GIFT";
      document.order_info.van_code.value = "SCHM";
    }
  }
    </script>

	<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.11.2.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/jquery-ui-1.11.4.custom/jquery-ui.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/bootstrap-3.3.4-dist/js/bootstrap.min.js"></script>

<body onload="jsf__chk_type();init_orderid();chk_pay();">
	<div id="wrap" class="wrap"  >
		<!-- 헤더 -->
		  <header>
		   <div class="mb_top">
				<h1 class="head_logo"></h1>
			</div>
		  </header>
		<!--//헤더 -->  
		<!-- container -->
		<div id="container" class="clm_order_detail" >
	    <div id="m_content form" >
	      <div class="clm_order_detail">
		    <!-- 타이틀 -->
	        <div class="clm_acdo_tit">
	          <h1>상품(모바일) 판매페이지</h1>   
	        </div>
	        <!--// 타이틀 --> 
			<form:form class="form-inline" role="form"  id="orderResultForm" name="orderResultForm" method="post" action="" >   
	        <!-- 1.접수정보 -->
	        <div class="clm_acdo_tit2">
	          <h2 class="h2_txo"> <strong><em class="num">1. </em></strong>고객정보</h2>
	        </div>
	        <div class="clm_acdo_sec">
	          <dl class="clm_ip2">
	            <dt><span class="tit">고객키</span></dt>
	            <dd>
	              <p class="tx1" id="customerKey">${customerKey}</p>
	            </dd>
	          </dl>
	        </div>
	        <!--// 1. 접수정보 --> 
	        <!-- 2.배송정보 -->
	        <div class="clm_acdo_tit2">
	          <h2 class="h2_txo"> <strong><em class="date">2.</em></strong>&nbsp;상품정보</h2>
	        </div>
	        <div class="clm_acdo_sec">
	          <dl class="clm_ip2">
	            <dt><span class="tit">상품명</span></dt>
	            <dd>
	              <p class="tx1" id="productName">${productName}</p>
	            </dd>
	          </dl>
	          <hr class="odr_line_ty1">
	          <dl class="clm_ip2">
	            <dt><span class="tit">상품코드</span></dt>
	            <dd>
	              <p class="tx1" id="productCode">${productCode}</p>
	            </dd>
	          </dl>
	          <hr class="odr_line_ty1">
	          <dl class="clm_ip2">
	            <dt><span class="tit">상품가격</span></dt>
	            <dd>
	              <p class="tx1" id="productPrice">${productPrice}</p>
	            </dd>
	          </dl>
	        </div>
	        </form:form>
	        </div>
	        
	        <div class="clm_detail_btn">
	          <div class="clm_btn">
	            <script src="https://www.paypalobjects.com/js/external/paypal-button.min.js?merchant=Z75Q9DK6L5VRC" async="async" 
								    data-name="offact" 
								    data-button="buynow" 
								    data-quantity="1" 
								    data-callback="http://dev.addys.co.kr" 
								    data-env="sandbox"
								    data-charset="UTF-8"
								></script>
	          </div>
	          <br></br>
	          <div align="center" id="cert_info">
				<!-- 주문정보 입력 form : order_info -->
				<form name="order_info" method="post">
		
				   <!-- 신용카드 -->
		
				   <!-- 주문번호(ordr_idxx) -->
				   <input type="hidden" name="ordr_idxx" value="" />
				   <!-- 상품명(good_name) -->
				   <input type="hidden" name="good_name" value="좋은상품" />
				    <!-- 결제금액(good_mny) - ※ 필수 : 값 설정시 ,(콤마)를 제외한 숫자만 입력하여 주십시오. -->
				   <input type="hidden" name="good_mny" value="1500" />
				    <!-- 주문자명(buyr_name) -->
				   <input type="hidden" name="buyr_name" value="박영준" />
				   <!-- 주문자 E-mail(buyr_mail) -->
				   <input type="hidden" name="buyr_mail" value="test@test.co.kr" />
				   <!-- 주문자 연락처1(buyr_tel1) -->
				   <input type="hidden" name="buyr_tel1" value="02-2108-1000" />
				   <!-- 휴대폰번호(buyr_tel2) -->
				   <input type="hidden" name="buyr_tel2" value="010-0000-0000" />

				   <!-- 결제 요청/처음으로 이미지 -->
		             
		           <div class="clm_acdo_sec" id="display_pay_button" style="display:block">
		                      <th>KCP 모바일지불 방법</th>
		                      <td>
			                    <select name="ActionResult" onchange="jsf__chk_type();">
					                <option value="" selected>선택하십시오</option>
					                <option value="card">신용카드</option>
					                <option value="acnt">계좌이체</option>
					                <option value="vcnt">가상계좌</option>
					                <!--  <option value="mobx">휴대폰</option>-->
					                <option value="ocb">OK캐쉬백</option>
					                <option value="tpnt">복지포인트</option>
					                <option value="scbl">도서상품권</option>
					                <option value="sccl">문화상품권</option>
					                <option value="schm">해피머니</option>
					            </select>
		                      </td>
		                 <input name="" type="button" class="submit" value="결제요청" onclick="kcp_AJAX('<%= request.getContextPath() %>/kcp/mobile_sample/order_approval.jsp');">
		           </div>
	        </div>

      </div>

  <!-- 공통정보 -->
  <input type="hidden" name="req_tx"          value="pay">                           <!-- 요청 구분 -->
  <input type="hidden" name="shop_name"       value="<%= g_conf_site_name %>">       <!-- 사이트 이름 --> 
  <input type="hidden" name="site_cd"         value="<%= g_conf_site_cd   %>">       <!-- 사이트 키 -->
  <input type="hidden" name="currency"        value="410"/>                          <!-- 통화 코드 -->
  <input type="hidden" name="eng_flag"        value="N"/>                            <!-- 한 / 영 -->
  <!-- 결제등록 키 -->
  <input type="hidden" name="approval_key"    id="approval">
  <!-- 인증시 필요한 파라미터(변경불가)-->
  <input type="hidden" name="escw_used"       value="N">
  <input type="hidden" name="pay_method"      value="">
  <input type="hidden" name="van_code"        value="<%=van_code%>">
  <!-- 신용카드 설정 -->
  <input type="hidden" name="quotaopt"        value="12"/>                           <!-- 최대 할부개월수 -->
  <!-- 가상계좌 설정 -->
  <input type="hidden" name="ipgm_date"       value=""/>
  <!-- 가맹점에서 관리하는 고객 아이디 설정을 해야 합니다.(필수 설정) -->
  <input type="hidden" name="shop_user_id"    value=""/>
  <!-- 복지포인트 결제시 가맹점에 할당되어진 코드 값을 입력해야합니다.(필수 설정) -->
  <input type="hidden" name="pt_memcorp_cd"   value=""/>
  <!-- 현금영수증 설정 -->
  <input type="hidden" name="disp_tax_yn"     value="Y"/>
  <!-- 리턴 URL (kcp와 통신후 결제를 요청할 수 있는 암호화 데이터를 전송 받을 가맹점의 주문페이지 URL) -->
  <input type="hidden" name="Ret_URL"         value="<%=s_host_url%>/salesb/kcp/mobile_sample/order_mobile.jsp">
  <!--<input type="hidden" name="Ret_URL"         value="<%=url%>">-->
  <!-- 화면 크기조정 -->
  <input type="hidden" name="tablet_size"     value="<%=tablet_size%>">

  <!-- 추가 파라미터 ( 가맹점에서 별도의 값전달시 param_opt 를 사용하여 값 전달 ) -->
  <input type="hidden" name="param_opt_1"     value="">
  <input type="hidden" name="param_opt_2"     value="">
  <input type="hidden" name="param_opt_3"     value="">

  <!-- 결제 정보 등록시 응답 타입 ( 필드가 없거나 값이 '' 일경우 TEXT, 값이 XML 또는 JSON 지원 -->
  <input type="hidden" name="response_type"  value="TEXT"/>
  <input type="hidden" name="PayUrl"   id="PayUrl"   value=""/>
  <input type="hidden" name="traceNo"  id="traceNo"  value=""/>

<%
    /* ============================================================================== */
    /* =   옵션 정보                                                                = */
    /* = -------------------------------------------------------------------------- = */
    /* =   ※ 옵션 - 결제에 필요한 추가 옵션 정보를 입력 및 설정합니다.             = */
    /* = -------------------------------------------------------------------------- = */
    /* 카드사 리스트 설정
    예) 비씨카드와 신한카드 사용 설정시
    <input type="hidden" name='used_card'    value="CCBC:CCLG">

    /*  무이자 옵션
            ※ 설정할부    (가맹점 관리자 페이지에 설정 된 무이자 설정을 따른다)                             - "" 로 설정
            ※ 일반할부    (KCP 이벤트 이외에 설정 된 모든 무이자 설정을 무시한다)                           - "N" 로 설정
            ※ 무이자 할부 (가맹점 관리자 페이지에 설정 된 무이자 이벤트 중 원하는 무이자 설정을 세팅한다)   - "Y" 로 설정
    <input type="hidden" name="kcp_noint"       value=""/> */

    /*  무이자 설정
            ※ 주의 1 : 할부는 결제금액이 50,000 원 이상일 경우에만 가능
            ※ 주의 2 : 무이자 설정값은 무이자 옵션이 Y일 경우에만 결제 창에 적용
            예) 전 카드 2,3,6개월 무이자(국민,비씨,엘지,삼성,신한,현대,롯데,외환) : ALL-02:03:04
            BC 2,3,6개월, 국민 3,6개월, 삼성 6,9개월 무이자 : CCBC-02:03:06,CCKM-03:06,CCSS-03:06:04
    <input type="hidden" name="kcp_noint_quota" value="CCBC-02:03:06,CCKM-03:06,CCSS-03:06:09"/> */

    /* KCP는 과세상품과 비과세상품을 동시에 판매하는 업체들의 결제관리에 대한 편의성을 제공해드리고자, 
       복합과세 전용 사이트코드를 지원해 드리며 총 금액에 대해 복합과세 처리가 가능하도록 제공하고 있습니다
       복합과세 전용 사이트 코드로 계약하신 가맹점에만 해당이 됩니다
       상품별이 아니라 금액으로 구분하여 요청하셔야 합니다
       총결제 금액은 과세금액 + 부과세 + 비과세금액의 합과 같아야 합니다. 
       (good_mny = comm_tax_mny + comm_vat_mny + comm_free_mny)

        <input type="hidden" name="tax_flag"       value="TG03">  <!-- 변경불가	   -->
        <input type="hidden" name="comm_tax_mny"   value=""    >  <!-- 과세금액	   --> 
        <input type="hidden" name="comm_vat_mny"   value=""    >  <!-- 부가세	   -->
        <input type="hidden" name="comm_free_mny"  value=""    >  <!-- 비과세 금액 --> */
    /* = -------------------------------------------------------------------------- = */
    /* =   옵션 정보 END                                                            = */
    /* ============================================================================== */
%>
		</form>		   
	 </div>
   </div>
   <div id="footer" class="footer">
    <span class="Copyright">Copyright 2015 ⓒ salesb Corp. All rights reserved. v1.0.0</span>
  </div>
   
 </div>

 <!-- 스마트폰에서 KCP 결제창을 레이어 형태로 구현-->
<div id="layer_all" style="position:absolute; left:0px; top:0px; width:100%;height:100%; z-index:1; display:none;">
    <table height="100%" width="100%" border="-" cellspacing="0" cellpadding="0" style="text-align:center">
        <tr height="100%" width="100%">
            <td>
                <iframe name="frm_all" frameborder="0" marginheight="0" marginwidth="0" border="0" width="100%" height="100%" scrolling="auto"></iframe>
            </td>
        </tr>
    </table>
</div>
<form name="pay_form" method="post" action="<%= request.getContextPath() %>/kcp/mobile_sample/pp_cli_hub.jsp">
    <input type="hidden" name="req_tx"         value="<%=req_tx%>">               <!-- 요청 구분          -->
    <input type="hidden" name="res_cd"         value="<%=res_cd%>">               <!-- 결과 코드          -->
    <input type="hidden" name="tran_cd"        value="<%=tran_cd%>">              <!-- 트랜잭션 코드      -->
    <input type="hidden" name="ordr_idxx"      value="<%=ordr_idxx%>">            <!-- 주문번호           -->
    <input type="hidden" name="good_mny"       value="<%=good_mny%>">             <!-- 휴대폰 결제금액    -->
    <input type="hidden" name="good_name"      value="<%=good_name%>">            <!-- 상품명             -->
    <input type="hidden" name="buyr_name"      value="<%=buyr_name%>">            <!-- 주문자명           -->
    <input type="hidden" name="buyr_tel1"      value="<%=buyr_tel1%>">            <!-- 주문자 전화번호    -->
    <input type="hidden" name="buyr_tel2"      value="<%=buyr_tel2%>">            <!-- 주문자 휴대폰번호  -->
    <input type="hidden" name="buyr_mail"      value="<%=buyr_mail%>">            <!-- 주문자 E-mail      -->
    <input type="hidden" name="cash_yn"		   value="<%=cash_yn%>">              <!-- 현금영수증 등록여부-->
    <input type="hidden" name="enc_info"       value="<%=enc_info%>">
    <input type="hidden" name="enc_data"       value="<%=enc_data%>">
    <input type="hidden" name="use_pay_method" value="<%=use_pay_method%>">
    <input type="hidden" name="cash_tr_code"   value="<%=cash_tr_code%>">
    <!-- 추가 파라미터 -->
    <input type="hidden" name="param_opt_1"	   value="<%=param_opt_1%>">
    <input type="hidden" name="param_opt_2"	   value="<%=param_opt_2%>">
    <input type="hidden" name="param_opt_3"	   value="<%=param_opt_3%>">
</form>       
</body>
</html>