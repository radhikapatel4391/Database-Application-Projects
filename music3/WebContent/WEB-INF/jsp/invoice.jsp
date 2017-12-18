<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />


<!-- begin middle column -->

<section class="cart">

	<h1>Your invoice</h1>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<table>
		<tr>
			<th>Date</th>
			<td>${invoice.invoiceDate}</td>
			<td></td>
		</tr>
		<tr>
			<th class="top">Ship To</th>
			<td>${user.firstname}</td>
			<td>${user.emailAddress}</td>
			<td></td>
		</tr>
		<tr>
			<td colspan="3"><hr></td>
		</tr>
		<tr>
			<th>Total Amount:</th>
			<td></td>
			<td>${invoice.totalAmount}</td>
		</tr>
	</table>

	<form action="<c:url value='/completeOrder.sa' />" method="post"
		id="float_left">
		<input type="submit" value="Complete">
	</form>



</section>

<!-- end middle column -->

<jsp:include page="/includes/footer.jsp" />