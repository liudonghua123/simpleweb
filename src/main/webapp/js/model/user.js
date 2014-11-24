/*global Backbone */
var app = app || {};

(function() {
	app.User = Backbone.Model.extend({
		urlRoot : '/users',
		defaults: {
		    name: '',
		    age: '',
		    salary: ''
		}
	});
})();