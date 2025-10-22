<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<%@include file="/WEB-INF/views/inc/asset.jsp"%>
    <style>
/* main-content 전용 스타일 */
.main-content .view-container {
    width: 100%;
    max-width: 1200px;
    margin: 0 auto;
    padding: 30px;
    border-radius: 12px;
    background-color: #f7f9ff;
    box-shadow: 0 10px 25px rgba(0,0,0,0.08);
    animation: fadeIn 0.5s ease-in-out;
}

.main-content .view-header {
    border-bottom: 2px solid #e0e4f7;
    padding-bottom: 15px;
    margin-bottom: 25px;
}

.main-content .view-header h2 {
    margin: 0;
    font-size: 28px;
    color: #1f2a56;
    font-weight: 700;
}

.main-content .view-meta {
    font-size: 14px;
    color: #666;
    margin-top: 10px;
}

.main-content .view-meta span {
    margin-right: 20px;
}

.main-content .view-content {
    padding: 20px 0;
    min-height: 200px;
    line-height: 1.6;
    font-size: 15px;
    color: #333;
}

.main-content .view-content p {
    white-space: pre-wrap; 
}

.main-content .view-attachment img {
    max-width: 100%;
    height: auto;
    margin-top: 15px;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.05);
}

.main-content .view-buttons {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 25px;
}

.main-content .view-buttons .like-btn {
    background-color: #ff5f5f;
    color: white;
    border: none;
    border-radius: 8px;
    padding: 10px 20px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.25s ease;
}

.main-content .view-buttons .like-btn:hover {
    background-color: #e04848;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(255, 95, 95, 0.25);
}

.main-content .view-buttons button {
    padding: 8px 18px;
    border-radius: 8px;
    border: none;
    cursor: pointer;
    transition: all 0.25s ease;
}

.main-content .view-buttons button:hover {
    transform: translateY(-1px);
    box-shadow: 0 3px 10px rgba(0,0,0,0.1);
}

.main-content .comment-section {
    margin-top: 40px;
    border-top: 2px solid #e0e4f7;
    padding-top: 25px;
}

.main-content .comment-section h3 {
    margin-bottom: 20px;
    font-size: 20px;
    color: #1f2a56;
    font-weight: 600;
}

.main-content .comment-form {
    display: flex;
    gap: 10px;
    margin-bottom: 25px;
}

.main-content .comment-form textarea {
    flex-grow: 1;
    padding: 12px;
    border-radius: 8px;
    border: 1px solid #d0d7e5;
    font-size: 14px;
    resize: vertical;
    min-height: 60px;
    transition: all 0.2s ease;
}

.main-content .comment-form textarea:focus {
    outline: none;
    border-color: #4f6ef7;
    box-shadow: 0 0 6px rgba(79, 110, 247, 0.25);
}

.main-content .comment-form button {
    background-color: #4f6ef7;
    color: white;
    border: none;
    border-radius: 8px;
    padding: 0 20px;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.25s ease;
}

.main-content .comment-form button:hover {
    background-color: #3554db;
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(79, 110, 247, 0.25);
}

.main-content .comment-list .comment {
    padding: 15px 0;
    border-bottom: 1px solid #e5e8f0;
}

.main-content .comment-list .comment:last-child {
    border-bottom: none;
}

.main-content .comment-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 13px;
    color: #888;
    margin-bottom: 8px;
}

.main-content .comment-meta .author {
    font-weight: 600;
    color: #4f6ef7;
}

.main-content .comment-actions button {
    background: none;
    border: none;
    color: #a0a0a0;
    cursor: pointer;
    font-size: 12px;
    transition: color 0.2s ease;
}

.main-content .comment-actions button:hover {
    color: #ff5f5f;
}

.main-content .comment-content {
    line-height: 1.6;
    color: #333;
}

/* 애니메이션 */
@keyframes fadeIn {
    from { opacity: 0; transform: translateY(20px); }
    to { opacity: 1; transform: translateY(0); }
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
    			<div class="view-container">
				<div class="view-header">
					<h2>${dto.title}</h2>
					<div class="view-meta">
						<span><strong>작성자:</strong> ${dto.nickname}</span>
						<span><strong>작성일:</strong> ${dto.regdate}</span>
						<span><strong>조회수:</strong> ${dto.readCount}</span>
					</div>
				</div>
				<div class="view-content">
					<c:if test="${not empty dto.attach}">
						<div class="view-attachment">
							<img src="/alldayrun/crewboardFile/${dto.attach}" alt="첨부파일">
						</div>
					</c:if>
					<p>${dto.content}</p>
				</div>
				<div class="view-buttons">
					<button name="llike-btn" class="like-btn" onclick="location.href='/alldayrun/crewboardlike.do?boardContentSeq=${dto.boardContentSeq}'">❤️ 좋아요 ${dto.favoriteCount}</button>
    				<div>
						<button onclick="location.href='/alldayrun/crewboardedit.do?boardContentSeq=${dto.boardContentSeq}'">수정</button>
						<button id="deleteButton" data-seq="${dto.boardContentSeq}">삭제</button>
						<button onclick="location.href='/alldayrun/crewboardlist.do'">목록</button>
					</div>
				</div>
				
				<!-- ==================== 댓글 섹션 시작 ==================== -->
				<div class="comment-section">
					<h3>댓글</h3>
					
					<!-- 댓글 입력 폼 -->
					<form class="comment-form" method="POST" action="/alldayrun/crewcommentaddok.do">
						<textarea name="content" placeholder="댓글을 입력하세요." required></textarea>
						<button type="submit">등록</button>
						<input type="hidden" name="boardContentSeq" value="${dto.boardContentSeq}">
					</form>
					
					<!-- 댓글 목록 -->
					<div class="comment-list">
						<c:if test="${empty commentList}">
							<div class="comment">
								<p>등록된 댓글이 없습니다.</p>
							</div>
						</c:if>
						
						<c:forEach items="${commentList}" var="comment">
							<div class="comment">
								<div class="comment-meta">
									<div>
										<span class="author">${comment.nickname}</span>
										<span class="date">${comment.regdate}</span>
									</div>
									<div class="comment-actions">
										<%-- 세션에 저장된 사용자 ID(auth.accountId)와 댓글 작성자가 같을 경우 삭제 버튼 표시 --%>
										<c:if test="${auth.accountId == comment.accountId}">
											<button onclick="location.href='/alldayrun/commentdelete.do?commentSeq=${comment.commentSeq}&boardContentSeq=${dto.boardContentSeq}'">삭제</button>
										</c:if>
									</div>
								</div>
								<div class="comment-content">
									<p>${comment.content}</p>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
				<!-- ==================== 댓글 섹션 끝 ==================== -->
				
			</div>
    		</div>
    		
  		</div>
	</div>
	
	
	<%@include file="/WEB-INF/views/inc/javascriptasset.jsp"%>
	
	<script>
        $(document).ready(function() {
            $('#deleteButton').on('click', function() {
                const boardContentSeq = $(this).data('seq');
                if (confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
                    location.href = '/alldayrun/crewboarddelete.do?boardContentSeq=' + boardContentSeq;
                }
            });
        });
	</script>
</body>
</html>