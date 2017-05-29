var gulp = require('gulp');
var concat = require('gulp-concat');
var connect = require('gulp-connect');
var watch = require('gulp-watch');

gulp.task('scripts', function(){
  gulp.src(['src/services/*/*.*.js', 'src/app.module.js', 'src/app.route.js', 'src/components/*/*.*.js', 'src/directives/*/*.*.js'])
  .pipe(concat('app.min.js'))
  .pipe(gulp.dest('./dist/scripts'));

  gulp.src(['node_modules/angular/angular.js',
    'node_modules/jquery/dist/jquery.min.js',
    'node_modules/angular-route/angular-route.min.js',
    'node_modules/bootstrap/dist/js/bootstrap.min.js'])
  .pipe(concat('ven.min.js'))
  .pipe(gulp.dest('./dist/scripts'));
});

gulp.task('styles', function(){
  gulp.src(['src/styles/*.css'])
  .pipe(concat('app.css'))
  .pipe(gulp.dest('./dist/styles'));

  gulp.src(['node_modules/bootstrap/dist/css/bootstrap.min.css'])
  .pipe(concat('ven.css'))
  .pipe(gulp.dest('./dist/styles'));
});

gulp.task('moveFiles', function(){
  gulp.src(['src/index.html'])
  .pipe(gulp.dest('./dist'));

  gulp.src('src/views/*.html')
  .pipe(gulp.dest('./dist/views'));

  gulp.src('src/images/*.*')
  .pipe(gulp.dest('./dist/images'));

  gulp.src('node_modules/bootstrap/dist/fonts/*.*')
  .pipe(gulp.dest('./dist/fonts'));
});

gulp.task('build', ['moveFiles', 'scripts', 'styles'], function(){
  gulp.watch(['src/index.html', 'src/views/*.html'], ['moveFiles']);
  gulp.watch(['src/app.route.js', 'src/app.module.js', 'src/components/*/*.*.js', 'src/services/*/*.*.js', 'src/directives/*/*.*.js'], ['scripts']);
  gulp.watch(['src/styles/*.css'], ['styles']);
});
