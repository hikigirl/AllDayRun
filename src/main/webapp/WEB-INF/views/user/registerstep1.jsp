<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<style>
:root{--text:#222;--muted:#666;--line:#e5e7eb;--primary:#4e86ff;--primary-weak:#edf2ff;--ok:#188038;--danger:#d93025;--radius:14px;--r:12px;}
*{box-sizing:border-box}
.page{max-width:720px;margin:30px auto;padding:0 18px}
h1{font-size:20px;margin:0 0 18px}
.sec-title{font-weight:700;margin-bottom:32px;font-size:16px;color:#222}
.section{background:#fff;border:1px solid #eee;border-radius:var(--radius);box-shadow:0 2px 10px rgba(0,0,0,.04);padding:24px;margin:0 0 10px}
.row{display:flex;gap:12px;align-items:center;margin-bottom:12px;justify-content:flex-start;max-width:460px;margin-left:auto;margin-right:auto}
.row input[type="text"],.row input[type="password"]{flex:1;padding:12px 14px;border:1.5px solid #cfd4d9;border-radius:var(--r);outline:0;background:#fff;font-size:14px}
.row input:focus{border-color:var(--primary);box-shadow:0 0 0 3px rgba(78,134,255,.15)}
.row button{white-space:nowrap;padding:11px 16px;height:44px;background:var(--primary-weak);color:#3563ff;border:none;border-radius:var(--r);cursor:pointer;font-size:14px}
.row button[disabled]{opacity:.6;cursor:not-allowed}
.btn{display:inline-flex;align-items:center;justify-content:center;padding:11px 16px;border:0;border-radius:var(--r);cursor:pointer}
.btn-primary{background:var(--primary);color:#fff;}
.btn-sub{background:var(--primary-weak);color:#3563ff}
.actions{display:flex;justify-content:center;margin-top:24px}
.actions .btn{width:fit-content}
.msg{font-size:13px;margin-top:4px;color:var(--danger);text-align:left;padding-left:2px;max-width:460px;margin-left:auto;margin-right:auto}
.msg.ok{color:var(--ok)}
.hint{font-size:12px;color:#666;margin-top:4px;text-align:left;padding-left:2px;max-width:460px;margin-left:auto;margin-right:auto}
.divider{height:1px;background:#eee;margin:18px auto;max-width:460px}
.badge{font-size:12px;padding:2px 8px;border:1px solid var(--line);border-radius:999px;background:#fff;color:#444}

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
			  <div class="page">
			
			    <div class="header">
			      <h1>계정 정보를 입력해주세요 <span class="badge">STEP 1</span></h1>
			    </div>
			
			    <div class="section">
			      <div class="sec-title">이메일 인증</div>
			
			      <!-- 이메일 입력 + 코드 발송 -->
			      <div class="row">
			        <input type="text" id="email" name="email" placeholder="이메일 주소 입력" value="${emailValue != null ? emailValue : ''}">
			        <button id="btnSend" class="btn btn-sub">인증번호 발송</button>
			      </div>
			      <div id="sendMsg" class="msg"></div>
			      <div class="hint">이메일 형식이 아니면 전송되지 않습니다.</div>
			
			      <div class="divider"></div>
			
			      <!-- 인증코드 입력 -->
			      <form id="frmVerify" method="post" action="<c:out value='${pageContext.request.contextPath}'/>/user/registerstep1.do">
			        <input type="hidden" name="action" value="verifyCode">
			        <input type="hidden" name="email" id="verifyEmail" value="${emailValue != null ? emailValue : ''}">
			        <div class="row">
			          <input type="text" name="code" placeholder="인증번호 5자리" value="${codeValue != null ? codeValue : ''}">
			          <button type="submit" class="btn btn-sub">인증하기</button>
			        </div>
			      </form>
			
			      <c:if test="${not empty emailMsg}">
			        <div class="msg ${emailMsg eq '이메일 인증이 완료되었습니다.' ? 'ok' : 'err'}">
			          <c:out value="${emailMsg}"/>
			        </div>
			      </c:if>
			
			      <div class="divider"></div>
			
			      <!-- 비밀번호 설정 -->
			      <form id="frmSubmit" method="post" action="<c:out value='${pageContext.request.contextPath}'/>/user/registerstep1.do">
			        <div class="row">
			          <input type="text" id="emailConfirm" name="email" placeholder="인증된 이메일" readonly>
			        </div>
			
			        <input type="hidden" name="action" value="submit">
			
			        <div class="row">
			          <input type="password" name="password" placeholder="비밀번호 (8~16자, 영문/숫자/!?#$&)">
			        </div>
			        <div class="row">
			          <input type="password" name="confirm" placeholder="비밀번호 확인">
			        </div>
			
			        <c:if test="${not empty pwMsg}">
			          <div class="msg err"><c:out value="${pwMsg}"/></div>
			        </c:if>
			        <c:if test="${not empty confirmMsg}">
			          <div class="msg err"><c:out value="${confirmMsg}"/></div>
			        </c:if>
			
			        <div class="actions">
			          <button class="btn btn-primary" type="submit">다음 단계로</button>
			        </div>
			      </form>
			    </div>
			
			  </div>
			</div>

    			
    		</div>
    		
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script>
		const ctx = '<c:out value="${pageContext.request.contextPath}"/>';
	
	    // 이메일 정규식
	    function isEmail(v){
	      return /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(v);
	    }
	
	    // 인증번호 발송(Ajax -> /user/sendmail.do)
	    $('#btnSend').on('click', function(){
	      const email = $('#email').val().trim();
	      const $btn = $(this);
	      const $msg = $('#sendMsg');
	
	      if(!isEmail(email)){
	        $msg.removeClass('ok').addClass('err').text('이메일 형식이 올바르지 않습니다.');
	        return;
	      }
	
	      $btn.prop('disabled', true);
	      $msg.removeClass('ok err').text('전송 중...');
	
	      $.ajax({
	        type: 'POST',
	        url: ctx + '/user/sendmail.do',
	        dataType: 'json',
	        data: { email },
	        success: function(res){
	          if(res && res.result == 1){
	            $msg.removeClass('err').addClass('ok').text('인증번호를 발송했습니다. 메일함을 확인하세요.');
	          }else{
	            $msg.removeClass('ok').addClass('err').text('메일 전송 실패');
	          }
	        },
	        error: function(){
	          $msg.removeClass('ok').addClass('err').text('통신 오류');
	        },
	        complete: function(){
	          $btn.prop('disabled', false);
	        }
	      });
	    });
	    
	    $(function(){
	        // 이메일 입력 → 인증폼에도 반영
	        $('#email').on('input', function() {
	          $('#verifyEmail').val($(this).val());
	        });
	
	        // 인증 완료 메시지가 뜬 경우 → emailValue가 설정되어 있음
	        const emailVal = '${emailValue}';
	        const msg = '${emailMsg}';
	        if (emailVal && msg === '이메일 인증이 완료되었습니다.') {
	          $('#emailConfirm').val(emailVal);
	        }
	      });
	</script>
</body>
</html>