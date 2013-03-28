<%@ include file="init.jsp"%>
<portlet:defineObjects />
<div id="<portlet:namespace/>_results" class="hotelResults">

</div>
<script type="text/html" id="<portlet:namespace/>_results_template">
<div class="dl_result hotel-%HID%">
	<h2>%HNAME%</h2>
	<div class="left">
		<img src="%HIMG%"/>
		<span class="price">%HPRICE%</price>
	</div>
	<div class="dl_hotel_desc">%HDESC%</div>
</div>
</script>
<script type="text/javascript">
	var _template = $('#<portlet:namespace/>_results_template').html();
	function getResults() {
		var resUrl = "${resultsUrl}"; 
		if(resUrl.length != 0){
			$.getJSON(resUrl + "&token=${token}", function(data) {
				// Render hotels
				console.log(data);
				var target = $('#<portlet:namespace/>_results');
				$.each(data, function(i, hotel){
					target.append(_template
									.replace('%HNAME%', hotel.name)
									.replace('%HIMG%', hotel.images[0].src)
									.replace('%HID%', hotel.id)
									.replace('%HDESC%', hotel.desc)
									.replace('%HPRICE%', hotel.prices[0].totalPrice));
				})
			});
		}
	}
	getResults();
</script>

