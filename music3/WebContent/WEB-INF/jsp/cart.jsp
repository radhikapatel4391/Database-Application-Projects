<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />

<!-- begin middle column -->

<section class="cart">
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

	<h1>Your cart</h1>
	<br /> <br />
	<c:choose>
		<c:when test="${empty cartItem}">
			<p>Your cart is empty.</p>
		</c:when>
		<c:otherwise>
			<table>
				<tr>
					<th><h2>ProductCode</h2></th>
					<th><h2>Qty</h2></th>
					<th><h2>Description</h2></th>
					<th><h2>Price</h2></th>

					<th>&nbsp;</th>
				</tr>
				<c:forEach var="item" items="${cartItem}">
					<tr class="cart_row">
						<td>${item.code}</td>
						<td>
							<form action="<c:url value='/updateCart.html'/>" method="post">
								<input type="hidden" name="productId"
									value="<c:out value='${item.productId}'/>">

								<c:choose>
									<c:when test="${not empty item.productId}">
										<input type="submit" value="Update" />
										<input type=text name="quantity"
											value="<c:out value='${item.quantity}'/>" id="quantity">
									</c:when>
									<c:otherwise>
										<input type="submit" value="Update" style="display: none;" />
									</c:otherwise>
								</c:choose>

							</form>
						</td>
						<td>${item.description}</td>
						<td>${item.price}</td>

						<td>
							<form action="<c:url value='/removeCart.html'/>" method="post">
								<input type="hidden" name="productId"
									value="<c:out value='${item.productId}'/>"> <input
									type="submit" value="Remove">
							</form>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="2"><br /></td>
				</tr>
				<tr>
					<td colspan="2">
						<p style="font-size: x-small;">
							<b>To change the quantity for an item</b>, enter the new quantity
							and click on the Update button.
						</p>
						<p style="font-size: x-small;">
							<b>To remove an item</b>, click on the Remove button.
						</p>
					</td>
					<td colspan="3">&nbsp;</td>
				</tr>
			</table>
		</c:otherwise>
	</c:choose>

	<form action="<c:url value='/catalog.html'/>" method="get"
		id="float_left">
		<input type="submit" value="Continue Shopping">
	</form>

	<c:choose>
		<c:when test="${not empty cartItem}">
			<form action="<c:url value='/checkOut.sa'/>" method="post">
				<input type="submit" value="Checkout">
			</form>
		</c:when>
		<c:otherwise>
			<form action="<c:url value='/checkOut.sa'/>" method="post">
				<input type="submit" value="Checkout" style="display: none;">
			</form>

		</c:otherwise>
	</c:choose>

</section>

<!-- end middle column -->

<jsp:include page="/includes/footer.jsp" />