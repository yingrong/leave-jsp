const { defineConfig } = require('@vue/cli-service')
const webpack = require('webpack')

module.exports = defineConfig({
  transpileDependencies: true,
  publicPath: '/todo_list/vue_cli_basic/dist',
  configureWebpack: {
    plugins: [
      new webpack.ProvidePlugin({
        $: "jquery"
      })
    ]
  }
})
