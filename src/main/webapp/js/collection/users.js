/*global Backbone */
var app = app || {};

(function() {
	app.Users = Backbone.Collection.extend({
		url : '/users',
		filterWithIds: function(ids) {
		    return _(this.models.filter(function(c) { return _.contains(ids, c.id); }));
		}
	});
})();