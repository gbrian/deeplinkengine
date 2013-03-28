<%@include file="include.jsp"%>

<portlet:defineObjects />
<jsp:useBean class="java.lang.String" id="jsid" scope="request" />

<div id="${portletId}_picker"></div>

<script type="text/javascript">
<!--
	<%  if(jsid != null){ %>
		var <%= jsid%> = 
	<%}%>
	$('#${portletId}_picker').multiDatesPicker(${settings});
//-->
</script>
