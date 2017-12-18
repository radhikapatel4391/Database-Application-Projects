<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<aside id="sidebarA">
    <nav>
    <c:url var="catalogURL" value="/catalog.html" />
      <ul>
          <li><a class="current" href="<c:url value='/' />">
                  Home</a></li>
          <li><a href = "${catalogURL}">
                  Browse Catalog</a></li>          
      </ul>
    </nav>
</aside>

