/*global Backbone */
var app = app || {};

(function() {
	app.Users = Backbone.Collection.extend({
		url : '/users'
	});
})();