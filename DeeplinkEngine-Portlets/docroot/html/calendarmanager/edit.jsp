<%@include file="include.jsp"%>

<portlet:defineObjects />

<form action="${settingsUrl}" method="post"	name="<portlet:namespace />fm">
	MultiDatepicker JS id <input type="text" name="jsid" value="${jsid}"/>, needed to show the dates.
	<input type="button" value="<liferay-ui:message key="save" />"
			onClick="submitForm(document.<portlet:namespace />fm);" />
</form>
