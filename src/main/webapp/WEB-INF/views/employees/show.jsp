<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst"%>
<%@ page import="constants.JpaConst"%>
<!-- JpaConstをインポート -->
<%@ page import="constants.AttributeConst"%>
<!-- AttributeConstをインポート -->

<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <h2>id : ${employee.id} の従業員情報 編集ページ</h2>

        <form method="post"
            action="<c:url value='?action=${actEmp}&command=update' />">
            <p>（パスワードは変更する場合のみ入力してください）</p>
            <table>
                <tr>
                    <th>社員番号</th>
                    <td><input type="text" name="code" value="${employee.code}" /></td>
                </tr>
                <tr>
                    <th>氏名</th>
                    <td><input type="text" name="name" value="${employee.name}" /></td>
                </tr>
                <tr>
                    <th>パスワード</th>
                    <td><input type="password" name="password" /></td>
                </tr>
                <tr>
                    <th>権限</th>
                    <td><select name="adminFlag">
                            <option value="${JpaConst.ROLE_GENERAL}"
                                <c:if test="${employee.adminFlag == JpaConst.ROLE_GENERAL}">selected</c:if>>一般</option>
                            <option value="${JpaConst.ROLE_ADMIN}"
                                <c:if test="${employee.adminFlag == JpaConst.ROLE_ADMIN}">selected</c:if>>管理者</option>
                    </select></td>
                </tr>
            </table>

            <button type="submit">投稿</button>
        </form>
<!-- 管理者のみ削除可能 -->
session<c:out value="${sessionScope.login_employee.adminFlag}" />
const<c:out value="${AttributeConst.ROLE_ADMIN.getIntegerValue()}" />

<c:if test="${sessionScope.login_employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}">
    <!-- 削除フォーム -->
    <form method="POST" action="<c:url value='?action=${actRep}&command=delete&id=${requestScope.REPORT.id}' />">
        <button type="submit" onclick="return confirm('本当に削除しますか？');">削除</button>
    </form>
</c:if>
        <p>
            <a href="<c:url value='?action=${actEmp}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>
