<%@include file="init.jsp" %>

<form action="<liferay-portlet:actionURL portletConfiguration="true" />"
method="post" name="<portlet:namespace />fm">

<input name="<portlet:namespace /><%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />

<table class="lfr-table">
<tr>
	<td>Email</td>
		<td><input type="text" name="<portlet:namespace />email"  value="<%=email %>"/>
	</td>

	<td>Subject</td>
		<td><input type="text" name="<portlet:namespace />subject"  value="<%=subject %>" />
	</td>
</tr>
<tr>
       <td colspan="2">
            <input type="button" value="<liferay-ui:message key="save" />"
onClick="submitForm(document.<portlet:namespace />fm);" />
       </td>
</tr>
</table>
</form>