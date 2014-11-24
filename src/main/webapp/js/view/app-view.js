/*global Backbone, jQuery, _, ENTER_KEY */
var app = app || {};

(function($) {
	app.UserListView = Backbone.View.extend({
		el : '.page',
		template : _.template($('#user-list-template').html()),
        events : {
            'click #statistics_button' : 'showStatistics'
        },
		render : function() {
			var that = this;
		    app.users = new app.Users();
			app.users.fetch({
				success : function(users) {
					that.$el.html(that.template({
						users : app.users.models
					}));
				}
			})
		},
		showStatistics : function() {
		    new app.StatisticsView().render();
		}
	});
    app.userListView = new app.UserListView();
})(jQuery);