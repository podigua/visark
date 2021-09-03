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
    },
    pages: {
        index: {
            entry: 'src/main.js',
            template: 'public/index.html',
            filename: 'index.html',
            title: 'imkt',
            chunks: ['chunk-vendors', 'chunk-common', 'index']
        },
        cluster: {
            entry: 'src/cluster.js',
            template: 'public/cluster.html',
            filename: 'cluster.html',
            title: 'cluster',
            chunks: ['chunk-vendors', 'chunk-common', 'cluster']
        },
        option: {
            entry: 'src/option.js',
            template: 'public/option.html',
            filename: 'option.html',
            title: 'cluster',
            chunks: ['chunk-vendors', 'chunk-common', 'option']
        }
    }
}