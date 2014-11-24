/*global Backbone, jQuery, _, ENTER_KEY */
var app = app || {};

(function($) {
	app.UserListView = Backbone.View.extend({
		el : '.page',
		template : _.template($('#user-list-template').html()),
        events : {
            'click #show_statistics' : 'showStatistics',
            'click #create_user' : 'createUser',
            'click .delete' : 'deleteUser'
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
		},
        createUser : function() {
          app.router.navigate('create', {
              trigger : true
          });
        },
        deleteUser : function(ev) {
            var that = this;
            // find the model
            var user = app.users.filterWithIds([parseInt($(ev.target).attr('data-user-id'))])._wrapped[0];
            user.destroy({
                success : function() {
                    // re-render UserListView
                    that.render();
                }
            });
            
        }
	});
    app.userListView = new app.UserListView();
})(jQuery);