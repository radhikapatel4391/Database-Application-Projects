<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />

<!-- start the middle column -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<section>

<img src="<c:url value='/images/${product.code}_cover_t.jpg'/>"
			alt="Fresh Corn Records Logo" width="175" height="175">
		
		<p>${product.description}</p>
		<p>Price: ${product.price}</p>

		<p class="clear_both">To <B>listen</B> to a track, <B>click</B> on the song's
		link.</p>
			

	<c:choose>
		<c:when test="${product.code  == 'pf01'}">	
		
			<p>1. Pete and Jimmy</p>
			<p>
				2.<a href="<c:url value="/track.html?trackId=3">
            		<c:param name="linkURL" value="/sound/pf01/whiskey.mp3"/>           
        			</c:url>
        			">Whiskey Before
					Breakfast</a>
				
			</p>
			<p>3. Fishing Rod</p>
			<p>4. The Murphy-Ryan Polkas</p>
			<p>5. John Henry's Blues</p>
			<p>
				6.<a href="<c:url value="/track.html?trackId=4">
            		<c:param name="linkURL" value="/sound/pf01/corvair.mp3"/>           
        			</c:url>
        			">64 Corvair, Part
					2</a> 
			</p>
			<p>7. Racoons Like Moonshine, Too</p>
			<p>8. Shelf Life</p>
			<p>9. Hey Chris</p>
			<p>10. Rock and Roll Scene</p>
			
		</c:when>

		<c:when test="${product.code  == '8601'}">
		
			<p>1. How to Get There</p>
			<p>
				
				2. <a href="<c:url value="/track.html?trackId=1">
            		<c:param name="linkURL" value="/sound/8601/star.mp3"/>           
        			</c:url>
        			">You Are a Star</a>
			</p>
			<p>
				3.<a href="<c:url value="/track.html?trackId=2">
            		<c:param name="linkURL" value="/sound/8601/no_difference.mp3"/>           
        			</c:url>
        			">Don't Make
					No Difference</a>
			</p>
			<p>4. Unidentified Fiddling Object</p>
			<p>5. Beat Up Old Car</p>
			<p>6. Wildflowers</p>
			<p>7. What You Whistle When You Walk Home</p>
			<p>8. Three Sheets to the Wind</p>
			<p>9. Singin' Drunk</p>
			<p>10. Don't Close Your Eyes</p>
			
		</c:when>

		<c:when test="${product.code  == 'jr01'}">
			<p>
				1.<a href="<c:url value="/track.html?trackId=7">
            		<c:param name="linkURL" value="/sound/jr01/filter.mp3"/>           
        			</c:url>
        			">Filter</a>
			</p>
			<p>2. Find My Way Marie</p>
			<p>3. Hole</p>
			<p>4. 1400 Years</p>
			<p>
				5.<a href="<c:url value="/track.html?trackId=8">
            		<c:param name="linkURL" value="/sound/jr01/so_long.mp3"/>           
        			</c:url>
        			">So Long Lazy Ray</a>				 
			</p>
			<p>6. A Tuna Is a Damn Big Fish</p>
			<p>7. El Dorado</p>
			<p>8. Dream of You</p>
			<p>9. This Sea Is Full of Monsters</p>
			<p>10. A Place in All This</p>
			
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
	<form method="post" action="<c:url value= '/cart.html' />">
		<input type="hidden" name="productId" value="${product.id}">
		<input type="image" src="<c:url value='/images/addtocart.gif'/>"
			width="113" alt="Add to Cart">
	</form>
	<form method="get" action="<c:url value= '/sound.html' />">
		<input type="hidden" name="productCode" value="${product.code}">
		<input class="top_margin" type="image"
			src="<c:url value='/images/listen.gif'/>" width="113"
			alt="Listen to Samples">
	</form>

</aside>
<jsp:include page="/includes/footer.jsp" />