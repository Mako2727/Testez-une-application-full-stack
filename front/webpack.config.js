module.exports = {
  module: {
    rules: [
      {
        test: /\.[jt]s$/,
        exclude: /(node_modules|cypress)/,
        use: {
          loader: 'babel-loader',
          options: {
            plugins: ['istanbul'],
          },
        },
      },
    ],
  },
};