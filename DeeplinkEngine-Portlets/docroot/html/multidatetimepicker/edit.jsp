<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>

<portlet:defineObjects />

This is the
<b>Multi Datetime Picker</b>
portlet in Edit mode. For valid settings see: 
<ul>
<li><a target="_blank" href="http://api.jqueryui.com/datepicker/">http://api.jqueryui.com/datepicker/</a></li>
<li><a target="_blank" href="http://multidatespickr.sourceforge.net/">http://multidatespickr.sourceforge.net/</a></li>
</ul>
<form action="${settingsUrl}" method="post"	name="<portlet:namespace />fm">
	JS id <input type="text" name="jsid" value="${jsid}"/> used to be referenced by other portlets.
	<br/>
	Settings
	<textarea rows="20" cols="60" name="settings">${settings}</textarea>
	<br /> 
	<input type="button" value="<liferay-ui:message key="save" />"
			onClick="submitForm(document.<portlet:namespace />fm);" />
</form>
