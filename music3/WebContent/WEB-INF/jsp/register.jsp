<jsp:include page="/includes/header.jsp" />
<jsp:include page="/includes/sidebar.jsp" />

<!-- start the middle column -->

<section>

  <h1>Download registration</h1>

  <p>Before you can download and listen to these sound files, 
  you must register with us by entering your name and email 
  address below.</p>

  <!-- Import the core JSTL library -->
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

  <!-- Use the JSTL url tag to encode the URL -->
  <form action="<c:url value='/registerFormReceive.sa'/>" method="post">  	
    <p id="required">Required <span class="required">*</span></p>
    
    <label><span class="required">*</span>First Name</label>    
    <input type="text" name="firstName"  maxlength=20 value="${user.firstname}" required>
    <br>
    
    <label><span class="required">*</span>Last Name</label>    
    <input type="text" name="lastName" value="${user.lastname}" required>
    <br>
    
    <label><span class="required">*</span>Email Address</label>    
    <input type="email" name="email" value="${user.emailAddress}" required>
    <br>  
    
    <label><span class="required">*</span>Address</label>    
    <input type="text" name="address" value="${user.address}" required> 
    <br>   
    
    
    <label>&nbsp;</label>
    <input type="submit" value="Continue">    

  </form>

</section>

<!-- end the middle column -->


<jsp:include page="/includes/footer.jsp" />