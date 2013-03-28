<%@include file="init.jsp"%>

<jsp:useBean class="java.lang.String" id="settingsUrl" scope="request" />
<jsp:useBean type="java.util.List<java.lang.String>" id="providers" scope="request" />
<jsp:useBean class="java.lang.String" id="provider" scope="request" />

<form action="<%=settingsUrl%>" method="post"
	name="<portlet:namespace />fm">
	Provider 
	<select name="provider">
		<%
			for(String s : providers){
				%>
				<option value="<%= s %>" selected="<%= s.equals(provider) %>"><%= s %></option>
		<%
			}
		%>	
	</select>
	<br/> 
	Template <i>//TODO: Add template design support</i>
	<br/>
	<input type="button" value="<liferay-ui:message key="save" />"
			onClick="submitForm(document.<portlet:namespace />fm);" />
</form>