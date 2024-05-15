<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="constants.ForwardConst"%>
<%@ page import="constants.AttributeConst"%>

<c:set var="actRep" value="${ForwardConst.ACT_REP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commUpd" value="${ForwardConst.CMD_UPDATE.getValue()}" />
<c:set var="commDel" value="${ForwardConst.CMD_DESTROY.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <h2>日報 編集ページ</h2>
        <form method="POST" action="<c:url value='?action=${actRep}&command=${commUpd}' />">
            <c:import url="_form.jsp" />
            <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${token}" />
        </form>

        <!-- 管理者のみ削除可能 -->
        <c:if test="${sessionScope.login_employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}">
            <!-- 削除フォーム -->
            <form method="POST" action="<c:url value='?action=${actRep}&command=${commDel}' />">
                <input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${token}" />
                <input type="hidden" name="${AttributeConst.REP_ID.getValue()}" value="${report.id}" />
                <button type="submit" onclick="return confirm('本当に削除しますか？');">削除</button>
            </form>
        </c:if>

        <p>
            <a href="<c:url value='?action=${actRep}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>
