/**
 * Utils
 */
MultiDatetimePickerUtils = {
	/**
	 * Return a set of date ranges
	 * @param _dates Array An incremental sorted set of dates
	 */
	ranges: function(_dates) {
		var rgs = [ {from : _dates[0], to : _dates[0],count : 1	} ];
		var r = rgs[rgs.length - 1];
		__dates = _dates.slice(1);
		for ( var c in __dates) {
			var d = __dates[c];
			var to = new Date(r.from);
			to.setDate(r.from.getDate() + r.count);
			if (d.getTime() == to.getTime()) {
				r.count++;
				r.to = d;
			} else {
				r = rgs[rgs.push({ from : d, to : d, count : 1 }) - 1];
			}
		}
		return rgs;
	}
}