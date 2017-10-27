module.exports = function(webpackConfig) {

  var path=require('path');
  //var webpack = require('webpack');
  // var HtmlWebpackPlugin = require('html-webpack-plugin');
  // var ext = require('extend');

  webpackConfig.babel.plugins.push('antd');

  // Fix ie8 compatibility
  webpackConfig.module.loaders.unshift({
    test: /\.jsx?$/,
    loader: 'es3ify-loader',
  });

   // webpackConfig.module.resolve({
   //      alias: {
   //          jquery: "/bower_components/jquery/dist/jquery.min.js"
   //      }
   //  });

  // ext(webpackConfig.output,{
  //        path: path.join(__dirname,'dist'),
  //        publicPath: "/dist/js/",
  //        filename: "js/[name].js",
  //        chunkFilename: "js/[id].chunk.js"
  //    });

  // webpackConfig.plugins.push(
  //      new HtmlWebpackPlugin({                        //根据模板插入css/js等生成最终HTML
  //          //favicon:'./src/img/favicon.ico', //favicon路径
  //          filename:'./page/*.html',    //生成的html存放路径，相对于 path
  //          template:'./[name].html',    //html模板路径
  //          inject:'body',    //允许插件修改哪些内容，包括head与body
  //          hash:true,    //为静态资源生成hash值
  //          cache: true,
  //          minify:{//压缩HTML文件
  //             removeComments:true,    //移除HTML中的注释
  //             collapseWhitespace:false    //删除空白符与换行符
  //          }
  //      })
  // );
  return webpackConfig;
};
