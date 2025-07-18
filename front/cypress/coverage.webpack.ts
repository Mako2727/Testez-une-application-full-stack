import type { Configuration } from 'webpack';

const webpackConfig: Configuration = {
  module: {
    rules: [
      {
        test: /\.[jt]s$/,
        exclude: /(node_modules|\.spec\.ts$)/,
       use: {
  loader: '@jsdevtools/coverage-istanbul-loader',
  options: { esModules: true }
},
        enforce: 'post'
      }
    ]
  }
};
console.log('Webpack instrumentation active');
export default webpackConfig;