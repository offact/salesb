<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/taglib.tld" prefix="taglib"%>

<%
    /* ============================================================================== */
    /* =   PAGE : 결제 결과 출력 PAGE                                               = */
    /* = -------------------------------------------------------------------------- = */
    /* =   결제 요청 결과값을 출력하는 페이지입니다.                                = */
    /* = -------------------------------------------------------------------------- = */
    /* =   연동시 오류가 발생하는 경우 아래의 주소로 접속하셔서 확인하시기 바랍니다.= */
    /* =   접속 주소 : http://kcp.co.kr/technique.requestcode.do			        = */
    /* = -------------------------------------------------------------------------- = */
    /* =   Copyright (c)  2013   KCP Inc.   All Rights Reserverd.                   = */
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
    request.setCharacterEncoding("utf-8") ;
    /* ============================================================================== */
    /* =   지불 결과                                                                = */
    /* = -------------------------------------------------------------------------- = */
    String site_cd          = f_get_parm( request.getParameter( "site_cd"        ) );      // 사이트 코드
    String req_tx           = f_get_parm( request.getParameter( "req_tx"         ) );      // 요청 구분(승인/취소)
    String use_pay_method   = f_get_parm( request.getParameter( "use_pay_method" ) );      // 사용 결제 수단
    String bSucc            = f_get_parm( request.getParameter( "bSucc"          ) );      // 업체 DB 정상처리 완료 여부
    /* = -------------------------------------------------------------------------- = */
    String res_cd           = f_get_parm( request.getParameter( "res_cd"         ) );      // 결과 코드
    String res_msg          = f_get_parm( request.getParameter( "res_msg"        ) );      // 결과 메시지
    String res_msg_bsucc    = "";
    /* = -------------------------------------------------------------------------- = */
    String amount           = f_get_parm( request.getParameter( "amount"         ) );      // 금액
    String ordr_idxx        = f_get_parm( request.getParameter( "ordr_idxx"      ) );      // 주문번호
    String tno              = f_get_parm( request.getParameter( "tno"            ) );      // KCP 거래번호
    String good_mny         = f_get_parm( request.getParameter( "good_mny"       ) );      // 결제 금액
    String good_name        = f_get_parm( request.getParameter( "good_name"      ) );      // 상품명
    String buyr_name        = f_get_parm( request.getParameter( "buyr_name"      ) );      // 구매자명
    String buyr_tel1        = f_get_parm( request.getParameter( "buyr_tel1"      ) );      // 구매자 전화번호
    String buyr_tel2        = f_get_parm( request.getParameter( "buyr_tel2"      ) );      // 구매자 휴대폰번호
    String buyr_mail        = f_get_parm( request.getParameter( "buyr_mail"      ) );      // 구매자 E-Mail
    /* = -------------------------------------------------------------------------- = */
    // 공통
    String pnt_issue        = f_get_parm( request.getParameter( "pnt_issue"      ) );      // 포인트 서비스사
    String app_time         = f_get_parm( request.getParameter( "app_time"       ) );      // 승인시간 (공통)
    /* = -------------------------------------------------------------------------- = */
    // 신용카드
    String card_cd          = f_get_parm( request.getParameter( "card_cd"        ) );      // 카드 코드
    String card_name        = f_get_parm( request.getParameter( "card_name"      ) );      // 카드명
    String noinf            = f_get_parm( request.getParameter( "noinf"          ) );      // 무이자 여부
    String quota            = f_get_parm( request.getParameter( "quota"          ) );      // 할부개월
    String app_no           = f_get_parm( request.getParameter( "app_no"         ) );      // 승인번호
    /* = -------------------------------------------------------------------------- = */
    // 계좌이체
    String bank_name        = f_get_parm( request.getParameter( "bank_name"      ) );      // 은행명
    String bank_code        = f_get_parm( request.getParameter( "bank_code"      ) );      // 은행코드
    /* = -------------------------------------------------------------------------- = */
    // 가상계좌
    String bankname         = f_get_parm( request.getParameter( "bankname"       ) );      // 입금할 은행
    String depositor        = f_get_parm( request.getParameter( "depositor"      ) );      // 입금할 계좌 예금주
    String account          = f_get_parm( request.getParameter( "account"        ) );      // 입금할 계좌 번호
    String va_date          = f_get_parm( request.getParameter( "va_date"        ) );      // 가상계좌 입금마감시간
    /* = -------------------------------------------------------------------------- = */
    // 포인트
    String add_pnt          = f_get_parm( request.getParameter( "add_pnt"        ) );      // 발생 포인트
    String use_pnt          = f_get_parm( request.getParameter( "use_pnt"        ) );      // 사용가능 포인트
    String rsv_pnt          = f_get_parm( request.getParameter( "rsv_pnt"        ) );      // 적립 포인트
    String pnt_app_time     = f_get_parm( request.getParameter( "pnt_app_time"   ) );      // 승인시간
    String pnt_app_no       = f_get_parm( request.getParameter( "pnt_app_no"     ) );      // 승인번호
    String pnt_amount       = f_get_parm( request.getParameter( "pnt_amount"     ) );      // 적립금액 or 사용금액
    /* = -------------------------------------------------------------------------- = */
    //휴대폰
    String commid           = f_get_parm( request.getParameter( "commid"         ) );      // 통신사 코드
    String mobile_no        = f_get_parm( request.getParameter( "mobile_no"      ) );      // 휴대폰 번호
    /* = -------------------------------------------------------------------------- = */
    //상품권
    String tk_van_code      = f_get_parm( request.getParameter( "tk_van_code"    ) );      // 발급사 코드
    String tk_app_no        = f_get_parm( request.getParameter( "tk_app_no"      ) );      // 승인 번호
    /* = -------------------------------------------------------------------------- = */
    // 현금영수증
    String cash_yn          = f_get_parm( request.getParameter( "cash_yn"        ) );      // 현금 영수증 등록 여부
    String cash_authno      = f_get_parm( request.getParameter( "cash_authno"    ) );      // 현금 영수증 승인 번호
    String cash_tr_code     = f_get_parm( request.getParameter( "cash_tr_code"   ) );      // 현금 영수증 발행 구분
    String cash_id_info     = f_get_parm( request.getParameter( "cash_id_info"   ) );      // 현금 영수증 등록 번호
    /* ============================================================================== */

    String req_tx_name = "";

    if     ( req_tx.equals( "pay" ) ) 
    {
        req_tx_name = "지불" ;
    }
    else if( req_tx.equals( "mod" ) )
    {
        req_tx_name = "취소/매입" ;
    }

    /* ============================================================================== */
    /* =   가맹점 측 DB 처리 실패시 상세 결과 메시지 설정                           = */
    /* = -------------------------------------------------------------------------- = */

    if ( "pay".equals ( req_tx ) )
    {
        // 업체 DB 처리 실패
        if ( "false".equals ( bSucc ) )
        {
            if ( "0000".equals ( res_cd ) )
            {
                res_msg_bsucc = "결제는 정상적으로 이루어졌지만 쇼핑몰에서 결제 결과를 처리하는 중 오류가 발생하여 시스템에서 자동으로 취소 요청을 하였습니다. <br> 쇼핑몰로 전화하여 확인하시기 바랍니다." ;
            }
            else
            {
                res_msg_bsucc = "결제는 정상적으로 이루어졌지만 쇼핑몰에서 결제 결과를 처리하는 중 오류가 발생하여 시스템에서 자동으로 취소 요청을 하였으나, <br> <b>취소가 실패 되었습니다.</b><br> 쇼핑몰로 전화하여 확인하시기 바랍니다." ;
            }
        }
    }

    /* = -------------------------------------------------------------------------- = */
    /* =   가맹점 측 DB 처리 실패시 상세 결과 메시지 설정 끝                        = */
    /* ============================================================================== */

%>

<!DOCTYPE html>
<html>
  <head> 
    <title>kcp</title>
    <link rel="shortcut icon" href="<%= request.getContextPath() %>/images/favicon.ico" type='image/ico'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Latest compiled and minified CSS-->
	<link href="<%= request.getContextPath() %>/css/reset.css" rel="stylesheet">
	<link href="<%= request.getContextPath() %>/css/common.css" rel="stylesheet">
	<link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet">
	<link href="<%= request.getContextPath() %>/css/comunity.css" rel="stylesheet">
	
	<link href="<%= request.getContextPath() %>/kcp/sample/css/style.css" rel="stylesheet" type="text/css" id="cssLink"/>

	<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.11.2.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/jquery-ui-1.11.4.custom/jquery-ui.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/bootstrap-3.3.4-dist/js/bootstrap.min.js"></script>
	<script type="text/javascript">
        /* 신용카드 영수증 */ 
        /* 실결제시 : "https://admin8.kcp.co.kr/assist/bill.BillAction.do?cmd=card_bill&tno=" */ 
        /* 테스트시 : "https://testadmin8.kcp.co.kr/assist/bill.BillAction.do?cmd=card_bill&tno=" */ 
         function receiptView( tno, ordr_idxx, amount ) 
        {
            receiptWin = "https://admin8.kcp.co.kr/assist/bill.BillActionNew.do?cmd=card_bill&tno=";
            receiptWin += tno + "&";
            receiptWin += "order_no=" + ordr_idxx + "&"; 
            receiptWin += "trade_mony=" + amount ;

            window.open(receiptWin, "", "width=455, height=815"); 
        }

        /* 현금 영수증 */ 
        /* 실결제시 : "https://admin.kcp.co.kr/Modules/Service/Cash/Cash_Bill_Common_View.jsp" */ 
        /* 테스트시 : "https://testadmin8.kcp.co.kr/Modules/Service/Cash/Cash_Bill_Common_View.jsp" */   
        function receiptView2( site_cd, order_id, bill_yn, auth_no ) 
        {
            receiptWin2 = "https://testadmin8.kcp.co.kr/Modules/Service/Cash/Cash_Bill_Common_View.jsp"; 
            receiptWin2 += "?"; 
            receiptWin2 += "term_id=PGNW" + site_cd + "&"; 
            receiptWin2 += "orderid=" + order_id + "&"; 
            receiptWin2 += "bill_yn=" + bill_yn + "&"; 
            receiptWin2 += "authno=" + auth_no ; 

            window.open(receiptWin2, "", "width=370, height=625"); 
        }
        /* 가상 계좌 모의입금 페이지 호출 */
        /* 테스트시에만 사용가능 */
        /* 실결제시 해당 스크립트 주석처리 */
        function receiptView3() 
        {
            receiptWin3 = "http://devadmin.kcp.co.kr/Modules/Noti/TEST_Vcnt_Noti.jsp"; 
            window.open(receiptWin3, "", "width=520, height=300"); 
        }
    </script>
<body>
<%
    /* ============================================================================== */
    /* =   결제 결과 코드 및 메시지 출력(결과페이지에 반드시 출력해주시기 바랍니다.)= */
    /* = -------------------------------------------------------------------------- = */
    /* =   결제 정상 : res_cd값이 0000으로 설정됩니다.                              = */
    /* =   결제 실패 : res_cd값이 0000이외의 값으로 설정됩니다.                     = */
    /* = -------------------------------------------------------------------------- = */
%>
<form:form class="form-inline" role="form"  id="orderResultForm" name="orderResultForm" method="post" action="" >
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
          <h1>KCP 주문결과</h1>   
        </div>
        <!--// 타이틀 --> 
        <!-- 1.접수정보 -->
        <div class="clm_acdo_tit2">
          <h2 class="h2_txo"> <strong><em class="num">1. </em></strong>주문결과</h2>
        </div>
        <div class="clm_acdo_sec">
          <dl class="clm_ip2">
            <dt><span class="tit">결과</span></dt>
            <dd>
              <p class="tx1" id="result"><%=res_cd%>(<%=res_msg%>)
              <%
    // 처리 페이지(pp_cli_hub.jsp)에서 가맹점 DB처리 작업이 실패한 경우 상세메시지를 출력합니다.
    if( !"".equals ( res_msg_bsucc ) )
    {
%>
결과 상세 메세지:<%=res_msg_bsucc%>
<%
    }
%>
              </p>
            </dd>
          </dl>
<%
    /* = -------------------------------------------------------------------------- = */
    /* =   결제 결과 코드 및 메시지 출력 끝                                         = */
    /* ============================================================================== */
%>

<%
    /* ============================================================================== */
    /* =   01. 결제 결과 출력                                                       = */
    /* = -------------------------------------------------------------------------- = */
    if ( "pay".equals ( req_tx ) )
    {
        /* ============================================================================== */
        /* =   01-1. 업체 DB 처리 정상(bSucc값이 false가 아닌 경우)                     = */
        /* = -------------------------------------------------------------------------- = */
        if ( ! "false".equals ( bSucc ) )
        {
            /* ============================================================================== */
            /* =   01-1-1. 정상 결제시 결제 결과 출력 ( res_cd값이 0000인 경우)             = */
            /* = -------------------------------------------------------------------------- = */
            if ( "0000".equals ( res_cd ) )
            {
%>          
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">주문금액</span></dt>
            <dd>
              <p class="tx1" id="good_mny"><%= good_mny %>원</p>
            </dd>
          </dl>
        </div>
        <!--// 1. 접수정보 --> 
        <!-- 2.배송정보 -->
        <div class="clm_acdo_tit2">
          <h2 class="h2_txo"> <strong><em class="date">2.</em></strong>&nbsp;주문자 정보</h2>
        </div>
        <div class="clm_acdo_sec">
          <dl class="clm_ip2">
            <dt><span class="tit">주문자명</span></dt>
            <dd>
              <p class="tx1" id="buyr_name"><%= buyr_name %></p>
            </dd>
          </dl>
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">주문번호</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><%= ordr_idxx %></p>
            </dd>
          </dl>
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">주문내역</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx">주문 내역은 다음 사이트를 통해 확인 가능하십니다.<font style='color:blue'><a href='${domainUrl}'>http://salesb.net</a></font></p>
            </dd>
          </dl>
<%
                /* ============================================================================== */
                /* =   신용카드 결제 결과 출력                                             = */
                /* = -------------------------------------------------------------------------- = */
                if ( use_pay_method.equals("100000000000") )
                {
%>
     <!-- 3.신용카드 정보 -->
        <div class="clm_acdo_tit2">
          <h2 class="h2_txo"> <strong><em class="date">2.</em></strong>&nbsp;신용카드 정보</h2>
        </div>
        <div class="clm_acdo_sec">
          <dl class="clm_ip2">
            <dt><span class="tit">결제 카드</span></dt>
            <dd>
              <p class="tx1" id="buyr_name"><%= card_cd %> / <%= card_name %></p>
            </dd>
          </dl>
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">승인 번호</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><%= app_no %></p>
            </dd>
          </dl>
<%
                    /* = -------------------------------------------------------------- = */
                    /* =   복합결제(포인트+신용카드) 승인 결과 처리                     = */
                    /* = -------------------------------------------------------------- = */
                     if ( pnt_issue.equals("SCSK") || pnt_issue.equals( "SCWB" ) )
                    {
%>          
      <!-- 3.포인트 정보 -->
        <div class="clm_acdo_tit2">
          <h2 class="h2_txo"> <strong><em class="date">2.</em></strong>&nbsp;포인트 정보</h2>
        </div>
        <div class="clm_acdo_sec">
          <dl class="clm_ip2">
            <dt><span class="tit">포인트사</span></dt>
            <dd>
              <p class="tx1" id="buyr_name"><%= pnt_issue %></p>
            </dd>
          </dl>
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">포인트 승인번호</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><%= pnt_app_no %></p>
            </dd>
          </dl>
<%                  }
                    /* ============================================================================== */
                    /* =   신용카드 영수증 출력                                                     = */
                    /* = -------------------------------------------------------------------------- = */
                    /* =   실제 거래건에 대해서 영수증을 출력할 수 있습니다.                        = */
                    /* = -------------------------------------------------------------------------- = */
%>
                    <tr>
                        <th>영수증 확인</th>
                        <td class="sub_content1"><a href="javascript:receiptView('<%=tno%>','<%= ordr_idxx %>','<%= amount %>')"><img src="<%= request.getContextPath() %>/img/btn_receipt.png" alt="영수증을 확인합니다." />
                    </td>
                    </table>
<%              }
                /* ============================================================================== */
                /* =   계좌이체 결제 결과 출력                                                  = */
                /* = -------------------------------------------------------------------------- = */
                else if (use_pay_method.equals("010000000000"))       // 계좌이체
                {
%>          
  		<!-- 3.계좌이체 정보 -->
        <div class="clm_acdo_tit2">
          <h2 class="h2_txo"> <strong><em class="date">2.</em></strong>&nbsp;계좌이체 정보</h2>
        </div>
        <div class="clm_acdo_sec">
          <dl class="clm_ip2">
            <dt><span class="tit">이체 은행</span></dt>
            <dd>
              <p class="tx1" id="buyr_name"><%= bank_name %></p>
            </dd>
          </dl>
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">이체 은행코드</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><%= bank_code %></p>
            </dd>
          </dl>  
<%
                }
                /* ============================================================================== */
                /* =   가상계좌 결제 결과 출력                                                  = */
                /* = -------------------------------------------------------------------------- = */
                else if (use_pay_method.equals("001000000000"))
                {
%>          
  		<!-- 3.가상계좌 정보 -->
        <div class="clm_acdo_tit2">
          <h2 class="h2_txo"> <strong><em class="date">2.</em></strong>&nbsp;가상계좌 정보</h2>
        </div>
        <div class="clm_acdo_sec">
          <dl class="clm_ip2">
            <dt><span class="tit">입금 은행</span></dt>
            <dd>
              <p class="tx1" id="buyr_name"><%= bankname %></p>
            </dd>
          </dl>
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">입금할 계좌 예금주</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><%= depositor %></p>
            </dd>
          </dl>  
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">입금할 계좌 번호</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><%= account %></p>
            </dd>
          </dl>  
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">가상계좌 입금마감시간</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><%= account %></p>
            </dd>
          </dl>          
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">가상계좌 모의입금(테스트시 사용)</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><a href="javascript:receiptView3()"><img src="<%= request.getContextPath() %>/img/btn_vcn.png" alt="모의입금 페이지로 이동합니다." /></p>
            </dd>
          </dl>
 <%
                }
                /* ============================================================================== */
                /* =   포인트 결제 결과 출력                                                    = */
                /* = -------------------------------------------------------------------------- = */
                else if (use_pay_method.equals("000100000000"))
                {
%>         
        <!-- 3.포인트 정보 -->
        <div class="clm_acdo_tit2">
          <h2 class="h2_txo"> <strong><em class="date">2.</em></strong>&nbsp;포인트 정보</h2>
        </div>
        <div class="clm_acdo_sec">
          <dl class="clm_ip2">
            <dt><span class="tit">포인트사</span></dt>
            <dd>
              <p class="tx1" id="buyr_name"><%= pnt_issue %></p>
            </dd>
          </dl>
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">포인트 승인번호</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><%= pnt_app_no %></p>
            </dd>
          </dl>  
<%
                }
                /* ============================================================================== */
                /* =   휴대폰 결제 결과 출력                                                    = */
                /* = -------------------------------------------------------------------------- = */
                else if (use_pay_method.equals("000010000000"))
                {
%>
        <!-- 3.휴대폰 정보 -->
        <div class="clm_acdo_tit2">
          <h2 class="h2_txo"> <strong><em class="date">2.</em></strong>&nbsp;휴대폰 정보</h2>
        </div>
        <div class="clm_acdo_sec">
          <dl class="clm_ip2">
            <dt><span class="tit">통신사 코드</span></dt>
            <dd>
              <p class="tx1" id="buyr_name"><%= commid %></p>
            </dd>
          </dl>
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">휴대폰 번호</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><%= mobile_no %></p>
            </dd>
          </dl>
<%
                }
                /* ============================================================================== */
                /* =   상품권 결제 결과 출력                                                    = */
                /* = -------------------------------------------------------------------------- = */
                else if (use_pay_method.equals("000000001000"))
                {
%>          
        <!-- 3.상품권 정보 -->
        <div class="clm_acdo_tit2">
          <h2 class="h2_txo"> <strong><em class="date">2.</em></strong>&nbsp;상품권 정보</h2>
        </div>
        <div class="clm_acdo_sec">
          <dl class="clm_ip2">
            <dt><span class="tit">발급사 코드</span></dt>
            <dd>
              <p class="tx1" id="buyr_name"><%= tk_van_code %></p>
            </dd>
          </dl>
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">승인 번호</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><%= tk_app_no %></p>
            </dd>
          </dl>
 <%
                }
                /* ============================================================================== */
                /* =   현금영수증 정보 출력                                                     = */
                /* = -------------------------------------------------------------------------- = */
                if( !"".equals ( cash_yn ) )
                {
                    if ( "010000000000".equals ( use_pay_method ) | "001000000000".equals ( use_pay_method ) )
                    {
%>
         <!-- 3.현금영수증 정보 -->
        <div class="clm_acdo_tit2">
          <h2 class="h2_txo"> <strong><em class="date">2.</em></strong>&nbsp;현금영수증 정보</h2>
        </div>
        <div class="clm_acdo_sec">
          <dl class="clm_ip2">
            <dt><span class="tit">현금영수증 등록여부</span></dt>
            <dd>
              <p class="tx1" id="buyr_name"><%= cash_yn %></p>
            </dd>
          </dl>
<%
                    //현금영수증이 등록된 경우 승인번호 값이 존재
                    if( !"".equals ( cash_authno ) )
                    {
%>          
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">현금영수증 승인번호</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><%= cash_authno %></p>
            </dd>
          </dl>        
          <hr class="odr_line_ty1">
          <dl class="clm_ip2">
            <dt><span class="tit">영수증 확인</span></dt>
            <dd>
              <p class="tx1" id="ordr_idxx"><a href="javascript:receiptView2('<%=site_cd%>','<%= ordr_idxx %>', '<%= cash_yn %>', '<%= cash_authno %>')"><img src="<%= request.getContextPath() %>/img/btn_receipt.png" alt="현금영수증을  확인합니다." /></p>
            </dd>
          </dl>  
<%
                    }
%>

<%
                    }
                }
            }
            /* = -------------------------------------------------------------------------- = */
            /* =   01-1-1. 정상 결제시 결제 결과 출력 END                                   = */
            /* ============================================================================== */
        }
        /* = -------------------------------------------------------------------------- = */
        /* =   01-1. 업체 DB 처리 정상 END                                              = */
        /* ============================================================================== */
    }
    /* = -------------------------------------------------------------------------- = */
    /* =   01. 결제 결과 출력 END                                                   = */
    /* ============================================================================== */
%>
                                                
        </div>
       </div>
      </div>
  </div>
  <!-- //container -->
  </form:form>
  <div id="footer" class="footer">
    <span class="Copyright">Copyright 2015 ⓒ salesb Corp. All rights reserved. v1.0.0</span>
  </div>
</body>
</html>