/*global Backbone */
var app = app || {};

(function() {
	var Router = Backbone.Router.extend({
		routes : {
			"" : "home",
			"edit/:id" : "edit",
			"create" : "edit",
//			"statistics" : "statistics"
		}
	});

	app.router = new Router;
	app.router.on('route:home', function() {
		// render user list
		app.userListView.render();
	})
	app.router.on('route:edit', function(id) {
		app.userEditView.render({
			id : id
		});
	})
//	app.router.on('route:statistics', function(id) {
//		app.userStaisticsView.render();
//	})
	Backbone.history.start();
})();