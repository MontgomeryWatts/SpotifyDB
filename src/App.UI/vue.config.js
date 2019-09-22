module.exports = {
  devServer: {
    proxy: {
      '/api' : {
        target: 'http://localhost:9876',
        ws: true,
        pathRewrite: function (path, req) {
          return path.replace('/api', '');
        }
      }
    }
  }
}