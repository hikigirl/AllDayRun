<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<style>
	:root{--text:#222;--muted:#666;--line:#e5e7eb;--primary:#4e86ff;--primary-weak:#edf2ff;--ok:#188038;--danger:#d93025;--radius:14px;--r:12px;}
	*{box-sizing:border-box}
	.page{max-width:720px;margin:30px auto;padding:0 18px}
	h1 { font-size: 20px; margin: 0 0 18px; }
	.section{ background:#fff; border:1px solid #eee; border-radius: var(--radius); box-shadow: 0 2px 10px rgba(0,0,0,.04); padding: 24px; margin: 0 0 10px; }
	.sec-title{font-weight:700;margin-bottom:12px}
	label{display:block;margin:8px 0 6px;color:#444;font-size:13px}
	.input,select{width:100%;padding:12px 14px;border:1.5px solid #cfd4d9;border-radius:var(--r);outline:0;background:#fff}
	.input:focus,select:focus{border-color:var(--primary);box-shadow:0 0 0 3px rgba(78,134,255,.15)}
	.row{display:flex;gap:12px}
	.row .col{flex:1}
	.msg{font-size:12px;color:var(--danger);margin-top:6px}
	.hint{font-size:12px;color:#888;margin-top:4px}
	.btn{display:inline-flex;align-items:center;justify-content:center;padding:11px 16px;border:0;border-radius:var(--r);cursor:pointer}
	.btn-primary{background:var(--primary);color:#fff}
	.btn-sub{background:var(--primary-weak);color:#3563ff}
	.actions{display:flex;justify-content:center;margin-top:16px}
	
	/* 프로필 업로드 박스 */
	.uploader{
	  width:100%;min-height:210px;border:2px dashed #cfd4d9;border-radius:22px;
	  display:flex;align-items:center;justify-content:center;position:relative;background:#fafafa
	}
	.uploader img{max-width:180px;max-height:180px;border-radius:18px}
	.uploader .hint{position:absolute;bottom:12px}
	.uploader input[type=file]{display:none}
	.uploader .overlay{position:absolute;inset:0;border-radius:22px}
	.badge{font-size:12px;padding:2px 8px;border:1px solid var(--line);border-radius:999px;background:#fff;color:#444}
	
	/* 전화번호 */
	.row.phone{display:flex;gap:12px;align-items:flex-start;} /* flex-end → flex-start */
	.row.phone .field{display:flex;flex-direction:column;}
	.row.phone .field label{height:20px;line-height:20px;margin:0 0 6px;} /* 라벨 높이 고정 */
	.row.phone .field .input{height:44px;}
	
	/* 체크류 */
	.choice-row{display:flex;flex-wrap:wrap;gap:10px}
	.choice-row .chip{display:inline-flex;align-items:center;gap:8px;padding:10px 12px;border:1.5px solid #cfd4d9;border-radius:12px}
	.choice-row input{margin:0}

    /* 지역 */
	.region-row{display:flex;gap:12px;flex-wrap:nowrap;align-items:flex-end;} /* wrap → nowrap */
	.region-field{flex:0 0 calc((100% - 24px)/3);} /* 3열 고정 배치 */
	.region-field label{display:block;margin:8px 0 6px}
	.region-field select,.region-field input{
	    width:100%;padding:10px;border:1px solid #ddd;border-radius:10px
	    
	/* 운동 목적 칩 정렬 */
	.choice-row{display:flex;flex-wrap:wrap;gap:10px 12px;align-items:center;}
	.chip{display:inline-flex;align-items:center;gap:8px;line-height:1;}
	
	/* 체크박스 수직보정(브라우저 기본 라인박스 오프셋 제거) */
	.choice-row{display:flex;flex-wrap:wrap;gap:10px 12px;}
	.chip{display:inline-flex;align-items:center;gap:8px;line-height:1;}
	.chip input[type="checkbox"]{margin:0;width:18px;height:18px;}


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
				    <h1>이제 회원 정보만 입력하면 돼요 <span class="badge">STEP 2</span></h1>
				  </div>
				
				  <form method="post" action="${pageContext.request.contextPath}/user/registerstep2.do" enctype="multipart/form-data">
				
				    <!-- 프로필 사진 -->
				    <!-- 프로필 사진 -->
					<div class="section">
					  <div class="sec-title">프로필 사진</div>
					  <label class="hint">png, jpg, jpeg만 업로드. 미업로드 시 기본이미지 사용</label>
					  <div class="uploader" id="uploader">
					    
					    <c:choose>
					      <c:when test="${not empty photoName && (fn:startsWith(photoName, 'http'))}">
					        <img id="preview" src="${photoName}" alt="preview">
					      </c:when>
					      <c:otherwise>
					        <img id="preview" src="${pageContext.request.contextPath}/asset/pic/${empty photoName ? 'pic.png' : photoName}" alt="preview">
					      </c:otherwise>
					    </c:choose>
					
					    <input id="photo" name="photo" type="file" accept="image/png,image/jpeg">
					    <label class="overlay" for="photo" title="이미지 선택"></label>
					    <div class="hint">클릭하여 사진 파일을 첨부하거나 아래에서 초기화하세요.</div>
					  </div>
					
					  <!-- 사진 초기화 버튼 -->
					  <div style="margin-top:8px; text-align:right;">
					    <button type="button" class="btn btn-sub" onclick="resetPhoto()">사진 초기화</button>
					  </div>
					
					  <!-- 현재 사진 상태 전달용 히든 필드 (삭제 후에도 'pic.png' 전달 보장) -->
					  <input type="hidden" id="photoName" name="photoName" value="${empty photoName ? 'pic.png' : photoName}">
					
					  <p class="msg"><c:out value="${photoMsg}"/></p>
					</div>
				
				    
				    <!-- 이름 (성 / 이름) -->
					<div class="section">
					  <div class="sec-title">이름</div>
					  <div class="row" style="display:flex;gap:12px;align-items:flex-end;flex-wrap:nowrap;">
					    <div class="field" style="min-width:180px;flex:1;">
					      <label for="lastName">성</label>
					      <input class="input" id="lastName" name="lastName" maxlength="8" type="text" placeholder="예: 김 / Lee" value="${lastName}">
					    </div>
					    <div class="field" style="min-width:180px;flex:1;">
					      <label for="firstName">이름</label>
					      <input class="input" id="firstName" name="firstName" maxlength="8" type="text" placeholder="예: 철수 / Minho" value="${firstName}">
					    </div>
					  </div>
					  <c:if test="${not empty nameMsg}"><div class="msg">${nameMsg}</div></c:if>
					</div>
				
				    <!-- 닉네임 -->
				    <div class="section">
				      <div class="sec-title">닉네임</div>
				      <div class="row">
				        <div class="col">
				          <label>닉네임 (한글/영문/숫자 1~12자)</label>
				          <input class="input" type="text" id="nickname" name="nickname" maxlength="12" required value="${nickname}">
				          <p id="nickMsg" class="msg"><c:out value="${nickMsg}"/></p>
				        </div>
				        <div>
				          <label>&nbsp;</label>
				          <button type="button" id="btnNick" class="btn btn-sub">중복 검사</button>
				        </div>
				      </div>
				     </div>
				
					<div class="section">
				    <!-- 전화번호 -->
					  <div class="sec-title">전화번호</div>
					
					  <div class="row phone">
					    <div class="field" style="width:120px">
					      <label>앞자리</label>
					      <input class="input" type="text" value="010" readonly>
					    </div>
					
					    <div class="field" style="flex:1">
					      <label>나머지 8자리(숫자만)</label>
					      <input class="input" type="text" id="phoneTail" name="phoneTail"
					             maxlength="8" placeholder="예) 12345678" required value="${phoneTail}">
					      <p class="msg"><c:out value="${phoneMsg}"/></p>
					    </div>
					  </div>
				    <br>
				
				    <!-- 성별 & 생년월일 -->
				      <div class="sec-title">성별</div>
				      <div class="choice-row">
				        <label class="chip"><input type="radio" name="gender" value="남자" <c:if test="${gender == '남자'}">checked</c:if>> 남자</label>
						<label class="chip"><input type="radio" name="gender" value="여자" <c:if test="${gender == '여자'}">checked</c:if>> 여자</label>
				      </div>
				      <p class="msg"><c:out value="${genderMsg}"/></p>
				      <br>
				
				      <div class="sec-title">생년월일</div>
				      <div class="row" style="margin-top:12px">
				        <div class="col">
				          <label>연도</label>
				          <select class="input" id="yyyy" name="yyyy"></select>
				        </div>
				        <div class="col">
				          <label>월</label>
				          <select class="input" id="mm" name="mm"></select>
				        </div>
				        <div class="col">
				          <label>일</label>
				          <select class="input" id="dd" name="dd"></select>
				        </div>
				      </div>
				      <p class="msg"><c:out value="${birthMsg}"/></p>
				    <br>
				
				    <!-- 지역 -->
					  <div class="sec-title">지역 정보</div>
						  <div class="region-row">
						    <div class="region-field">
						      <label for="sido">시/도</label>
						      <select id="sido" name="regionCity">
								<option value="" <c:if test="${regionCity == ''}">selected</c:if>>선택</option>
								<option value="서울특별시" <c:if test="${regionCity == '서울특별시'}">selected</c:if>>서울특별시</option>
								<option value="부산광역시" <c:if test="${regionCity == '부산광역시'}">selected</c:if>>부산광역시</option>
								<option value="대구광역시" <c:if test="${regionCity == '대구광역시'}">selected</c:if>>대구광역시</option>
						        <option value="인천광역시" <c:if test="${regionCity == '인천광역시'}">selected</c:if>>인천광역시</option>
						        <option value="광주광역시" <c:if test="${regionCity == '광주광역시'}">selected</c:if>>광주광역시</option>
						        <option value="대전광역시" <c:if test="${regionCity == '대전광역시'}">selected</c:if>>대전광역시</option>
						        <option value="울산광역시" <c:if test="${regionCity == '울산광역시'}">selected</c:if>>울산광역시</option>
						        <option value="세종특별자치시" <c:if test="${regionCity == '세종특별자치시'}">selected</c:if>>세종특별자치시</option>
						        <option value="경기도" <c:if test="${regionCity == '경기도'}">selected</c:if>>경기도</option>
						        <option value="강원특별자치도" <c:if test="${regionCity == '강원특별자치도'}">selected</c:if>>강원특별자치도</option>
						        <option value="충청북도" <c:if test="${regionCity == '충청북도'}">selected</c:if>>충청북도</option>
						        <option value="충청남도" <c:if test="${regionCity == '충청남도'}">selected</c:if>>충청남도</option>
						        <option value="전북특별자치도" <c:if test="${regionCity == '전북특별자치도'}">selected</c:if>>전북특별자치도</option>
						        <option value="전라남도" <c:if test="${regionCity == '전라남도'}">selected</c:if>>전라남도</option>
						        <option value="경상북도" <c:if test="${regionCity == '경상북도'}">selected</c:if>>경상북도</option>
						        <option value="경상남도" <c:if test="${regionCity == '경상남도'}">selected</c:if>>경상남도</option>
						        <option value="제주특별자치도" <c:if test="${regionCity == '제주특별자치도'}">selected</c:if>>제주특별자치도</option>
						      </select>
						      <c:if test="${not empty regionCityMsg}">
						        <div class="msg err">${regionCityMsg}</div>
						      </c:if>
						    </div>
						
						    <div class="region-field">
						      <label for="sigungu">시군/구</label>
						      <input type="text" id="sigungu" name="regionCounty" placeholder="예: 강남구, 수원시 영통구" value="${regionCounty}">
						      <c:if test="${not empty regionCountyMsg}">
						        <div class="msg err">${regionCountyMsg}</div>
						      </c:if>
						    </div>
						
						    <div class="region-field">
						      <label for="dong">동/읍</label>
						      <input type="text" id="dong" name="regionDistrict" placeholder="예: 서초동, ○○읍" value="${regionDistrict}">
						      <c:if test="${not empty regionDistrictMsg}">
						        <div class="msg err">${regionDistrictMsg}</div>
						      </c:if>
						    </div>
						  </div>
					</div>
				
					<div class="section">
				    <!-- 운동 빈도 -->
				      <div class="sec-title">운동 빈도</div>
				      <div class="choice-row">
				        <label class="chip"><input type="radio" name="freq" value="0" <c:if test="${freq == '0'}">checked</c:if>> 주 1회 미만</label>
				        <label class="chip"><input type="radio" name="freq" value="1-3" <c:if test="${freq == '1-3'}">checked</c:if>> 주 1~3회</label>
				        <label class="chip"><input type="radio" name="freq" value="4-5" <c:if test="${freq == '4-5'}">checked</c:if>> 주 4~5회</label>
				        <label class="chip"><input type="radio" name="freq" value="6+" <c:if test="${freq == '6+'}">checked</c:if>> 주 6회 이상</label>
				      </div>
				      <p class="msg"><c:out value="${freqMsg}"/></p>
				    <br>
				
				    <!-- 운동 목적 -->
				      <div class="sec-title">운동 목적(복수 선택)</div>
				      <div class="choice-row">
				        <label class="chip"><input type="checkbox" name="goals" value="체중감량" <c:if test="${fn:contains(goals, '체중감량')}">checked</c:if>> 체중감량</label>
				        <label class="chip"><input type="checkbox" name="goals" value="기록갱신" <c:if test="${fn:contains(goals, '기록갱신')}">checked</c:if>> 기록갱신</label>
				        <label class="chip"><input type="checkbox" name="goals" value="건강유지" <c:if test="${fn:contains(goals, '건강유지')}">checked</c:if>> 건강유지</label>
				        <label class="chip"><input type="checkbox" name="goals" value="스트레스해소" <c:if test="${fn:contains(goals, '스트레스해소')}">checked</c:if>> 스트레스해소</label>
				        <label class="chip"><input type="checkbox" name="goals" value="사람들과의교류" <c:if test="${fn:contains(goals, '사람들과의교류')}">checked</c:if>> 사람들과의교류</label>
				        <label class="chip"><input type="checkbox" name="goals" value="대회참가준비" <c:if test="${fn:contains(goals, '대회참가준비')}">checked</c:if>> 대회참가준비</label>
				      </div>
				      <p class="msg"><c:out value="${goalsMsg}"/></p>
				    </div>
				
				    <div class="actions">
				      <button type="submit" name="action" value="submit" class="btn btn-primary">입력 완료</button>
				    </div>
				  </form>
				</div>
    			
    		</div>
    		
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script>
	// 이미지 미리보기 + 확장자 검증
	const photo = document.getElementById('photo');
	const preview = document.getElementById('preview');
	photo.addEventListener('change', e=>{
	  const f = e.target.files[0];
	  if(!f) return;
	  const ok = /(png|jpg|jpeg)$/i.test(f.name);
	  if(!ok){ alert('png, jpg, jpeg만 업로드 가능합니다.'); photo.value=''; return; }
	  const url = URL.createObjectURL(f);
	  preview.src = url;
	});

	//이름 유효성(1~8자, 한글/영문만)
	(function(){
	  var ko = /^[가-힣]{1,8}$/;
	  var en = /^[A-Za-z]{1,8}$/;
	  var form = document.getElementById('frmStep2');
	  if (!form) return;
	  form.addEventListener('submit', function(e){
	    var last = document.getElementById('lastName').value.trim();
	    var first = document.getElementById('firstName').value.trim();
	    var ok = (ko.test(last) && ko.test(first)) || (en.test(last) && en.test(first));
	    if (!ok) {
	      e.preventDefault();
	      var msg = document.querySelector('.name-msg');
	      if (!msg) {
	        var div = document.createElement('div');
	        div.className = 'msg name-msg';
	        div.textContent = '사용할 수 없는 이름입니다.';
	        form.querySelector('.section .sec-title + .row').parentNode.appendChild(div);
	      } else {
	        msg.textContent = '사용할 수 없는 이름입니다.';
	      }
	    }
	  });
	})();

	// 닉네임 중복 검사
	(function(){
	  // JSP 파일 안이라면 OK. 외부 .js라면 서버에서 주입된 전역 ctx를 쓰거나 data-attr로 가져와라.
	  const ctx = '${pageContext.request.contextPath}';

	  $('#btnNick').on('click', function(){
	    const nick = $('#nickname').val().trim();
	    const $msg = $('#nickMsg');
	    const rule = /^(?=.*[가-힣A-Za-z0-9])[가-힣A-Za-z0-9]{1,12}$/;

	    if (!rule.test(nick)) {
	      $msg.text('닉네임 형식을 확인해주세요.').css('color','#b00020');
	      return;
	    }

	    $msg.text('검사 중...').css('color','#666');

	    $.ajax({
	      type: 'POST',
	      url: ctx + '/user/registerstep2.do',
	      data: { action: 'checkNick', nickname: nick },
	      dataType: 'json',              // 반드시 JSON으로
	      timeout: 8000
	    })
	    .done(function(r){
	      if (r && r.duplicate === true) {
	        $msg.text('이미 존재하는 닉네임입니다.').css('color','#b00020');
	      } else if (r && r.duplicate === false) {
	        $msg.text('사용 가능한 닉네임입니다.').css('color','#188038');
	      } else {
	        $msg.text('응답 형식이 올바르지 않습니다.').css('color','#b00020');
	      }
	    })
	    .fail(function(xhr){
	      $msg.text('중복 검사 실패 (' + xhr.status + ')').css('color','#b00020');
	    });
	  });
	})();


	// 전화번호 숫자만(8자리)
	$('#phoneTail').on('input', function(){
	  this.value = this.value.replace(/[^0-9]/g,'').slice(0,8);
	});

	// 지역 미리보기
	$('#regionDetail').on('input', function(){
	  const v = this.value.trim();
	  $('#regionPreview').text(v ? `활동 지역: ${v}` : '');
	});

	// 생년월일 셀렉트 채우기
	(function(){
	  const yyyy = document.getElementById('yyyy'), mm=document.getElementById('mm'), dd=document.getElementById('dd');
	  const yNow = new Date().getFullYear();
	  for(let y=yNow; y>=1900; y--){ yyyy.add(new Option(y, y)); }
	  for(let m=1;m<=12;m++){ mm.add(new Option(m, m)); }
	  function fillDays(){
	    dd.length = 0;
	    const y = parseInt(yyyy.value), m = parseInt(mm.value);
	    if(!y||!m) return;
	    const last = new Date(y, m, 0).getDate();
	    for(let d=1; d<=last; d++){ dd.add(new Option(d, d)); }
	  }
	  yyyy.addEventListener('change', fillDays);
	  mm.addEventListener('change', fillDays);
	  // 초기값
	  yyyy.value = yNow; mm.value = 1; fillDays(); dd.value = 1;
	})();

	//사진 초기화 버튼 → 기본 이미지로 복원
	function resetPhoto() {
	  const preview = document.getElementById('preview');
	  const input = document.getElementById('photo');
	  const hidden = document.getElementById('photoName');

	  // 기본 이미지로 변경
	  preview.src = '${pageContext.request.contextPath}/asset/pic/pic.png';
	  input.value = '';
	  hidden.value = 'pic.png';
	}
	</script>
</body>
</html>