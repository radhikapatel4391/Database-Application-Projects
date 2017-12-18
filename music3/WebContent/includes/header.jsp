<%@page contentType="text/html" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>

<html>
<head>
<meta charset="utf-8">
<title>Online Music Store</title>
<link rel="shortcut icon" href="<c:url value='/images/logo.jpg'/>">
<link rel="stylesheet" href="<c:url value='/styles/main.css'/> ">
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
</head>

<body>

	<header>
		<img src="<c:url value='/images/logo.jpg'/>"
			alt="Music  Logo" width="58">
		<h1>Online Music Store</h1>
		<h2>Quality Sounds Served Up Fresh!</h2>
	</header>
	<nav id="nav_bar">
		<ul>
			<li><a href="<c:url value='/adminController/adminWelcome.html'/>">
					Admin</a></li>
					<li><a href="<c:url value='/adminController/listVariables.html'/>">
					List Variables</a></li>

<%-- 			<c:if test="${empty user}"> --%>
<%-- 				<li><a href="<c:url value='/login.sa'/>"> Login</a></li> --%>
<%-- 			</c:if> --%>

<%-- 			<c:if test="${user}"> --%>
<%-- 				<li><a href="<c:url value='/logout.sa'/>"> LogOut</a></li> --%>
<%-- 			</c:if> --%>

			<li><a href="<c:url value='/cart.html'/>"> Show Cart</a></li>
		</ul>
	</nav>



	