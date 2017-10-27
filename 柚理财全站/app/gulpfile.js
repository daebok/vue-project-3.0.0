/**
 Gulpfile for gulp-webpack-demo
 created by fwon
*/

// var gulp = require('gulp'),
//     gutil = require('gulp-util'),
//     md5 = require('gulp-md5-plus');
//     webpack = require('webpack'),
//     webpackConfig = require('./webpack.config.js');

// // var myDevConfig = Object.create(webpackConfig);

// // var devCompiler = webpack(myDevConfig);

// // //引用webpack对js进行操作
// // gulp.task("build-js",function(callback) {
// //     devCompiler.run(function(err, stats) {
// //         if(err) throw new gutil.PluginError("webpack:build", err);
// //         gutil.log("[webpack:build]", stats.toString({
// //             colors: true
// //         }));
// //         //callback();
// //     });
// // });

// gulp.task('copy:html', function (done) {
//     gulp.src(['./*.html']).pipe(gulp.dest('dist/page/')).on('end', done);
// });

// //将js加上10位md5,并修改html中的引用路径，该动作依赖build-js
// gulp.task('md5:js', function (done) {
//     gulp.src('dist/themes/*.js')
//         .pipe(md5(10, 'dist/page/*.html'))
//         .pipe(gulp.dest('dist/themes/'))
//         .on('end', done);
// });

// //发布
// gulp.task('default', ['copy:html','md5:js']);

var gulp         = require('gulp'),
    rev          = require('gulp-rev'),
    clean        = require('gulp-clean'),
    gulpSequence = require('gulp-sequence'),
    fileinclude  = require('gulp-file-include');

gulp.task('css', function () {
    return gulp.src('dist/*.css')
        .pipe(rev())
        .pipe(gulp.dest('../web/src/main/webapp/themes/css'))
        .pipe( rev.manifest() )
        .pipe( gulp.dest( 'rev/css' ) );
});

gulp.task('scripts', function () {
    return gulp.src('dist/*.js')
        .pipe(rev())
        .pipe(gulp.dest('../web/src/main/webapp/themes/js'))
        .pipe( rev.manifest() )
        .pipe( gulp.dest( 'rev/js' ) );
});


// gulp.task('clean', function() {
//     return gulp.src(['rev','../web/src/main/webapp/WEB-INF/html_default'], {
//             read: false
//         })
//         .pipe(clean());
// });

// gulp.task('fileinclude', function (done) {
//     gulp.src(['src/**/*.html'])
//         .pipe(fileinclude({
//           prefix: '@@',
//           basepath: '@file'
//         }))
//         .pipe(gulp.dest('www'))
//         .on('end', done);
//         //.pipe(connect.reload())
// });

// gulp.task('clean', function (done) {
//     gulp.src(['rev','www'])
//         .pipe(clean())
//         .on('end', done);
// });

var revCollector = require('gulp-rev-collector');
var minifyHTML   = require('gulp-minify-html');

gulp.task('rev', function () {
    return gulp.src(['rev/**/*.json','src/**/*.html'])
.pipe( revCollector({
            replaceReved: true,
            dirReplacements: {
                'href="/': 'href="/themes/css/',
                'src="/': 'src="/themes/js/',
                'href="../../': 'href="/themes/css/',
                'src="../../': 'src="/themes/js/',               
                'href="../../../': 'href="/themes/css/',
                'src="../../../': 'src="/themes/js/',         
                'src="../../../../': 'src="/themes/js/',
                'href="../../../../': 'href="/themes/css/'         
                // 'cdn/': function(manifest_value) {
                //     return '//cdn' + (Math.floor(Math.random() * 9) + 1) + '.' + 'exsample.dot' + '/img/' + manifest_value;
                // }
            }
        }) )
        .pipe( gulp.dest('../web/src/main/webapp/WEB-INF') );
});


//将图片拷贝到目标目录
gulp.task('copy:img', function (done) {
    gulp.src(['dist/*.{png,jpg,gif}']).pipe(gulp.dest('../web/src/main/webapp/themes/css')).on('end', done);
});
gulp.task('copy:js', function (done) {
    gulp.src(['themes/js/*.js']).pipe(gulp.dest('../web/src/main/webapp/themes/js')).on('end', done);
});
gulp.task('mytask', gulpSequence(['css', 'scripts'], 'rev',['copy:img','copy:js']));

gulp.task('default', function() {
    gulp.start(['mytask']);
});