module.exports = {
    publicPath: process.env.NODE_ENV === 'production'
        ? "/"
        : '/',
    filenameHashing: true,
    runtimeCompiler: true,
    devServer: {
        port: 8080,
        compress: true,
        proxy: {
            "^/receive": {
                target: "ws://localhost:8082/receive",
                changeOrigin: true,
                ws:true,
                pathRewrite: {'^/receive': '/'}
            },
            '^/': {
                target: 'http://localhost:8082',
                changeOrigin: true,
                secure: false,
                pathRewrite: {'^/': '/'}
            }
        },
        overlay: {
            warnings: false,
            errors: false
        }
    }
}