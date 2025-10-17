<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
  <style>
    :root{
      --primary:#4e86ff; --ok:#1a9b50; --text:#222; --muted:#6b7280;
      --line:#e5e7eb; --radius:16px;
    }
    /* html,body{height:100%}
    body{margin:0; background:#fff; color:var(--text);
         font-family:'Noto Sans KR',system-ui,Segoe UI,Roboto,sans-serif;} */
    .wrap{min-height:100%; display:flex; align-items:center; justify-content:center; padding:40px 16px;}
    .card{
      width: 520px; max-width:100%;
      border:1px solid var(--line); border-radius:var(--radius);
      padding:32px 28px; text-align:center; box-shadow:0 4px 14px rgba(0,0,0,.04);
    }
    .icon{
      width:72px; height:72px; border-radius:50%;
      margin:0 auto 16px; display:grid; place-items:center;
      background:#e9fbf1; color:var(--ok); font-size:40px; font-weight:700;
    }
    h1{margin:0 0 8px; font-size:24px}
    p{margin:0; color:var(--muted); font-size:14px}
    .actions{margin-top:24px}
    .btn{
      display:inline-flex; align-items:center; justify-content:center;
      padding:12px 18px; border:0; border-radius:12px; cursor:pointer;
      background:var(--primary); color:#fff; font-size:14px; text-decoration:none;
    }
  </style>
</head>
<body>
	
	<div id="layout">
		<!-- 헤더 -->
  		<%@include file="/WEB-INF/views/inc/header.jsp"%>

 		 <!-- 사이드바 + 메인 -->
  		<div class="content-wrapper">
    		<%@include file="/WEB-INF/views/inc/sidebar.jsp"%>
    		<!-- 메인 컨텐츠 영역 -->
    		<div class="main-content" id="">
    		<!-- 여기 태그 내부에 본인 담당 페이지 html를 작성 -->
    			 <div class="wrap">
				    <div class="card">
				      <div class="icon">✓</div>
				      <h1>회원가입이 완료되었습니다</h1>
				      <p>올데이런에 오신 것을 환영합니다.</p>
				
				      <div class="actions">
				        <a class="btn" href="${pageContext.request.contextPath}/user/login.do">로그인하기</a>
				      </div>
				    </div>
				  </div>
    			
    		</div>
    		
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script>
		
	</script>
</body>
</html>