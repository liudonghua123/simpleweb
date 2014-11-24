/*global Backbone, jQuery, _, ENTER_KEY */
var app = app || {};

(function($) {

    app.StatisticsView = Backbone.View.extend({
        //el : '#statistics-modal',
        className: "modal backbone-modal",
        template: _.template($('#statistics-modal-template').html()),
        buttonTemplate: _.template('<a href="<%=href%>" class="btn <%=className%>"><%=label%></a>'),
        buttonDefaults: {
            className: "",
            href: "#",
            label: "",
            close: false
        },
        defaults: {
            title: "<h3>Info</h3>",
            backdrop: true,
            body: "",
            buttons: [{
                className: "btn-primary",
                href: "#",
                label: "Close",
                close: true
            }],
            postRender: function() {
                return this;
            }
        },
        initialize: function(options) {
            options || (options = {});
            _.defaults(this, this.defaults);
            _.extend(this, _.pick(options, _.keys(this.defaults)));
            _.bindAll(this, "close");
        },
        render: function() {
            var view = this;

            view.statistics = new app.Statistics;
            view.statistics.fetch({
                success : function(statistics) {
                    view.$el.html(view.template({
                        statistics : statistics
                    }));
                    view.$el.modal({
                        keyboard: false,
                        backdrop: this.backdrop
                    });
                }
            })
            this.$header = this.$el.find('.modal-header');
            this.$body = this.$el.find('.modal-body');
            this.$footer = this.$el.find('.modal-footer');

            _.each(this.buttons, function(button) {
                _.defaults(button, view.buttonDefaults);
                var $button = $(view.buttonTemplate(button));
                view.$footer.append($button);
                if (button.close) $button.on("click", view.close);
            });

            this.$el.modal({
                keyboard: false,
                backdrop: this.backdrop
            });

            this.$header.find("a.close").click(view.close);

            if (this.backdrop === true) {
                $('.modal-backdrop').off().click(view.close);
            }

            this.postRender();

            return this;
        },
        close: function(e) {
            if (e) e.preventDefault();
            var view = this;
            this.trigger("close", this);
            setTimeout(function() {
                view.$el.modal("hide");
                view.remove();
            }, 25);
        }
    });

})(jQuery);