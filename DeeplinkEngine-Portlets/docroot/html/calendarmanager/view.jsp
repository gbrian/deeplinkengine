<%@include file="include.jsp"%>

<portlet:defineObjects />
<jsp:useBean type="java.util.List<java.lang.String>" id="calendars" scope="request" />
<jsp:useBean type="java.lang.String" id="jsid" scope="request" />

<div>
	<liferay-ui:message key="Calendar" /><select id="calmcalendarSelector">
	<% for(String cal : calendars){ %>
		<option value="<%= cal%>"><%= cal%></option>
	<%} %>
	</select>
	<div class="calendartoolbar">
		<input id="calmbtnload" type="button" value="<liferay-ui:message key="Load" />"/>
		<input id="calmbtnsave" type="button" value="<liferay-ui:message key="Save" />"/>
	</div>
	<div id="calmout" class="message"></div>
</div>
<script type="text/javascript">
<!--
	if(!"${jsid}".length){
		$('#calmout').text('Check configuration');
		throw("Error in configuration");
	}
	<% if(jsid != ""){%>
		manager = new CalendarManager({
			loadUrl: '${LOAD}',
			saveUrl: '${SAVE}',
			picker: ${jsid},
			log: $('#calmout')
		}); 
		$('#calmbtnload').click(function(){
			manager.loadCalendar($('#calmcalendarSelector').val());
		});
		$('#calmbtnload').click(function(){
			manager.saveCalendar($('#calmcalendarSelector').val());
		});
	<%}%>
//-->
</script>