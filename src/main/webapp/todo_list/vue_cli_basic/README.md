# vue_cli_basic

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).


## integrate with tomcat
1. 将vue的构建结果打包到war包中，通过tomcat访问页面
   1. 修改 vue.config.js publicPath
   2. build 到 默认 dist 目录
   3. servlet 重定向到 dist目录 index.html
2. vue独立部署，将vue的构建结果独立发布到nignx等独立的web服务器，数据请求通过服务端API获取。