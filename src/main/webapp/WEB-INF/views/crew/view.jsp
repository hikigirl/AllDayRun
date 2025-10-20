<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/views/inc/asset.jsp"%>
<style>
/* General container for the view */
.crew-view-container {
	width: 100%;
	max-width: 1200px; /* Adjust as needed for wide banner */
	margin: 20px auto 0 auto; /* Added margin-top */
	padding: 0;
	background-color: #fff;
	min-height: 70vh; /* Added responsive min-height */
}
/* Top banner image */
.crew-banner-image {
	width: 100%;
	height: 300px;
	overflow: hidden;
	margin-bottom: 20px;
	border-radius: 8px; /* Added curvature */
}


.crew-banner-image img {
	width: 100%;
	height: 100%;
	object-fit: cover; /* Cover the area without distortion */
	display: block;
}

/* Content below the banner */
.crew-content-area {
	padding: 0 20px 20px 20px; /* Add padding to content below banner */
}

.crew-name {
	font-size: 3em;
	color: #333;
	text-align: left;
	margin: 0 0 0 5px;
	/* Reset all margins, apply specific bottom and left */
	padding-left: 0; /* Reset default browser padding */
}

.crew-meta-info {
	font-size: 1.0em;
	color: #888;
	text-align: left;
	margin-bottom: 7px;
	margin-left: 0; /* Reset default browser margin */
	padding-left: 0; /* Reset default browser padding */
}

.crew-meta-info span {
	margin: 0 10px;
}

.crew-description {
	font-size: 1.1em;
	line-height: 1.6;
	color: #333;
	margin: 0 auto 30px 0;
	max-width: 800px;
	white-space: pre-wrap;
	text-align: left;
}

.crew-description p {
	margin-top: -35px;
	margin-left: 10px;
	padding-top: 0;
}

.crew-header-actions {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-left : -17px;
	margin-bottom: 10px; /* Space below this section */
	padding: 0 20px; /* Match parent padding */
}

.btn-join {
	padding: 20px 30px;
	background-color: #337ab7;
	color: white;
	border: none;
	border-radius: 20px;
	cursor: pointer;
	text-decoration: none;
	font-size: 20px;
	margin-top: 40px;
}

/* Styles for activity photos section (will be uncommented/modified later) */
.crew-section-title {
	font-size: 1.8em;
	color: #333;
	margin-top: 40px;
	margin-bottom: 20px;
	padding-bottom: 10px;
	border-bottom: 2px solid #337ab7;
	text-align: center;
}

.photo-gallery {
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
	gap: 15px;
	padding: 0 20px;
}

.photo-item {
	border: 1px solid #eee;
	border-radius: 5px;
	overflow: hidden;
	box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.photo-item img {
	width: 100%;
	height: 150px; /* Fixed height for consistency */
	object-fit: cover;
	display: block;
}

.photo-item p {
	padding: 10px;
	font-size: 0.85em;
	color: #666;
	text-align: center;
	margin: 0;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.no-photos {
	text-align: center;
	color: #999;
	padding: 30px;
	border: 1px dashed #eee;
	border-radius: 5px;
	margin: 0 20px;
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
				<div class="crew-view-container">
					<div class="crew-banner-image">
						<img src="/alldayrun/crewmainFile/${dto.crewAttach}"
							alt="${dto.crewName} 대표 이미지">
					</div>
					<div class="crew-content-area">
						<div class="crew-header-actions">
							<h2 class="crew-name">${dto.crewName}</h2>
							<div class="crew-actions">
								<a href="/alldayrun/crewjoin.do?crewSeq=${dto.crewSeq}"
									class="btn-join">가입신청</a>
							</div>
						</div>
						<div class="crew-meta-info">
							<span>${dto.regionCity} ${dto.regionCounty}
								${dto.regionDistrict} | ${dto.memberCount}명</span> 
							<div>
							<span>크루장:${dto.nickname}</span>
							</div>
						</div>
						<div class="crew-description">
							<p>${dto.description}</p>
						</div>

						<!-- Activity Photos Section (will be uncommented/modified later) -->
						<%--
				        <h3 class="crew-section-title">크루 활동 사진</h3>
				        <div class="photo-gallery">
				            <c:choose>
				                <c:when test="${not empty photoList}">
				                    <c:forEach items="${photoList}" var="photo">
				                        <div class="photo-item">
				                            <img src="/alldayrun/crewboardFile/${photo.attach}" alt="${photo.title}">
				                            <p>${photo.title}</p>
				                        </div>
				                    </c:forEach>
				                </c:when>
				                <c:otherwise>
				                    <div class="no-photos">
				                        <p>등록된 활동 사진이 없습니다.</p>
				                    </div>
				                </c:otherwise>
				            </c:choose>
				        </div>
				        --%>
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