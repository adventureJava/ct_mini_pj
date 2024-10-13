package com.psjoon.codingtest.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PagingUtil {
	private int maxNum; // 전체 글 개수
	private int pageNum; // 현재 페이지 번호
	private int listCnt; // 한 페이지 당 보일 게시글 수
	private int pageCnt; // 보여질 페이지 번호 개수

	// 페이징용 HTML 코드를 만드는 메서드 (urlPrefix 추가)
	public String makePaging(String urlPrefix) {
		String pageStr = null;
		StringBuffer sb = new StringBuffer();

		// 1. 전체 페이지 수 계산
		int totalPage = (maxNum % listCnt) > 0 ? maxNum / listCnt + 1 : maxNum / listCnt;

		// 2. 현재 페이지가 속한 그룹 번호 계산
		int curGroup = (pageNum % pageCnt) > 0 ? pageNum / pageCnt + 1 : pageNum / pageCnt;

		// 3. 현재 보이는 페이지 그룹의 시작 번호 계산
		int start = (curGroup * pageCnt) - (pageCnt - 1);

		// 4. 현재 보이는 페이지 그룹의 마지막 번호 계산
		int end = (curGroup * pageCnt) >= totalPage ? totalPage : curGroup * pageCnt;

		// 이전 버튼 처리
		if (start != 1) {
			sb.append("<a class='pno' href='" + urlPrefix + "?pageNum=" + (start - 1) + "'>");
			sb.append("◀</a>");
		}

		// 중간 페이지 번호 처리
		for (int i = start; i <= end; i++) {
			if (pageNum != i) {
				sb.append("<a class='pno' href='" + urlPrefix + "?pageNum=" + i + "'>");
				sb.append(i + "</a>");
			} else {
				sb.append("<font class='pno'>");
				sb.append(i + "</font>");
			}
		}

		// 다음 버튼 처리
		if (end != totalPage) {
			sb.append("<a class='pno' href='" + urlPrefix + "?pageNum=" + (end + 1) + "'>");
			sb.append("▶</a>");
		}

		// StringBuffer 내용을 문자열로 변환
		pageStr = sb.toString();

		return pageStr;
	}
}




