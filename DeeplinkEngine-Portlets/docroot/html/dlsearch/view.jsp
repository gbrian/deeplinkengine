<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />

<script type="text/javascript">
<!--
	var search = ${search};
//-->
</script>

<label>Provider</label> <span class="ui-spinner ui-widget ui-widget-content ui-corner-all"><select id="ssproviders"></select></span>
Language <span class="ui-spinner ui-widget ui-widget-content ui-corner-all"><input type="text" id="sslanguage"/> </span>
Currency <span class="ui-spinner ui-widget ui-widget-content ui-corner-all"><input type="text" id="sscurrency"/></span>
<br/>
<label>Destination/s</label> <input type="text" id="ssdestinations"/>
<br/>
<label>Dates</label> <div id="ssdates"></div>
<br/>
<label>Occupancy Rooms</label> <input type="text" id="ssrooms"/> Adults <input type="text" id="ssadults"/> Children <input type="text" id="sschildren"/>
<br/>
<label>Max results</label> <input type="text" id="ssmaxres"/>
<br/>


