
/**
 * Creates a new JS calendar manager
 * @param settings object Contains the parameters needed to connect to the server and the Multidatepicker instance
 * @param settings.loadUrl string Url to invoke to load a calendar
 * @param settings.saveUrl string Url to invoke to save a calendar
 * @param settings.picker jQuery Multidatepicker instance
 * @param settings.messages Object Translations for CalendarManager.Messages
 *  @param settings.log jQuery/object Object overriding the "text" method. It will be called to show messages
 * @returns {CalendarManager}
 *  
 */

CalendarManager = function(settings){
	this.settings = settings;
	this.settings.messages = (this.settings.messages || CalendarManager.Messages); 
};

/**
 * STATIC
 */
CalendarManager.Messages = {
	CAL_LOADED: 'Calendar loaded',
	CAL_LOAD_ERROR: 'Calendar load error or calendar not found',
	CAL_SAVED: 'Calendar saved',
	CAL_SAVE_ERROR: 'Error saving the calendar'
};

CalendarManager.prototype.loadCalendar = function(id){
	var manager = this;
	$.ajax({
		url: this.settings.loadUrl,
		type: "GET",
		data: {calendar : id},
		dataType: "text/json",
		success: function(data){
			manager.setDates(data);
			manager.out(this.settings.messages.CAL_LOADED);
		},
		error: function(data){
			manager.setDates(data);
			manager.out(this.settings.messages.CAL_LOAD_ERROR);
		}
	});
};

CalendarManager.prototype.saveCalendar = function(id){
	var manager = this;
	var dates = this.settings.picker.multiDatesPicker('getDates');
	$.ajax({
		url: this.settings.loadUrl,
		type: "GET",
		data: {calendar : id, dates: dates},
		dataType: "text/json",
		success: function(data){
			manager.out(this.settings.messages.CAL_SAVED);
		},
		error: function(data){
			manager.out(this.settings.messages.CAL_SAVE_ERROR);
		}
	});
};

/**
 * Updates the Multidatepicker dates
 * @param dates
 */
CalendarManager.prototype.setDates = function(dates){
	this.settings.picker.multiDatesPicker('resetDates');
	this.settings.picker.multiDatesPicker('addDates', dates);
};

/**
 * 
 * @param msg
 */
CalendarManager.prototype.out = function(msg){
	this.settings.log.text(msg);
};
