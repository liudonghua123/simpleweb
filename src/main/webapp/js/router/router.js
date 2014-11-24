/*global Backbone */
var app = app || {};

(function() {
	var Router = Backbone.Router.extend({
		routes : {
			"" : "home",
			"edit/:id" : "edit",
			"new" : "edit",
			"statistics" : "statistics"
		}
	});

	router = new Router;
	router.on('route:home', function() {
		// render user list
		app.userListView.render();
	})
	router.on('route:edit', function(id) {
		app.userEditView.render({
			id : id
		});
	})
	router.on('route:statistics', function(id) {
		app.userStaisticsView.render();
	})
	Backbone.history.start();
})();