// Generated using webpack-cli https://github.com/webpack/webpack-cli

const path = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const isProduction = process.env.NODE_ENV == 'production';

const config = {
    entry: {
        editor: './src/main/resources/static/js/editor.js',
        navbar: './src/main/resources/static/js/navbar.js',
    },
    output: {
        path: path.resolve(__dirname, './src/main/resources/static/dist/'),
        filename: '[name].bundle.js',
        clean: true
    },
    plugins: [
        new MiniCssExtractPlugin(),
    ],
    optimization: {
        mergeDuplicateChunks: true,
        minimize: true,
        splitChunks: {
            chunks: 'all',
            name: 'vendor',
        }
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/i,
                loader: 'babel-loader',
            },
            {
                test: /\.css$/i,
                use: [MiniCssExtractPlugin.loader,'css-loader'],
            },
            {
                test: /\.(eot|svg|ttf|woff|woff2|png|jpg|gif)$/i,
                type: 'asset',
            },
        ],
    },

    devtool: 'eval-source-map',
    devServer: {
        open: true,
        port: 8081,
        host: 'localhost',
        hot: true,
        devMiddleware: {
            index: true,
            publicPath: '/dist/',
            writeToDisk: true,
        },
        static: {
            directory: path.resolve(__dirname, './src/main/resources/static/'),
            publicPath: '/dist/',
        },
        proxy: [{
            context: '**',
            target: 'http://localhost:8080',
            secure: false,
            prependPath: false,
        }]
    },
};

module.exports = () => {
    if (isProduction) {
        config.mode = 'production';
    } else {
        config.mode = 'development';
    }
    return config;
};
