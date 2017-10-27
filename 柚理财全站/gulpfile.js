var gulp = require('gulp'),
	clean = require('gulp-clean');
 
gulp.task('clean:css', function (done) {
   return gulp.src('./web/src/main/webapp/themes/css/*.*')
        .pipe(clean())
        
});
gulp.task('clean:js', function (done) {
   return gulp.src('./web/src/main/webapp/themes/js/*.js')
        .pipe(clean())
        
});

gulp.task('default',['clean:css','clean:js']); 