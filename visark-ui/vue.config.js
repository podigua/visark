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