module.exports = function(grunt) {

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        // grunt-contrib-uglify
        uglify: {
            options: {
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            build: {
                src: 'src/<%= pkg.name %>.js',
                dest: 'build/<%= pkg.name %>.min.js'
            }
        },
        // grunt-bower-install
        bowerInstall: {
            target: {
                // Point to the files that should be updated when
                // you run `grunt bower-install`
                src: [
                    //'app/views/**/*.html', // .html support...
                    //'app/views/**/*.jade', // .jade support...
                    //'app/styles/main.scss', // .scss & .sass support...
                    //'app/config.yml' // and .yml & .yaml support out of the box!
                    'index.html'
                ],
                // Optional:
                // ---------
                cwd: '',
                dependencies: true,
                devDependencies: false,
                exclude: [],
                fileTypes: {},
                ignorePath: '',
                overrides: {}
            }
        },
//        // grunt-bower
//        bower: {
//            dev: {
//              dest: 'dest/',
//              js_dest: 'dest/js',
//              css_dest: 'dest/css',
//              fonts_dest: 'public/fonts/', //covers font types ['svg','eot', 'ttf', 'woff', 'otf']
//            }
//          }
        // grunt-bower-task
        bower: {
            install: {
                //just run 'grunt bower:install' and you'll see files from your Bower packages in lib directory
                options: {
                    layout:"byComponent"
                }
            }
        }
    });

    // Load the plugin that provides the "uglify" task.
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-bower-install');
    grunt.loadNpmTasks('grunt-bower');
    grunt.loadNpmTasks('grunt-bower-task');

    // Default task(s).
    grunt.registerTask('default', ['uglify']);

};