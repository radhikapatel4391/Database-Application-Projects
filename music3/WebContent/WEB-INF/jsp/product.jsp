<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />

<!-- start the middle column -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<section>
	<img src="<c:url value='/images/${product.code}_cover.jpg'/>"
			alt="Fresh Corn Records Logo" width="175" height="175">
	
		<p>${product.description}</p>
		<p>Price: ${product.price}</p>

	
	<c:choose>
		<c:when test="${product.code  == 'pf01'}">
			<p class="clear_both">This 68-minute opus from San Francisco's
				Paddlefoot doesn't pull any punches. Songs like "64 Corvair, Part
				2", "Whiskey Before Breakfast", and "The Murphy-Ryan Polkas" mix
				traditional American and Irish fiddle tunes with indie rock. The
				result is somewhere between The Pogues, Camper Van Beethoven, and
				Uncle Tupelo. The sincerity and quirkiness of other songs like "When
				I Was Nine" and "Tiny House" are more reminiscent of Jonathan
				Richman.</p>
		</c:when>

		<c:when test="${product.code  == '8601'}">
			<p class="clear_both">The debut album from 86 (the band), True
				Life Songs and Pictures rocks and twangs in equal measure. Filled
				with banjo, one-string bass, fiddle, and 3-part harmonies, this
				semi-rock, semi-country, semi-bluegrass album covers a lot of
				ground. "How to Get There" is a rambling epic that unleashes a
				rapid-fire lyrical barrage while "Don't Close Your Eyes" and
				"Morning Sun" are more melancholy and bittersweet.</p>
		</c:when>

		<c:when test="${product.code  == 'jr01'}">
			<p class="clear_both">The debut album from Joe Rut rambles from
				Byrds-esque folk pop of "Filter" to the country sounds of "Find My
				Way Marie" to psychedelic Brit-pop tunes like "A Place In All This."
				This well-crafted album is unique and cohesive, revealing its many
				layers on repeated listens.</p>
		</c:when>

		
		<c:otherwise>
			<p class="clear_both">Lorem Ipsum is simply dummy text of the
				printing and typesetting industry. Lorem Ipsum has been the
				industry's standard dummy text ever since the 1500s, when an unknown
				printer took a galley of type and scrambled it to make a type
				specimen book. It has survived not only five centuries, but also the
				leap into electronic typesetting, remaining essentially unchanged.
				It was popularised in the 1960s with the release of Letraset sheets
				containing Lorem Ipsum passages, and more recently with desktop
				publishing software like Aldus PageMaker including versions of Lorem
				Ipsum</p>
		</c:otherwise>
	</c:choose>

</section>

<!-- end the middle column -->

<aside id="sidebarB">
        <form method="post" action="<c:url value= '/addItemTOCart.html' />">
            <input type="hidden" name="productId" value="${product.id}">
            <input type="image" src="<c:url value='/images/addtocart.gif'/>" 
                   width="113" alt="Add to Cart">
        </form>
        <form method="get" action="<c:url value= '/sound.html' />">
            <input type="hidden" name="productcode" value="${product.code}">
            <input class="top_margin" type="image" src="<c:url value='/images/listen.gif'/>" 
                   width="113" alt="Listen to Samples">
        </form>
        
    </aside>
<jsp:include page="/includes/footer.jsp" />