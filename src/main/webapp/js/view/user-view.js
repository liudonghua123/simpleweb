/*global Backbone, jQuery, _, ENTER_KEY, ESC_KEY */
var app = app || {};

(function($) {
	var UserEditView = Backbone.View.extend({
		el : '.page',
		template : _.template($('#edit-user-template').html()),
		events : {
			'submit .edit-user-form' : 'saveUser',
			'click button.back' : 'back'
		},
		saveUser : function(ev) {
			var userDetails = $(ev.currentTarget).serializeObject();
			var user = new app.User();
			user.save(userDetails, {
				success : function(user) {
					app.router.navigate('', {
						trigger : true
					});
				}
			});
			return false;
		},
        back : function(ev) {
            // history.go(-1)
            app.router.navigate('', {
                trigger : true
            });
            return true;
        },
		deleteUser : function(ev) {
			this.user.destroy({
				success : function() {
					app.router.navigate('', {
						trigger : true
					});
				}
			})
		},
		render : function(options) {
			var that = this;
			if (options.id) {
				that.user = new app.User({
					id : options.id
				});
				that.user.fetch({
					success : function(user) {
						that.$el.html(that.template({
							user : user
						}));
					}
				})
			} else {
				that.$el.html(that.template({
					user : null
				}));
			}
		}
	});
	app.userEditView = new UserEditView();
})(jQuery);