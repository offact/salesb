<%@ include file="/WEB-INF/views/salesb/base.jsp" %>
<SCRIPT>
    // 페이지 이동
    function goPageGoodsPageList(page) {
        document.goodsManageConForm.curPage.value = page;
        var dataParam = $("#goodsManageConForm").serialize();
        //commonDim(true);
        $.ajax({
            type: "POST",
               url:  "<%= request.getContextPath() %>/business/goodspagelist",
               	data:dataParam,
               success: function(result) {
                   //commonDim(false);
                   $("#goodsPageList").html(result);
               },
               error:function() {
                  // commonDim(false);
               }
        });

    }
    
    // 페이지 이동
    function fcGoods_View(idx) {

    	var curPage='${productConVO.curPage}';
    	var url="<%= request.getContextPath() %>/business/productdetail?idx="+idx+"&curPage="+curPage;

    	fcMenu(url);
    }

</SCRIPT>
    <p><span style="color:#FF9900"> <span class="glyphicon glyphicon-asterisk"></span> 전체 : <f:formatNumber type="currency" currencySymbol="" pattern="#,##0" value="${totalCount}" />개 검색 </span></p>  
	<table  class="table table-bordered table-hover table-striped">
	  <thead>
	    <tr>
	      <th>상품명</th>
	         <th>상품코드</th>
	         <th>수정확인</th>
	    </tr>
	  </thead>
	  <tbody>
	  	<c:if test="${!empty productList}">
	          <c:forEach items="${productList}" var="ProductVO" varStatus="status">
	          <tr id="select_tr_${ProductVO.idx}">
	              <td><c:out value="${ProductVO.productName}"></c:out></td>
	              <td><c:out value="${ProductVO.productCode}"></c:out></td>
	              <td><button type="button" id="receivebtn" class="btn btn-xs btn-success" onClick="fcGoods_View('${ProductVO.idx}');">수정</button></td>
	           </tr>
	          </c:forEach>
	         </c:if>
	        <c:if test="${empty productList}">
	        <tr>
	            <td colspan='3' class='text-center'>조회된 데이터가 없습니다.</td>
	        </tr>
	       </c:if>
	  </tbody>
	</table>
	<!-- 페이징 -->
	<taglib:paging cbFnc="goPageGoodsPageList" totalCount="${totalCount}" curPage="${productConVO.curPage}" rowCount="${productConVO.rowCount}" />
	<!-- //페이징 -->  
	 

