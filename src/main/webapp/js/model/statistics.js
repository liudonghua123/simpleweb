/*global Backbone */
var app = app || {};

(function() {
    app.Statistics = Backbone.Model.extend({
        //urlRoot : '/users/statistics',
        url : '/users/statistics',
        defaults: {
            min_salary: '',
            max_salary: '',
            avg_salary: ''
        }
    });
})();